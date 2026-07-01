package fag.dados;

import java.util.ArrayList;
import java.util.List;

// Esta classe guarda o apelido do usuario e controla as listas de series do sistema.

// guarda o apelido local e as tres listas pessoais.
public class UsuarioDoSistema {
	private String apelido;
	private List<SerieDeTv> favoritas;
	private List<SerieDeTv> assistidas;
	private List<SerieDeTv> desejoAssistir;

	
	public UsuarioDoSistema() {
		this("Usuario");
	}

	// Construtor com apelido: cria um usuario com listas vazias.
	public UsuarioDoSistema(String apelido) {
		this.apelido = apelido == null || apelido.isBlank() ? "Usuario" : apelido.trim();
		this.favoritas = new ArrayList<>();
		this.assistidas = new ArrayList<>();
		this.desejoAssistir = new ArrayList<>();
	}

	//  retorna o nome ou apelido do usuario.
	public String obterApelido() {
		return apelido;
	}

	// altera o nome do usuario se o texto for valido.
	public void definirApelido(String apelido) {
		if (apelido != null && !apelido.isBlank()) {
			this.apelido = apelido.trim();
		}
	}

	//  retorna a lista de favoritas.
	public List<SerieDeTv> obterFavoritas() {
		return favoritas;
	}

	//  altera a lista de favoritas.
	public void definirFavoritas(List<SerieDeTv> favoritas) {
		this.favoritas = novasSeriesOuListaVazia(favoritas);
	}

	//  retorna a lista de series ja assistidas.
	public List<SerieDeTv> obterAssistidas() {
		return assistidas;
	}

	//  altera a lista de series ja assistidas.
	public void definirAssistidas(List<SerieDeTv> assistidas) {
		this.assistidas = novasSeriesOuListaVazia(assistidas);
	}

	// retorna a lista de series desejadas.
	public List<SerieDeTv> obterDesejoAssistir() {
		return desejoAssistir;
	}

	//  altera a lista de series desejadas.
	public void definirDesejoAssistir(List<SerieDeTv> desejoAssistir) {
		this.desejoAssistir = novasSeriesOuListaVazia(desejoAssistir);
	}

	// devolve a lista correta a partir do enum.
	public List<SerieDeTv> obterListaPorTipo(TipoDeListaDeSeries tipoDeLista) {
		return switch (tipoDeLista) {
			case FAVORITAS -> favoritas;
			case ASSISTIDAS -> assistidas;
			case DESEJO_ASSISTIR -> desejoAssistir;
		};
	}

	// adiciona uma serie na lista escolhida.
	public boolean adicionarSerie(TipoDeListaDeSeries tipoDeLista, SerieDeTv serie) {
		var lista = obterListaPorTipo(tipoDeLista);
		if (serie == null || contemSerie(lista, serie)) {
			return false;
		}
		return lista.add(serie);
	}

	// remove uma serie da lista escolhida.
	public boolean removerSerie(TipoDeListaDeSeries tipoDeLista, SerieDeTv serie) {
		return obterListaPorTipo(tipoDeLista).removeIf(item -> item.obterCodigo() == serie.obterCodigo());
	}

	// ordena a lista escolhida.
	public void ordenarLista(TipoDeListaDeSeries tipoDeLista, OpcaoDeOrdenacaoDeSeries opcaoDeOrdenacao) {
		obterListaPorTipo(tipoDeLista).sort(opcaoDeOrdenacao.obterComparador());
	}

	// verifica duplicidade pelo codigo da API.
	private boolean contemSerie(List<SerieDeTv> lista, SerieDeTv serie) {
		return lista.stream().anyMatch(item -> item.obterCodigo() == serie.obterCodigo());
	}

	// Metodo novasSeriesOuListaVazia: evita listas nulas depois de carregar JSON.
	private List<SerieDeTv> novasSeriesOuListaVazia(List<SerieDeTv> series) {
		return new ArrayList<>(series == null ? List.of() : series);
	}
}

