import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Serie {

    private final int id;
    private final String nome;
    private final String idioma;
    private final List<String> generos;
    private final Double notaGeral;
    private final StatusSerie status;
    private final String statusOriginal;
    private final String dataEstreia;
    private final String dataTermino;
    private final String emissora;
    private final String resumo;

    public Serie(int id, String nome, String idioma, List<String> generos, Double notaGeral,
                 String statusOriginal, String dataEstreia, String dataTermino,
                 String emissora, String resumo) {
        this.id = id;
        this.nome = nome == null ? "(sem nome)" : nome;
        this.idioma = idioma;
        this.generos = generos == null ? new ArrayList<>() : new ArrayList<>(generos);
        this.notaGeral = notaGeral;
        this.statusOriginal = statusOriginal;
        this.status = StatusSerie.deTexto(statusOriginal);
        this.dataEstreia = dataEstreia;
        this.dataTermino = dataTermino;
        this.emissora = emissora;
        this.resumo = resumo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getIdioma() {
        return idioma == null || idioma.isEmpty() ? "Desconhecido" : idioma;
    }

    public List<String> getGeneros() {
        return Collections.unmodifiableList(generos);
    }

    public String getGenerosFormatado() {
        return generos.isEmpty() ? "-" : String.join(", ", generos);
    }

    public Double getNotaGeral() {
        return notaGeral;
    }

    public String getNotaFormatada() {
        return notaGeral == null ? "-" : String.valueOf(notaGeral);
    }

    public StatusSerie getStatus() {
        return status;
    }

    public String getStatusOriginal() {
        return statusOriginal;
    }

    public String getDataEstreia() {
        return dataEstreia;
    }

    public String getDataEstreiaFormatada() {
        return dataEstreia == null || dataEstreia.isEmpty() ? "-" : dataEstreia;
    }

    public String getDataTermino() {
        return dataTermino;
    }

    public String getDataTerminoFormatada() {
        return dataTermino == null || dataTermino.isEmpty() ? "-" : dataTermino;
    }

    public String getEmissora() {
        return emissora == null || emissora.isEmpty() ? "-" : emissora;
    }

    public String getResumo() {
        return resumo == null ? "" : resumo;
    }

    @SuppressWarnings("unchecked")
    public static Serie deJson(Map<String, Object> show) {
        if (show == null) {
            return null;
        }

        int id = paraInt(show.get("id"));
        String nome = (String) show.get("name");
        String idioma = (String) show.get("language");

        List<String> generos = new ArrayList<>();
        Object gen = show.get("genres");
        if (gen instanceof List) {
            for (Object g : (List<Object>) gen) {
                if (g != null) {
                    generos.add(g.toString());
                }
            }
        }

        Double nota = null;
        Object ratingObj = show.get("rating");
        if (ratingObj instanceof Map) {
            Object media = ((Map<String, Object>) ratingObj).get("average");
            if (media instanceof Number) {
                nota = ((Number) media).doubleValue();
            }
        }

        String status = (String) show.get("status");
        String estreia = (String) show.get("premiered");
        String termino = (String) show.get("ended");

        String emissora = null;
        Object network = show.get("network");
        if (network instanceof Map) {
            Object nomeNet = ((Map<String, Object>) network).get("name");
            if (nomeNet != null) {
                emissora = nomeNet.toString();
            }
        }
        if (emissora == null) {
            Object webChannel = show.get("webChannel");
            if (webChannel instanceof Map) {
                Object nomeWeb = ((Map<String, Object>) webChannel).get("name");
                if (nomeWeb != null) {
                    emissora = nomeWeb.toString();
                }
            }
        }

        String resumo = limparHtml((String) show.get("summary"));

        return new Serie(id, nome, idioma, generos, nota, status, estreia, termino, emissora, resumo);
    }

    public Map<String, Object> paraJson() {
        Map<String, Object> show = new LinkedHashMap<>();
        show.put("id", id);
        show.put("name", nome);
        show.put("language", idioma);
        show.put("genres", new ArrayList<String>(generos));
        if (notaGeral != null) {
            Map<String, Object> rating = new LinkedHashMap<>();
            rating.put("average", notaGeral);
            show.put("rating", rating);
        } else {
            show.put("rating", null);
        }
        show.put("status", statusOriginal);
        show.put("premiered", dataEstreia);
        show.put("ended", dataTermino);
        if (emissora != null) {
            Map<String, Object> network = new LinkedHashMap<>();
            network.put("name", emissora);
            show.put("network", network);
        } else {
            show.put("network", null);
        }
        show.put("summary", resumo);
        return show;
    }

    private static int paraInt(Object valor) {
        if (valor instanceof Number) {
            return ((Number) valor).intValue();
        }
        return 0;
    }

    private static String limparHtml(String texto) {
        if (texto == null) {
            return null;
        }
        return texto.replaceAll("<[^>]*>", "").trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Serie)) {
            return false;
        }
        return id == ((Serie) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return nome;
    }
}
