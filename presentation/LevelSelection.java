package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class LevelSelection extends JFrame {

    private Font pixelFont;
    private JLabel previewLabel;
    private Consumer<String> onLevelSelected;

    public LevelSelection(Dimension size,
                          Consumer<String> onLevelSelected,
                          Runnable onBack) {

        this.onLevelSelected = onLevelSelected;

        setTitle("Select Level - Bad Dopo Cream");
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        pixelFont = new Font("Monospaced", Font.BOLD, 18);

        // Fondo
        ImageIcon fondo = new ImageIcon(getClass().getResource("fondoNivel.png"));
        Image img = fondo.getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);
        JLabel background = new JLabel(new ImageIcon(img));
        background.setLayout(null);
        setContentPane(background);

        // Título
        JLabel title = new JLabel("SELECT LEVEL");
        title.setFont(pixelFont);
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 10, size.width, 80);
        background.add(title);

        // Preview GIF a la izquierda, subido 50 pixeles + 15 hacia abajo (neto 65 px desde original)
        previewLabel = new JLabel();
        previewLabel.setBounds(80, 115, 400, 300); // Y desplazado 15 pixeles abajo respecto a antes
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        previewLabel.setVerticalAlignment(SwingConstants.CENTER);
        background.add(previewLabel);

        // Botones niveles 2x2 con letras fuera y desplazados +60px a la derecha (30+30)
        int bw = 150;
        int bh = 100;
        int col1X = size.width - 440; // antes 470, +30 px más a la derecha
        int col2X = size.width - 190; // antes 220, +30 px más a la derecha
        int row1Y = 170;
        int row2Y = 320;

        // Crear solo botones con imagen sin texto (para que texto vaya aparte)
        JButton btnNivel1 = createImageButton("nivel1.png", "Nivel 1");
        JButton btnNivel2 = createImageButton("nivel2.png", "Nivel 2");
        JButton btnNivel3 = createImageButton("nivel3.png", "Nivel 3");
        JButton btnNivel4 = createImageButton("nivel4.png", "Nivel 4");

        btnNivel1.setBounds(col1X, row1Y, bw, bh);
        btnNivel2.setBounds(col2X, row1Y, bw, bh);
        btnNivel3.setBounds(col1X, row2Y, bw, bh);
        btnNivel4.setBounds(col2X, row2Y, bw, bh);

        background.add(btnNivel1);
        background.add(btnNivel2);
        background.add(btnNivel3);
        background.add(btnNivel4);

        // Añadir etiquetas de texto justo debajo de cada botón
        JLabel lbl1 = createTextLabel("Nivel 1");
        JLabel lbl2 = createTextLabel("Nivel 2");
        JLabel lbl3 = createTextLabel("Nivel 3");
        JLabel lbl4 = createTextLabel("Nivel 4");

        lbl1.setBounds(col1X, row1Y + bh, bw, 30);
        lbl2.setBounds(col2X, row1Y + bh, bw, 30);
        lbl3.setBounds(col1X, row2Y + bh, bw, 30);
        lbl4.setBounds(col2X, row2Y + bh, bw, 30);

        background.add(lbl1);
        background.add(lbl2);
        background.add(lbl3);
        background.add(lbl4);

        // Botón Back
        JButton back = new JButton("BACK");
        back.setFont(pixelFont);
        back.setForeground(Color.WHITE);
        back.setOpaque(false);
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setBounds(20, size.height - 100, 150, 40);

        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { back.setForeground(new Color(255, 210, 80)); }
            @Override
            public void mouseExited(MouseEvent e) { back.setForeground(Color.WHITE); }
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                onBack.run();
            }
        });

        background.add(back);

        setVisible(true);
    }

    private JButton createImageButton(String imageName, String levelName) {

        ImageIcon icon = new ImageIcon(getClass().getResource(imageName));
        Image scaledImg = icon.getImage().getScaledInstance(140, 90, Image.SCALE_SMOOTH); // un poco más pequeño
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        JButton btn = new JButton(scaledIcon);

        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showPreviewGif(levelName);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onLevelSelected != null) onLevelSelected.accept(levelName);
                dispose();
            }
        });

        return btn;
    }

    private JLabel createTextLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(pixelFont);
        lbl.setForeground(new Color(212, 166, 53));
        return lbl;
    }

    private void showPreviewGif(String levelName) {
    
        String gifName;
    
        switch (levelName) {
            case "Nivel 1":
                gifName = "Nivel1.gif";
                break;
            case "Nivel 2":
                gifName = "Nivel2.gif";
                break;
            case "Nivel 3":
                gifName = "Nivel3.gif";
                break;
            case "Nivel 4":
                gifName = "Nivel0.gif";
                break;
            default:
                gifName = null;
        }
    
        if (gifName == null) {
            previewLabel.setIcon(null);
            return;
        }
    
        ImageIcon gif = new ImageIcon(getClass().getResource(gifName));
    
        if (levelName.equals("Nivel 4")) {
            // Nivel 4: tamaño igual que antes
            previewLabel.setBounds(110, 115, 350, 300);
            Image scaled = gif.getImage().getScaledInstance(350, 300, Image.SCALE_DEFAULT);
            previewLabel.setIcon(new ImageIcon(scaled));
        } else {
            // Otros niveles: más ancho (450 px) y mismo alto (300 px)
            previewLabel.setBounds(80, 115, 450, 300);
            Image scaled = gif.getImage().getScaledInstance(450, 300, Image.SCALE_DEFAULT);
            previewLabel.setIcon(new ImageIcon(scaled));
        }
    }

}
