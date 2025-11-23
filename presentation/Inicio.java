package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Inicio extends JFrame {

    private Font pixelFont;

    public Inicio(Dimension size, Runnable onBack, Runnable onPlay) {

        setTitle("Inicio - Bad Dopo Cream");
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // -------------------------------------------------------
        //   FUENTE (ESTÁNDAR QUE YA FUNCIONA)
        // -------------------------------------------------------
        pixelFont = new Font("Monospaced", Font.BOLD, 18);

        // -------------------------------------------------------
        //   FONDO GIF
        // -------------------------------------------------------
        ImageIcon gif = new ImageIcon(getClass().getResource("Fondo.gif"));
        Image img = gif.getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);

        JLabel fondo = new JLabel(new ImageIcon(img));
        fondo.setLayout(null);
        setContentPane(fondo);

        // -------------------------------------------------------
        //   PANEL = IMAGEN menu.png
        // -------------------------------------------------------
        ImageIcon panelImg = new ImageIcon(getClass().getResource("menu.png"));

        int pw = (int) (size.width * 0.65);
        int ph = (int) (size.height * 0.28);

        Image menuEscalado = panelImg.getImage().getScaledInstance(pw, ph, Image.SCALE_SMOOTH);
        JLabel panel = new JLabel(new ImageIcon(menuEscalado));
        panel.setLayout(null);

        int px = (size.width - pw) / 2;
        int py = (int) (size.height * 0.60);

        panel.setBounds(px, py, pw, ph);
        fondo.add(panel);

        // -------------------------------------------------------
        //   BOTONES
        // -------------------------------------------------------
        JButton play = crearBoton("PLAY");
        JButton back = crearBoton("BACK");

        int bw = pw - 70;
        int bh = 50;

        play.setBounds((pw - bw) / 2, (int) (ph * 0.25), bw, bh);
        back.setBounds((pw - bw) / 2, (int) (ph * 0.55), bw, bh);

        panel.add(play);
        panel.add(back);

        // -------------------------------------------------------
        //   Señales
        // -------------------------------------------------------
        play.addActionListener(e -> {
            dispose();
            onPlay.run();
        });

        back.addActionListener(e -> {
            dispose();
            onBack.run();
        });

        setVisible(true);
    }

    // -------------------------------------------------------
    //   BOTONES ESTILO PIXEL (SIN FUENTE TTF)
    // -------------------------------------------------------
    private JButton crearBoton(String txt) {

        JButton b = new JButton(txt);

        b.setFont(pixelFont);
        b.setForeground(new Color(92, 59, 30));

        b.setOpaque(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);

        b.setHorizontalAlignment(SwingConstants.CENTER);

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setForeground(new Color(212, 166, 53));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setForeground(new Color(92, 59, 30));
            }
        });

        return b;
    }
}
