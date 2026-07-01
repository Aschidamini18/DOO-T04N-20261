package fag.servico;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fag.dados.SerieDeTv;

// Esta classe pega as informacoes da API, traduz o JSON e entrega series prontas para o sistema.

public class BuscadorDeSeriesNaApiTvMaze {
	private static final String ENDERECO_BUSCA = "https://api.tvmaze.com/search/shows?q=%s";
	private final HttpClient clienteHttp;
	private final ObjectMapper conversorJson;

	
	public BuscadorDeSeriesNaApiTvMaze() {
		this.clienteHttp = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(8))
				.followRedirects(HttpClient.Redirect.NORMAL)
				.build();
		this.conversorJson = new ObjectMapper();
	}

	// Metodo buscarSeriesPorNome: procura series pelo nome informado pelo usuario.
	public List<SerieDeTv> buscarSeriesPorNome(String nomeSerie) throws IOException, InterruptedException {
		var nomeCodificado = URLEncoder.encode(nomeSerie, StandardCharsets.UTF_8);
		var requisicao = HttpRequest.newBuilder(URI.create(ENDERECO_BUSCA.formatted(nomeCodificado)))
				.timeout(Duration.ofSeconds(15))
				.header("Accept", "application/json")
				.GET()
				.build();

		var resposta = clienteHttp.send(requisicao, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
		validarRespostaDaApi(resposta);

		return StreamSupport.stream(conversorJson.readTree(resposta.body()).spliterator(), false)
				.map(resultado -> resultado.path("show"))
				.filter(show -> !show.isMissingNode() && !show.isNull())
				.map(this::converterJsonParaSerie)
				.toList();
	}

	// Metodo validarRespostaDaApi: transforma falhas HTTP em excecoes compreensiveis.
	private void validarRespostaDaApi(HttpResponse<String> resposta) throws IOException {
		if (resposta.statusCode() < 200 || resposta.statusCode() >= 300) {
			throw new IOException("A API TVMaze respondeu com status HTTP " + resposta.statusCode());
		}
	}

	// Metodo converterJsonParaSerie: transforma o JSON da API em objeto de serie.
	private SerieDeTv converterJsonParaSerie(JsonNode show) {
		return new SerieDeTv(
				show.path("id").asInt(),
				textoOuNulo(show, "name"),
				textoOuNulo(show, "language"),
				StreamSupport.stream(show.path("genres").spliterator(), false).map(JsonNode::asText).toList(),
				doubleOuNulo(show.path("rating").path("average")),
				textoOuNulo(show, "status"),
				textoOuNulo(show, "premiered"),
				textoOuNulo(show, "ended"),
				obterNomeEmissora(show));
	}

	// Metodo obterNomeEmissora: procura emissora normal ou canal de streaming.
	private String obterNomeEmissora(JsonNode show) {
		if (!show.path("network").isMissingNode() && !show.path("network").isNull()) {
			return textoOuNulo(show.path("network"), "name");
		}
		if (!show.path("webChannel").isMissingNode() && !show.path("webChannel").isNull()) {
			return textoOuNulo(show.path("webChannel"), "name");
		}
		return "Nao informado";
	}

	// Metodo textoOuNulo: le texto do JSON protegendo campos inexistentes.
	private String textoOuNulo(JsonNode no, String campo) {
		var valor = no.path(campo);
		return valor.isMissingNode() || valor.isNull() ? null : valor.asText();
	}

	// Metodo doubleOuNulo: le numero decimal do JSON protegendo campos inexistentes.
	private Double doubleOuNulo(JsonNode no) {
		return no.isMissingNode() || no.isNull() ? null : no.asDouble();
	}
}

