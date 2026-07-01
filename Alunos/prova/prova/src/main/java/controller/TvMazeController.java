package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Serie;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TvMazeController {
    private final HttpClient clienteHttp;
    private final ObjectMapper objectMapper;

    public TvMazeController() {
        this.clienteHttp = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Serie> consultarSeriesPorNome(String nomeSerieBusca) {
        List<Serie> listaSeriesEncontradas = new ArrayList<>();

        try {
            String urlFormatada = "https://api.tvmaze.com/search/shows?q=" +
                    URLEncoder.encode(nomeSerieBusca, StandardCharsets.UTF_8);

            HttpRequest requisicaoApi = HttpRequest.newBuilder()
                    .uri(URI.create(urlFormatada))
                    .GET()
                    .build();

            HttpResponse<String> respostaServidor = clienteHttp.send(requisicaoApi, HttpResponse.BodyHandlers.ofString());

            if (respostaServidor.statusCode() == 200) {
                JsonNode nodoRaiz = objectMapper.readTree(respostaServidor.body());

                if (nodoRaiz.isArray()) {
                    for (JsonNode itemNodo : nodoRaiz) {
                        JsonNode nodoDetalheShow = itemNodo.path("show");
                        if (!nodoDetalheShow.isMissingNode()) {
                            listaSeriesEncontradas.add(criarSerieAPartirDoJson(nodoDetalheShow));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return listaSeriesEncontradas;
    }

    private Serie criarSerieAPartirDoJson(JsonNode nodoDetalheShow) {
        int id = nodoDetalheShow.path("id").asInt();
        String nome = nodoDetalheShow.path("name").asText("Sem nome");
        String status = nodoDetalheShow.path("status").asText("Desconhecido");
        String dataEstreia = nodoDetalheShow.path("premiered").asText("N/A");
        String dataFim = nodoDetalheShow.path("ended").asText("N/A");
        double nota = nodoDetalheShow.path("rating").path("average").asDouble(0.0);
        String emissora = obterEmissora(nodoDetalheShow);
        String generos = obterGeneros(nodoDetalheShow.path("genres"));
        String sumario = limparHtml(nodoDetalheShow.path("summary").asText("Sem sinopse disponível."));
        String idioma = nodoDetalheShow.path("language").asText("Desconhecido");
        String imagemUrl = obterImagemUrl(nodoDetalheShow.path("image"));

        return new Serie(id, nome, status, dataEstreia, dataFim, nota, emissora, generos, sumario, idioma, imagemUrl);
    }

    private String obterEmissora(JsonNode nodoDetalheShow) {
        String emissora = nodoDetalheShow.path("network").path("name").asText(null);
        if (emissora == null || emissora.isBlank()) {
            emissora = nodoDetalheShow.path("webChannel").path("name").asText("Desconhecida");
        }
        return emissora;
    }

    private String obterGeneros(JsonNode nodoGeneros) {
        List<String> listaGeneros = new ArrayList<>();

        if (nodoGeneros.isArray()) {
            for (JsonNode genero : nodoGeneros) {
                listaGeneros.add(genero.asText());
            }
        }

        return listaGeneros.isEmpty() ? "Não informado" : String.join(", ", listaGeneros);
    }

    private String limparHtml(String texto) {
        if (texto == null || texto.isBlank()) {
            return "Sem sinopse disponível.";
        }
        return texto.replaceAll("<[^>]*>", "").trim();
    }

    private String obterImagemUrl(JsonNode nodoImagem) {
        if (nodoImagem == null || nodoImagem.isMissingNode() || nodoImagem.isNull()) {
            return "";
        }

        String imagemOriginal = nodoImagem.path("original").asText("");
        if (!imagemOriginal.isBlank()) {
            return imagemOriginal;
        }

        return nodoImagem.path("medium").asText("");
    }
}
