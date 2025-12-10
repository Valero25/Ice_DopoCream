package presentation;

import domain.shared.EntityInfo;

import javax.swing.*;
import java.awt.*;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class GamePanel extends JPanel {

    private ImageLoader loader;

    // --- ESTADO VISUAL (Lo que se va a pintar) ---
    private boolean[][] walls; // Mapa estático de muros
    private List<EntityInfo> entities; // Personajes y objetos dinámicos
    private int rows, cols;
    private int scoreP1;
    private int scoreP2;
    private float timeRemaining;
    private Image levelBackground; // Fondo específico del nivel
    private JButton pauseButton;

    // --- CALLBACKS (Para avisar a la GUI sin conocer el dominio) ---
    private Consumer<Integer> onKeyPressed;

    // Guardamos ancho previo para limpiar caché de imágenes
    private int lastWidth = 0;

    public GamePanel(Consumer<Integer> onKeyPressed) {
        this.onKeyPressed = onKeyPressed;
        this.loader = new ImageLoader();
        this.entities = Collections.emptyList(); // Lista vacía inicial

        setLayout(null); // Layout null para posicionar el botón de pausa
        setFocusable(true);
        setBackground(Color.BLACK);

        // Botón de pausa
        pauseButton = new JButton("❚❚");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 20));
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setBackground(new Color(100, 100, 100, 150));
        pauseButton.setBounds(10, 50, 50, 40);
        pauseButton.setFocusPainted(false);
        pauseButton.setBorderPainted(false);
        pauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pauseButton.addActionListener(new GamePauseListener(onKeyPressed));
        add(pauseButton);

        // Input: Capturamos la tecla y se la pasamos a la GUI
        // Input: Capturamos la tecla y se la pasamos a la GUI
        addKeyListener(new GameKeyListener(onKeyPressed));
    }

    /**
     * Configuración inicial del nivel (cosas que no cambian en cada frame).
     */
    public void setupBoard(boolean[][] walls) {
        this.walls = walls;
        this.rows = walls.length;
        this.cols = (rows > 0) ? walls[0].length : 0;
        repaint();
    }

    /**
     * Configura el fondo específico del nivel
     */
    public void setLevelBackground(Image background) {
        this.levelBackground = background;
        repaint();
    }

    // Jugadores separados (requisito del profesor)
    private EntityInfo player1;
    private EntityInfo player2;
    private String player1Name = "P1";
    private String player2Name = "P2";

    /**
     * MÉTODO PRINCIPAL: La GUI nos "empuja" los datos nuevos aquí para pintar.
     * Los jugadores se reciben por separado, no en la lista de entidades.
     * 
     * @param entities      Lista de items y enemigos (sin jugadores)
     * @param player1       EntityInfo del jugador 1 (puede ser null)
     * @param player2       EntityInfo del jugador 2 (puede ser null)
     * @param scoreP1       Puntuacion del jugador 1
     * @param scoreP2       Puntuacion del jugador 2
     * @param timeRemaining Tiempo restante en segundos
     */
    public void renderFrame(List<EntityInfo> entities, EntityInfo player1, EntityInfo player2,
            int scoreP1, int scoreP2, float timeRemaining) {
        this.entities = entities;
        this.player1 = player1;
        this.player2 = player2;
        this.scoreP1 = scoreP1;
        this.scoreP2 = scoreP2;
        this.timeRemaining = timeRemaining;
        repaint();
    }

    /**
     * Establece los nombres de los jugadores para mostrar en el HUD.
     */
    public void setPlayerNames(String p1Name, String p2Name) {
        this.player1Name = p1Name != null ? p1Name : "P1";
        this.player2Name = p2Name != null ? p2Name : "P2";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Si no hay mapa cargado, no pintamos nada
        if (walls == null || cols == 0 || rows == 0)
            return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 1. Fondo del nivel específico
        if (levelBackground != null) {
            g2.drawImage(levelBackground, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fallback si no hay fondo del nivel
            g2.setColor(new Color(200, 230, 255));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // --- DEFINIR ÁREA DE JUEGO CON MÁRGENES ---
        int hudHeight = 40; // Altura del HUD superior
        int topMargin = hudHeight + 10; // Margen superior mínimo
        int bottomMargin = 35; // Margen inferior reducido
        int leftMargin = 50; // Margen izquierdo (espacio para bordes)
        int rightMargin = 50; // Margen derecho (espacio para bordes)

        // Área disponible para el juego
        int gameAreaWidth = getWidth() - leftMargin - rightMargin;
        int gameAreaHeight = getHeight() - topMargin - bottomMargin;
        int gameAreaX = leftMargin;
        int gameAreaY = topMargin;

        // --- CÁLCULO DINÁMICO DE CELDAS ---
        int cellW = gameAreaWidth / cols;
        int cellH = gameAreaHeight / rows;

        // Limpiar caché si redimensionan
        if (Math.abs(getWidth() - lastWidth) > 50) {
            loader.clearCache();
            lastWidth = getWidth();
        }

        // 2. Muros y Bloques de Hielo con animación dominó
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                // Muros
                if (walls[y][x]) {
                    g2.setColor(new Color(0, 0, 0, 80));
                    g2.fillRect(gameAreaX + x * cellW, gameAreaY + y * cellH, cellW, cellH);
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.drawRect(gameAreaX + x * cellW, gameAreaY + y * cellH, cellW, cellH);
                }
            }
        }

        // 3. Entidades (La lista que nos pasó la GUI)
        for (EntityInfo info : entities) {
            int drawW = cellW;
            int drawH = cellH;
            int offsetXAdjust = 0;
            int offsetYAdjust = 0;

            if (info.type.equals("CHERRY")) {
                // Cerezas un poco más grandes pero dentro de su celda
                drawW = (int) (cellW * 1.3);
                drawH = (int) (cellH * 1.3);
                // Centrar dentro de la celda
                offsetXAdjust = (cellW - drawW) / 2;
                offsetYAdjust = (cellH - drawH) / 2;
            }

            Image img = loader.getImage(info.type, drawW, drawH);
            if (img != null) {
                g2.drawImage(img, gameAreaX + info.x * cellW + offsetXAdjust,
                        gameAreaY + info.y * cellH + offsetYAdjust, this);
            } else {
                drawFallback(g2, info, cellW, cellH, gameAreaX, gameAreaY);
            }
        }

        // 4. Dibujar Jugadores (separados de la lista de entidades)
        drawPlayer(g2, player1, cellW, cellH, gameAreaX, gameAreaY);
        drawPlayer(g2, player2, cellW, cellH, gameAreaX, gameAreaY);

        // 4. Líneas de Grid (matriz)
        g2.setColor(new Color(100, 100, 100, 100)); // Gris semi-transparente
        g2.setStroke(new BasicStroke(1));

        // Líneas verticales
        for (int x = 0; x <= cols; x++) {
            g2.drawLine(gameAreaX + x * cellW, gameAreaY, gameAreaX + x * cellW, gameAreaY + rows * cellH);
        }

        // Líneas horizontales
        for (int y = 0; y <= rows; y++) {
            g2.drawLine(gameAreaX, gameAreaY + y * cellH, gameAreaX + cols * cellW, gameAreaY + y * cellH);
        }

        // 5. HUD
        drawHUD(g2);
    }

    private void drawFallback(Graphics2D g, EntityInfo info, int w, int h, int offsetX, int offsetY) {
        if (info.type.contains("PLAYER"))
            g.setColor(Color.MAGENTA);
        else if (info.type.contains("ENEMY"))
            g.setColor(Color.RED);
        else if (info.type.equals("ICE"))
            g.setColor(Color.CYAN);
        else if (info.type.equals("BANANA"))
            g.setColor(Color.YELLOW);
        else
            g.setColor(Color.GREEN);
        g.fillOval(offsetX + info.x * w + 5, offsetY + info.y * h + 5, w - 10, h - 10);
    }

    /**
     * Dibuja un jugador en el tablero.
     * Metodo separado segun requisito del profesor.
     */
    private void drawPlayer(Graphics2D g, EntityInfo player, int cellW, int cellH, int gameAreaX, int gameAreaY) {
        if (player == null) {
            return;
        }
        Image img = loader.getImage(player.type, cellW, cellH);
        if (img != null) {
            g.drawImage(img, gameAreaX + player.x * cellW, gameAreaY + player.y * cellH, this);
        } else {
            drawFallback(g, player, cellW, cellH, gameAreaX, gameAreaY);
        }
    }

    private void drawHUD(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), 40);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 22));
        g.setColor(Color.GREEN);
        g.drawString("TIEMPO: " + (int) timeRemaining, 20, 28);

        g.setColor(Color.YELLOW);
        String s1 = player1Name + ": " + scoreP1;
        g.drawString(s1, getWidth() / 2 - 100, 28);

        g.setColor(Color.CYAN);
        String s2 = player2Name + ": " + scoreP2;
        g.drawString(s2, getWidth() / 2 + 50, 28);
    }
}