package presentation;

import javax.swing.*;
import java.awt.*;

public class LevelSelectionPanel extends JPanel {
    private Image bgImg;
    private ImageLoader loader;
    private JLabel previewLabel;

    public LevelSelectionPanel(ImageLoader loader, java.util.function.Consumer<String> onLevelSelect, Runnable onBack) {
        this.loader = loader;
        this.bgImg = loader.getBackgroundImage("LEVEL");
        setLayout(null);

        // Área de Preview (Dentro del marco de madera de la imagen de fondo)
        // Movido mucho más a la izquierda y agrandado para evitar recortes
        previewLabel = new JLabel();
        previewLabel.setBounds(-50, 100, 500, 500); // Ajustado para encajar en el marco
        add(previewLabel);

        // Botones de Nivel (Derecha) - Ahora usan imágenes
        createLevelBtn("Nivel 1", "LEVEL_1", "Nivel1.png", 550, 180, onLevelSelect);
        createLevelBtn("Nivel 2", "LEVEL_2", "Nivel2.png", 550, 280, onLevelSelect);
        createLevelBtn("Nivel 3", "LEVEL_3", "Nivel3.png", 550, 380, onLevelSelect);

        // Botón Back
        JButton back = new JButton("BACK");
        back.setBounds(50, 600, 100, 40);
        back.setFont(new Font("Monospaced", Font.BOLD, 18));
        back.setForeground(Color.ORANGE);
        back.setBackground(new Color(50, 50, 50));
        back.setFocusPainted(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        // Efecto hover - Color normal: (50,50,50)/ORANGE, Hover: (80,80,80)/YELLOW
        back.addMouseListener(new BackButtonHoverListener(
                back,
                new Color(50, 50, 50), new Color(80, 80, 80),
                Color.ORANGE, Color.YELLOW));

        back.addActionListener(e -> onBack.run());
        add(back);
    }

    private void createLevelBtn(String text, String gifKey, String imagePath, int x, int y,
            java.util.function.Consumer<String> action) {
        JButton btn = new JButton();
        btn.setBounds(x, y, 150, 80);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);

        // Cargar la imagen del botón (Nivel1.png, Nivel2.png, Nivel3.png)
        try {
            // CORRECCIÓN: Las imágenes están en /presentation/ y tienen mayúscula
            // (Nivel1.png)
            java.net.URL imageUrl = getClass().getResource("/presentation/" + imagePath);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                // Escalar la imagen al tamaño del botón
                Image scaledImage = icon.getImage().getScaledInstance(150, 80, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(scaledImage));
            } else {
                // Fallback: si no se encuentra la imagen, usar texto
                btn.setText(text);
                btn.setFont(new Font("Monospaced", Font.BOLD, 16));
                btn.setForeground(Color.WHITE);
                btn.setBackground(new Color(101, 67, 33));
                btn.setContentAreaFilled(true);
            }
        } catch (Exception e) {
            System.err.println("⚠ No se pudo cargar imagen del botón: " + imagePath);
            // Fallback: usar texto
            btn.setText(text);
            btn.setFont(new Font("Monospaced", Font.BOLD, 16));
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(101, 67, 33));
            btn.setContentAreaFilled(true);
        }

        // Cargar el GIF de preview
        ImageIcon previewGif = null;
        String path = loader.getPath(gifKey, "");
        if (path != null) {
            try {
                java.net.URL url = getClass().getResource(path);
                if (url != null) {
                    previewGif = new ImageIcon(url);
                }
            } catch (Exception e) {
                System.err.println("⚠ No se pudo cargar preview: " + path);
            }
        }

        final ImageIcon finalPreview = previewGif;

        btn.addMouseListener(new LevelButtonHoverListener(btn, x, y, previewLabel, finalPreview));

        btn.addActionListener(e -> action.accept(gifKey));
        add(btn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
    }
}