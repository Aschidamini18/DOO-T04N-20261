import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Usuario {

    private String nome;
    private final List<Serie> favoritos = new ArrayList<>();
    private final List<Serie> assistidas = new ArrayList<>();
    private final List<Serie> desejaAssistir = new ArrayList<>();

    public Usuario(String nome) {
        this.nome = (nome == null || nome.trim().isEmpty()) ? "Usuario" : nome.trim();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome.trim();
        }
    }

    public List<Serie> getLista(TipoLista tipo) {
        switch (tipo) {
            case FAVORITOS:
                return favoritos;
            case ASSISTIDAS:
                return assistidas;
            case DESEJA_ASSISTIR:
                return desejaAssistir;
            default:
                throw new IllegalArgumentException("Tipo de lista desconhecido.");
        }
    }

    public boolean adicionar(TipoLista tipo, Serie serie) {
        if (serie == null) {
            return false;
        }
        List<Serie> lista = getLista(tipo);
        if (lista.contains(serie)) {
            return false;
        }
        return lista.add(serie);
    }

    public boolean remover(TipoLista tipo, Serie serie) {
        if (serie == null) {
            return false;
        }
        return getLista(tipo).remove(serie);
    }

    public boolean contem(TipoLista tipo, Serie serie) {
        return serie != null && getLista(tipo).contains(serie);
    }

    public Map<String, Object> paraJson() {
        Map<String, Object> mapa = new LinkedHashMap<>();
        mapa.put("nome", nome);
        mapa.put("favoritos", listaParaJson(favoritos));
        mapa.put("assistidas", listaParaJson(assistidas));
        mapa.put("desejaAssistir", listaParaJson(desejaAssistir));
        return mapa;
    }

    private List<Object> listaParaJson(List<Serie> lista) {
        List<Object> saida = new ArrayList<>();
        for (Serie s : lista) {
            saida.add(s.paraJson());
        }
        return saida;
    }

    @SuppressWarnings("unchecked")
    public static Usuario deJson(Map<String, Object> mapa) {
        Object nome = mapa.get("nome");
        Usuario u = new Usuario(nome == null ? null : nome.toString());
        carregarLista(u.favoritos, mapa.get("favoritos"));
        carregarLista(u.assistidas, mapa.get("assistidas"));
        carregarLista(u.desejaAssistir, mapa.get("desejaAssistir"));
        return u;
    }

    @SuppressWarnings("unchecked")
    private static void carregarLista(List<Serie> destino, Object bruto) {
        if (!(bruto instanceof List)) {
            return;
        }
        for (Object item : (List<Object>) bruto) {
            if (item instanceof Map) {
                Serie s = Serie.deJson((Map<String, Object>) item);
                if (s != null && !destino.contains(s)) {
                    destino.add(s);
                }
            }
        }
    }
}
