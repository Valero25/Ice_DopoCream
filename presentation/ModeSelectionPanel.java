package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para seleccionar el modo de juego.
 * Permite elegir entre los diferentes modos disponibles.
 * 
 * <p>Modos disponibles:</p>
 * <ul>
 *   <li>SINGLE - Un jugador contra enemigos</li>
 *   <li>PVP - Jugador vs Jugador</li>
 *   <li>PVM - Jugador vs Maquina</li>
 *   <li>MVM - Maquina vs Maquina</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see BadOpoGUI
 */
public class ModeSelectionPanel extends JPanel {
    private Image bgImg;

    public ModeSelectionPanel(ImageLoader loader, java.util.function.Consumer<String> onModeSelect, Runnable onBack) {
        this.bgImg = loader.getBackgroundImage("MODE"); // FondoModo.png
        setLayout(null);

        // Botones GIF/PNG
        createModeButton(loader, "BTN_SINGLE", 300, 100, onModeSelect, "SINGLE");
        createModeButton(loader, "BTN_PVP", 300, 200, onModeSelect, "PVP");
        createModeButton(loader, "BTN_PVM", 300, 300, onModeSelect, "PVM");
        createModeButton(loader, "BTN_MVM", 300, 400, onModeSelect, "MVM");

        JButton back = new JButton("BACK");
        back.setBounds(50, 600, 100, 50);
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.setForeground(Color.ORANGE);
        back.setFont(new Font("Monospaced", Font.BOLD, 20));
        back.setFocusPainted(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        // Efecto hover
        back.addMouseListener(StandardMouseListener.onHoverFg(
                back,
                Color.ORANGE,
                Color.YELLOW)
                .withFontEffect(
                        new Font("Monospaced", Font.BOLD, 20),
                        new Font("Monospaced", Font.BOLD, 22)));

        back.addActionListener(e -> onBack.run());
        add(back);
    }

    private void createModeButton(ImageLoader loader, String imgKey, int x, int y,
            java.util.function.Consumer<String> callback, String modeKey) {
        JButton btn;

        // Intentar cargar la imagen del botón
        String path = loader.getPath(imgKey, "");
        if (path != null) {
            try {
                java.net.URL url = getClass().getResource(path);
                if (url != null) {
                    ImageIcon icon = new ImageIcon(url);
                    btn = new JButton(icon);
                    btn.setBorderPainted(false);
                    btn.setContentAreaFilled(false);
                    btn.setFocusPainted(false);
                    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    // Efecto hover: Agregar un listener que cambie el borde
                    // Efecto hover: Agregar un listener que cambie el borde
                    btn.addMouseListener(StandardMouseListener.onHoverBg(btn, null, null) // Dummy for starting
                            .withBorderEffect(Color.YELLOW, 3));
                } else {
                    // Fallback: botón con texto y efectos
                    btn = createTextButton(modeKey);
                }
            } catch (Exception e) {
                // Fallback: botón con texto y efectos
                btn = createTextButton(modeKey);
            }
        } else {
            // Fallback: botón con texto y efectos
            btn = createTextButton(modeKey);
        }

        btn.setBounds(x, y, 300, 100);
        btn.addActionListener(e -> callback.accept(modeKey));
        add(btn);
    }

    private JButton createTextButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 24));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(101, 67, 33));
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover: cambiar color de fondo
        // Efecto hover: cambiar color de fondo
        btn.addMouseListener(StandardMouseListener.onHoverBg(
                btn,
                new Color(101, 67, 33),
                new Color(150, 100, 50)));

        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
    }
}