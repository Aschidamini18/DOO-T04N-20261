package fag;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import fag.arquivo.RepositorioJsonComJackson;
import fag.servico.BuscadorDeSeriesNaApiTvMaze;
import fag.tela.TelaPrincipalSistema;


public class Principal {

	// inicia a aplicacao Swing com tratamento de erro.
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				var repositorio = new RepositorioJsonComJackson();
				var buscadorDeSeries = new BuscadorDeSeriesNaApiTvMaze();
				var tela = new TelaPrincipalSistema(repositorio, buscadorDeSeries);
				tela.setVisible(true);
			} catch (Exception erro) {
				TelaPrincipalSistema.exibirErroFatal(erro);
			}
		});
	}
}

