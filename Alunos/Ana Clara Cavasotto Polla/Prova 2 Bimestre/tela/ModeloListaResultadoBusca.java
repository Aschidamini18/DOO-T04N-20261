package fag.tela;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import fag.dados.SerieDeTv;

// adapta uma lista de series para ser exibida em um JList.
public class ModeloListaResultadoBusca extends AbstractListModel<SerieDeTv> {
	private final List<SerieDeTv> series;

	public ModeloListaResultadoBusca() {
		this.series = new ArrayList<>();
	}

	// substitui os resultados exibidos na busca.
	public void definirSeries(List<SerieDeTv> novasSeries) {
		series.clear();
		series.addAll(novasSeries == null ? List.of() : novasSeries);
		fireContentsChanged(this, 0, Math.max(0, series.size() - 1));
	}

	// informa ao JList quantos itens existem.
	@Override
	public int getSize() {
		return series.size();
	}

	// retorna a serie de uma posicao especifica.
	@Override
	public SerieDeTv getElementAt(int indice) {
		return series.get(indice);
	}
}

