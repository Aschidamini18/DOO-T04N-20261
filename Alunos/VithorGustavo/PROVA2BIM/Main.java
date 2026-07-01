import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        Thread.setDefaultUncaughtExceptionHandler((thread, erro) -> {
            erro.printStackTrace();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Ocorreu um erro inesperado: " + erro.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE));
        });

        SwingUtilities.invokeLater(Main::iniciar);
    }

    private static void iniciar() {
        try {

            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignorado) {

        }

        SerieService service = new SerieService();

        Usuario usuario = null;
        try {
            usuario = service.carregarUsuario();
        } catch (PersistenciaException e) {
            JOptionPane.showMessageDialog(null,
                    "Nao foi possivel carregar os dados salvos (" + e.getMessage()
                            + ").\nSera criado um novo perfil.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        if (usuario == null) {
            String nome = JOptionPane.showInputDialog(null,
                    "Bem-vindo! Como voce gostaria de ser chamado?",
                    "Identificacao", JOptionPane.QUESTION_MESSAGE);
            if (nome == null) {

                System.exit(0);
                return;
            }
            try {
                service.criarUsuarioPadrao(nome);
            } catch (PersistenciaException e) {
                JOptionPane.showMessageDialog(null,
                        "O perfil foi criado, mas nao foi possivel salva-lo agora: "
                                + e.getMessage(),
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }

        new JanelaPrincipal(service).setVisible(true);
    }
}
