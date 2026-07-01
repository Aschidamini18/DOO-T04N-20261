public enum StatusSerie {

    EM_TRANSMISSAO("Em transmissao"),
    CONCLUIDA("Concluida"),
    CANCELADA("Cancelada"),
    EM_DESENVOLVIMENTO("Em desenvolvimento"),
    INDETERMINADO("Indeterminado");

    private final String descricao;

    StatusSerie(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusSerie deTexto(String valor) {
        if (valor == null) {
            return INDETERMINADO;
        }
        switch (valor.trim().toLowerCase()) {
            case "running":
            case "em transmissao":
            case "em transmissão":
                return EM_TRANSMISSAO;
            case "ended":
            case "concluida":
            case "concluída":
                return CONCLUIDA;
            case "cancelled":
            case "canceled":
            case "cancelada":
                return CANCELADA;
            case "in development":
            case "em desenvolvimento":
                return EM_DESENVOLVIMENTO;
            default:
                return INDETERMINADO;
        }
    }

    @Override
    public String toString() {
        return descricao;
    }
}
