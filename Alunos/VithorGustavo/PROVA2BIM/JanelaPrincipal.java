import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class JanelaPrincipal extends JFrame {

    public static final Color FUNDO = new Color(0xF1F2F6);
    public static final Color BARRA = new Color(0x222B45);
    public static final Color BARRA_HOVER = new Color(0x303A5C);
    public static final Color ACENTO = new Color(0x6C63FF);
    public static final Color ACENTO_HOVER = new Color(0x564FE0);
    public static final Color CARTAO = Color.WHITE;
    public static final Color TEXTO = new Color(0x2B2D42);
    public static final Color TEXTO_SUAVE = new Color(0x8A8D9F);
    public static final Color TEXTO_BARRA = new Color(0xC9CDE0);
    public static final Color BORDA = new Color(0xE3E5ED);
    public static final Color SELECAO = new Color(0xE4E2FF);
    public static final Color LINHA_ALT = new Color(0xF7F8FB);

    public static final Font FONTE_TITULO = new Font("SansSerif", Font.BOLD, 20);
    public static final Font FONTE_SECAO = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FONTE_NORMAL = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONTE_NEGRITO = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FONTE_NAV = new Font("SansSerif", Font.BOLD, 14);

    private final SerieService service;
    private final PainelLista painelFavoritos;
    private final PainelLista painelAssistidas;
    private final PainelLista painelDeseja;

    private final CardLayout cartoes = new CardLayout();
    private final JPanel area = new JPanel(cartoes);
    private final List<JButton> navBotoes = new ArrayList<>();
    private int ativo = 0;

    private final String[] chaves = {"buscar", "favoritos", "assistidas", "deseja"};
    private final String[] titulos = {
            "Buscar series", "Meus favoritos", "Series assistidas", "Quero assistir"
    };
    private final String[] subtitulos = {
            "Pesquise na base do TVmaze e organize suas series",
            "As series que voce marcou como favoritas",
            "Tudo o que voce ja terminou de assistir",
            "Sua lista de proximas series para maratonar"
    };

    private final JLabel tituloSecao = new JLabel();
    private final JLabel subtituloSecao = new JLabel();

    public JanelaPrincipal(SerieService service) {
        this.service = service;

        setTitle("MinhasSeries");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(900, 580));
        setLayout(new BorderLayout());

        PainelBusca painelBusca = new PainelBusca(service, this);
        painelFavoritos = new PainelLista(service, this, TipoLista.FAVORITOS);
        painelAssistidas = new PainelLista(service, this, TipoLista.ASSISTIDAS);
        painelDeseja = new PainelLista(service, this, TipoLista.DESEJA_ASSISTIR);

        area.setBackground(FUNDO);
        area.add(painelBusca, chaves[0]);
        area.add(painelFavoritos, chaves[1]);
        area.add(painelAssistidas, chaves[2]);
        area.add(painelDeseja, chaves[3]);

        add(criarBarraLateral(), BorderLayout.WEST);
        add(criarConteudo(), BorderLayout.CENTER);

        selecionar(0);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                aoFechar();
            }
        });
    }

    private JPanel criarBarraLateral() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(BARRA);
        barra.setPreferredSize(new Dimension(220, 0));

        JPanel marca = new JPanel();
        marca.setOpaque(false);
        marca.setLayout(new BoxLayout(marca, BoxLayout.X_AXIS));
        marca.setBorder(new EmptyBorder(22, 20, 22, 20));

        PainelArredondado emblema = new PainelArredondado(12, ACENTO);
        emblema.setLayout(new BorderLayout());
        emblema.setPreferredSize(new Dimension(38, 38));
        emblema.setMaximumSize(new Dimension(38, 38));
        JLabel sigla = new JLabel("MS", SwingConstants.CENTER);
        sigla.setForeground(Color.WHITE);
        sigla.setFont(new Font("SansSerif", Font.BOLD, 16));
        emblema.add(sigla, BorderLayout.CENTER);

        JLabel nomeApp = new JLabel("  MinhasSeries");
        nomeApp.setForeground(Color.WHITE);
        nomeApp.setFont(new Font("SansSerif", Font.BOLD, 17));

        marca.add(emblema);
        marca.add(nomeApp);
        barra.add(marca, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(6, 12, 6, 12));

        String[] rotulos = {"Buscar", "Favoritos", "Assistidas", "Quero assistir"};
        for (int i = 0; i < rotulos.length; i++) {
            final int idx = i;
            JButton b = new JButton(rotulos[i]);
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setFont(FONTE_NAV);
            b.setForeground(TEXTO_BARRA);
            b.setBackground(BARRA);
            b.setOpaque(true);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setBorder(new EmptyBorder(11, 18, 11, 12));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            b.addActionListener(e -> selecionar(idx));
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (idx != ativo) {
                        b.setBackground(BARRA_HOVER);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    aplicarEstiloNav();
                }
            });
            navBotoes.add(b);
            menu.add(b);
            menu.add(Box.createVerticalStrut(4));
        }
        barra.add(menu, BorderLayout.CENTER);

        JPanel rodape = new JPanel();
        rodape.setOpaque(false);
        rodape.setLayout(new BoxLayout(rodape, BoxLayout.Y_AXIS));
        rodape.setBorder(new EmptyBorder(16, 20, 20, 20));

        JLabel etiqueta = new JLabel("Conectado como");
        etiqueta.setForeground(TEXTO_BARRA);
        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 11));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nomeUsuario = new JLabel(service.getUsuario().getNome());
        nomeUsuario.setForeground(Color.WHITE);
        nomeUsuario.setFont(new Font("SansSerif", Font.BOLD, 14));
        nomeUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        rodape.add(etiqueta);
        rodape.add(Box.createVerticalStrut(2));
        rodape.add(nomeUsuario);
        barra.add(rodape, BorderLayout.SOUTH);

        return barra;
    }

    private void aplicarEstiloNav() {
        for (int i = 0; i < navBotoes.size(); i++) {
            JButton b = navBotoes.get(i);
            if (i == ativo) {
                b.setBackground(ACENTO);
                b.setForeground(Color.WHITE);
            } else {
                b.setBackground(BARRA);
                b.setForeground(TEXTO_BARRA);
            }
        }
    }

    private JPanel criarConteudo() {
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(FUNDO);

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(FUNDO);
        cabecalho.setBorder(new CompoundBorder(
                new MatteBottom(BORDA),
                new EmptyBorder(18, 24, 16, 24)));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        tituloSecao.setFont(FONTE_TITULO);
        tituloSecao.setForeground(TEXTO);
        tituloSecao.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtituloSecao.setFont(FONTE_NORMAL);
        subtituloSecao.setForeground(TEXTO_SUAVE);
        subtituloSecao.setAlignmentX(Component.LEFT_ALIGNMENT);
        textos.add(tituloSecao);
        textos.add(Box.createVerticalStrut(3));
        textos.add(subtituloSecao);
        cabecalho.add(textos, BorderLayout.WEST);

        conteudo.add(cabecalho, BorderLayout.NORTH);
        conteudo.add(area, BorderLayout.CENTER);
        return conteudo;
    }

    private void selecionar(int idx) {
        ativo = idx;
        aplicarEstiloNav();
        cartoes.show(area, chaves[idx]);
        tituloSecao.setText(titulos[idx]);
        subtituloSecao.setText(subtitulos[idx]);
        if (idx == 1) {
            painelFavoritos.recarregar();
        } else if (idx == 2) {
            painelAssistidas.recarregar();
        } else if (idx == 3) {
            painelDeseja.recarregar();
        }
    }

    public void atualizarListas() {
        painelFavoritos.recarregar();
        painelAssistidas.recarregar();
        painelDeseja.recarregar();
    }

    private void aoFechar() {
        try {
            service.salvar();
        } catch (PersistenciaException ex) {
            int op = JOptionPane.showConfirmDialog(this,
                    "Nao foi possivel salvar os dados: " + ex.getMessage()
                            + "\nSair mesmo assim?",
                    "Erro ao salvar", JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            if (op != JOptionPane.YES_OPTION) {
                return;
            }
        }
        dispose();
        System.exit(0);
    }

    public static JButton botaoPrimario(String texto) {
        JButton b = botaoBase(texto);
        b.setBackground(ACENTO);
        b.setForeground(Color.WHITE);
        b.setBorder(new EmptyBorder(9, 20, 9, 20));
        adicionarHover(b, ACENTO, ACENTO_HOVER);
        return b;
    }

    public static JButton botaoSecundario(String texto) {
        JButton b = botaoBase(texto);
        b.setBackground(CARTAO);
        b.setForeground(TEXTO);
        b.setBorder(new CompoundBorder(
                new LineBorder(BORDA, 1, true),
                new EmptyBorder(8, 16, 8, 16)));
        adicionarHover(b, CARTAO, LINHA_ALT);
        return b;
    }

    private static JButton botaoBase(String texto) {
        JButton b = new JButton(texto);
        b.setFont(FONTE_NEGRITO);
        b.setOpaque(true);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static void adicionarHover(JButton b, Color normal, Color sobre) {
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (b.isEnabled()) {
                    b.setBackground(sobre);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(normal);
            }
        });
    }

    public static void estilizarCampo(JTextField campo) {
        campo.setFont(FONTE_NORMAL);
        campo.setBorder(new CompoundBorder(
                new LineBorder(BORDA, 1, true),
                new EmptyBorder(7, 10, 7, 10)));
    }

    public static JPanel criarCartao() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(CARTAO);
        p.setBorder(new CompoundBorder(
                new LineBorder(BORDA, 1, true),
                new EmptyBorder(16, 16, 16, 16)));
        return p;
    }

    public static void estilizarTabela(JTable t) {
        t.setRowHeight(30);
        t.setFont(FONTE_NORMAL);
        t.setForeground(TEXTO);
        t.setBackground(CARTAO);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(SELECAO);
        t.setSelectionForeground(TEXTO);
        t.setFillsViewportHeight(true);
        t.setDefaultRenderer(Object.class, new RenderizadorZebra());

        JTableHeader h = t.getTableHeader();
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(0, 36));
        DefaultTableCellRenderer hr = new DefaultTableCellRenderer();
        hr.setOpaque(true);
        hr.setBackground(BARRA);
        hr.setForeground(Color.WHITE);
        hr.setFont(FONTE_NEGRITO);
        hr.setHorizontalAlignment(SwingConstants.LEFT);
        hr.setBorder(new EmptyBorder(0, 10, 0, 10));
        h.setDefaultRenderer(hr);
    }

    static class PainelArredondado extends JPanel {
        private final int raio;
        private final Color cor;

        PainelArredondado(int raio, Color cor) {
            this.raio = raio;
            this.cor = cor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), raio, raio);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RenderizadorZebra extends DefaultTableCellRenderer {
        RenderizadorZebra() {
            setBorder(new EmptyBorder(0, 10, 0, 10));
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabela, Object valor,
                boolean selecionado, boolean foco, int linha, int coluna) {
            Component c = super.getTableCellRendererComponent(
                    tabela, valor, selecionado, foco, linha, coluna);
            if (!selecionado) {
                c.setBackground(linha % 2 == 0 ? CARTAO : LINHA_ALT);
                c.setForeground(TEXTO);
            }
            return c;
        }
    }

    static class MatteBottom extends LineBorder {
        MatteBottom(Color cor) {
            super(cor, 1);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int largura, int altura) {
            g.setColor(getLineColor());
            g.drawLine(x, y + altura - 1, x + largura, y + altura - 1);
        }

        @Override
        public java.awt.Insets getBorderInsets(Component c) {
            return new java.awt.Insets(0, 0, 1, 0);
        }
    }
}
