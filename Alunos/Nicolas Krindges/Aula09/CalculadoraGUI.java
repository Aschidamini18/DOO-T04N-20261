import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Interface gráfica da calculadora, construída com Java Swing.
 * Contém os campos de entrada, os botões de operação e a área
 * de exibição do resultado.
 */
public class CalculadoraGUI extends JFrame {

    private final JTextField campoNumero1;
    private final JTextField campoNumero2;
    private final JLabel labelResultado;
    private final Calculadora calculadora;

    public CalculadoraGUI() {
        calculadora = new Calculadora();

        setTitle("Calculadora Simples");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Painel de entrada de dados
        JPanel painelEntrada = new JPanel(new GridLayout(2, 2, 8, 8));
        painelEntrada.add(new JLabel("Número 1:"));
        campoNumero1 = new JTextField();
        painelEntrada.add(campoNumero1);

        painelEntrada.add(new JLabel("Número 2:"));
        campoNumero2 = new JTextField();
        painelEntrada.add(campoNumero2);

        // Painel com os botões de operação
        JPanel painelBotoes = new JPanel(new GridLayout(1, 4, 8, 8));
        JButton botaoSomar = new JButton("+");
        JButton botaoSubtrair = new JButton("-");
        JButton botaoMultiplicar = new JButton("×");
        JButton botaoDividir = new JButton("÷");

        painelBotoes.add(botaoSomar);
        painelBotoes.add(botaoSubtrair);
        painelBotoes.add(botaoMultiplicar);
        painelBotoes.add(botaoDividir);

        // Área de exibição do resultado
        labelResultado = new JLabel("Resultado: ");
        labelResultado.setFont(new Font("SansSerif", Font.BOLD, 16));
        labelResultado.setHorizontalAlignment(SwingConstants.CENTER);
        labelResultado.setBorder(BorderFactory.createEtchedBorder());
        labelResultado.setOpaque(true);
        labelResultado.setBackground(Color.WHITE);
        labelResultado.setPreferredSize(new Dimension(0, 50));

        painelPrincipal.add(painelEntrada, BorderLayout.NORTH);
        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);
        painelPrincipal.add(labelResultado, BorderLayout.SOUTH);

        add(painelPrincipal);

        // Associa cada botão à sua respectiva operação
        botaoSomar.addActionListener(e -> executarOperacao('+'));
        botaoSubtrair.addActionListener(e -> executarOperacao('-'));
        botaoMultiplicar.addActionListener(e -> executarOperacao('×'));
        botaoDividir.addActionListener(e -> executarOperacao('÷'));
    }

    /**
     * Lê os campos de entrada, executa a operação escolhida e atualiza
     * o resultado. Todos os erros previstos são tratados aqui, exibindo
     * mensagens amigáveis ao usuário.
     */
    private void executarOperacao(char operador) {
        try {
            double numero1 = calculadora.converterEntrada(campoNumero1.getText(), "Número 1");
            double numero2 = calculadora.converterEntrada(campoNumero2.getText(), "Número 2");

            double resultado = calculadora.calcular(numero1, numero2, operador);

            labelResultado.setText("Resultado: " + formatarResultado(resultado));
            labelResultado.setForeground(Color.BLACK);

        } catch (CalculadoraException erro) {
            // Trata erros previstos: entrada inválida ou divisão por zero
            labelResultado.setText("Erro: " + erro.getMessage());
            labelResultado.setForeground(Color.RED);
            JOptionPane.showMessageDialog(
                this,
                erro.getMessage(),
                "Entrada inválida",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception erroInesperado) {
            // Tratamento genérico para qualquer erro não previsto
            labelResultado.setText("Erro inesperado.");
            labelResultado.setForeground(Color.RED);
            JOptionPane.showMessageDialog(
                this,
                "Ocorreu um erro inesperado: " + erroInesperado.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Remove casas decimais desnecessárias (ex: 4.0 -> 4) e limita
     * a exibição a 6 casas decimais quando necessário.
     */
    private String formatarResultado(double valor) {
        if (valor == Math.floor(valor) && !Double.isInfinite(valor)) {
            return String.valueOf((long) valor);
        }
        return String.format("%.6f", valor).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    public static void main(String[] args) {
        // Garante que a interface seja criada na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            CalculadoraGUI calculadoraGUI = new CalculadoraGUI();
            calculadoraGUI.setVisible(true);
        });
    }
}
