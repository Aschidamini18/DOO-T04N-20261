package fag.dados;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// Esta classe armazena as informacoes de uma serie e prepara esses dados para aparecerem na tela.

//  representa uma serie de TV com todas as informacoes exigidas.
public class SerieDeTv {
	private int codigo;
	private String nome;
	private String idioma;
	private List<String> generos;
	private Double notaGeral;
	private String estado;
	private String dataEstreia;
	private String dataTermino;
	private String nomeEmissora;

	// Construtor vazio
	public SerieDeTv() {
		this.generos = new ArrayList<>();
	}

	// Construtor completo
	public SerieDeTv(int codigo, String nome, String idioma, List<String> generos, Double notaGeral, String estado,
			String dataEstreia, String dataTermino, String nomeEmissora) {
		this.codigo = codigo;
		this.nome = textoOuPadrao(nome, "Sem nome");
		this.idioma = textoOuPadrao(idioma, "Nao informado");
		this.generos = new ArrayList<>(generos == null ? List.of() : generos);
		this.notaGeral = notaGeral;
		this.estado = textoOuPadrao(estado, "Nao informado");
		this.dataEstreia = textoOuPadrao(dataEstreia, "Nao informado");
		this.dataTermino = textoOuPadrao(dataTermino, "Nao informado");
		this.nomeEmissora = textoOuPadrao(nomeEmissora, "Nao informado");
	}

	// Metodo obterCodigo: retorna o codigo da serie na API.
	public int obterCodigo() {
		return codigo;
	}

	// Metodo definirCodigo: altera o codigo da serie.
	public void definirCodigo(int codigo) {
		this.codigo = codigo;
	}

	// Metodo obterNome: retorna o nome da serie.
	public String obterNome() {
		return nome;
	}

	// Metodo definirNome: altera o nome da serie.
	public void definirNome(String nome) {
		this.nome = textoOuPadrao(nome, "Sem nome");
	}

	// Metodo obterIdioma: retorna o idioma da serie.
	public String obterIdioma() {
		return idioma;
	}

	// Metodo definirIdioma: altera o idioma da serie.
	public void definirIdioma(String idioma) {
		this.idioma = textoOuPadrao(idioma, "Nao informado");
	}

	// Metodo obterGeneros: retorna os generos da serie.
	public List<String> obterGeneros() {
		return generos;
	}

	// Metodo definirGeneros: altera os generos da serie.
	public void definirGeneros(List<String> generos) {
		this.generos = new ArrayList<>(generos == null ? List.of() : generos);
	}

	// Metodo obterNotaGeral: retorna a nota geral.
	public Double obterNotaGeral() {
		return notaGeral;
	}

	// Metodo definirNotaGeral: altera a nota geral.
	public void definirNotaGeral(Double notaGeral) {
		this.notaGeral = notaGeral;
	}

	// Metodo obterEstado: retorna o estado da serie.
	public String obterEstado() {
		return estado;
	}

	// Metodo definirEstado: altera o estado da serie.
	public void definirEstado(String estado) {
		this.estado = textoOuPadrao(estado, "Nao informado");
	}

	// Metodo obterDataEstreia: retorna a data de estreia.
	public String obterDataEstreia() {
		return dataEstreia;
	}

	// Metodo definirDataEstreia: altera a data de estreia.
	public void definirDataEstreia(String dataEstreia) {
		this.dataEstreia = textoOuPadrao(dataEstreia, "Nao informado");
	}

	// Metodo obterDataTermino: retorna a data de termino.
	public String obterDataTermino() {
		return dataTermino;
	}

	// Metodo definirDataTermino: altera a data de termino.
	public void definirDataTermino(String dataTermino) {
		this.dataTermino = textoOuPadrao(dataTermino, "Nao informado");
	}

	// Metodo obterNomeEmissora: retorna a emissora ou canal da serie.
	public String obterNomeEmissora() {
		return nomeEmissora;
	}

	// Metodo definirNomeEmissora: altera a emissora ou canal da serie.
	public void definirNomeEmissora(String nomeEmissora) {
		this.nomeEmissora = textoOuPadrao(nomeEmissora, "Nao informado");
	}

	// Metodo obterDataEstreiaComoData: converte a data para ordenar por estreia.
	public LocalDate obterDataEstreiaComoData() {
		try {
			return "Nao informado".equals(dataEstreia) ? null : LocalDate.parse(dataEstreia);
		} catch (DateTimeParseException erro) {
			return null;
		}
	}

	// Metodo obterNotaComoTexto: formata a nota para exibicao.
	public String obterNotaComoTexto() {
		return notaGeral == null ? "Sem nota" : "%.1f".formatted(notaGeral);
	}

	// Metodo obterGenerosComoTexto: junta os generos em um texto unico.
	public String obterGenerosComoTexto() {
		return generos == null || generos.isEmpty() ? "Nao informado" : String.join(", ", generos);
	}

	// Metodo obterDetalhes: monta os dados completos para a tela.
	public String obterDetalhes() {
		return """
				Nome: %s
				Idioma: %s
				Generos: %s
				Nota geral: %s
				Estado: %s
				Data de estreia: %s
				Data de termino: %s
				Emissora: %s
				""".formatted(nome, idioma, obterGenerosComoTexto(), obterNotaComoTexto(), estado, dataEstreia,
				dataTermino, nomeEmissora);
	}

	// Metodo toString: define como a serie aparece em listas simples.
	@Override
	public String toString() {
		return obterNome();
	}

	// Metodo textoOuPadrao: evita textos nulos ou vazios.
	private String textoOuPadrao(String valor, String padrao) {
		return valor == null || valor.isBlank() ? padrao : valor;
	}
}

