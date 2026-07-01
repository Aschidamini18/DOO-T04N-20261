package fag.tela;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import fag.dados.OpcaoDeOrdenacaoDeSeries;
import fag.dados.SerieDeTv;
import fag.dados.TipoDeListaDeSeries;
import fag.dados.UsuarioDoSistema;
import fag.arquivo.RepositorioDeDadosDoSistema;
import fag.servico.BuscadorDeSeriesNaApiTvMaze;

//  monta a janela do sistema, mostra os botoes, tabelas e resultados da busca.

//  cria todas as telas Swing usadas pelo sistema.
public class TelaPrincipalSistema extends JFrame {
	private final RepositorioDeDadosDoSistema repositorioDeDados;
	private final BuscadorDeSeriesNaApiTvMaze buscadorDeSeries;
	private UsuarioDoSistema usuario;
	private final JTextField campoApelido;
	private final JTextField campoBusca;
	private final JButton botaoBuscar;
	private final JList<SerieDeTv> listaResultadoBusca;
	private final JTextArea areaDetalhes;
	private final ModeloTabelaSeriesSalvas modeloFavoritas;
	private final ModeloTabelaSeriesSalvas modeloAssistidas;
	private final ModeloTabelaSeriesSalvas modeloDesejoAssistir;

	
	public TelaPrincipalSistema(RepositorioDeDadosDoSistema repositorioDeDados,
			BuscadorDeSeriesNaApiTvMaze buscadorDeSeries) throws IOException {
		super("Minhas Series de TV");
		this.repositorioDeDados = repositorioDeDados;
		this.buscadorDeSeries = buscadorDeSeries;
		this.usuario = repositorioDeDados.carregarDados();
		this.campoApelido = new JTextField(16);
		this.campoBusca = new JTextField(28);
		this.botaoBuscar = new JButton("Buscar");
		this.listaResultadoBusca = new JList<>(new ModeloListaResultadoBusca());
		this.areaDetalhes = new JTextArea();
		this.modeloFavoritas = new ModeloTabelaSeriesSalvas();
		this.modeloAssistidas = new ModeloTabelaSeriesSalvas();
		this.modeloDesejoAssistir = new ModeloTabelaSeriesSalvas();
		configurarJanela();
		montarTela();
		atualizarTabelas();
	}

	//  mostra erro quando o sistema nao consegue iniciar.
	public static void exibirErroFatal(Exception erro) {
		JOptionPane.showMessageDialog(null, "Erro ao iniciar o sistema: " + erro.getMessage(), "Erro",
				JOptionPane.ERROR_MESSAGE);
	}

	//  define tamanho e comportamento principal do JFrame.
	private void configurarJanela() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1080, 680));
		setLocationRelativeTo(null);
		getContentPane().setBackground(EstiloVisualNetflix.FUNDO_PRINCIPAL);
	}

	//  adiciona cabecalho e abas na janela.
	private void montarTela() {
		setTitle("FAGFLIX - Minhas Series de TV");
		add(criarPainelCabecalho(), BorderLayout.NORTH);
		add(criarPainelAbas(), BorderLayout.CENTER);
	}

	//  cria a area de nome ou apelido do usuario.
	private JPanel criarPainelCabecalho() {
		var painel = new JPanel(new BorderLayout(18, 0));
		var painelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		var titulo = new JLabel("FAGFLIX");
		var rotuloUsuario = new JLabel("Usuario:");
		var botaoSalvarUsuario = new JButton("Salvar usuario");
		EstiloVisualNetflix.aplicarPainelEscuro(painel);
		EstiloVisualNetflix.aplicarPainelEscuro(painelUsuario);
		EstiloVisualNetflix.aplicarRotulo(rotuloUsuario);
		EstiloVisualNetflix.aplicarCampoTexto(campoApelido);
		EstiloVisualNetflix.aplicarBotaoEscuro(botaoSalvarUsuario);
		titulo.setForeground(EstiloVisualNetflix.VERMELHO);
		titulo.setFont(EstiloVisualNetflix.FONTE_TITULO);
		painel.setBorder(BorderFactory.createEmptyBorder(18, 24, 12, 24));
		campoApelido.setText(usuario.obterApelido());
		botaoSalvarUsuario.addActionListener(evento -> salvarApelidoUsuario());
		painelUsuario.add(rotuloUsuario);
		painelUsuario.add(campoApelido);
		painelUsuario.add(botaoSalvarUsuario);
		painel.add(titulo, BorderLayout.WEST);
		painel.add(painelUsuario, BorderLayout.EAST);
		return painel;
	}

	// cria as abas de busca e listas.
	private JTabbedPane criarPainelAbas() {
		var abas = new JTabbedPane();
		abas.setBackground(EstiloVisualNetflix.FUNDO_PRINCIPAL);
		abas.setForeground(Color.BLACK);
		abas.setFont(EstiloVisualNetflix.FONTE_NEGRITO);
		abas.setBorder(BorderFactory.createEmptyBorder(0, 18, 18, 18));
		abas.addTab("Busca", criarPainelBusca());
		abas.addTab(TipoDeListaDeSeries.FAVORITAS.obterTitulo(),
				criarPainelLista(TipoDeListaDeSeries.FAVORITAS, modeloFavoritas));
		abas.addTab(TipoDeListaDeSeries.ASSISTIDAS.obterTitulo(),
				criarPainelLista(TipoDeListaDeSeries.ASSISTIDAS, modeloAssistidas));
		abas.addTab(TipoDeListaDeSeries.DESEJO_ASSISTIR.obterTitulo(),
				criarPainelLista(TipoDeListaDeSeries.DESEJO_ASSISTIR, modeloDesejoAssistir));
		configurarCoresDasAbas(abas);
		return abas;
	}

	//  
	private void configurarCoresDasAbas(JTabbedPane abas) {
		for (var indice = 0; indice < abas.getTabCount(); indice++) {
			abas.setForegroundAt(indice, Color.BLACK);
			abas.setBackgroundAt(indice, new Color(230, 230, 230));
		}
	}

	//  cria a tela para pesquisar series na API.
	private JPanel criarPainelBusca() {
		var painel = new JPanel(new BorderLayout(10, 10));
		EstiloVisualNetflix.aplicarPainelEscuro(painel);
		painel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

		var painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		var rotuloBusca = new JLabel("Nome da serie:");
		var textoAjuda = new JLabel("Busque, escolha e organize suas series.");
		EstiloVisualNetflix.aplicarPainelEscuro(painelBusca);
		EstiloVisualNetflix.aplicarRotulo(rotuloBusca);
		textoAjuda.setForeground(EstiloVisualNetflix.TEXTO_APAGADO);
		textoAjuda.setFont(EstiloVisualNetflix.FONTE_PADRAO);
		EstiloVisualNetflix.aplicarCampoTexto(campoBusca);
		EstiloVisualNetflix.aplicarBotaoVermelho(botaoBuscar);
		botaoBuscar.addActionListener(evento -> buscarSeries());
		campoBusca.addActionListener(evento -> buscarSeries());
		painelBusca.add(rotuloBusca);
		painelBusca.add(campoBusca);
		painelBusca.add(botaoBuscar);
		painelBusca.add(textoAjuda);

		listaResultadoBusca.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaResultadoBusca.setCellRenderer(new RenderizadorResultadoBusca());
		listaResultadoBusca.addListSelectionListener(evento -> exibirDetalhesDaSerieBuscada());
		listaResultadoBusca.setBackground(EstiloVisualNetflix.FUNDO_SECUNDARIO);
		listaResultadoBusca.setForeground(EstiloVisualNetflix.TEXTO_CLARO);
		listaResultadoBusca.setSelectionBackground(EstiloVisualNetflix.VERMELHO_ESCURO);
		listaResultadoBusca.setSelectionForeground(EstiloVisualNetflix.TEXTO_CLARO);
		listaResultadoBusca.setFont(EstiloVisualNetflix.FONTE_PADRAO);

		areaDetalhes.setEditable(false);
		areaDetalhes.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		areaDetalhes.setLineWrap(true);
		areaDetalhes.setWrapStyleWord(true);
		EstiloVisualNetflix.aplicarAreaTexto(areaDetalhes);
		areaDetalhes.setText("Pesquise uma serie para ver os detalhes aqui.");

		var painelBotoes = new JPanel(new GridLayout(1, 3, 8, 0));
		EstiloVisualNetflix.aplicarPainelSecundario(painelBotoes);
		painelBotoes.add(criarBotao("Adicionar em favoritas", () -> adicionarSerieSelecionada(TipoDeListaDeSeries.FAVORITAS)));
		painelBotoes.add(criarBotao("Adicionar em assistidas", () -> adicionarSerieSelecionada(TipoDeListaDeSeries.ASSISTIDAS)));
		painelBotoes.add(criarBotao("Adicionar em quero assistir",
				() -> adicionarSerieSelecionada(TipoDeListaDeSeries.DESEJO_ASSISTIR)));

		var painelDetalhes = new JPanel(new BorderLayout(8, 8));
		EstiloVisualNetflix.aplicarPainelSecundario(painelDetalhes);
		var rolagemDetalhes = new JScrollPane(areaDetalhes);
		var rolagemLista = new JScrollPane(listaResultadoBusca);
		EstiloVisualNetflix.aplicarRolagem(rolagemDetalhes);
		EstiloVisualNetflix.aplicarRolagem(rolagemLista);
		painelDetalhes.add(rolagemDetalhes, BorderLayout.CENTER);
		painelDetalhes.add(painelBotoes, BorderLayout.SOUTH);

		var divisao = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rolagemLista, painelDetalhes);
		divisao.setBackground(EstiloVisualNetflix.FUNDO_PRINCIPAL);
		divisao.setBorder(BorderFactory.createEmptyBorder());
		divisao.setDividerSize(8);
		divisao.setResizeWeight(0.35);

		painel.add(painelBusca, BorderLayout.NORTH);
		painel.add(divisao, BorderLayout.CENTER);
		return painel;
	}

	//  cria uma aba com JTable, ordenacao, detalhes e remocao.
	private JPanel criarPainelLista(TipoDeListaDeSeries tipoDeLista, ModeloTabelaSeriesSalvas modeloTabela) {
		var painel = new JPanel(new BorderLayout(10, 10));
		var tabela = new JTable(modeloTabela);
		var comboOrdenacao = new JComboBox<>(OpcaoDeOrdenacaoDeSeries.values());
		var botaoOrdenar = new JButton("Ordenar");
		var botaoDetalhes = new JButton("Ver detalhes");
		var botaoRemover = new JButton("Remover selecionada");
		var painelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		var rotuloOrdenar = new JLabel("Ordenar por:");

		EstiloVisualNetflix.aplicarPainelEscuro(painel);
		EstiloVisualNetflix.aplicarPainelEscuro(painelControles);
		EstiloVisualNetflix.aplicarRotulo(rotuloOrdenar);
		EstiloVisualNetflix.aplicarCombo(comboOrdenacao);
		EstiloVisualNetflix.aplicarBotaoVermelho(botaoOrdenar);
		EstiloVisualNetflix.aplicarBotaoEscuro(botaoDetalhes);
		EstiloVisualNetflix.aplicarBotaoEscuro(botaoRemover);
		EstiloVisualNetflix.aplicarTabela(tabela);
		painel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabela.setFillsViewportHeight(true);
		tabela.setShowVerticalLines(false);

		botaoOrdenar.addActionListener(evento -> ordenarLista(tipoDeLista, comboOrdenacao));
		botaoDetalhes.addActionListener(evento -> exibirDetalhesDaTabela(tabela, modeloTabela));
		botaoRemover.addActionListener(evento -> removerSerieSelecionada(tabela, tipoDeLista));

		painelControles.add(rotuloOrdenar);
		painelControles.add(comboOrdenacao);
		painelControles.add(botaoOrdenar);
		painelControles.add(botaoDetalhes);
		painelControles.add(botaoRemover);

		var rolagemTabela = new JScrollPane(tabela);
		EstiloVisualNetflix.aplicarRolagem(rolagemTabela);
		painel.add(painelControles, BorderLayout.NORTH);
		painel.add(rolagemTabela, BorderLayout.CENTER);
		return painel;
	}

	//  cria um JButton simples ligado a uma acao.
	private JButton criarBotao(String texto, Runnable acao) {
		var botao = new JButton(texto);
		EstiloVisualNetflix.aplicarBotaoVermelho(botao);
		botao.addActionListener(evento -> acao.run());
		return botao;
	}

	//  valida o texto e inicia a busca sem travar a tela.
	private void buscarSeries() {
		var nomeSerie = campoBusca.getText().trim();
		if (nomeSerie.isBlank()) {
			exibirAviso("Digite o nome de uma serie para buscar.");
			return;
		}

		botaoBuscar.setEnabled(false);
		areaDetalhes.setText("Buscando na API TVMaze...");

		CompletableFuture.supplyAsync(() -> executarBuscaComTratamento(nomeSerie))
				.whenComplete((series, erro) -> SwingUtilities.invokeLater(() -> finalizarBusca(series, erro)));
	}

	//  chama a API e converte erros em RuntimeException.
	private List<SerieDeTv> executarBuscaComTratamento(String nomeSerie) {
		try {
			return buscadorDeSeries.buscarSeriesPorNome(nomeSerie);
		} catch (IOException erro) {
			throw new RuntimeException("Falha ao acessar a API TVMaze. Verifique sua conexao.", erro);
		} catch (InterruptedException erro) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("A busca foi interrompida.", erro);
		}
	}

	// Metodo finalizarBusca: atualiza a tela depois da resposta da API.
	private void finalizarBusca(List<SerieDeTv> series, Throwable erro) {
		botaoBuscar.setEnabled(true);
		if (erro != null) {
			((ModeloListaResultadoBusca) listaResultadoBusca.getModel()).definirSeries(List.of());
			areaDetalhes.setText("");
			exibirErro(erro.getMessage());
			return;
		}
		((ModeloListaResultadoBusca) listaResultadoBusca.getModel()).definirSeries(series);
		areaDetalhes.setText(series.isEmpty() ? "Nenhuma serie encontrada."
				: "Selecione uma serie na lista ao lado para ver os detalhes.");
	}

	// Metodo adicionarSerieSelecionada: adiciona a serie da busca na lista escolhida.
	private void adicionarSerieSelecionada(TipoDeListaDeSeries tipoDeLista) {
		var serie = listaResultadoBusca.getSelectedValue();
		if (serie == null) {
			exibirAviso("Selecione uma serie da busca primeiro.");
			return;
		}

		var adicionou = usuario.adicionarSerie(tipoDeLista, serie);
		if (!adicionou) {
			exibirAviso("Essa serie ja esta na lista \"" + tipoDeLista.obterTitulo() + "\".");
			return;
		}
		atualizarTabelas();
		if (armazenarDados()) {
			exibirInformacao("Serie salva em \"" + tipoDeLista.obterTitulo() + "\" com sucesso.");
		}
	}

	// Metodo removerSerieSelecionada: remove a serie selecionada de uma tabela.
	private void removerSerieSelecionada(JTable tabela, TipoDeListaDeSeries tipoDeLista) {
		var linha = tabela.getSelectedRow();
		if (linha < 0) {
			exibirAviso("Selecione uma serie para remover.");
			return;
		}
		var serie = usuario.obterListaPorTipo(tipoDeLista).get(linha);
		usuario.removerSerie(tipoDeLista, serie);
		atualizarTabelas();
		armazenarDados();
	}

	// Metodo ordenarLista: ordena uma lista conforme a opcao escolhida.
	private void ordenarLista(TipoDeListaDeSeries tipoDeLista, JComboBox<OpcaoDeOrdenacaoDeSeries> comboOrdenacao) {
		var opcao = (OpcaoDeOrdenacaoDeSeries) comboOrdenacao.getSelectedItem();
		usuario.ordenarLista(tipoDeLista, opcao);
		atualizarTabelas();
		armazenarDados();
	}

	// Metodo exibirDetalhesDaSerieBuscada: mostra os dados da serie selecionada na busca.
	private void exibirDetalhesDaSerieBuscada() {
		var serie = listaResultadoBusca.getSelectedValue();
		if (serie != null) {
			areaDetalhes.setText(serie.obterDetalhes());
			areaDetalhes.setCaretPosition(0);
		}
	}

	// Metodo exibirDetalhesDaTabela: mostra detalhes da serie selecionada em uma lista.
	private void exibirDetalhesDaTabela(JTable tabela, ModeloTabelaSeriesSalvas modeloTabela) {
		var linha = tabela.getSelectedRow();
		if (linha < 0) {
			exibirAviso("Selecione uma serie para ver detalhes.");
			return;
		}
		JOptionPane.showMessageDialog(this, modeloTabela.obterSerieDaLinha(linha).obterDetalhes(), "Detalhes da serie",
				JOptionPane.INFORMATION_MESSAGE);
	}

	// Metodo salvarApelidoUsuario: salva o apelido digitado pelo usuario.
	private void salvarApelidoUsuario() {
		var apelidoDigitado = campoApelido.getText().trim();
		if (apelidoDigitado.isBlank()) {
			exibirAviso("Digite um nome de usuario.");
			return;
		}

		if (apelidoDigitado.equalsIgnoreCase(usuario.obterApelido())) {
			usuario.definirApelido(apelidoDigitado);
			campoApelido.setText(usuario.obterApelido());
			if (armazenarDados()) {
				exibirInformacao("Usuario salvo com sucesso.");
			}
			return;
		}

		try {
			repositorioDeDados.armazenarDados(usuario);
			usuario = repositorioDeDados.carregarDadosDoUsuario(apelidoDigitado);
			campoApelido.setText(usuario.obterApelido());
			atualizarTabelas();
			areaDetalhes.setText("Usuario \"" + usuario.obterApelido() + "\" carregado. Pesquise uma serie para ver os detalhes aqui.");
			exibirInformacao("Usuario \"" + usuario.obterApelido() + "\" carregado com sucesso.");
		} catch (IOException erro) {
			exibirErro("Nao foi possivel trocar de usuario: " + erro.getMessage());
		}
	}

	// Metodo armazenarDados: persiste os dados em JSON usando o repositorio.
	private boolean armazenarDados() {
		try {
			repositorioDeDados.armazenarDados(usuario);
			return true;
		} catch (IOException erro) {
			exibirErro("Nao foi possivel salvar os dados em JSON: " + erro.getMessage());
			return false;
		}
	}

	// Metodo atualizarTabelas: recarrega os dados visiveis nas tres tabelas.
	private void atualizarTabelas() {
		modeloFavoritas.definirSeries(usuario.obterFavoritas());
		modeloAssistidas.definirSeries(usuario.obterAssistidas());
		modeloDesejoAssistir.definirSeries(usuario.obterDesejoAssistir());
	}

	// Metodo exibirAviso: mostra mensagens de validacao para o usuario.
	private void exibirAviso(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem, "Atencao", JOptionPane.WARNING_MESSAGE);
	}

	// Metodo exibirInformacao: mostra mensagens de sucesso para o usuario.
	private void exibirInformacao(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
	}

	// Metodo exibirErro: mostra mensagens de erro para o usuario.
	private void exibirErro(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
	}
}

