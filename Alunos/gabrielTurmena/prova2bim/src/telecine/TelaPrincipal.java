package telecine;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaPrincipal extends JFrame {

	//declarando as variáveis Java do código em si que vão receber valores (os botões e a tabela e as variáveis que herdam dos metodos)
    private JTextField campoPesquisa;
    private JButton botaoPesquisar;
    private JButton botaoFavoritar;
    private JButton botaoAssistida;
    private JButton botaoDesejo;
    private JButton botaoVerListas;
    private JTable tabelaSeries;
    private DefaultTableModel modeloTabela;
    //acima tudo swing jpanel
    //abaixo manipulação dos JSON dos objetos e dos usuários e listas de favoritos
    private List<Serie> seriesEncontradas;
    private Usuario usuario;
    private UsuarioRepositorio usuarioRepositorio;
    private ServicoTvMaze servicoTvMaze;

    public TelaPrincipal() {
    	//inicializando tudo os construtores das classes
        servicoTvMaze = new ServicoTvMaze();
        usuarioRepositorio = new UsuarioRepositorio();
        usuario = usuarioRepositorio.carregar();
        usuarioRepositorio = new UsuarioRepositorio();
        seriesEncontradas = new ArrayList<>();
        // AQUI INICIALIZA
        setTitle("Séries TV");
        setSize(900, 500);
        //muito importante se eu fechar a tela e não tiver essa bomba ele roda infinito e come meu pc
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        criarComponentes();
        configurarEventos();
    }

    private void criarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        JPanel painelBusca = new JPanel(new FlowLayout());
        
//campo de pesquisa
        JLabel labelPesquisa = new JLabel("Nome da série:");
        campoPesquisa = new JTextField(25);
        botaoPesquisar = new JButton("Pesquisar");
//adicionando ao painel de busca elementos dele
        painelBusca.add(labelPesquisa);
        painelBusca.add(campoPesquisa);
        painelBusca.add(botaoPesquisar);

        modeloTabela = new DefaultTableModel(); //modelo padrão de tabela

        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Idioma");
        modeloTabela.addColumn("Gêneros");
        modeloTabela.addColumn("Nota");
        modeloTabela.addColumn("Estado");
        modeloTabela.addColumn("Estreia");
        modeloTabela.addColumn("Término");
        modeloTabela.addColumn("Emissora");

        tabelaSeries = new JTable(modeloTabela);
        JPanel painelBotoes = new JPanel(new FlowLayout());

        JScrollPane scrollTabela = new JScrollPane(tabelaSeries);


        botaoFavoritar = new JButton("Adicionar aos favoritos");
        botaoAssistida = new JButton("Adicionar às assistidas");
        botaoDesejo = new JButton("Adicionar à assistir mais tarde");
        botaoVerListas = new JButton("Acessar Listas");
        
        painelBotoes.add(botaoFavoritar);
        painelBotoes.add(botaoAssistida);
        painelBotoes.add(botaoDesejo);
        painelBotoes.add(botaoVerListas);

        painelPrincipal.add(painelBusca, BorderLayout.NORTH);
        painelPrincipal.add(scrollTabela, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);
    }
//método que tem todos os eventos da tela em si cada tela tem um desse a segunda tela tem um no caso
    private void configurarEventos() {
    	
    	
    	botaoVerListas.addActionListener(e -> {
    		TelasListas telaListas = new TelasListas(usuario, usuarioRepositorio);
    	    telaListas.setVisible(true);
    	});
    	
    	//botão de pesquisa
        botaoPesquisar.addActionListener(e -> pesquisarSerie());
      //botão de favoritar
        botaoFavoritar.addActionListener(e -> {
        	  Serie serie = getSerieSelecionada();

        	    if (serie != null) {
        	        try {
        	            usuario.adicionarFavorita(serie);
        	            usuarioRepositorio.salvar(usuario);

        	            JOptionPane.showMessageDialog(this, "Série adicionada aos favoritos.");
        	        } catch (Exception ex) {
        	            JOptionPane.showMessageDialog(
        	                    this,
        	                    "Erro ao salvar favorito: " + ex.getMessage(),
        	                    "Erro",
        	                    JOptionPane.ERROR_MESSAGE
        	            );
        	        }
        	    }});
      //botão de assistida
        botaoAssistida.addActionListener(e -> {
            Serie serie = getSerieSelecionada();

            if (serie != null) {
                try {
                    usuario.adicionarAssistida(serie);
                    usuarioRepositorio.salvar(usuario);

                    JOptionPane.showMessageDialog(this, "Série adicionada à lista de desejo.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Erro ao salvar série na lista de desejo: " + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
      //botão de desejo ver
        botaoDesejo.addActionListener(e -> {
            Serie serie = getSerieSelecionada();

            if (serie != null) {
                try {
                    usuario.adicionarQueroAssistir(serie);
                    usuarioRepositorio.salvar(usuario);

                    JOptionPane.showMessageDialog(this, "Série adicionada à lista de desejo.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Erro ao salvar série na lista de desejo: " + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        
    }
//pesquisar ela na API
    private void pesquisarSerie() {
        String nome = campoPesquisa.getText();

        try {
        	seriesEncontradas = servicoTvMaze.buscarSeriesPorNome(nome);

            modeloTabela.setRowCount(0);

            if (seriesEncontradas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma série encontrada.");
                return;
            }

            for (Serie serie : seriesEncontradas) {
                modeloTabela.addRow(new Object[]{
                		//usando a classe série para getters e setter e fazendo POO
                        serie.getNome(),
                        serie.getIdioma(),
                        serie.getGenero(),
                        serie.getNotaGeral(),
                        serie.getEstado(),
                        serie.getDataEstreia(),
                        serie.getDataTermino(),
                        serie.getEmissora()
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao buscar série: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
     
        }
    }
    //adicionar as listas
    private Serie getSerieSelecionada() {
        int linhaSelecionada = tabelaSeries.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma série na tabela.");
            return null;
        }

        return seriesEncontradas.get(linhaSelecionada);
    }
    
}