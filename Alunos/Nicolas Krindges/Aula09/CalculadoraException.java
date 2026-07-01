/**
 * Exceção personalizada utilizada para tratar erros específicos
 * da calculadora, como entrada inválida e divisão por zero.
 */
public class CalculadoraException extends Exception {

    public CalculadoraException(String mensagem) {
        super(mensagem);
    }

    public CalculadoraException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
