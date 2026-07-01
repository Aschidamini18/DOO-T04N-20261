import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class PainelLista extends JPanel {

    private final SerieService service;
    private final JanelaPrincipal janela;
    private final TipoLista tipo;

    private final TabelaSeriesModel modelo = new TabelaSeriesModel();
    private final JTable tabela = new JTable(modelo);
    private final JComboBox<OrdenacaoSeries> comboOrdem =
            new JComboBox<>(OrdenacaoSeries.values());
    private final JLabel contador = new JLabel(" ");

    public PainelLista(SerieService service, JanelaPrincipal janela, TipoLista tipo) {
        this.service = service;
        this.janela = janela;
        this.tipo = tipo;

        setBackground(JanelaPrincipal.FUNDO);
        setBorder(new EmptyBorder(20, 24, 20, 24));
        setLayout(new BorderLayout());

        JPanel cartao = JanelaPrincipal.criarCartao();

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topo.setOpaque(false);
        JLabel rotulo = new JLabel("Ordenar por:");
        rotulo.setFont(JanelaPrincipal.FONTE_NEGRITO);
        rotulo.setForeground(JanelaPrincipal.TEXTO);
        comboOrdem.setFont(JanelaPrincipal.FONTE_NORMAL);
        comboOrdem.setBackground(JanelaPrincipal.CARTAO);
        JButton atualizar = JanelaPrincipal.botaoSecundario("Atualizar");
        topo.add(rotulo);
        topo.add(comboOrdem);
        topo.add(atualizar);
        cartao.add(topo, BorderLayout.NORTH);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JanelaPrincipal.estilizarTabela(tabela);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(new LineBorder(JanelaPrincipal.BORDA, 1, true));
        scroll.getViewport().setBackground(JanelaPrincipal.CARTAO);
        cartao.add(scroll, BorderLayout.CENTER);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acoes.setOpaque(false);
        JButton detalhes = JanelaPrincipal.botaoSecundario("Ver detalhes");
        JButton remover = JanelaPrincipal.botaoPrimario("Remover da lista");
        acoes.add(detalhes);
        acoes.add(remover);

        contador.setFont(JanelaPrincipal.FONTE_NORMAL);
        contador.setForeground(JanelaPrincipal.TEXTO_SUAVE);
        contador.setBorder(new EmptyBorder(8, 2, 0, 2));

        JPanel sul = new JPanel(new BorderLayout());
        sul.setOpaque(false);
        sul.add(acoes, BorderLayout.NORTH);
        sul.add(contador, BorderLayout.SOUTH);
        cartao.add(sul, BorderLayout.SOUTH);

        add(cartao, BorderLayout.CENTER);

        comboOrdem.addActionListener(e -> recarregar());
        atualizar.addActionListener(e -> recarregar());
        detalhes.addActionListener(e -> verDetalhes());
        remover.addActionListener(e -> remover());

        recarregar();
    }

    public void recarregar() {
        OrdenacaoSeries ordem = (OrdenacaoSeries) comboOrdem.getSelectedItem();
        List<Serie> lista = service.listarOrdenado(tipo, ordem);
        modelo.setSeries(lista);
        contador.setText(lista.size() + " serie(s) em " + tipo.getTitulo() + ".");
    }

    private Serie selecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma serie na tabela.",
                    "Atencao", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return modelo.getSerieEm(tabela.convertRowIndexToModel(linha));
    }

    private void verDetalhes() {
        Serie s = selecionada();
        if (s != null) {
            new DialogoDetalhes(janela, s).setVisible(true);
        }
    }

    private void remover() {
        Serie s = selecionada();
        if (s == null) {
            return;
        }
        int op = JOptionPane.showConfirmDialog(this,
                "Remover \"" + s.getNome() + "\" de " + tipo.getTitulo() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            service.removerDaLista(tipo, s);
            recarregar();
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this,
                    "A serie foi removida, mas houve erro ao salvar: " + ex.getMessage(),
                    "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
