package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Modo extends JFrame {

    private Font pixelFont;

    public Modo(Dimension size, Runnable onBack,
                Runnable onPvsP, Runnable onPvsM, Runnable onMvsM) {

        setTitle("Modo de Juego - Bad Dopo Cream");
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        pixelFont = new Font("Monospaced", Font.BOLD, 18);

        // FONDO ORIGINAL
        ImageIcon fondoGif = new ImageIcon(getClass().getResource("FondoModo.gif"));
        Image fondoImg = fondoGif.getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);

        JLabel fondo = new JLabel(new ImageIcon(fondoImg));
        fondo.setLayout(null);
        setContentPane(fondo);

        JLabel selectMode = new JLabel("SELECT MODE");
        selectMode.setFont(pixelFont);
        selectMode.setForeground(Color.WHITE);
        selectMode.setHorizontalAlignment(SwingConstants.CENTER);
        selectMode.setBounds(0, 5, size.width, 90);
        fondo.add(selectMode);

        JButton botonJvJ = crearBotonGif("BotonJvJ.gif",
                "Jugador<br>VS<br>Jugador");

        JButton botonMvJ = crearBotonGif("BotonMvJ.gif",
                "Jugador<br>VS<br>Maquina");

        JButton botonMvM = crearBotonGif("BotonMvM.gif",
                "Maquina<br>VS<br>Maquina");

        int bw = botonJvJ.getWidth();
        int bh = botonJvJ.getHeight();

        int cx = (size.width - bw) / 2;

        int yStart = 60;
        int espacio = 5;

        botonJvJ.setBounds(cx, yStart, bw, bh);
        botonMvJ.setBounds(cx, yStart + bh + espacio, bw, bh);
        botonMvM.setBounds(cx, yStart + (bh * 2) + (espacio * 2), bw, bh);

        fondo.add(botonJvJ);
        fondo.add(botonMvJ);
        fondo.add(botonMvM);

        // BACK — ahora SIEMPRE amarillo
        JButton back = crearBoton("BACK");
        back.setBounds(20, size.height - 120, 180, 50);
        fondo.add(back);

        // ACCIONES
        botonJvJ.addActionListener(e -> { dispose(); onPvsP.run(); });
        botonMvJ.addActionListener(e -> { dispose(); onPvsM.run(); });
        botonMvM.addActionListener(e -> { dispose(); onMvsM.run(); });
        back.addActionListener(e -> { dispose(); onBack.run(); });

        setVisible(true);
    }

    // BOTÓN GIF + TEXTO PIXEL
    private JButton crearBotonGif(String gifName, String textoHTML) {

        ImageIcon original = new ImageIcon(getClass().getResource(gifName));

        int newW = (int) (original.getIconWidth() * 0.60);
        int newH = (int) (original.getIconHeight() * 0.42);

        Image imgAnim = original.getImage().getScaledInstance(newW, newH, Image.SCALE_DEFAULT);
        ImageIcon animado = new ImageIcon(imgAnim);

        Image imgQuieto = original.getImage().getScaledInstance(newW, newH, Image.SCALE_DEFAULT);
        ImageIcon quieto = new ImageIcon(imgQuieto);

        JButton b = new JButton(quieto);
        b.setLayout(null);
        b.setPreferredSize(new Dimension(newW, newH));
        b.setSize(newW, newH);

        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);

        // TEXTO PIXEL
        JLabel lbl = new JLabel("<html><center>" + textoHTML + "</center></html>");
        lbl.setBounds(0, 0, newW, newH);
        lbl.setFont(pixelFont);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        lbl.setForeground(new Color(92, 59, 30));

        b.add(lbl);

        // HOVER
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                b.setIcon(animado);
                lbl.setForeground(new Color(212, 166, 53));
            }
            @Override public void mouseExited(MouseEvent e) {
                b.setIcon(quieto);
                lbl.setForeground(new Color(92, 59, 30));
            }
        });

        return b;
    }

    // BOTÓN BACK — AHORA SIEMPRE AMARILLO
    private JButton crearBoton(String txt) {

        JButton b = new JButton(txt);

        b.setFont(pixelFont);

        // color AMARILLO por defecto
        b.setForeground(new Color(212, 166, 53));

        b.setOpaque(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);

        // hover → solo cambia tono
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                b.setForeground(new Color(255, 210, 80));
            }
            @Override public void mouseExited(MouseEvent e) {
                b.setForeground(new Color(212, 166, 53));
            }
        });

        return b;
    }
}
