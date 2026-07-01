package fag.arquivo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fag.dados.SerieDeTv;
import fag.dados.UsuarioDoSistema;

// Esta classe usa Jackson para salvar e carregar o usuario e suas listas no arquivo JSON.

//  salva e carrega os dados usando JSON com Jackson.

public class RepositorioJsonComJackson implements RepositorioDeDadosDoSistema {
	private final Path arquivoDados;
	private final ObjectMapper conversorJson;

	// Construtor padrao: define o arquivo JSON dentro da pasta do projeto.
	public RepositorioJsonComJackson() {
		this(Path.of(System.getProperty("user.dir"), "series-data.json"));
	}

	
	public RepositorioJsonComJackson(Path arquivoDados) {
		this.arquivoDados = arquivoDados;
		this.conversorJson = new ObjectMapper()
				.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
				.setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
				.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
				.enable(SerializationFeature.INDENT_OUTPUT);
	}

	// Metodo carregarDados: le o arquivo JSON ou cria um usuario novo.
	@Override
	public UsuarioDoSistema carregarDados() throws IOException {
		if (Files.notExists(arquivoDados)) {
			return new UsuarioDoSistema("Usuario");
		}
		var dadosJson = conversorJson.readTree(arquivoDados.toFile());
		if (dadosJson.has("usuarios")) {
			return carregarUsuarioDoFormatoNovo(dadosJson, obterTexto(dadosJson, "ultimoUsuario", "Usuario"));
		}
		return montarUsuarioAPartirDoJson(dadosJson);
	}

	// Metodo carregarDadosDoUsuario: carrega um usuario pelo apelido ou cria um novo.
	@Override
	public UsuarioDoSistema carregarDadosDoUsuario(String apelido) throws IOException {
		var apelidoTratado = apelido == null || apelido.isBlank() ? "Usuario" : apelido.trim();
		if (Files.notExists(arquivoDados)) {
			return new UsuarioDoSistema(apelidoTratado);
		}
		var dadosJson = conversorJson.readTree(arquivoDados.toFile());
		if (dadosJson.has("usuarios")) {
			return carregarUsuarioDoFormatoNovo(dadosJson, apelidoTratado);
		}
		var usuarioAntigo = montarUsuarioAPartirDoJson(dadosJson);
		if (usuarioAntigo.obterApelido().equalsIgnoreCase(apelidoTratado)) {
			return usuarioAntigo;
		}
		return new UsuarioDoSistema(apelidoTratado);
	}

	// Metodo armazenarDados: grava o usuario completo em formato JSON.
	@Override
	public void armazenarDados(UsuarioDoSistema usuario) throws IOException {
		var raiz = carregarRaizComVariosUsuarios();
		if (!raiz.has("usuarios") || !raiz.path("usuarios").isObject()) {
			raiz.set("usuarios", conversorJson.createObjectNode());
		}
		var usuarios = (ObjectNode) raiz.path("usuarios");
		usuarios.set(usuario.obterApelido(), conversorJson.valueToTree(usuario));
		raiz.put("ultimoUsuario", usuario.obterApelido());
		conversorJson.writeValue(arquivoDados.toFile(), raiz);
	}

	// Metodo carregarUsuarioDoFormatoNovo: busca um usuario dentro do arquivo com varios usuarios.
	private UsuarioDoSistema carregarUsuarioDoFormatoNovo(JsonNode dadosJson, String apelido) throws IOException {
		var usuarios = dadosJson.path("usuarios");
		var usuarioJson = usuarios.path(apelido);
		if (usuarioJson.isMissingNode() || usuarioJson.isNull()) {
			return new UsuarioDoSistema(apelido);
		}
		return montarUsuarioAPartirDoJson(usuarioJson);
	}

	// Metodo carregarRaizComVariosUsuarios: cria ou migra a estrutura para suportar varios usuarios.
	private ObjectNode carregarRaizComVariosUsuarios() throws IOException {
		var raiz = conversorJson.createObjectNode();
		raiz.put("ultimoUsuario", "Usuario");
		raiz.set("usuarios", conversorJson.createObjectNode());

		if (Files.notExists(arquivoDados)) {
			return raiz;
		}

		var dadosJson = conversorJson.readTree(arquivoDados.toFile());
		if (dadosJson.has("usuarios")) {
			return (ObjectNode) dadosJson;
		}

		var usuarioAntigo = montarUsuarioAPartirDoJson(dadosJson);
		raiz.put("ultimoUsuario", usuarioAntigo.obterApelido());
		((ObjectNode) raiz.path("usuarios")).set(usuarioAntigo.obterApelido(), conversorJson.valueToTree(usuarioAntigo));
		return raiz;
	}

	// Metodo montarUsuarioAPartirDoJson: monta um usuario lendo listas novas ou antigas.
	private UsuarioDoSistema montarUsuarioAPartirDoJson(JsonNode dadosJson) throws IOException {
		var usuario = new UsuarioDoSistema(obterTexto(dadosJson, "apelido", "Usuario"));
		usuario.definirFavoritas(lerListaDeSeries(dadosJson.path("favoritas")));
		usuario.definirAssistidas(lerListaDeSeries(dadosJson.path("assistidas")));
		usuario.definirDesejoAssistir(lerListaDeSeries(dadosJson.path("desejoAssistir")));
		return usuario;
	}

	// Metodo lerListaDeSeries: aceita o JSON novo em lista e o JSON antigo com campo "series".
	private List<SerieDeTv> lerListaDeSeries(JsonNode listaJson) throws IOException {
		if (listaJson == null || listaJson.isMissingNode() || listaJson.isNull()) {
			return List.of();
		}
		var seriesJson = listaJson.has("series") ? listaJson.path("series") : listaJson;
		return conversorJson.readerForListOf(SerieDeTv.class).readValue(seriesJson);
	}

	// Metodo obterTexto: le um texto simples do JSON e usa padrao se estiver vazio.
	private String obterTexto(JsonNode dadosJson, String campo, String padrao) {
		var valor = dadosJson.path(campo);
		return valor.isMissingNode() || valor.isNull() || valor.asText().isBlank() ? padrao : valor.asText();
	}
}

