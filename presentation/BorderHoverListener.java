package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BorderHoverListener extends MouseAdapter {
    private JButton button;
    private Color borderColor;
    private int thickness;

    public BorderHoverListener(JButton button, Color borderColor, int thickness) {
        this.button = button;
        this.borderColor = borderColor;
        this.thickness = thickness;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(borderColor, thickness));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        button.setBorderPainted(false);
    }
}
