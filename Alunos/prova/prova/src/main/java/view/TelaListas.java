package view;

import controller.UsuarioController;
import model.Serie;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaListas extends JFrame {
    private final UsuarioController usuarioController;

    private JTabbedPane abasCategorias;
    private JList<Serie> listaFavoritos;
    private JList<Serie> listaAssistidas;
    private JList<Serie> listaQueroAssistir;
    private DefaultListModel<Serie> modeloFav;
    private DefaultListModel<Serie> modeloAssis;
    private DefaultListModel<Serie> modeloQuero;
    private JLabel labelStatus;

    public TelaListas(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        configurarJanela();
        construirInterfaceGrafica();
        sincronizarListasInterface();
    }

    private void configurarJanela() {
        setTitle("Sist. Prova - Minhas Coleções Pessoais");
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
        painelPrincipal.add(criarAbasListas(), BorderLayout.CENTER);
        painelPrincipal.add(criarRodapeAcoes(), BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private JPanel criarCabecalho() {
        JPanel painelCabecalho = new JPanel(new BorderLayout(0, 12));
        painelCabecalho.setBackground(TelaPrincipal.COR_CARD_CLARO);
        painelCabecalho.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(244, 194, 219)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel titulo = new JLabel("Minhas listas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(TelaPrincipal.COR_TEXTO_MAIN);

        JLabel instrucao = new JLabel("Selecione uma aba, ordene a lista atual e use Ver detalhes para abrir a ficha completa.");
        instrucao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instrucao.setForeground(TelaPrincipal.COR_TEXTO_MUTED);

        JPanel painelTitulo = new JPanel(new GridLayout(2, 1, 0, 4));
        painelTitulo.setBackground(TelaPrincipal.COR_CARD_CLARO);
        painelTitulo.add(titulo);
        painelTitulo.add(instrucao);

        JPanel painelOrdenacao = new JPanel(new GridLayout(1, 4, 10, 0));
        painelOrdenacao.setBackground(TelaPrincipal.COR_CARD_CLARO);

        JButton btnNome = TelaPrincipal.criarBotaoEstilizado("Nome", TelaPrincipal.COR_AZUL_PADRAO);
        JButton btnNota = TelaPrincipal.criarBotaoEstilizado("Nota", TelaPrincipal.COR_AZUL_PADRAO);
        JButton btnStatus = TelaPrincipal.criarBotaoEstilizado("Status", TelaPrincipal.COR_AZUL_PADRAO);
        JButton btnData = TelaPrincipal.criarBotaoEstilizado("Estreia", TelaPrincipal.COR_AZUL_PADRAO);

        btnNome.addActionListener(e -> dispararOrdenacao("nome"));
        btnNota.addActionListener(e -> dispararOrdenacao("nota"));
        btnStatus.addActionListener(e -> dispararOrdenacao("status"));
        btnData.addActionListener(e -> dispararOrdenacao("data"));

        painelOrdenacao.add(btnNome);
        painelOrdenacao.add(btnNota);
        painelOrdenacao.add(btnStatus);
        painelOrdenacao.add(btnData);

        painelCabecalho.add(painelTitulo, BorderLayout.NORTH);
        painelCabecalho.add(painelOrdenacao, BorderLayout.CENTER);

        return painelCabecalho;
    }

    private JTabbedPane criarAbasListas() {
        modeloFav = new DefaultListModel<>();
        listaFavoritos = new JList<>(modeloFav);
        configurarRenderizadorDeNome(listaFavoritos);

        modeloAssis = new DefaultListModel<>();
        listaAssistidas = new JList<>(modeloAssis);
        configurarRenderizadorDeNome(listaAssistidas);

        modeloQuero = new DefaultListModel<>();
        listaQueroAssistir = new JList<>(modeloQuero);
        configurarRenderizadorDeNome(listaQueroAssistir);

        abasCategorias = new JTabbedPane();
        abasCategorias.setFont(new Font("Segoe UI", Font.BOLD, 13));
        abasCategorias.addTab("Favoritos", new JScrollPane(listaFavoritos));
        abasCategorias.addTab("Assistidos", new JScrollPane(listaAssistidas));
        abasCategorias.addTab("Quero Ver", new JScrollPane(listaQueroAssistir));
        abasCategorias.setBorder(BorderFactory.createLineBorder(new Color(244, 194, 219)));

        return abasCategorias;
    }

    private JPanel criarRodapeAcoes() {
        JPanel painelRodape = new JPanel(new BorderLayout(0, 10));
        painelRodape.setBackground(TelaPrincipal.COR_FUNDO_CLARO);

        labelStatus = new JLabel("Escolha um item para gerenciar.");
        labelStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelStatus.setForeground(TelaPrincipal.COR_TEXTO_MUTED);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 3, 10, 0));
        painelBotoes.setBackground(TelaPrincipal.COR_FUNDO_CLARO);

        JButton botaoVerDetalhes = TelaPrincipal.criarBotaoEstilizado("Ver detalhes", new Color(174, 96, 186));
        JButton botaoRemover = TelaPrincipal.criarBotaoEstilizado("Remover selecionado", new Color(198, 75, 109));
        JButton botaoVoltar = TelaPrincipal.criarBotaoEstilizado("← Voltar", new Color(177, 138, 163));

        botaoVerDetalhes.addActionListener(e -> DetalhesSerieDialog.exibir(this, obterSerieSelecionadaNaAbaAtual()));
        botaoRemover.addActionListener(e -> executarExclusaoItem());
        botaoVoltar.addActionListener(e -> voltarAoMenuPrincipal());

        painelBotoes.add(botaoVerDetalhes);
        painelBotoes.add(botaoRemover);
        painelBotoes.add(botaoVoltar);

        painelRodape.add(labelStatus, BorderLayout.NORTH);
        painelRodape.add(painelBotoes, BorderLayout.CENTER);

        return painelRodape;
    }

    private void configurarRenderizadorDeNome(JList<Serie> listaAlvo) {
        listaAlvo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaAlvo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaAlvo.setFixedCellHeight(34);
        listaAlvo.setCellRenderer((list, serie, index, isSelected, cellHasFocus) -> {
            DefaultListCellRenderer renderer = new DefaultListCellRenderer();
            JLabel label = (JLabel) renderer.getListCellRendererComponent(list, serie, index, isSelected, cellHasFocus);
            if (serie != null) {
                String statusTransmissao = valorOuPadrao(serie.getStatus());
                label.setText(String.format("%s  •  Nota %.1f  •  %s", serie.getNome(), serie.getNota(), statusTransmissao));
                label.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            }
            return label;
        });
    }

    private void sincronizarListasInterface() {
        povoarModelo(modeloFav, usuarioController.getUsuario().getFavoritos());
        povoarModelo(modeloAssis, usuarioController.getUsuario().getAssistidos());
        povoarModelo(modeloQuero, usuarioController.getUsuario().getQueroAssistir());
    }

    private void povoarModelo(DefaultListModel<Serie> model, List<Serie> listaOriginal) {
        model.clear();
        for (Serie serie : listaOriginal) {
            model.addElement(serie);
        }
    }

    private void dispararOrdenacao(String criterio) {
        List<Serie> listaAlvo = obterListaDaAbaAtual();
        DefaultListModel<Serie> modeloAlvo = obterModeloDaAbaAtual();

        if (listaAlvo == null || listaAlvo.isEmpty()) {
            labelStatus.setText("A lista atual está vazia.");
            return;
        }

        switch (criterio) {
            case "nome" -> usuarioController.ordenarListaPorNome(listaAlvo);
            case "nota" -> usuarioController.ordenarListaPorNota(listaAlvo);
            case "status" -> usuarioController.ordenarListaPorStatus(listaAlvo);
            case "data" -> usuarioController.ordenarListaPorDataEstreia(listaAlvo);
        }

        povoarModelo(modeloAlvo, listaAlvo);
        labelStatus.setText("Lista ordenada com sucesso.");
    }

    private void executarExclusaoItem() {
        Serie serieSelecionada = obterSerieSelecionadaNaAbaAtual();
        if (serieSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione um item primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int escolha = JOptionPane.showConfirmDialog(
                this,
                "Remover '" + serieSelecionada.getNome() + "' da lista atual?",
                "Confirmar remoção",
                JOptionPane.YES_NO_OPTION
        );

        if (escolha == JOptionPane.YES_OPTION) {
            int abaSelecionada = abasCategorias.getSelectedIndex();
            if (abaSelecionada == 0) usuarioController.desfavoritarSerie(serieSelecionada);
            else if (abaSelecionada == 1) usuarioController.removerDeAssistidas(serieSelecionada);
            else if (abaSelecionada == 2) usuarioController.removerListaQueroAssistir(serieSelecionada);

            sincronizarListasInterface();
            labelStatus.setText("Item removido com sucesso.");
        }
    }

    private List<Serie> obterListaDaAbaAtual() {
        int abaSelecionada = abasCategorias.getSelectedIndex();
        if (abaSelecionada == 0) return usuarioController.getUsuario().getFavoritos();
        if (abaSelecionada == 1) return usuarioController.getUsuario().getAssistidos();
        if (abaSelecionada == 2) return usuarioController.getUsuario().getQueroAssistir();
        return null;
    }

    private DefaultListModel<Serie> obterModeloDaAbaAtual() {
        int abaSelecionada = abasCategorias.getSelectedIndex();
        if (abaSelecionada == 0) return modeloFav;
        if (abaSelecionada == 1) return modeloAssis;
        return modeloQuero;
    }

    private Serie obterSerieSelecionadaNaAbaAtual() {
        int abaSelecionada = abasCategorias.getSelectedIndex();
        if (abaSelecionada == 0) return listaFavoritos.getSelectedValue();
        if (abaSelecionada == 1) return listaAssistidas.getSelectedValue();
        if (abaSelecionada == 2) return listaQueroAssistir.getSelectedValue();
        return null;
    }

    private String valorOuPadrao(String valor) {
        return (valor == null || valor.isBlank()) ? "Não informado" : valor;
    }

    private void voltarAoMenuPrincipal() {
        new TelaPrincipal(usuarioController).setVisible(true);
        dispose();
    }
}
