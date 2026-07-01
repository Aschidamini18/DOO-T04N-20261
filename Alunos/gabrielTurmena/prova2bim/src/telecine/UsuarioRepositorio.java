package telecine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//recebe usuário, converte para JSON os dados dele, abre e cria o arquivos de JSON que vai receber os dados
//escreve no arquivo e fecha o arquivo (no caso manipula ela pelo gson)

public class UsuarioRepositorio {
//final é tipo const de dentro do JS e colocam com maiúscula
    private static final String CAMINHO_ARQUIVO = "usuario.json";

    public void salvar(Usuario usuario) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Path caminho = Paths.get(CAMINHO_ARQUIVO);

        try (Writer writer = Files.newBufferedWriter(caminho, StandardCharsets.UTF_8)) {
            gson.toJson(usuario, writer);
        }

        System.out.println("Arquivo salvo em: " + caminho.toAbsolutePath());
    }

    public Usuario carregar() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Path caminho = Paths.get(CAMINHO_ARQUIVO);

        if (!Files.exists(caminho)) {
            return new Usuario("Arthur");
        }

        try (Reader reader = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {
            Usuario usuario = gson.fromJson(reader, Usuario.class);

            if (usuario == null) {
                return new Usuario("Arthur");
            }

            return usuario;

        } catch (Exception e) {
            System.out.println("Erro ao carregar usuário: " + e.getMessage());
            return new Usuario("Arthur");
        }
    }
}