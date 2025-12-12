package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de fin de juego que muestra los resultados de la partida.
 * Presenta el ganador, puntajes finales y opciones para continuar.
 * 
 * <p>Informacion mostrada:</p>
 * <ul>
 *   <li>Resultado (Victoria/Derrota/Empate)</li>
 *   <li>Puntaje de cada jugador</li>
 *   <li>Estadisticas de la partida</li>
 * </ul>
 * 
 * <p>Opciones disponibles:</p>
 * <ul>
 *   <li>Volver a jugar</li>
 *   <li>Regresar al menu principal</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see BadOpoGUI
 */
public class GameOverPanel extends JPanel {

    public GameOverPanel(String gameMode, String result, String winner, int score,
            Runnable onMenu, Runnable onLevelSelect, Runnable onNextLevel,
            Runnable onRestartLevel, Runnable onExit, boolean hasNextLevel,
            String playerName) {
        setLayout(new GridBagLayout());

        // Panel central
        // Panel central
        StandardBackgroundPanel resultPanel = new StandardBackgroundPanel(StandardBackgroundPanel.Style.RESULT);

        // Título según resultado
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (result.equals("WON")) {
            titleLabel.setText("¡VICTORIA!");
            titleLabel.setForeground(new Color(50, 255, 50));
        } else if (result.equals("GAME_OVER")) {
            titleLabel.setText("GAME OVER");
            titleLabel.setForeground(new Color(255, 50, 50));
        } else if (result.equals("TIMEOUT")) {
            titleLabel.setText("¡TIEMPO AGOTADO!");
            titleLabel.setForeground(new Color(255, 200, 50));
        }

        resultPanel.add(titleLabel);
        resultPanel.add(Box.createVerticalStrut(30));

        // Mostrar nombre del jugador en modo SINGLE
        if (gameMode.equals("SINGLE") && playerName != null && !playerName.isEmpty()) {
            JLabel playerLabel = new JLabel("Jugador: " + playerName);
            playerLabel.setFont(new Font("Arial", Font.BOLD, 28));
            playerLabel.setForeground(Color.CYAN);
            playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultPanel.add(playerLabel);
            resultPanel.add(Box.createVerticalStrut(20));
        }
        // Mostrar ganador en PVP, PVM o MVM
        else if (winner != null && !winner.isEmpty() && !winner.equals("EMPATE") &&
                (gameMode.equals("PVP") || gameMode.equals("MVM") || gameMode.equals("PVM"))) {
            JLabel winnerLabel = new JLabel("Ganador: " + winner);
            winnerLabel.setFont(new Font("Arial", Font.BOLD, 32));
            winnerLabel.setForeground(Color.YELLOW);
            winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultPanel.add(winnerLabel);
            resultPanel.add(Box.createVerticalStrut(20));
        }
        // Mostrar empate
        else if (winner != null && winner.equals("EMPATE")) {
            JLabel empateLabel = new JLabel("¡EMPATE!");
            empateLabel.setFont(new Font("Arial", Font.BOLD, 32));
            empateLabel.setForeground(Color.ORANGE);
            empateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultPanel.add(empateLabel);
            resultPanel.add(Box.createVerticalStrut(20));
        }

        // Puntaje
        JLabel scoreLabel = new JLabel("Puntaje: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(scoreLabel);

        resultPanel.add(Box.createVerticalStrut(40));

        // Botón Reiniciar Nivel (siempre disponible)
        JButton restartBtn = createResultButton("REINICIAR NIVEL");
        restartBtn.addActionListener(e -> onRestartLevel.run());
        resultPanel.add(restartBtn);
        resultPanel.add(Box.createVerticalStrut(15));

        // Botones según el modo
        if (result.equals("WON") && hasNextLevel) {
            // En cualquier modo con victoria y si hay siguiente nivel
            JButton nextBtn = createResultButton("SIGUIENTE NIVEL");
            nextBtn.addActionListener(e -> onNextLevel.run());
            resultPanel.add(nextBtn);
            resultPanel.add(Box.createVerticalStrut(15));
        }

        // Botón Niveles (siempre disponible)
        JButton levelsBtn = createResultButton("SELECCIÓN DE NIVELES");
        levelsBtn.addActionListener(e -> onLevelSelect.run());
        resultPanel.add(levelsBtn);
        resultPanel.add(Box.createVerticalStrut(15));

        // Botón Menú Principal
        JButton menuBtn = createResultButton("MENÚ PRINCIPAL");
        menuBtn.addActionListener(e -> onMenu.run());
        resultPanel.add(menuBtn);
        resultPanel.add(Box.createVerticalStrut(15));

        // Botón Salir
        JButton exitBtn = createResultButton("SALIR");
        exitBtn.addActionListener(e -> onExit.run());
        resultPanel.add(exitBtn);

        add(resultPanel);
    }

    private JButton createResultButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(50, 100, 150));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(350, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        // Efecto hover
        btn.addMouseListener(StandardMouseListener.onHoverBg(
                btn,
                new Color(50, 100, 150),
                new Color(80, 150, 200)));

        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Fondo semi-transparente
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
