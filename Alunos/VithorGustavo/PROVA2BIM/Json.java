import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Json {

    private Json() {
    }

    public static Object parse(String texto) {
        if (texto == null) {
            throw new IllegalArgumentException("Texto JSON nulo.");
        }
        Parser p = new Parser(texto);
        Object valor = p.lerValor();
        p.pularEspacos();
        if (!p.fim()) {
            throw new RuntimeException("Conteudo extra apos o JSON na posicao " + p.posicao());
        }
        return valor;
    }

    private static final class Parser {
        private final String s;
        private int i;

        Parser(String s) {
            this.s = s;
            this.i = 0;
        }

        boolean fim() {
            return i >= s.length();
        }

        int posicao() {
            return i;
        }

        void pularEspacos() {
            while (i < s.length()) {
                char c = s.charAt(i);
                if (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\uFEFF') {
                    i++;
                } else {
                    break;
                }
            }
        }

        Object lerValor() {
            pularEspacos();
            if (fim()) {
                throw new RuntimeException("JSON incompleto.");
            }
            char c = s.charAt(i);
            switch (c) {
                case '{':
                    return lerObjeto();
                case '[':
                    return lerArray();
                case '"':
                    return lerTexto();
                case 't':
                case 'f':
                    return lerBooleano();
                case 'n':
                    return lerNulo();
                default:
                    return lerNumero();
            }
        }

        private Map<String, Object> lerObjeto() {
            Map<String, Object> mapa = new LinkedHashMap<>();
            i++;
            pularEspacos();
            if (!fim() && s.charAt(i) == '}') {
                i++;
                return mapa;
            }
            while (true) {
                pularEspacos();
                if (fim() || s.charAt(i) != '"') {
                    throw new RuntimeException("Chave de objeto esperada na posicao " + i);
                }
                String chave = lerTexto();
                pularEspacos();
                if (fim() || s.charAt(i) != ':') {
                    throw new RuntimeException("':' esperado na posicao " + i);
                }
                i++;
                Object valor = lerValor();
                mapa.put(chave, valor);
                pularEspacos();
                if (fim()) {
                    throw new RuntimeException("Objeto JSON nao finalizado.");
                }
                char c = s.charAt(i);
                if (c == ',') {
                    i++;
                } else if (c == '}') {
                    i++;
                    break;
                } else {
                    throw new RuntimeException("',' ou '}' esperado na posicao " + i);
                }
            }
            return mapa;
        }

        private List<Object> lerArray() {
            List<Object> lista = new ArrayList<>();
            i++;
            pularEspacos();
            if (!fim() && s.charAt(i) == ']') {
                i++;
                return lista;
            }
            while (true) {
                Object valor = lerValor();
                lista.add(valor);
                pularEspacos();
                if (fim()) {
                    throw new RuntimeException("Array JSON nao finalizado.");
                }
                char c = s.charAt(i);
                if (c == ',') {
                    i++;
                } else if (c == ']') {
                    i++;
                    break;
                } else {
                    throw new RuntimeException("',' ou ']' esperado na posicao " + i);
                }
            }
            return lista;
        }

        private String lerTexto() {
            StringBuilder sb = new StringBuilder();
            i++;
            while (i < s.length()) {
                char c = s.charAt(i++);
                if (c == '"') {
                    return sb.toString();
                }
                if (c == '\\') {
                    if (i >= s.length()) {
                        break;
                    }
                    char esc = s.charAt(i++);
                    switch (esc) {
                        case '"':
                            sb.append('"');
                            break;
                        case '\\':
                            sb.append('\\');
                            break;
                        case '/':
                            sb.append('/');
                            break;
                        case 'b':
                            sb.append('\b');
                            break;
                        case 'f':
                            sb.append('\f');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        case 'u':
                            if (i + 4 > s.length()) {
                                throw new RuntimeException("Sequencia unicode invalida.");
                            }
                            String hex = s.substring(i, i + 4);
                            sb.append((char) Integer.parseInt(hex, 16));
                            i += 4;
                            break;
                        default:
                            throw new RuntimeException("Escape invalido: \\" + esc);
                    }
                } else {
                    sb.append(c);
                }
            }
            throw new RuntimeException("Texto JSON nao finalizado.");
        }

        private Boolean lerBooleano() {
            if (s.startsWith("true", i)) {
                i += 4;
                return Boolean.TRUE;
            }
            if (s.startsWith("false", i)) {
                i += 5;
                return Boolean.FALSE;
            }
            throw new RuntimeException("Valor booleano invalido na posicao " + i);
        }

        private Object lerNulo() {
            if (s.startsWith("null", i)) {
                i += 4;
                return null;
            }
            throw new RuntimeException("Valor nulo invalido na posicao " + i);
        }

        private Double lerNumero() {
            int inicio = i;
            if (!fim() && s.charAt(i) == '-') {
                i++;
            }
            while (!fim()) {
                char c = s.charAt(i);
                if ((c >= '0' && c <= '9') || c == '.' || c == 'e' || c == 'E' || c == '+' || c == '-') {
                    i++;
                } else {
                    break;
                }
            }
            String numero = s.substring(inicio, i);
            if (numero.isEmpty()) {
                throw new RuntimeException("Numero invalido na posicao " + inicio);
            }
            try {
                return Double.parseDouble(numero);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Numero invalido: " + numero);
            }
        }
    }

    public static String escrever(Object valor) {
        StringBuilder sb = new StringBuilder();
        escrever(valor, sb, 0);
        return sb.toString();
    }

    private static void escrever(Object valor, StringBuilder sb, int nivel) {
        if (valor == null) {
            sb.append("null");
        } else if (valor instanceof String) {
            escreverTexto((String) valor, sb);
        } else if (valor instanceof Boolean) {
            sb.append(valor.toString());
        } else if (valor instanceof Number) {
            escreverNumero((Number) valor, sb);
        } else if (valor instanceof Map) {
            escreverObjeto((Map<?, ?>) valor, sb, nivel);
        } else if (valor instanceof List) {
            escreverArray((List<?>) valor, sb, nivel);
        } else {

            escreverTexto(valor.toString(), sb);
        }
    }

    private static void escreverNumero(Number n, StringBuilder sb) {
        double d = n.doubleValue();
        if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15) {
            sb.append(Long.toString((long) d));
        } else {
            sb.append(n.toString());
        }
    }

    private static void escreverObjeto(Map<?, ?> mapa, StringBuilder sb, int nivel) {
        if (mapa.isEmpty()) {
            sb.append("{}");
            return;
        }
        sb.append("{\n");
        int restantes = mapa.size();
        for (Map.Entry<?, ?> e : mapa.entrySet()) {
            indentar(sb, nivel + 1);
            escreverTexto(String.valueOf(e.getKey()), sb);
            sb.append(": ");
            escrever(e.getValue(), sb, nivel + 1);
            if (--restantes > 0) {
                sb.append(',');
            }
            sb.append('\n');
        }
        indentar(sb, nivel);
        sb.append('}');
    }

    private static void escreverArray(List<?> lista, StringBuilder sb, int nivel) {
        if (lista.isEmpty()) {
            sb.append("[]");
            return;
        }
        sb.append("[\n");
        for (int k = 0; k < lista.size(); k++) {
            indentar(sb, nivel + 1);
            escrever(lista.get(k), sb, nivel + 1);
            if (k < lista.size() - 1) {
                sb.append(',');
            }
            sb.append('\n');
        }
        indentar(sb, nivel);
        sb.append(']');
    }

    private static void escreverTexto(String texto, StringBuilder sb) {
        sb.append('"');
        for (int k = 0; k < texto.length(); k++) {
            char c = texto.charAt(k);
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
    }

    private static void indentar(StringBuilder sb, int nivel) {
        for (int k = 0; k < nivel; k++) {
            sb.append("  ");
        }
    }
}
