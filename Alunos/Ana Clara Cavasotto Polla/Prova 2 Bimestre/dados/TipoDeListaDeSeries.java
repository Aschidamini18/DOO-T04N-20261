package fag.dados;

// Classe que identifica as tres listas que o usuario pode usar para organizar as series.

// Enum TipoDeListaDeSeries: identifica as listas que o usuario pode manipular.
public enum TipoDeListaDeSeries {
	FAVORITAS("Favoritas"),
	ASSISTIDAS("Ja assistidas"),
	DESEJO_ASSISTIR("Quero assistir");

	private final String titulo;

	// Construtor do enum: guarda o texto exibido na tela principal.
	TipoDeListaDeSeries(String titulo) {
		this.titulo = titulo;
	}

	// Metodo obterTitulo: retorna o texto amigavel do tipo de lista.
	public String obterTitulo() {
		return titulo;
	}
}

