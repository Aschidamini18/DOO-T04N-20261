import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class RepositorioUsuario {

    private final Path arquivo;

    public RepositorioUsuario() {
        this("usuario.json");
    }

    public RepositorioUsuario(String caminho) {
        this.arquivo = Paths.get(caminho);
    }

    public boolean existe() {
        return Files.exists(arquivo);
    }

    public void salvar(Usuario usuario) throws PersistenciaException {
        if (usuario == null) {
            throw new PersistenciaException("Usuario nulo nao pode ser salvo.");
        }
        try {
            String json = Json.escrever(usuario.paraJson());
            Files.write(arquivo, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao salvar os dados no arquivo "
                    + arquivo + ".", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Usuario carregar() throws PersistenciaException {
        if (!existe()) {
            return null;
        }
        try {
            byte[] bytes = Files.readAllBytes(arquivo);
            String conteudo = new String(bytes, StandardCharsets.UTF_8);
            if (conteudo.trim().isEmpty()) {
                return null;
            }
            Object raiz = Json.parse(conteudo);
            if (!(raiz instanceof Map)) {
                throw new PersistenciaException("Arquivo de dados em formato inesperado.");
            }
            return Usuario.deJson((Map<String, Object>) raiz);
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao ler o arquivo de dados "
                    + arquivo + ".", e);
        } catch (RuntimeException e) {
            throw new PersistenciaException("Arquivo de dados corrompido ou invalido.", e);
        }
    }
}
