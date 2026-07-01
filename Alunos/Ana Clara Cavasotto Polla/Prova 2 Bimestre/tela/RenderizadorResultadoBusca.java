package fag.tela;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import fag.dados.SerieDeTv;

//  define o texto mostrado em cada item do JList.
public class RenderizadorResultadoBusca extends DefaultListCellRenderer {

	// monta a linha visivel para cada serie.
	@Override
	public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice, boolean selecionado,
			boolean comFoco) {
		var componente = super.getListCellRendererComponent(lista, valor, indice, selecionado, comFoco);
		componente.setFont(EstiloVisualNetflix.FONTE_PADRAO);
		setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
		setOpaque(true);
		if (valor instanceof SerieDeTv serie) {
			setText("%s (%s) - %s".formatted(serie.obterNome(), serie.obterDataEstreia(),
					serie.obterNotaComoTexto()));
		}
		if (selecionado) {
			componente.setBackground(EstiloVisualNetflix.VERMELHO_ESCURO);
			componente.setForeground(EstiloVisualNetflix.TEXTO_CLARO);
		} else {
			componente.setBackground(
					indice % 2 == 0 ? EstiloVisualNetflix.FUNDO_SECUNDARIO : EstiloVisualNetflix.LINHA_TABELA);
			componente.setForeground(EstiloVisualNetflix.TEXTO_CLARO);
		}
		return componente;
	}
}

