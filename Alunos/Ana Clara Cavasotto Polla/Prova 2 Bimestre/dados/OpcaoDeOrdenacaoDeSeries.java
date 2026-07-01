package fag.dados;

import java.time.LocalDate;
import java.util.Comparator;

// Esta classe guarda as regras de ordenacao que o usuario pode escolher nas listas.

// Enum OpcaoDeOrdenacaoDeSeries: concentra as formas de ordenar as listas.
public enum OpcaoDeOrdenacaoDeSeries {
	NOME("Ordem alfabetica", Comparator.comparing(SerieDeTv::obterNome, String.CASE_INSENSITIVE_ORDER)),
	NOTA("Nota geral",
			Comparator.comparing(SerieDeTv::obterNotaGeral, Comparator.nullsLast(Comparator.reverseOrder()))),
	ESTADO("Estado da serie", Comparator.comparing(SerieDeTv::obterEstado, String.CASE_INSENSITIVE_ORDER)),
	DATA_ESTREIA("Data de estreia",
			Comparator.comparing(SerieDeTv::obterDataEstreiaComoData, Comparator.nullsLast(LocalDate::compareTo)));

	private final String titulo;
	private final Comparator<SerieDeTv> comparador;

	// Construtor do enum: recebe o titulo e a regra de comparacao.
	OpcaoDeOrdenacaoDeSeries(String titulo, Comparator<SerieDeTv> comparador) {
		this.titulo = titulo;
		this.comparador = comparador;
	}

	// Metodo obterComparador: devolve a regra usada para ordenar.
	public Comparator<SerieDeTv> obterComparador() {
		return comparador;
	}

	// Metodo toString: mostra o titulo da opcao no JComboBox.
	@Override
	public String toString() {
		return titulo;
	}
}

