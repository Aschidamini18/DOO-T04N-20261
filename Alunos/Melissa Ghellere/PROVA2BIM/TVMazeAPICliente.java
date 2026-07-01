import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// busca as series la no site do tvmaze 
public class TVMazeAPICliente {
    private static final String BASE_URL = "https://api.tvmaze.com/search/shows?q=";

    // Dicionário de traduções (🇺🇸 -> 🇧🇷)
    private static final Map<String, String> TRADUCOES = new HashMap<>();
    
    static {
        // Status
        TRADUCOES.put("Running", "Em Exibição");
        TRADUCOES.put("Ended", "Finalizada");
        TRADUCOES.put("To Be Determined", "A Definir");
        TRADUCOES.put("In Development", "Em Desenvolvimento");
        
        // Gêneros
        TRADUCOES.put("Action", "Ação");
        TRADUCOES.put("Adventure", "Aventura");
        TRADUCOES.put("Anime", "Anime");
        TRADUCOES.put("Comedy", "Comédia");
        TRADUCOES.put("Crime", "Crime");
        TRADUCOES.put("Drama", "Drama");
        TRADUCOES.put("Espionage", "Espionagem");
        TRADUCOES.put("Family", "Família");
        TRADUCOES.put("Fantasy", "Fantasia");
        TRADUCOES.put("History", "História");
        TRADUCOES.put("Horror", "Terror");
        TRADUCOES.put("Mystery", "Mistério");
        TRADUCOES.put("Romance", "Romance");
        TRADUCOES.put("Science-Fiction", "Ficção Científica");
        TRADUCOES.put("Sports", "Esportes");
        TRADUCOES.put("Supernatural", "Sobrenatural");
        TRADUCOES.put("Thriller", "Suspense");
        
        // Idiomas
        TRADUCOES.put("English", "Inglês");
        TRADUCOES.put("Japanese", "Japonês");
        TRADUCOES.put("Korean", "Coreano");
        TRADUCOES.put("Spanish", "Espanhol");
        TRADUCOES.put("French", "Francês");
        TRADUCOES.put("Portuguese", "Português");
    }

    // Método ajudante para traduzir, retorna o original se não achar no mapa
    private String traduzir(String termo) {
        return TRADUCOES.getOrDefault(termo, termo);
    }

    // Agora o método lança a exceção (throws Exception) para a TelaPrincipal capturar
    public List<Serie> buscarSeriePorNome(String nomeBusca) throws Exception {
        List<Serie> seriesEncontradas = new ArrayList<>();
        
        // arruma o texto pro link nao quebrar (🩹)
        String query = nomeBusca.replace(" ", "%20");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + query))
                .GET() 
                .build();

        // Se estiver sem internet, o client.send já vai estourar um erro aqui que sobe pra Tela
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // se deu bom (200), a gente corta o texto json 
        if (response.statusCode() == 200) {
            String jsonBody = response.body();
            String[] blocosDeShows = jsonBody.split("\"show\":\\{");
            
            for (int i = 1; i < blocosDeShows.length; i++) {
                String bloco = blocosDeShows[i];
                Serie s = new Serie();
                
                // preenche os dados da serie aplicando a tradução ✨
                s.setName(extrairTexto(bloco, "\"name\":\""));
                s.setLanguage(traduzir(extrairTexto(bloco, "\"language\":\"")));
                s.setStatus(traduzir(extrairTexto(bloco, "\"status\":\"")));
                s.setPremiered(extrairTexto(bloco, "\"premiered\":\""));
                s.setEnded(extrairTexto(bloco, "\"ended\":\""));
                
                // cuida pra nao bugar se nao tiver emissora
                String network = extrairTexto(bloco, "\"network\":{\"id\":");
                if (!network.equals("N/A")) {
                    s.setNetworkName(extrairTexto(network + bloco.substring(bloco.indexOf("\"network\":{\"id\":") + 10), "\"name\":\""));
                } else {
                    s.setNetworkName("N/A");
                }
                
                s.setImageUrl(extrairTexto(bloco, "\"medium\":\""));
                s.setScore(extrairNota(bloco));
                s.setGenres(extrairGeneros(bloco));
                
                seriesEncontradas.add(s);
            }
        } else {
            // Se a API cair ou der limite de requisição, joga o erro pra tela
            throw new Exception("O servidor da API retornou o código: " + response.statusCode());
        }
        
        return seriesEncontradas;
    }

    // recorta texto do json sujo 
    private String extrairTexto(String json, String chave) {
        int inicio = json.indexOf(chave);
        if (inicio != -1) {
            inicio += chave.length();
            int fim = json.indexOf("\"", inicio);
            if (fim != -1) return json.substring(inicio, fim);
        }
        return "N/A";
    }

    // puxa a nota e converte pra numero 
    private double extrairNota(String json) {
        String chave = "\"rating\":{\"average\":";
        int inicio = json.indexOf(chave);
        if (inicio != -1) {
            inicio += chave.length();
            int fim = json.indexOf("}", inicio);
            String valor = json.substring(inicio, fim).replace("null", "0.0");
            try { return Double.parseDouble(valor); } catch (Exception e) { return 0.0; }
        }
        return 0.0;
    }

    // limpa os generos e traduz 
    private List<String> extrairGeneros(String json) {
        List<String> generos = new ArrayList<>();
        String chave = "\"genres\":[";
        int inicio = json.indexOf(chave);
        if (inicio != -1) {
            inicio += chave.length();
            int fim = json.indexOf("]", inicio);
            String arrayGeneros = json.substring(inicio, fim);
            
            if (!arrayGeneros.isEmpty()) {
                for (String item : arrayGeneros.split(",")) {
                    String generoEmIngles = item.replace("\"", "").trim();
                    generos.add(traduzir(generoEmIngles)); // Traduz cada gênero
                }
            }
        }
        return generos;
    }
}