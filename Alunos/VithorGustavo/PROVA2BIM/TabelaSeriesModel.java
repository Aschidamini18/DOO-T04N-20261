import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabelaSeriesModel extends AbstractTableModel {

    private static final String[] COLUNAS = {
            "Nome", "Idioma", "Generos", "Nota", "Estado",
            "Estreia", "Termino", "Emissora"
    };

    private List<Serie> series = new ArrayList<>();

    public void setSeries(List<Serie> novas) {
        this.series = (novas == null) ? new ArrayList<>() : new ArrayList<>(novas);
        fireTableDataChanged();
    }

    public Serie getSerieEm(int linha) {
        if (linha < 0 || linha >= series.size()) {
            return null;
        }
        return series.get(linha);
    }

    public boolean estaVazia() {
        return series.isEmpty();
    }

    @Override
    public int getRowCount() {
        return series.size();
    }

    @Override
    public int getColumnCount() {
        return COLUNAS.length;
    }

    @Override
    public String getColumnName(int coluna) {
        return COLUNAS[coluna];
    }

    @Override
    public boolean isCellEditable(int linha, int coluna) {
        return false;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Serie s = series.get(linha);
        switch (coluna) {
            case 0:
                return s.getNome();
            case 1:
                return s.getIdioma();
            case 2:
                return s.getGenerosFormatado();
            case 3:
                return s.getNotaFormatada();
            case 4:
                return s.getStatus().getDescricao();
            case 5:
                return s.getDataEstreiaFormatada();
            case 6:
                return s.getDataTerminoFormatada();
            case 7:
                return s.getEmissora();
            default:
                return "";
        }
    }
}
