package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Ventana de seleccion de modo de juego (clase legacy).
 * Proporciona una interfaz para elegir entre modos de juego.
 * 
 * <p>Modos disponibles:</p>
 * <ul>
 *   <li>PvsP - Jugador vs Jugador</li>
 *   <li>PvsM - Jugador vs Maquina</li>
 *   <li>MvsM - Maquina vs Maquina</li>
 * </ul>
 * 
 * <p>Nota: Esta clase puede estar siendo reemplazada por
 * ModeSelectionPanel en la nueva arquitectura.</p>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see ModeSelectionPanel
 */
public class ModeSelection extends JFrame {

    private Font pixelFont;

    public ModeSelection(Dimension size, Runnable onBack,
            Runnable onPvsP, Runnable onPvsM, Runnable onMvsM) {

        setTitle("Game Mode - Bad Dopo Cream");
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        pixelFont = new Font("Monospaced", Font.BOLD, 18);

        ImageIcon gif = new ImageIcon(getClass().getResource("FondoModo.gif"));
        Image img = gif.getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);

        JLabel background = new JLabel(new ImageIcon(img));
        background.setLayout(null);
        setContentPane(background);

        JLabel selectMode = new JLabel("SELECT MODE");
        selectMode.setFont(pixelFont);
        selectMode.setForeground(Color.WHITE);
        selectMode.setHorizontalAlignment(SwingConstants.CENTER);
        selectMode.setBounds(0, 5, size.width, 90);
        background.add(selectMode);

        JButton btnPvsP = createGifButton("BotonJvJ.gif", "Player<br>VS<br>Player");
        JButton btnPvsM = createGifButton("BotonMvJ.gif", "Player<br>VS<br>Machine");
        JButton btnMvsM = createGifButton("BotonMvM.gif", "Machine<br>VS<br>Machine");

        int bw = btnPvsP.getWidth();
        int bh = btnPvsP.getHeight();
        int cx = (size.width - bw) / 2;
        int yStart = 60;
        int spacing = 5;

        btnPvsP.setBounds(cx, yStart, bw, bh);
        btnPvsM.setBounds(cx, yStart + bh + spacing, bw, bh);
        btnMvsM.setBounds(cx, yStart + (bh * 2) + (spacing * 2), bw, bh);

        background.add(btnPvsP);
        background.add(btnPvsM);
        background.add(btnMvsM);

        JButton back = createBackButton("BACK");
        back.setBounds(20, size.height - 120, 180, 50);
        background.add(back);

        btnPvsP.addActionListener(e -> {
            dispose();
            onPvsP.run();
        });
        btnPvsM.addActionListener(e -> {
            dispose();
            onPvsM.run();
        });
        btnMvsM.addActionListener(e -> {
            dispose();
            onMvsM.run();
        });
        back.addActionListener(e -> {
            dispose();
            onBack.run();
        });

        setVisible(true);
    }

    private JButton createGifButton(String gifName, String htmlText) {
        ImageIcon original = new ImageIcon(getClass().getResource(gifName));

        int newW = (int) (original.getIconWidth() * 0.60);
        int newH = (int) (original.getIconHeight() * 0.42);

        Image img1 = original.getImage().getScaledInstance(newW, newH, Image.SCALE_DEFAULT);
        ImageIcon animated = new ImageIcon(img1);

        Image img2 = original.getImage().getScaledInstance(newW, newH, Image.SCALE_DEFAULT);
        ImageIcon idle = new ImageIcon(img2);

        JButton b = new JButton(idle);
        b.setLayout(null);
        b.setPreferredSize(new Dimension(newW, newH));
        b.setSize(newW, newH);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);

        JLabel lbl = new JLabel("<html><center>" + htmlText + "</center></html>");
        lbl.setBounds(0, 0, newW, newH);
        lbl.setFont(pixelFont);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        lbl.setForeground(new Color(92, 59, 30));
        b.add(lbl);

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setIcon(animated);
                lbl.setForeground(new Color(212, 166, 53));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setIcon(idle);
                lbl.setForeground(new Color(92, 59, 30));
            }
        });

        return b;
    }

    private JButton createBackButton(String txt) {
        JButton b = new JButton(txt);
        b.setFont(pixelFont);
        b.setForeground(new Color(212, 166, 53));
        b.setOpaque(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setForeground(new Color(255, 210, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setForeground(new Color(212, 166, 53));
            }
        });

        return b;
    }
}
