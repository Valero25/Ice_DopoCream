package presentation;

import javax.swing.*;
import java.awt.*;

public class ResultBackgroundPanel extends JPanel {
    public ResultBackgroundPanel() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
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
}
