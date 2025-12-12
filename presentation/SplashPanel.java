package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de pantalla de inicio (splash screen).
 * Muestra una animacion o imagen de bienvenida.
 * El usuario puede hacer clic para continuar al menu principal.
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see BadOpoGUI
 */
public class SplashPanel extends JPanel {
    private Image splashImage;

    public SplashPanel(ImageLoader loader, Runnable onClick) {
        setBackground(Color.BLACK);

        // Mouse click avanza al siguiente menú
        addMouseListener(StandardMouseListener.onClick(this, onClick));

        // Intentamos cargar la imagen
        splashImage = loader.getBackgroundImage("SPLASH");

        if (splashImage == null) {
            System.err.println("⚠ ADVERTENCIA: No se encontró la imagen de Splash.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (splashImage != null) {
            // Dibuja la imagen escalada a pantalla completa
            g.drawImage(splashImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // FALLBACK: Mostrar mensaje de error
            g.setColor(Color.RED);
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            String msg = "MISSING: Fondo.gif";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = getHeight() / 2;
            g.drawString(msg, x, y);

            g.setColor(Color.WHITE);
            String info = "Haz clic para continuar...";
            x = (getWidth() - fm.stringWidth(info)) / 2;
            g.drawString(info, x, y + 40);
        }
    }
}