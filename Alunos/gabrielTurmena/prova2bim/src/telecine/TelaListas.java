package telecine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class TelasListas extends JFrame {

    private Usuario usuario;
    private UsuarioRepositorio usuarioRepositorio;
    private JComboBox<String> comboLista;
    private JTable tabelaListas;
    private JButton botaoRemover;
    private DefaultTableModel modeloTabela;
    //filtragem pelo que o professor pediu
    private JComboBox<String> comboOrdenacao;
    private List<Serie> listaExibida;
    

    public TelasListas(Usuario usuario, UsuarioRepositorio usuarioRepositorio) {
        this.usuario = usuario;
        this.usuarioRepositorio = usuarioRepositorio;

        setTitle("Minhas listas");
        setSize(900, 500);
        setLocationRelativeTo(null);

        criarComponentes();
        configurarEventos();
        carregarLista();
    }

    private void criarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        JPanel painelTopo = new JPanel(new FlowLayout());

        comboLista = new JComboBox<>(new String[]{
                "Favoritas",
                "Assistidas",
                "Assistir Mais Tarde"
        });
        //ordenar
        comboOrdenacao = new JComboBox<>(new String[]{
                "Ordem alfabética",
                "Nota geral",
                "Estado",
                "Data de estreia"
        });
        //listas comnbobox
        painelTopo.add(new JLabel("Escolha a lista:"));
        botaoRemover = new JButton("Remover da lista");
        //ordenação
        painelTopo.add(new JLabel("Ordenar por:"));
        
        //adicionando ao painel
        painelTopo.add(comboOrdenacao);
        painelTopo.add(comboLista);
        painelTopo.add(botaoRemover);

        modeloTabela = new DefaultTableModel();

        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Idioma");
        modeloTabela.addColumn("Gêneros");
        modeloTabela.addColumn("Nota");
        modeloTabela.addColumn("Estado");
        modeloTabela.addColumn("Estreia");
        modeloTabela.addColumn("Término");
        modeloTabela.addColumn("Emissora");

        tabelaListas = new JTable(modeloTabela);

        painelPrincipal.add(painelTopo, BorderLayout.NORTH);
        painelPrincipal.add(new JScrollPane(tabelaListas), BorderLayout.CENTER);

        add(painelPrincipal);
    }

    private void configurarEventos() {
        comboLista.addActionListener(e -> carregarLista());
        comboOrdenacao.addActionListener(e -> carregarLista());
        botaoRemover.addActionListener(e -> removerSerieSelecionada());
    }
    
    private void ordenarLista(List<Serie> lista) {
        String ordenacao = comboOrdenacao.getSelectedItem().toString();

        if (ordenacao.equals("Ordem alfabética")) {
            lista.sort(Comparator.comparing(
                    Serie::getNome,
                    Comparator.nullsLast(String::compareToIgnoreCase)
            ));
        }

        if (ordenacao.equals("Nota geral")) {
            lista.sort(Comparator.comparing(
                    Serie::getNotaGeral,
                    Comparator.nullsLast(Double::compareTo)
            ).reversed());
        }

        if (ordenacao.equals("Estado")) {
            lista.sort(Comparator.comparing(
                    Serie::getEstado,
                    Comparator.nullsLast(String::compareToIgnoreCase)
            ));
        }

        if (ordenacao.equals("Data de estreia")) {
            lista.sort(Comparator.comparing(
                    Serie::getDataEstreia,
                    Comparator.nullsLast(String::compareTo)
            ));
        }
    }

    private void carregarLista() {
        listaExibida = new ArrayList<>(buscarListaSelecionada());

        ordenarLista(listaExibida);

        modeloTabela.setRowCount(0);

        for (Serie serie : listaExibida) {
            modeloTabela.addRow(new Object[]{
                    serie.getNome(),
                    serie.getIdioma(),
                    String.join(", ", serie.getGenero()),
                    serie.getNotaGeral(),
                    serie.getEstado(),
                    serie.getDataEstreia(),
                    serie.getDataTermino(),
                    serie.getEmissora()
            });
        }
    }

    private List<Serie> buscarListaSelecionada() {
        String listaSelecionada = comboLista.getSelectedItem().toString();

        if (listaSelecionada.equals("Favoritas")) {
            return usuario.getFavoritas();
        }

        if (listaSelecionada.equals("Assistidas")) {
            return usuario.getAssistidas();
        }

        return usuario.getQueroAssistir();
    }
    
    private void removerSerieSelecionada() {
        int linhaSelecionada = tabelaListas.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma série para remover.");
            return;
        }

        List<Serie> lista = buscarListaSelecionada();

        Serie serieSelecionada = listaExibida.get(linhaSelecionada);

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja remover \"" + serieSelecionada.getNome() + "\" da lista?",
                "Confirmar remoção",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                lista.remove(serieSelecionada);
                usuarioRepositorio.salvar(usuario);

                JOptionPane.showMessageDialog(this, "Série removida com sucesso.");
                carregarLista();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao remover série: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}