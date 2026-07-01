import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

class CalculadoraException extends Exception {
    public CalculadoraException(String mensagem) {
        super(mensagem);
    }
}

public class CalculadoraVisual extends JFrame implements ActionListener {

    private JTextField visor;
    private double valorAcumulado = 0;
    private String operadorAtual = "";
    private boolean iniciarNovoNumero = true;

    public CalculadoraVisual() {
        setTitle("Calculadora");
        setSize(320, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(32, 32, 32));

        visor = new JTextField("0");
        visor.setFont(new Font("Segoe UI", Font.BOLD, 48));
        visor.setHorizontalAlignment(JTextField.RIGHT);
        visor.setEditable(false);
        visor.setBackground(new Color(32, 32, 32));
        visor.setForeground(Color.WHITE);
        visor.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(visor, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(4, 4, 5, 5));
        painelBotoes.setBackground(new Color(32, 32, 32));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] botoes = {
            "7", "8", "9", "÷",
            "4", "5", "6", "x",
            "1", "2", "3", "-",
            "C", "0", "=", "+"
        };

        for (String texto : botoes) {
            JButton botao = new JButton(texto);
            botao.setFont(new Font("Segoe UI", Font.BOLD, 20));
            botao.setFocusPainted(false);
            botao.setBorder(BorderFactory.createEmptyBorder());
            botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
            botao.addActionListener(this);

            if (texto.matches("[0-9]")) {
                botao.setBackground(new Color(59, 59, 59));
                botao.setForeground(Color.WHITE);
            } else if (texto.equals("=")) {
                botao.setBackground(new Color(255, 102, 178));
                botao.setForeground(Color.BLACK);
            } else {
                botao.setBackground(new Color(50, 50, 50));
                botao.setForeground(Color.LIGHT_GRAY);
            }
            painelBotoes.add(botao);
        }

        add(painelBotoes, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.matches("[0-9]")) {
            if (iniciarNovoNumero) {
                visor.setText(comando);
                iniciarNovoNumero = false;
            } else {
                visor.setText(visor.getText() + comando);
            }
        } 
        else if (comando.equals("C")) {
            visor.setText("0");
            valorAcumulado = 0;
            operadorAtual = "";
            iniciarNovoNumero = true;
        } 
        else if (comando.equals("=")) {
            calcularResultado();
            operadorAtual = "";
        } 
        else {
            if (!iniciarNovoNumero) {
                if (operadorAtual.isEmpty()) {
                    valorAcumulado = Double.parseDouble(visor.getText().replace(",", "."));
                } else {
                    calcularResultado();
                }
            }
            operadorAtual = comando;
            iniciarNovoNumero = true;
        }
    }

    private void calcularResultado() {
        if (operadorAtual.isEmpty()) return;

        try {
            double valorVisor = Double.parseDouble(visor.getText().replace(",", "."));
            double resultado = 0;

            switch (operadorAtual) {
                case "+": resultado = valorAcumulado + valorVisor; break;
                case "-": resultado = valorAcumulado - valorVisor; break;
                case "x": resultado = valorAcumulado * valorVisor; break;
                case "÷":
                    if (valorVisor == 0) {
                        throw new CalculadoraException("Erro: Divisão por zero");
                    }
                    resultado = valorAcumulado / valorVisor;
                    break;
            }

            if (resultado == (long) resultado) {
                visor.setText(String.format("%d", (long) resultado));
            } else {
                visor.setText(String.format("%s", resultado).replace(",", "."));
            }
            
            valorAcumulado = resultado;
            iniciarNovoNumero = true;

        } catch (CalculadoraException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Operação Inválida", JOptionPane.ERROR_MESSAGE);
            visor.setText("Erro");
            iniciarNovoNumero = true;
        } catch (Exception ex) {
            visor.setText("Erro");
            iniciarNovoNumero = true;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculadoraVisual tela = new CalculadoraVisual();
            tela.setVisible(true);
        });
    }
}