/**
 * Classe responsável pela lógica das operações matemáticas
 * e pela validação dos dados de entrada da calculadora.
 */
public class Calculadora {

    /**
     * Converte um texto em número double.
     * Lança CalculadoraException caso o texto esteja vazio
     * ou contenha caracteres não numéricos.
     */
    public double converterEntrada(String texto, String nomeCampo) throws CalculadoraException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new CalculadoraException("O campo \"" + nomeCampo + "\" não pode estar vazio.");
        }

        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new CalculadoraException(
                "Entrada inválida em \"" + nomeCampo + "\". Digite apenas números.", e);
        }
    }

    public double somar(double a, double b) {
        return a + b;
    }

    public double subtrair(double a, double b) {
        return a - b;
    }

    public double multiplicar(double a, double b) {
        return a * b;
    }

    public double dividir(double a, double b) throws CalculadoraException {
        if (b == 0) {
            throw new CalculadoraException("Erro: divisão por zero não é permitida.");
        }
        return a / b;
    }

    /**
     * Executa a operação de acordo com o operador informado (+, -, ×, ÷).
     */
    public double calcular(double a, double b, char operador) throws CalculadoraException {
        switch (operador) {
            case '+':
                return somar(a, b);
            case '-':
                return subtrair(a, b);
            case '×':
            case '*':
                return multiplicar(a, b);
            case '÷':
            case '/':
                return dividir(a, b);
            default:
                throw new CalculadoraException("Operação inválida: " + operador);
        }
    }
}
