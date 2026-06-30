package view;

import controller.UsuarioController;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {
    private final UsuarioController usuarioController;

    protected static final Color COR_FUNDO_CLARO = new Color(255, 241, 248);
    protected static final Color COR_CARD_CLARO  = new Color(255, 250, 253);
    protected static final Color COR_TEXTO_MAIN  = new Color(83, 45, 72);
    protected static final Color COR_TEXTO_MUTED = new Color(139, 94, 122);
    protected static final Color COR_AZUL_PADRAO = new Color(218, 79, 132);

    public TelaPrincipal(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        configurarJanela();
        construirInterfaceGrafica();
    }

    private void configurarJanela() {
        setTitle("Sist. Prova");
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                exibirConfirmacaoSaida();
            }
        });
    }

    private void construirInterfaceGrafica() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(0, 24));
        painelPrincipal.setBackground(COR_FUNDO_CLARO);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JPanel cardPrincipal = new JPanel(new BorderLayout(0, 24));
        cardPrincipal.setBackground(COR_CARD_CLARO);
        cardPrincipal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(244, 194, 219)),
                BorderFactory.createEmptyBorder(28, 28, 28, 28)
        ));

        cardPrincipal.add(criarCabecalho(), BorderLayout.NORTH);
        cardPrincipal.add(criarResumoListas(), BorderLayout.CENTER);
        cardPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);

        painelPrincipal.add(cardPrincipal, BorderLayout.CENTER);
        add(painelPrincipal);
    }

    private JPanel criarCabecalho() {
        JPanel painelCabecalho = new JPanel(new GridLayout(3, 1, 0, 6));
        painelCabecalho.setBackground(COR_CARD_CLARO);

        JLabel labelTituloSistema = new JLabel("Prova T04N", SwingConstants.CENTER);
        labelTituloSistema.setFont(new Font("Segoe UI", Font.BOLD, 30));
        labelTituloSistema.setForeground(COR_TEXTO_MAIN);

        JLabel labelSaudacao = new JLabel("Olá,", SwingConstants.CENTER);
        labelSaudacao.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        labelSaudacao.setForeground(COR_TEXTO_MUTED);

        JLabel labelNomeUsuario = new JLabel(usuarioController.getUsuario().getNome(), SwingConstants.CENTER);
        labelNomeUsuario.setFont(new Font("Segoe UI", Font.BOLD, 22));
        labelNomeUsuario.setForeground(COR_TEXTO_MAIN);

        painelCabecalho.add(labelTituloSistema);
        painelCabecalho.add(labelSaudacao);
        painelCabecalho.add(labelNomeUsuario);

        return painelCabecalho;
    }

    private JPanel criarResumoListas() {
        JPanel painelResumo = new JPanel(new GridLayout(1, 3, 14, 0));
        painelResumo.setBackground(COR_CARD_CLARO);

        painelResumo.add(criarCardEstatistica("Favoritas", usuarioController.getUsuario().getFavoritos().size()));
        painelResumo.add(criarCardEstatistica("Já Assistidas", usuarioController.getUsuario().getAssistidos().size()));
        painelResumo.add(criarCardEstatistica("Quero Assistir", usuarioController.getUsuario().getQueroAssistir().size()));

        return painelResumo;
    }

    private JPanel criarPainelBotoes() {
        JPanel painelBotoes = new JPanel(new GridLayout(1, 3, 12, 0));
        painelBotoes.setBackground(COR_CARD_CLARO);

        JButton botaoAbrirBusca = criarBotaoEstilizado("Buscar Novas Séries", COR_AZUL_PADRAO);
        JButton botaoAbrirListas = criarBotaoEstilizado("Ver Minhas Listas", new Color(201, 116, 186));
        JButton botaoSairSistema = criarBotaoEstilizado("Salvar e Sair", new Color(198, 75, 109));

        botaoAbrirBusca.addActionListener(e -> {
            new TelaBusca(usuarioController).setVisible(true);
            dispose();
        });
        botaoAbrirListas.addActionListener(e -> {
            new TelaListas(usuarioController).setVisible(true);
            dispose();
        });
        botaoSairSistema.addActionListener(e -> exibirConfirmacaoSaida());

        painelBotoes.add(botaoAbrirBusca);
        painelBotoes.add(botaoAbrirListas);
        painelBotoes.add(botaoSairSistema);

        return painelBotoes;
    }

    private JPanel criarCardEstatistica(String titulo, int valor) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(COR_FUNDO_CLARO);
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(COR_TEXTO_MUTED);

        JLabel lblValor = new JLabel(String.valueOf(valor), SwingConstants.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblValor.setForeground(COR_AZUL_PADRAO);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    public static JButton criarBotaoEstilizado(String texto, Color corBase) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botao.setBackground(corBase);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        return botao;
    }

    private void exibirConfirmacaoSaida() {
        int resultado = JOptionPane.showConfirmDialog(this, "Deseja salvar e sair?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (resultado == JOptionPane.YES_OPTION) {
            try {
                usuarioController.salvarDados();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }
    }
}
