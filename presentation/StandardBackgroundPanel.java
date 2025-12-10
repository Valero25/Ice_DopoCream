package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de fondo unificado con estilos predefinidos.
 * Reemplaza InfoBackgroundPanel, PausePanelBackground, ResultBackgroundPanel.
 */
public class StandardBackgroundPanel extends JPanel {

    public enum Style {
        WINTER, // Gradiente celeste + copos de nieve (Info)
        DARK_OVERLAY, // Negro semi-transparente (Pausa)
        RESULT // Gradiente oscuro + borde amarillo (Resultados)
    }

    private Style style;

    public StandardBackgroundPanel(Style style) {
        this.style = style;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Configurar padding según estilo
        switch (style) {
            case WINTER:
                setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
                break;
            case DARK_OVERLAY:
            case RESULT:
                setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Importante para limpiar si fuera opaco, aqui es false
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (style) {
            case WINTER:
                paintWinter(g2);
                break;
            case DARK_OVERLAY:
                paintDarkOverlay(g2);
                break;
            case RESULT:
                paintResult(g2);
                break;
        }
    }

    private void paintWinter(Graphics2D g2) {
        // Fondo con gradiente invernal
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(240, 248, 255),
                0, getHeight(), new Color(230, 240, 255));
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // Borde con efecto de nieve
        g2.setColor(new Color(180, 220, 255));
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 30, 30);

        // Decoración de copos de nieve en las esquinas
        g2.setColor(new Color(200, 230, 255, 150));
        drawSnowflake(g2, 30, 30, 20);
        drawSnowflake(g2, getWidth() - 30, 30, 20);
        drawSnowflake(g2, 30, getHeight() - 30, 20);
        drawSnowflake(g2, getWidth() - 30, getHeight() - 30, 20);
    }

    private void paintDarkOverlay(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
    }

    private void paintResult(Graphics2D g2) {
        // Fondo con gradiente
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(20, 20, 60),
                0, getHeight(), new Color(60, 20, 60));
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Borde
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
    }

    private void drawSnowflake(Graphics2D g2, int x, int y, int size) {
        int half = size / 2;
        // Líneas principales
        g2.drawLine(x - half, y, x + half, y);
        g2.drawLine(x, y - half, x, y + half);
        g2.drawLine(x - half / 2, y - half / 2, x + half / 2, y + half / 2);
        g2.drawLine(x - half / 2, y + half / 2, x + half / 2, y - half / 2);
    }
}
