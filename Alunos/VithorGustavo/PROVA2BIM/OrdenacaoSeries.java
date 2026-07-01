import java.util.Comparator;

public enum OrdenacaoSeries implements Comparator<Serie> {

    NOME("Ordem alfabetica do nome") {
        @Override
        public int compare(Serie a, Serie b) {
            return textoSeguro(a.getNome()).compareToIgnoreCase(textoSeguro(b.getNome()));
        }
    },

    NOTA("Nota geral (maior primeiro)") {
        @Override
        public int compare(Serie a, Serie b) {
            Double na = a.getNotaGeral();
            Double nb = b.getNotaGeral();
            if (na == null && nb == null) {
                return 0;
            }
            if (na == null) {
                return 1;
            }
            if (nb == null) {
                return -1;
            }
            return Double.compare(nb, na);
        }
    },

    ESTADO("Estado da serie") {
        @Override
        public int compare(Serie a, Serie b) {
            int cmp = a.getStatus().ordinal() - b.getStatus().ordinal();
            if (cmp != 0) {
                return cmp;
            }
            return textoSeguro(a.getNome()).compareToIgnoreCase(textoSeguro(b.getNome()));
        }
    },

    ESTREIA("Data de estreia (mais antiga primeiro)") {
        @Override
        public int compare(Serie a, Serie b) {
            String da = a.getDataEstreia();
            String db = b.getDataEstreia();
            boolean va = da == null || da.isEmpty();
            boolean vb = db == null || db.isEmpty();
            if (va && vb) {
                return 0;
            }
            if (va) {
                return 1;
            }
            if (vb) {
                return -1;
            }

            return da.compareTo(db);
        }
    };

    private final String descricao;

    OrdenacaoSeries(String descricao) {
        this.descricao = descricao;
    }

    private static String textoSeguro(String s) {
        return s == null ? "" : s;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
