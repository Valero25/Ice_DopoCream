package presentation;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private Image bgImg;
    private Image menuImg;

    public HomePanel(ImageLoader loader, Runnable onPlay, Runnable onExit) {
        this.bgImg = loader.getBackgroundImage("HOME");
        this.menuImg = loader.getBackgroundImage("MENU_BOARD");
        setLayout(null);

        // Botones invisibles sobre la imagen del menú (Coordenadas aproximadas)
        JButton btnPlay = createTextButton("PLAY", 400, 380, 100, 40, onPlay);
        JButton btnExit = createTextButton("EXIT", 400, 440, 100, 40, onExit);

        add(btnPlay);
        add(btnExit);
    }

    private JButton createTextButton(String text, int x, int y, int w, int h, Runnable action) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font("Monospaced", Font.BOLD, 24));
        btn.setForeground(new Color(101, 67, 33)); // Color café madera
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efectos hover: cambiar color del texto
        // Efectos hover: cambiar color del texto y tamaño
        btn.addMouseListener(new ColorHoverListener(
                btn,
                new Color(101, 67, 33),
                new Color(255, 140, 0),
                true,
                new Font("Monospaced", Font.BOLD, 24),
                new Font("Monospaced", Font.BOLD, 26)));

        btn.addActionListener(e -> action.run());
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
        // Dibujar el tablero de menú centrado abajo
        int menuW = 400;
        int menuH = 250;
        int mx = (getWidth() - menuW) / 2;
        int my = 300;
        g.drawImage(menuImg, mx, my, menuW, menuH, this);
    }
}