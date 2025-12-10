package presentation;

import domain.game.DomainController;
import javax.swing.SwingUtilities;
import domain.shared.BadOpoLogger;

public class App {
    public static void main(String[] args) {
        // Optimización gráfica para evitar parpadeos en algunos sistemas
        System.setProperty("sun.java2d.opengl", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Instanciamos el ÚNICO punto de entrada al dominio
                DomainController domain = new DomainController();

                // 2. Iniciamos la GUI pasándole el dominio
                BadOpoGUI gui = new BadOpoGUI(domain);
                gui.setVisible(true);

            } catch (Exception e) {
                BadOpoLogger.logError("Error fatal al iniciar la aplicación", e);
            }
        });
    }
}
