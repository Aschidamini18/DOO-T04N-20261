import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class PainelBusca extends JPanel {

    private final SerieService service;
    private final JanelaPrincipal janela;

    private final JTextField campoBusca = new JTextField(24);
    private final JButton botaoBuscar = JanelaPrincipal.botaoPrimario("Buscar");
    private final TabelaSeriesModel modelo = new TabelaSeriesModel();
    private final JTable tabela = new JTable(modelo);
    private final JLabel status = new JLabel(" ");

    public PainelBusca(SerieService service, JanelaPrincipal janela) {
        this.service = service;
        this.janela = janela;

        setBackground(JanelaPrincipal.FUNDO);
        setBorder(new EmptyBorder(20, 24, 20, 24));
        setLayout(new BorderLayout());

        JPanel cartao = JanelaPrincipal.criarCartao();

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topo.setOpaque(false);
        JLabel rotulo = new JLabel("Nome da serie:");
        rotulo.setFont(JanelaPrincipal.FONTE_NEGRITO);
        rotulo.setForeground(JanelaPrincipal.TEXTO);
        JanelaPrincipal.estilizarCampo(campoBusca);
        topo.add(rotulo);
        topo.add(campoBusca);
        topo.add(botaoBuscar);
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
        JButton favoritar = JanelaPrincipal.botaoSecundario("+ Favoritos");
        JButton assistida = JanelaPrincipal.botaoSecundario("+ Assistidas");
        JButton desejar = JanelaPrincipal.botaoSecundario("+ Quero assistir");
        acoes.add(detalhes);
        acoes.add(favoritar);
        acoes.add(assistida);
        acoes.add(desejar);

        status.setFont(JanelaPrincipal.FONTE_NORMAL);
        status.setForeground(JanelaPrincipal.TEXTO_SUAVE);
        status.setBorder(new EmptyBorder(8, 2, 0, 2));

        JPanel sul = new JPanel(new BorderLayout());
        sul.setOpaque(false);
        sul.add(acoes, BorderLayout.NORTH);
        sul.add(status, BorderLayout.SOUTH);
        cartao.add(sul, BorderLayout.SOUTH);

        add(cartao, BorderLayout.CENTER);

        botaoBuscar.addActionListener(e -> buscar());
        campoBusca.addActionListener(e -> buscar());
        detalhes.addActionListener(e -> verDetalhes());
        favoritar.addActionListener(e -> adicionar(TipoLista.FAVORITOS));
        assistida.addActionListener(e -> adicionar(TipoLista.ASSISTIDAS));
        desejar.addActionListener(e -> adicionar(TipoLista.DESEJA_ASSISTIR));
    }

    private void buscar() {
        final String termo = campoBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome de uma serie para buscar.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        botaoBuscar.setEnabled(false);
        status.setText("Buscando...");

        new SwingWorker<List<Serie>, Void>() {
            @Override
            protected List<Serie> doInBackground() {
                return service.buscar(termo);
            }

            @Override
            protected void done() {
                try {
                    List<Serie> resultado = get();
                    modelo.setSeries(resultado);
                    if (resultado.isEmpty()) {
                        status.setText("Nenhuma serie encontrada para \"" + termo + "\".");
                    } else if (service.isUltimaBuscaOffline()) {
                        status.setText(resultado.size()
                                + " resultado(s) do catalogo local (sem conexao com a API).");
                    } else {
                        status.setText(resultado.size() + " resultado(s) encontrados.");
                    }
                } catch (Exception ex) {
                    status.setText("Falha na busca.");
                    JOptionPane.showMessageDialog(PainelBusca.this,
                            "Ocorreu um erro ao buscar: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                } finally {
                    botaoBuscar.setEnabled(true);
                }
            }
        }.execute();
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

    private void adicionar(TipoLista tipo) {
        Serie s = selecionada();
        if (s == null) {
            return;
        }
        try {
            boolean ok = service.adicionarNaLista(tipo, s);
            if (ok) {
                status.setText("\"" + s.getNome() + "\" adicionada a " + tipo.getTitulo() + ".");
                janela.atualizarListas();
            } else {
                status.setText("\"" + s.getNome() + "\" ja estava em " + tipo.getTitulo() + ".");
            }
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this,
                    "A serie foi adicionada, mas houve erro ao salvar: " + ex.getMessage(),
                    "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
