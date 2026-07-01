import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SerieService {

    private final TVMazeClient cliente = new TVMazeClient();
    private final RepositorioUsuario repositorio = new RepositorioUsuario();
    private final List<Serie> catalogoLocal = DadosIniciais.catalogo();

    private Usuario usuario;
    private boolean ultimaBuscaOffline;

    public Usuario carregarUsuario() throws PersistenciaException {
        usuario = repositorio.carregar();
        return usuario;
    }

    public Usuario criarUsuarioPadrao(String nome) throws PersistenciaException {
        usuario = DadosIniciais.usuarioPadrao(nome);
        salvar();
        return usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void salvar() throws PersistenciaException {
        if (usuario != null) {
            repositorio.salvar(usuario);
        }
    }

    public List<Serie> buscar(String termo) {
        ultimaBuscaOffline = false;
        if (termo == null || termo.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            List<Serie> resultado = cliente.buscarPorNome(termo);
            if (resultado.isEmpty()) {

                List<Serie> local = buscarNoCatalogo(termo);
                if (!local.isEmpty()) {
                    ultimaBuscaOffline = true;
                    return local;
                }
            }
            return resultado;
        } catch (ApiException e) {
            ultimaBuscaOffline = true;
            return buscarNoCatalogo(termo);
        }
    }

    public boolean isUltimaBuscaOffline() {
        return ultimaBuscaOffline;
    }

    private List<Serie> buscarNoCatalogo(String termo) {
        String alvo = termo.trim().toLowerCase();
        List<Serie> achados = new ArrayList<>();
        for (Serie s : catalogoLocal) {
            if (s.getNome().toLowerCase().contains(alvo)) {
                achados.add(s);
            }
        }
        return achados;
    }

    public boolean adicionarNaLista(TipoLista tipo, Serie serie) throws PersistenciaException {
        boolean adicionou = usuario.adicionar(tipo, serie);
        if (adicionou) {
            salvar();
        }
        return adicionou;
    }

    public boolean removerDaLista(TipoLista tipo, Serie serie) throws PersistenciaException {
        boolean removeu = usuario.remover(tipo, serie);
        if (removeu) {
            salvar();
        }
        return removeu;
    }

    public List<Serie> listarOrdenado(TipoLista tipo, OrdenacaoSeries ordem) {
        List<Serie> copia = new ArrayList<>(usuario.getLista(tipo));
        if (ordem != null) {
            copia.sort(ordem);
        }
        return copia;
    }

    public Set<TipoLista> listasContendo(Serie serie) {
        Set<TipoLista> conjunto = new LinkedHashSet<>();
        for (TipoLista t : TipoLista.values()) {
            if (usuario.contem(t, serie)) {
                conjunto.add(t);
            }
        }
        return conjunto;
    }
}
