package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StartButton extends JButton {
    public StartButton(String text, ActionListener action) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 26));
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addActionListener(action);
        setPreferredSize(new Dimension(300, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(new Color(34, 139, 34));
        } else if (getModel().isRollover()) {
            GradientPaint gp = new GradientPaint(0, 0, new Color(60, 220, 60),
                    0, getHeight(), new Color(40, 180, 40));
            g2.setPaint(gp);
        } else {
            GradientPaint gp = new GradientPaint(0, 0, new Color(50, 205, 50),
                    0, getHeight(), new Color(34, 139, 34));
            g2.setPaint(gp);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Borde brillante
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);

        super.paintComponent(g);
    }
}
