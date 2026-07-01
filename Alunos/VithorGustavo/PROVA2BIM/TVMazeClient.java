import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TVMazeClient {

    private static final String BASE = "https://api.tvmaze.com";
    private static final int TIMEOUT_MS = 8000;

    @SuppressWarnings("unchecked")
    public List<Serie> buscarPorNome(String termo) throws ApiException {
        if (termo == null || termo.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String resposta = requisitar("/search/shows?q=" + codificar(termo.trim()));

        List<Serie> series = new ArrayList<>();
        Object raiz;
        try {
            raiz = Json.parse(resposta);
        } catch (RuntimeException e) {
            throw new ApiException("Resposta da API em formato invalido.", e);
        }

        if (!(raiz instanceof List)) {
            return series;
        }

        for (Object item : (List<Object>) raiz) {
            if (item instanceof Map) {
                Object show = ((Map<String, Object>) item).get("show");
                if (show instanceof Map) {
                    Serie s = Serie.deJson((Map<String, Object>) show);
                    if (s != null) {
                        series.add(s);
                    }
                }
            }
        }
        return series;
    }

    private String codificar(String termo) throws ApiException {
        try {
            return URLEncoder.encode(termo, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ApiException("Falha ao codificar o termo de busca.", e);
        }
    }

    private String requisitar(String caminho) throws ApiException {
        HttpURLConnection conexao = null;
        try {
            URL url = URI.create(BASE + caminho).toURL();
            conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(TIMEOUT_MS);
            conexao.setReadTimeout(TIMEOUT_MS);
            conexao.setRequestProperty("Accept", "application/json");
            conexao.setRequestProperty("User-Agent", "Prova2Bim-AcompanhaSeries/1.0");

            int codigo = conexao.getResponseCode();
            if (codigo < 200 || codigo >= 300) {
                throw new ApiException("A API respondeu com codigo HTTP " + codigo + ".");
            }

            try (InputStream is = conexao.getInputStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String linha;
                while ((linha = br.readLine()) != null) {
                    sb.append(linha);
                }
                return sb.toString();
            }
        } catch (ApiException e) {
            throw e;
        } catch (IOException e) {
            throw new ApiException("Nao foi possivel conectar a API do TVmaze. "
                    + "Verifique sua conexao com a internet.", e);
        } finally {
            if (conexao != null) {
                conexao.disconnect();
            }
        }
    }
}
