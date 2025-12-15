package presentation;

import javax.swing.*;
import java.awt.*;

public class PauseOverlay extends JPanel {

    public PauseOverlay(Runnable onResume, Runnable onSave, Runnable onMenu) {
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Panel central con fondo semi-transparente
        // Panel central con fondo semi-transparente
        StandardBackgroundPanel pausePanel = new StandardBackgroundPanel(StandardBackgroundPanel.Style.DARK_OVERLAY);

        // Título PAUSA
        JLabel titleLabel = new JLabel("PAUSA");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pausePanel.add(titleLabel);

        pausePanel.add(Box.createVerticalStrut(40));

        // Botón Reanudar
        JButton resumeBtn = createPauseButton("REANUDAR (P)");
        resumeBtn.addActionListener(e -> onResume.run());
        pausePanel.add(resumeBtn);

        pausePanel.add(Box.createVerticalStrut(20));

        // Botón Guardar
        JButton saveBtn = createPauseButton("GUARDAR");
        saveBtn.addActionListener(e -> onSave.run());
        pausePanel.add(saveBtn);

        pausePanel.add(Box.createVerticalStrut(20));

        // Botón Menú Principal
        JButton menuBtn = createPauseButton("MENÚ PRINCIPAL");
        menuBtn.addActionListener(e -> onMenu.run());
        pausePanel.add(menuBtn);

        add(pausePanel);
    }

    private JButton createPauseButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 24));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(50, 50, 50));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        btn.addMouseListener(StandardMouseListener.onHoverBg(
                btn,
                new Color(50, 50, 50),
                new Color(100, 100, 100)));

        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Fondo semi-transparente oscuro
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
