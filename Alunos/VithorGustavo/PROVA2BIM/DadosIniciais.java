import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DadosIniciais {

    private DadosIniciais() {
    }

    private static Serie serie(int id, String nome, String idioma, List<String> generos,
                               Double nota, String status, String estreia, String termino,
                               String emissora) {
        return new Serie(id, nome, idioma, generos, nota, status, estreia, termino, emissora, "");
    }

    public static List<Serie> catalogo() {
        List<Serie> c = new ArrayList<>();

        c.add(serie(169, "Breaking Bad", "English",
                Arrays.asList("Drama", "Crime", "Thriller"),
                9.2, "Ended", "2008-01-20", "2013-09-29", "AMC"));

        c.add(serie(82, "Game of Thrones", "English",
                Arrays.asList("Drama", "Adventure", "Fantasy"),
                8.9, "Ended", "2011-04-17", "2019-05-19", "HBO"));

        c.add(serie(431, "Friends", "English",
                Arrays.asList("Comedy", "Romance"),
                8.5, "Ended", "1994-09-22", "2004-05-06", "NBC"));

        c.add(serie(526, "The Office", "English",
                Arrays.asList("Comedy"),
                8.6, "Ended", "2005-03-24", "2013-05-16", "NBC"));

        c.add(serie(2993, "Stranger Things", "English",
                Arrays.asList("Drama", "Fantasy", "Horror"),
                8.4, "Running", "2016-07-15", null, "Netflix"));

        c.add(serie(83, "The Simpsons", "English",
                Arrays.asList("Animation", "Comedy"),
                7.8, "Running", "1989-12-17", null, "FOX"));

        c.add(serie(528, "Sherlock", "English",
                Arrays.asList("Drama", "Crime", "Mystery"),
                8.9, "Ended", "2010-07-25", "2017-01-15", "BBC One"));

        c.add(serie(36482, "The Witcher", "English",
                Arrays.asList("Drama", "Action", "Fantasy"),
                8.0, "Running", "2019-12-20", null, "Netflix"));

        c.add(serie(30770, "Chernobyl", "English",
                Arrays.asList("Drama", "History"),
                9.0, "Ended", "2019-05-06", "2019-06-03", "HBO"));

        c.add(serie(1371, "Dark", "German",
                Arrays.asList("Drama", "Science-Fiction", "Thriller"),
                8.5, "Ended", "2017-12-01", "2020-06-27", "Netflix"));

        c.add(serie(13916, "La Casa de Papel", "Spanish",
                Arrays.asList("Drama", "Crime", "Thriller"),
                8.0, "Ended", "2017-05-02", "2021-12-03", "Antena 3"));

        c.add(serie(73, "The Walking Dead", "English",
                Arrays.asList("Drama", "Action", "Horror"),
                7.9, "Ended", "2010-10-31", "2022-11-20", "AMC"));

        return c;
    }

    public static Usuario usuarioPadrao(String nome) {
        Usuario u = new Usuario(nome);
        List<Serie> c = catalogo();

        u.adicionar(TipoLista.FAVORITOS, c.get(0));
        u.adicionar(TipoLista.FAVORITOS, c.get(8));

        u.adicionar(TipoLista.ASSISTIDAS, c.get(1));
        u.adicionar(TipoLista.ASSISTIDAS, c.get(2));
        u.adicionar(TipoLista.ASSISTIDAS, c.get(6));

        u.adicionar(TipoLista.DESEJA_ASSISTIR, c.get(4));
        u.adicionar(TipoLista.DESEJA_ASSISTIR, c.get(9));

        return u;
    }
}
