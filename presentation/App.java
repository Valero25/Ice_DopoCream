package presentation;

import domain.game.DomainController;
import javax.swing.SwingUtilities;
import domain.shared.BadOpoLogger;

/**
 * Clase principal de la aplicacion Bad DOPO Cream.
 * Punto de entrada que inicializa el controlador del dominio y la interfaz grafica.
 * 
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Configurar optimizaciones graficas del sistema</li>
 *   <li>Instanciar el controlador del dominio</li>
 *   <li>Crear y mostrar la ventana principal de la GUI</li>
 *   <li>Manejar errores fatales de inicializacion</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see BadOpoGUI
 * @see DomainController
 */
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
