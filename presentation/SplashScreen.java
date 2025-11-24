package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SplashScreen extends JFrame {

    public SplashScreen(Dimension size, Runnable onClick) {

        setTitle("Splash - Bad Dopo Cream");
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        ImageIcon gif = new ImageIcon(getClass().getResource("Fondo.gif"));
        Image img = gif.getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);

        JLabel background = new JLabel(new ImageIcon(img));
        background.setLayout(null);
        setContentPane(background);

        background.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                onClick.run();
            }
        });

        setVisible(true);
    }
}
