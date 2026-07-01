# Atividade Extra — Nanowar of Steel, "HelloWorld.java"

---

## Conceito 1

**Nome:** Nico

**Conceito escolhido:** JVM e Portabilidade ("Write Once, Run Everywhere")

**Timestamp do vídeo que menciona o conceito:** aproximadamente 2:45–2:55 (trecho cantado: *"If I may introduce a bug, the JVM will manage it for me, ensuring both security and portability. Then I'll write my code once, and run it everywhere!"*)

**O que é?**
A JVM (*Java Virtual Machine*, ou Máquina Virtual Java) é um programa que executa o código Java já compilado (o *bytecode*, arquivos `.class`). Em vez do código-fonte ser traduzido diretamente para instruções de um processador específico (como acontece em C, por exemplo), o compilador Java (`javac`) gera um bytecode intermediário, que é o mesmo não importa o sistema operacional ou hardware. Quem entende e executa esse bytecode é a JVM instalada em cada máquina. Por isso o famoso slogan da linguagem: *"write once, run everywhere"* — escreve-se o código uma vez, e ele roda em qualquer lugar que tenha uma JVM, seja Windows, Linux, macOS, etc.

**Pra que serve?**
Serve para dar **portabilidade**: o mesmo `.jar` que compilamos no Windows roda sem alteração nenhuma em um Linux, por exemplo, desde que haja uma JVM instalada. Isso remove do programador a preocupação de adaptar o código pra cada sistema operacional. Além disso, a JVM funciona como uma camada de isolamento entre o programa e o sistema operacional real, o que também ajuda na **segurança**, já que o programa não acessa diretamente o hardware ou recursos sensíveis — tudo passa pela JVM, que pode controlar essas permissões.

**Como é normalmente utilizado?**
Na prática, isso é completamente transparente para quem está aprendendo Java: quando rodamos `javac Main.java`, geramos o bytecode (`Main.class`); quando rodamos `java Main`, é a JVM quem lê esse bytecode e o executa. O mesmo vale para um `.jar`: ao gerar o `CalculadoraSwing.jar`, por exemplo, qualquer pessoa com Java instalado consegue rodar `java -jar CalculadoraSwing.jar` independente do sistema operacional dela, sem precisar recompilar nada.

**Exemplo de código:**
```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Esse mesmo .class roda igual em Windows, Linux ou Mac!");
    }
}
```
```bash
# 1. Compila o código-fonte para bytecode (gera Main.class)
javac Main.java

# 2. A JVM interpreta/executa esse bytecode — funciona em qualquer SO com Java instalado
java Main
```

---

## Conceito 2

**Nome:** Nico

**Conceito escolhido:** Garbage Collector (Coletor de Lixo)

**Timestamp do vídeo que menciona o conceito:** aproximadamente 2:55–3:00 (trecho cantado: *"With static and strong typing, will let my programs be type safe! Garbage collector!"*)

**O que é?**
O Garbage Collector é um componente da JVM responsável por gerenciar automaticamente a memória do programa. Em linguagens como C, o programador precisa alocar e liberar memória manualmente (por exemplo, com `malloc`/`free`), e esquecer de liberar gera *memory leaks* (vazamentos de memória) ou até falhas graves de segurança. Em Java, isso não é necessário: quando um objeto deixa de ser referenciado por qualquer parte do programa (ou seja, ninguém mais "aponta" pra ele), o Garbage Collector detecta isso automaticamente e libera essa memória, deixando-a disponível para ser reutilizada.

**Pra que serve?**
Serve para evitar uma classe inteira de bugs relacionados a gerenciamento manual de memória — como vazamentos de memória, liberar memória duas vezes, ou usar memória já liberada (o famoso *use-after-free* bem comum em C/C++). Com isso, o programador foca na lógica do programa e não precisa se preocupar em "limpar" objetos que não usa mais.

**Como é normalmente utilizado?**
É um processo automático e invisível: o programador não chama o Garbage Collector diretamente no dia a dia (existe até um método `System.gc()`, mas seu uso é desencorajado, já que é só uma "sugestão" pra JVM — ela decide quando realmente rodar a coleta). Na prática, basta simplesmente parar de referenciar um objeto (atribuir `null` a ele, ou deixar ele sair de escopo) que, em algum momento, a JVM identifica que aquele espaço de memória não é mais usado e o recolhe.

**Exemplo de código:**
```java
public class Main {
    public static void main(String[] args) {
        Produto produto = new Produto("Mouse", 79.90);
        System.out.println("Produto criado: " + produto);

        // A partir daqui, ninguém mais referencia o objeto "produto".
        // O Garbage Collector vai, em algum momento, liberar essa memória
        // automaticamente — sem precisar de um "free(produto)" manual.
        produto = null;
    }
}

class Produto {
    String nome;
    double preco;

    Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    @Override
    public String toString() {
        return nome + " - R$ " + preco;
    }
}
```

---

### Fontes consultadas
- Letra anotada da música, com explicações linha a linha dos conceitos técnicos: lyricstranslate.com
- Repositório oficial com o código-fonte completo da música: github.com/NanowarOfSteel/HelloWorld
