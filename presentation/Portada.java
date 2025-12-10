package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Portada extends JFrame {

    public Portada(Dimension size, Runnable onClick) {

        setTitle("Portada - Bad Dopo Cream");
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Cargar GIF de fondo
        ImageIcon gif = new ImageIcon(getClass().getResource("Fondo.gif"));
        Image img = gif.getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);

        JLabel fondo = new JLabel(new ImageIcon(img));
        fondo.setLayout(null);
        setContentPane(fondo);

        // Detectar click para pasar a Inicio
        fondo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();      // Cerrar pantalla
                onClick.run();  // Señal a la GUI
            }
        });

        setVisible(true);
    }
}
