package presentation;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelButtonHoverListener extends MouseAdapter {
    private JButton btn;
    private int x;
    private int y;
    private JLabel previewLabel;
    private ImageIcon previewGif;

    public LevelButtonHoverListener(JButton btn, int x, int y, JLabel previewLabel, ImageIcon previewGif) {
        this.btn = btn;
        this.x = x;
        this.y = y;
        this.previewLabel = previewLabel;
        this.previewGif = previewGif;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Efecto hover: hacer el botón un poco más grande o brillante
        btn.setBounds(x - 5, y - 5, 160, 90);

        // Mostrar GIF en el marco si existe
        if (previewGif != null) {
            previewLabel.setIcon(previewGif);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Restaurar tamaño original
        btn.setBounds(x, y, 150, 80);
        previewLabel.setIcon(null);
    }
}
