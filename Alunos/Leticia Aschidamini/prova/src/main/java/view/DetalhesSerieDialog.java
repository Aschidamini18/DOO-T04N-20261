package view;

import model.Serie;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class DetalhesSerieDialog extends JDialog {
    private final Serie serie;
    private final JLabel labelImagem;

    private DetalhesSerieDialog(Window janelaPai, Serie serie) {
        super(janelaPai, "Detalhes - " + serie.getNome(), ModalityType.APPLICATION_MODAL);
        this.serie = serie;
        this.labelImagem = new JLabel("Carregando imagem...", SwingConstants.CENTER);

        configurarJanela();
        construirInterface();
        carregarImagem();
    }

    public static void exibir(Component componentePai, Serie serie) {
        if (serie == null) {
            JOptionPane.showMessageDialog(componentePai, "Selecione uma série primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Window janelaPai = SwingUtilities.getWindowAncestor(componentePai);
        DetalhesSerieDialog dialog = new DetalhesSerieDialog(janelaPai, serie);
        dialog.setVisible(true);
    }

    private void configurarJanela() {
        setSize(760, 520);
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void construirInterface() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(18, 18));
        painelPrincipal.setBackground(TelaPrincipal.COR_CARD_CLARO);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel(serie.getNome());
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(TelaPrincipal.COR_TEXTO_MAIN);

        JTextArea areaDetalhes = new JTextArea(montarTextoDetalhes());
        areaDetalhes.setEditable(false);
        areaDetalhes.setLineWrap(true);
        areaDetalhes.setWrapStyleWord(true);
        areaDetalhes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaDetalhes.setForeground(TelaPrincipal.COR_TEXTO_MAIN);
        areaDetalhes.setBackground(TelaPrincipal.COR_CARD_CLARO);
        areaDetalhes.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JScrollPane scrollDetalhes = new JScrollPane(areaDetalhes);
        scrollDetalhes.setBorder(BorderFactory.createLineBorder(new Color(244, 194, 219)));

        labelImagem.setPreferredSize(new Dimension(230, 330));
        labelImagem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelImagem.setForeground(TelaPrincipal.COR_TEXTO_MUTED);
        labelImagem.setBorder(BorderFactory.createLineBorder(new Color(244, 194, 219)));

        JButton botaoFechar = TelaPrincipal.criarBotaoEstilizado("Fechar", new Color(177, 138, 163));
        botaoFechar.addActionListener(e -> dispose());

        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        painelRodape.setBackground(TelaPrincipal.COR_CARD_CLARO);
        painelRodape.add(botaoFechar);

        painelPrincipal.add(titulo, BorderLayout.NORTH);
        painelPrincipal.add(labelImagem, BorderLayout.WEST);
        painelPrincipal.add(scrollDetalhes, BorderLayout.CENTER);
        painelPrincipal.add(painelRodape, BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private String montarTextoDetalhes() {
        return String.format(
                "ID: %d\n" +
                "Título: %s\n" +
                "Gênero(s): %s\n" +
                "Ano/Data de estreia: %s\n" +
                "Data de encerramento: %s\n" +
                "Nota: %.1f / 10\n" +
                "Status: %s\n" +
                "Idioma: %s\n" +
                "Emissora/Canal: %s\n" +
                "Imagem: %s\n\n" +
                "Descrição:\n%s",
                serie.getId(),
                valorOuPadrao(serie.getNome()),
                valorOuPadrao(serie.getGeneros()),
                valorOuPadrao(serie.getDataEstreia()),
                valorOuPadrao(serie.getDataFim()),
                serie.getNota(),
                valorOuPadrao(serie.getStatus()),
                valorOuPadrao(serie.getIdioma()),
                valorOuPadrao(serie.getEmissora()),
                valorOuPadrao(serie.getImagemUrl()),
                valorOuPadrao(serie.getSumario())
        );
    }

    private String valorOuPadrao(String valor) {
        if (valor == null || valor.isBlank()) {
            return "Não informado";
        }
        return valor;
    }

    private void carregarImagem() {
        String imagemUrl = serie.getImagemUrl();
        if (imagemUrl == null || imagemUrl.isBlank()) {
            labelImagem.setText("Imagem não disponível");
            return;
        }

        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                BufferedImage imagem = ImageIO.read(new URL(imagemUrl));
                if (imagem == null) {
                    return null;
                }

                Image imagemRedimensionada = imagem.getScaledInstance(230, 330, Image.SCALE_SMOOTH);
                return new ImageIcon(imagemRedimensionada);
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icone = get();
                    if (icone == null) {
                        labelImagem.setText("Imagem não disponível");
                    } else {
                        labelImagem.setText("");
                        labelImagem.setIcon(icone);
                    }
                } catch (Exception e) {
                    labelImagem.setText("Não foi possível carregar a imagem");
                }
            }
        };

        worker.execute();
    }
}
