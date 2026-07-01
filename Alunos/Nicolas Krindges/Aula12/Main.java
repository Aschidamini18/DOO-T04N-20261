import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        // ===================== //ATV1 =====================
        // Filtra apenas os números pares de uma lista de inteiros
        System.out.println("===== ATV1 - Números pares =====");

        List<Integer> numeros = Arrays.asList(4, 7, 10, 15, 22, 33, 8, 9, 6, 1);

        List<Integer> numerosPares = numeros.stream()
                .filter(numero -> numero % 2 == 0)
                .collect(Collectors.toList());

        System.out.println("Lista original: " + numeros);
        System.out.println("Números pares: " + numerosPares);
        System.out.println();

        // ===================== //ATV2 =====================
        // Converte todos os nomes de uma lista para letras maiúsculas
        System.out.println("===== ATV2 - Nomes em maiúsculas =====");

        List<String> nomes = Arrays.asList("roberto", "josé", "caio", "vinicius");

        List<String> nomesMaiusculos = nomes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println("Lista original: " + nomes);
        System.out.println("Nomes em maiúsculas: " + nomesMaiusculos);
        System.out.println();

        // ===================== //ATV3 =====================
        // Conta quantas vezes cada palavra única aparece na lista
        System.out.println("===== ATV3 - Contagem de palavras =====");

        List<String> palavras = Arrays.asList("se", "talvez", "hoje", "sábado", "se", "quarta", "sábado");

        Map<String, Long> contagemPalavras = palavras.stream()
                .collect(Collectors.groupingBy(palavra -> palavra, Collectors.counting()));

        System.out.println("Lista original: " + palavras);
        System.out.println("Contagem por palavra: " + contagemPalavras);
        System.out.println();

        // ===================== //ATV4 =====================
        // Filtra os produtos cujo preço seja maior que R$ 100,00
        System.out.println("===== ATV4 - Produtos acima de R$ 100,00 =====");

        List<Produto> produtos = Arrays.asList(
                new Produto("Mouse", 79.90),
                new Produto("Teclado mecânico", 249.90),
                new Produto("Monitor 24\"", 899.00),
                new Produto("Mousepad", 39.90)
        );

        List<Produto> produtosAcimaDe100 = produtos.stream()
                .filter(produto -> produto.getPreco() > 100.00)
                .collect(Collectors.toList());

        System.out.println("Lista de produtos: " + produtos);
        System.out.println("Produtos acima de R$ 100,00: " + produtosAcimaDe100);
        System.out.println();

        // ===================== //ATV5 =====================
        // Soma o valor total de todos os produtos da lista
        System.out.println("===== ATV5 - Soma total dos produtos =====");

        double valorTotal = produtos.stream()
                .mapToDouble(Produto::getPreco)
                .sum();

        System.out.printf("Valor total dos produtos: R$ %.2f%n", valorTotal);
        System.out.println();

        // ===================== //ATV6 =====================
        // Ordena a lista de strings pelo tamanho da palavra, da menor para a maior
        System.out.println("===== ATV6 - Ordenação por tamanho =====");

        List<String> linguagens = Arrays.asList("Java", "Python", "C", "JavaScript", "Ruby");

        List<String> linguagensOrdenadas = linguagens.stream()
                .sorted((a, b) -> Integer.compare(a.length(), b.length()))
                .collect(Collectors.toList());

        System.out.println("Lista original: " + linguagens);
        System.out.println("Lista ordenada por tamanho: " + linguagensOrdenadas);
    }

    /**
     * Classe que representa um produto, com nome e preço.
     * Utilizada na ATV4 e ATV5.
     */
    static class Produto {
        private final String nome;
        private final double preco;

        public Produto(String nome, double preco) {
            this.nome = nome;
            this.preco = preco;
        }

        public String getNome() {
            return nome;
        }

        public double getPreco() {
            return preco;
        }

        @Override
        public String toString() {
            return nome + " (R$ " + String.format("%.2f", preco) + ")";
        }
    }
}
