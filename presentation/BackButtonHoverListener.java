package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BackButtonHoverListener extends MouseAdapter {
    private JButton button;
    private Color normalBg;
    private Color hoverBg;
    private Color normalFg;
    private Color hoverFg;

    public BackButtonHoverListener(JButton button, Color normalBg, Color hoverBg, Color normalFg, Color hoverFg) {
        this.button = button;
        this.normalBg = normalBg;
        this.hoverBg = hoverBg;
        this.normalFg = normalFg;
        this.hoverFg = hoverFg;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        button.setBackground(hoverBg);
        button.setForeground(hoverFg);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        button.setBackground(normalBg);
        button.setForeground(normalFg);
    }
}
