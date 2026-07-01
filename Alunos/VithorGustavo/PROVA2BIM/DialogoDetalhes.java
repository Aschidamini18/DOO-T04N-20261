import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class DialogoDetalhes extends JDialog {

    public DialogoDetalhes(Window dono, Serie serie) {
        super(dono, "Detalhes da serie", ModalityType.APPLICATION_MODAL);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(JanelaPrincipal.CARTAO);

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(JanelaPrincipal.ACENTO);
        cabecalho.setBorder(new EmptyBorder(18, 20, 18, 20));
        JLabel nome = new JLabel(serie.getNome());
        nome.setForeground(Color.WHITE);
        nome.setFont(new Font("SansSerif", Font.BOLD, 19));
        JLabel estado = new JLabel(serie.getStatus().getDescricao()
                + "   |   Nota: " + serie.getNotaFormatada());
        estado.setForeground(new Color(0xEDECFF));
        estado.setFont(JanelaPrincipal.FONTE_NORMAL);
        JPanel textoTopo = new JPanel(new BorderLayout(0, 4));
        textoTopo.setOpaque(false);
        textoTopo.add(nome, BorderLayout.NORTH);
        textoTopo.add(estado, BorderLayout.SOUTH);
        cabecalho.add(textoTopo, BorderLayout.WEST);
        getContentPane().add(cabecalho, BorderLayout.NORTH);

        JPanel info = new JPanel(new GridBagLayout());
        info.setBackground(JanelaPrincipal.CARTAO);
        info.setBorder(new EmptyBorder(18, 20, 10, 20));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;

        int linha = 0;
        linha = adicionarLinha(info, g, linha, "Idioma:", serie.getIdioma());
        linha = adicionarLinha(info, g, linha, "Generos:", serie.getGenerosFormatado());
        linha = adicionarLinha(info, g, linha, "Nota geral:", serie.getNotaFormatada());
        linha = adicionarLinha(info, g, linha, "Estado:", serie.getStatus().getDescricao());
        linha = adicionarLinha(info, g, linha, "Data de estreia:", serie.getDataEstreiaFormatada());
        linha = adicionarLinha(info, g, linha, "Data de termino:", serie.getDataTerminoFormatada());
        linha = adicionarLinha(info, g, linha, "Emissora:", serie.getEmissora());
        getContentPane().add(info, BorderLayout.CENTER);

        JPanel sul = new JPanel(new BorderLayout());
        sul.setBackground(JanelaPrincipal.CARTAO);
        sul.setBorder(new EmptyBorder(0, 20, 16, 20));

        String resumo = serie.getResumo();
        if (resumo != null && !resumo.isEmpty()) {
            JTextArea area = new JTextArea(resumo);
            area.setWrapStyleWord(true);
            area.setLineWrap(true);
            area.setEditable(false);
            area.setFont(JanelaPrincipal.FONTE_NORMAL);
            area.setForeground(JanelaPrincipal.TEXTO);
            area.setBackground(JanelaPrincipal.LINHA_ALT);
            area.setBorder(new EmptyBorder(10, 12, 10, 12));
            JScrollPane scroll = new JScrollPane(area);
            scroll.setBorder(new EmptyBorder(0, 0, 12, 0));
            scroll.setPreferredSize(new Dimension(440, 120));
            sul.add(scroll, BorderLayout.CENTER);
        }

        JButton fechar = JanelaPrincipal.botaoPrimario("Fechar");
        fechar.addActionListener(e -> dispose());
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.add(fechar, BorderLayout.EAST);
        sul.add(rodape, BorderLayout.SOUTH);
        getContentPane().add(sul, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(500, getHeight()));
        setLocationRelativeTo(dono);
    }

    private int adicionarLinha(JPanel painel, GridBagConstraints g, int linha,
                               String rotulo, String valor) {
        g.gridx = 0;
        g.gridy = linha;
        g.weightx = 0;
        JLabel lbRotulo = new JLabel(rotulo);
        lbRotulo.setFont(JanelaPrincipal.FONTE_NEGRITO);
        lbRotulo.setForeground(JanelaPrincipal.TEXTO_SUAVE);
        painel.add(lbRotulo, g);

        g.gridx = 1;
        g.weightx = 1;
        JLabel lbValor = new JLabel(valor == null ? "-" : valor);
        lbValor.setFont(JanelaPrincipal.FONTE_NORMAL);
        lbValor.setForeground(JanelaPrincipal.TEXTO);
        painel.add(lbValor, g);
        return linha + 1;
    }
}
