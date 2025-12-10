package presentation;

import javax.swing.*;
import java.awt.*;

public class PausePanelBackground extends JPanel {
    public PausePanelBackground() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
    }
}
