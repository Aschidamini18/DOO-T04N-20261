package view;

import controller.TvMazeController;
import controller.UsuarioController;
import model.Serie;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaBusca extends JFrame {
    private final UsuarioController usuarioController;
    private final TvMazeController tvMazeController;

    private JTextField campoEntradaBusca;
    private JList<Serie> listaResultados;
    private DefaultListModel<Serie> modeloListaResultados;
    private JLabel labelStatusBusca;

    public TelaBusca(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        this.tvMazeController = new TvMazeController();
        configurarJanela();
        construirInterfaceGrafica();
    }

    private void configurarJanela() {
        setTitle("Sist. Prova - Buscar Catálogo");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void construirInterfaceGrafica() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(0, 16));
        painelPrincipal.setBackground(TelaPrincipal.COR_FUNDO_CLARO);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painelPrincipal.add(criarCabecalho(), BorderLayout.NORTH);
        painelPrincipal.add(criarListaResultados(), BorderLayout.CENTER);
        painelPrincipal.add(criarPainelAcoes(), BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private JPanel criarCabecalho() {
        JPanel painelCabecalho = new JPanel(new BorderLayout(0, 12));
        painelCabecalho.setBackground(TelaPrincipal.COR_CARD_CLARO);
        painelCabecalho.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(244, 194, 219)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel titulo = new JLabel("Buscar filmes e séries");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(TelaPrincipal.COR_TEXTO_MAIN);

        JLabel instrucao = new JLabel("Digite o nome, pesquise na API e selecione um resultado para ver detalhes ou salvar em uma lista.");
        instrucao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instrucao.setForeground(TelaPrincipal.COR_TEXTO_MUTED);

        campoEntradaBusca = new JTextField();
        campoEntradaBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoEntradaBusca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 169, 205)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        campoEntradaBusca.addActionListener(e -> executarBuscaApi());

        JButton botaoPesquisar = TelaPrincipal.criarBotaoEstilizado("Pesquisar", TelaPrincipal.COR_AZUL_PADRAO);
        botaoPesquisar.addActionListener(e -> executarBuscaApi());

        JPanel painelTitulo = new JPanel(new GridLayout(2, 1, 0, 4));
        painelTitulo.setBackground(TelaPrincipal.COR_CARD_CLARO);
        painelTitulo.add(titulo);
        painelTitulo.add(instrucao);

        JPanel painelPesquisa = new JPanel(new BorderLayout(10, 0));
        painelPesquisa.setBackground(TelaPrincipal.COR_CARD_CLARO);
        painelPesquisa.add(campoEntradaBusca, BorderLayout.CENTER);
        painelPesquisa.add(botaoPesquisar, BorderLayout.EAST);

        painelCabecalho.add(painelTitulo, BorderLayout.NORTH);
        painelCabecalho.add(painelPesquisa, BorderLayout.CENTER);

        return painelCabecalho;
    }

    private JScrollPane criarListaResultados() {
        modeloListaResultados = new DefaultListModel<>();
        listaResultados = new JList<>(modeloListaResultados);
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaResultados.setFixedCellHeight(34);
        listaResultados.setCellRenderer((list, serie, index, isSelected, cellHasFocus) -> {
            DefaultListCellRenderer renderer = new DefaultListCellRenderer();
            JLabel label = (JLabel) renderer.getListCellRendererComponent(list, serie, index, isSelected, cellHasFocus);
            if (serie != null) {
                label.setText(String.format("%s  •  Nota %.1f  •  %s", serie.getNome(), serie.getNota(), valorOuPadrao(serie.getStatus())));
                label.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            }
            return label;
        });

        JScrollPane scrollLista = new JScrollPane(listaResultados);
        scrollLista.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(244, 194, 219)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return scrollLista;
    }

    private JPanel criarPainelAcoes() {
        JPanel painelRodape = new JPanel(new BorderLayout(0, 10));
        painelRodape.setBackground(TelaPrincipal.COR_FUNDO_CLARO);

        labelStatusBusca = new JLabel("Pesquise para carregar resultados.");
        labelStatusBusca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelStatusBusca.setForeground(TelaPrincipal.COR_TEXTO_MUTED);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 5, 10, 0));
        painelBotoes.setBackground(TelaPrincipal.COR_FUNDO_CLARO);

        JButton botaoVerDetalhes = TelaPrincipal.criarBotaoEstilizado("Ver detalhes", new Color(174, 96, 186));
        JButton botaoAdicionarFav = TelaPrincipal.criarBotaoEstilizado("Favoritar", new Color(226, 139, 170));
        JButton botaoAdicionarAssistido = TelaPrincipal.criarBotaoEstilizado("Já assisti", new Color(201, 116, 186));
        JButton botaoAdicionarQuero = TelaPrincipal.criarBotaoEstilizado("Quero assistir", TelaPrincipal.COR_AZUL_PADRAO);
        JButton botaoVoltarMenu = TelaPrincipal.criarBotaoEstilizado("← Voltar", new Color(177, 138, 163));

        botaoVerDetalhes.addActionListener(e -> DetalhesSerieDialog.exibir(this, obterSerieSelecionada()));
        botaoAdicionarFav.addActionListener(e -> gerenciarInclusaoLista("favoritos"));
        botaoAdicionarAssistido.addActionListener(e -> gerenciarInclusaoLista("assistidas"));
        botaoAdicionarQuero.addActionListener(e -> gerenciarInclusaoLista("queroAssistir"));
        botaoVoltarMenu.addActionListener(e -> voltarAoMenuPrincipal());

        painelBotoes.add(botaoVerDetalhes);
        painelBotoes.add(botaoAdicionarFav);
        painelBotoes.add(botaoAdicionarAssistido);
        painelBotoes.add(botaoAdicionarQuero);
        painelBotoes.add(botaoVoltarMenu);

        painelRodape.add(labelStatusBusca, BorderLayout.NORTH);
        painelRodape.add(painelBotoes, BorderLayout.CENTER);

        return painelRodape;
    }

    private void executarBuscaApi() {
        String texto = campoEntradaBusca.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome de uma série ou filme para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloListaResultados.clear();
        labelStatusBusca.setText("Buscando dados na API...");

        SwingWorker<List<Serie>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Serie> doInBackground() {
                return tvMazeController.consultarSeriesPorNome(texto);
            }

            @Override
            protected void done() {
                try {
                    List<Serie> resultado = get();
                    if (resultado.isEmpty()) {
                        labelStatusBusca.setText("Nenhum resultado encontrado.");
                        return;
                    }

                    for (Serie serie : resultado) {
                        modeloListaResultados.addElement(serie);
                    }
                    labelStatusBusca.setText("Resultados carregados. Selecione um item e clique em Ver detalhes.");
                } catch (Exception e) {
                    labelStatusBusca.setText("Erro ao consultar a API.");
                }
            }
        };
        worker.execute();
    }

    private void gerenciarInclusaoLista(String tipoLista) {
        Serie serieSelecionada = obterSerieSelecionada();
        if (serieSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma série da lista primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        switch (tipoLista) {
            case "favoritos" -> usuarioController.favoritarSerie(serieSelecionada);
            case "assistidas" -> usuarioController.marcarComoAssistida(serieSelecionada);
            case "queroAssistir" -> usuarioController.adicionarListaQueroAssistir(serieSelecionada);
        }

        JOptionPane.showMessageDialog(this, "Item adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private Serie obterSerieSelecionada() {
        return listaResultados.getSelectedValue();
    }

    private String valorOuPadrao(String valor) {
        return (valor == null || valor.isBlank()) ? "Não informado" : valor;
    }

    private void voltarAoMenuPrincipal() {
        new TelaPrincipal(usuarioController).setVisible(true);
        dispose();
    }
}
