package telecine;

//tratamento de JSON
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//classes para leitura da resposta da API
import java.io.BufferedReader;
import java.io.InputStreamReader;

//classes para fazer requisição HTTP sem Java 11
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

//listas
import java.util.ArrayList;
import java.util.List;
//A comunicação HTTP foi feita com HttpURLConnection
public class ServicoTvMaze {

    public List<Serie> buscarSeriesPorNome(String nome) throws Exception { //throws Exception diz que pode trazer erros e temos que tratar na tela

        if (nome == null || nome.trim().isEmpty()) { //validação básica de nome se tem e se não é só espaço
            throw new IllegalArgumentException("Informe o nome da série.");
        }

        //formatando nome para passar como parâmetro na consulta
        String nomeFormatado = URLEncoder.encode(nome, "UTF-8");

        //consulta da API
        String url = "https://api.tvmaze.com/search/shows?q=" + nomeFormatado;

        //cria o endereço da requisição
        URL endereco = new URL(url);

        //abre a conexão HTTP
        HttpURLConnection conexao = (HttpURLConnection) endereco.openConnection();

        //define que a requisição será GET, ou seja, apenas buscar dados
        conexao.setRequestMethod("GET");

        //tempo máximo para tentar conectar
        conexao.setConnectTimeout(10000);

        //tempo máximo para esperar resposta da API
        conexao.setReadTimeout(10000);

        //pega o código de resposta HTTP
        int status = conexao.getResponseCode();

        if (status != 200) {
            throw new RuntimeException("Erro ao consultar API. Código HTTP: " + status);
        }

        //lê a resposta da API usando UTF-8
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conexao.getInputStream(), "UTF-8")
        );

        //StringBuilder monta o texto da resposta linha por linha
        StringBuilder resposta = new StringBuilder();
        String linha;

        while ((linha = reader.readLine()) != null) {
            resposta.append(linha);
        }

        //fecha a leitura e desconecta da API
        reader.close();
        conexao.disconnect();

        //a resposta da API vira uma String JSON
        String json = resposta.toString();

        //teste para ver o JSON retornado
        System.out.println(json);

        //transforma a String JSON em um array JSON
        JsonArray resultados = JsonParser.parseString(json).getAsJsonArray();

        //lista que será retornada para a tela
        List<Serie> series = new ArrayList<>();

        //se não encontrou resultado, retorna lista vazia
        if (resultados.size() == 0) {
            return series;
        }

        //for para percorrer todas as séries retornadas pela API
        for (int i = 0; i < resultados.size(); i++) {

            //cada item do resultado tem score e show
            JsonObject item = resultados.get(i).getAsJsonObject();

            //os dados da série ficam dentro de "show"
            JsonObject show = item.getAsJsonObject("show");

            //cria um novo objeto Serie para cada resultado da API
            Serie serie = new Serie();

            serie.setId(show.get("id").getAsInt());
            serie.setNome(show.get("name").getAsString());

            if (!show.get("language").isJsonNull()) {
                serie.setIdioma(show.get("language").getAsString());
            } else {
                serie.setIdioma("Não informado");
            }

            List<String> generos = new ArrayList<>();

            JsonArray generosJson = show.getAsJsonArray("genres");

            for (int j = 0; j < generosJson.size(); j++) {
                generos.add(generosJson.get(j).getAsString());
            }

            serie.setGeneros(generos);

            JsonObject rating = show.getAsJsonObject("rating");
            
            //salva como 0.0 se não vier nota (não tiver)
            if (rating != null && !rating.get("average").isJsonNull()) {
                serie.setNotaGeral(rating.get("average").getAsDouble());
            } else {
                serie.setNotaGeral(0.0);
            }

            if (!show.get("status").isJsonNull()) {
                serie.setEstado(show.get("status").getAsString());
            } else {
                serie.setEstado("Não informado");
            }

            if (!show.get("premiered").isJsonNull()) {
                serie.setDataEstreia(show.get("premiered").getAsString());
            } else {
                serie.setDataEstreia("Não informado");
            }

            if (!show.get("ended").isJsonNull()) {
                serie.setDataTermino(show.get("ended").getAsString());
            } else {
                serie.setDataTermino("Não informado");
            }

            if (!show.get("network").isJsonNull()) {
                JsonObject network = show.getAsJsonObject("network");
                serie.setEmissora(network.get("name").getAsString());
            } else if (!show.get("webChannel").isJsonNull()) {
                JsonObject webChannel = show.getAsJsonObject("webChannel");
                serie.setEmissora(webChannel.get("name").getAsString());
            } else {
                serie.setEmissora("Não informado");
            }

            series.add(serie);
        }

        return series;
    }
}