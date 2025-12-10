package presentation;

import javax.swing.*;
import java.awt.*;

public class InfoBackgroundPanel extends JPanel {
    public InfoBackgroundPanel() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

    private void drawSnowflake(Graphics2D g2, int x, int y, int size) {
        int half = size / 2;
        // Líneas principales
        g2.drawLine(x - half, y, x + half, y);
        g2.drawLine(x, y - half, x, y + half);
        g2.drawLine(x - half / 2, y - half / 2, x + half / 2, y + half / 2);
        g2.drawLine(x - half / 2, y + half / 2, x + half / 2, y - half / 2);
    }
}
