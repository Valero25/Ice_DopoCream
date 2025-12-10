package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PauseButtonHoverListener extends MouseAdapter {
    private JButton btn;

    public PauseButtonHoverListener(JButton btn) {
        this.btn = btn;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        btn.setBackground(new Color(100, 100, 100));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        btn.setBackground(new Color(50, 50, 50));
    }
}
