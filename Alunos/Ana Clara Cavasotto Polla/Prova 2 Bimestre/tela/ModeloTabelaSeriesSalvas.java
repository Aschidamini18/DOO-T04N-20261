package fag.tela;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import fag.dados.SerieDeTv;

//  adapta uma lista de series para ser exibida em uma JTable.
public class ModeloTabelaSeriesSalvas extends AbstractTableModel {
	private static final String[] COLUNAS = { "Nome", "Idioma", "Generos", "Nota", "Estado", "Estreia", "Termino",
			"Emissora" };
	private final List<SerieDeTv> series;

	public ModeloTabelaSeriesSalvas() {
		this.series = new ArrayList<>();
	}

	// atualiza as series exibidas na tabela.
	public void definirSeries(List<SerieDeTv> novasSeries) {
		series.clear();
		series.addAll(novasSeries == null ? List.of() : novasSeries);
		fireTableDataChanged();
	}

	//  retorna a serie selecionada pela linha.
	public SerieDeTv obterSerieDaLinha(int linha) {
		return series.get(linha);
	}

	// retorna a quantidade de linhas da tabela.
	@Override
	public int getRowCount() {
		return series.size();
	}

	// retorna a quantidade de colunas da tabela.
	@Override
	public int getColumnCount() {
		return COLUNAS.length;
	}

	// retorna o titulo da coluna.
	@Override
	public String getColumnName(int coluna) {
		return COLUNAS[coluna];
	}

	// retorna o valor apresentado em cada celula.
	@Override
	public Object getValueAt(int linha, int coluna) {
		var serie = series.get(linha);
		return switch (coluna) {
			case 0 -> serie.obterNome();
			case 1 -> serie.obterIdioma();
			case 2 -> serie.obterGenerosComoTexto();
			case 3 -> serie.obterNotaComoTexto();
			case 4 -> serie.obterEstado();
			case 5 -> serie.obterDataEstreia();
			case 6 -> serie.obterDataTermino();
			case 7 -> serie.obterNomeEmissora();
			default -> "";
		};
	}
}

