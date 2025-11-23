package presentation;

import javax.swing.*;
import java.awt.*;

public class DopoCreamGUI {

    private Dimension windowSize;

    public DopoCreamGUI() {

        // Tamaño = mitad de la pantalla
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        windowSize = new Dimension(screen.width / 2, screen.height / 2);

        mostrarPortada();
    }

    // ----------------------------------------------------
    //   Mostrar Portada
    // ----------------------------------------------------
    private void mostrarPortada() {
        new Portada(windowSize, this::mostrarInicio);
    }

    // ----------------------------------------------------
    //   Mostrar Inicio
    // ----------------------------------------------------
    private void mostrarInicio() {
        new Inicio(windowSize,
                this::mostrarPortada,   // Acción Back
                this::mostrarModo       // Acción Play
        );
    }

    // ----------------------------------------------------
    //   Mostrar Modo
    // ----------------------------------------------------
    private void mostrarModo() {
        new Modo(windowSize,
                this::mostrarInicio,
                () -> System.out.println("Modo JvJ seleccionado"),
                () -> System.out.println("Modo JvM seleccionado"),
                () -> System.out.println("Modo MvM seleccionado")
        );
    }

    // ----------------------------------------------------
    //   MAIN
    // ----------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DopoCreamGUI::new);
    }
}
