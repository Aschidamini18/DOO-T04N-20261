public enum TipoLista {

    FAVORITOS("Favoritos"),
    ASSISTIDAS("Series ja assistidas"),
    DESEJA_ASSISTIR("Series que deseja assistir");

    private final String titulo;

    TipoLista(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
