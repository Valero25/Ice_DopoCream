package presentation;

import domain.Game;
import domain.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameScreen extends JFrame {

    private Game game;
    private JPanel background;
    private JLabel player1Sprite;
    private JLabel player2Sprite;
    private java.util.List<JLabel> enemySprites;
    private java.util.Map<domain.Fruit, JLabel> fruitSprites;
    private Timer enemyTimer;
    private Timer gameTimer;
    private JLabel timerLabel;
    private Runnable onBackToLevels;
    
    // Tamaño de cada celda en la cuadrícula del tablero interior
    private final int CELL_WIDTH = 100;
    private final int CELL_HEIGHT = 80;
    
    // Offset inicial del tablero interior (esquina superior izquierda de la primera celda jugable)
    private final int OFFSET_X = 100;
    private final int OFFSET_Y = 40;
    
    // Tamaño del sprite
    private final int SPRITE_SIZE = 60;

    public GameScreen(Game game, Runnable onBackToLevels) {
        this.game = game;
        this.onBackToLevels = onBackToLevels;

        setTitle("DopoCream - Nivel " + game.getLevel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        // Obtener dimensiones de la primera ventana
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = new Dimension(screen.width * 3 / 4, screen.height * 3 / 4);
        setSize(windowSize);
        
        // Panel para dibujar la cuadrícula de la matriz
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Dibujar la cuadrícula de la matriz
                g.setColor(Color.BLACK);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(2));
                
                // Dibujar líneas verticales
                for (int col = 0; col <= game.getBoard().getCols(); col++) {
                    int x = OFFSET_X + (col * CELL_WIDTH);
                    g2d.drawLine(x, OFFSET_Y, x, OFFSET_Y + (game.getBoard().getRows() * CELL_HEIGHT));
                }
                
                // Dibujar líneas horizontales
                for (int row = 0; row <= game.getBoard().getRows(); row++) {
                    int y = OFFSET_Y + (row * CELL_HEIGHT);
                    g2d.drawLine(OFFSET_X, y, OFFSET_X + (game.getBoard().getCols() * CELL_WIDTH), y);
                }
                
                // Dibujar números de coordenadas
                g.setColor(Color.BLUE);
                g.setFont(new Font("Arial", Font.BOLD, 12));
                for (int row = 0; row < game.getBoard().getRows(); row++) {
                    for (int col = 0; col < game.getBoard().getCols(); col++) {
                        int x = OFFSET_X + (col * CELL_WIDTH) + 5;
                        int y = OFFSET_Y + (row * CELL_HEIGHT) + 15;
                        g.drawString(row + "," + col, x, y);
                    }
                }
            }
        };
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(0, 0, windowSize.width, windowSize.height);
        setContentPane(panel);
        
        background = panel;

        // Crear sprites de jugadores
        createPlayerSprites();
        
        // Crear sprites de enemigos
        createEnemySprites();
        
        // Crear sprites de frutas
        createFruitSprites();
        
        // Crear temporizador visual
        createTimerLabel();
        
        // Iniciar temporizador para mover enemigos
        startEnemyMovement();
        
        // Iniciar temporizador del juego
        startGameTimer();

        // Agregar listener de teclado
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        // Información del juego
        System.out.println("=== NIVEL " + game.getLevel() + " ===");
        System.out.println("Jugador 1: " + game.getPlayer1().getCharacter());
        System.out.println("Jugador 2: " + game.getPlayer2().getCharacter());
        System.out.println("Enemigos: " + game.getEnemies().size() + " Trolls");
        System.out.println("Controles: W=Arriba, A=Izquierda, S=Abajo, D=Derecha");

        setVisible(true);
    }

    private void createTimerLabel() {
        timerLabel = new JLabel();
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        timerLabel.setForeground(Color.RED);
        timerLabel.setBounds(10, 10, 200, 30);
        updateTimerLabel();
        background.add(timerLabel);
    }
    
    private void updateTimerLabel() {
        int seconds = game.getRemainingTimeSeconds();
        int minutes = seconds / 60;
        int secs = seconds % 60;
        timerLabel.setText(String.format("Tiempo: %02d:%02d", minutes, secs));
        
        // Cambiar color si queda poco tiempo
        if (seconds <= 30) {
            timerLabel.setForeground(Color.RED);
        } else if (seconds <= 60) {
            timerLabel.setForeground(Color.ORANGE);
        } else {
            timerLabel.setForeground(new Color(0, 150, 0));
        }
    }
    
    private void startGameTimer() {
        gameTimer = new Timer(1000, e -> {
            if (game.isGameOver() || game.isLevelCompleted()) {
                gameTimer.stop();
                return;
            }
            
            game.decrementTime();
            updateTimerLabel();
            
            if (game.isTimeExpired()) {
                gameTimer.stop();
                enemyTimer.stop();
                showTimeExpired();
            }
        });
        gameTimer.start();
    }
    
    private void createPlayerSprites() {
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        // Cargar gif del jugador 1
        GifLoader loader1 = new GifLoader(player1.getCharacter());
        player1Sprite = new JLabel(loader1.getCaminandoAbajo(SPRITE_SIZE, SPRITE_SIZE));
        background.add(player1Sprite);
        updatePlayerPosition(player1, player1Sprite);

        // Cargar gif del jugador 2
        GifLoader loader2 = new GifLoader(player2.getCharacter());
        player2Sprite = new JLabel(loader2.getCaminandoAbajo(SPRITE_SIZE, SPRITE_SIZE));
        background.add(player2Sprite);
        updatePlayerPosition(player2, player2Sprite);
    }

    /**
     * Maneja las teclas presionadas y convierte a direcciones del dominio
     * SRP: Solo se encarga de capturar input y delegar al dominio
     */
    private void handleKeyPress(KeyEvent e) {
        if (game.isGameOver() || game.isLevelCompleted()) return;
        
        char key = Character.toLowerCase(e.getKeyChar());
        domain.Direction direction = domain.Direction.fromString(String.valueOf(key));
        
        if (direction != null) {
            boolean moved = game.movePlayer1(direction);
            if (moved) {
                updatePlayerPosition(game.getPlayer1(), player1Sprite);
                updateFruitSprites(); // Actualizar frutas después del movimiento
                background.repaint();
            }
        }
    }

    private void updatePlayerPosition(Player player, JLabel sprite) {
        // Calcular la posición centrada en la celda
        int x = OFFSET_X + (player.getPosition().getCol() * CELL_WIDTH) + (CELL_WIDTH - SPRITE_SIZE) / 2;
        int y = OFFSET_Y + (player.getPosition().getRow() * CELL_HEIGHT) + (CELL_HEIGHT - SPRITE_SIZE) / 2;
        sprite.setBounds(x, y, SPRITE_SIZE, SPRITE_SIZE);
    }
    
    private void createEnemySprites() {
        enemySprites = new java.util.ArrayList<>();
        
        for (domain.Enemy enemy : game.getEnemies()) {
            GifLoader loader = new GifLoader(enemy.getType());
            JLabel enemySprite = new JLabel(loader.getGif("Caminar Abajo animation.gif", SPRITE_SIZE, SPRITE_SIZE));
            background.add(enemySprite);
            updateEnemyPosition(enemy, enemySprite);
            enemySprites.add(enemySprite);
        }
    }
    
    private void updateEnemyPosition(domain.Enemy enemy, JLabel sprite) {
        int x = OFFSET_X + (enemy.getPosition().getCol() * CELL_WIDTH) + (CELL_WIDTH - SPRITE_SIZE) / 2;
        int y = OFFSET_Y + (enemy.getPosition().getRow() * CELL_HEIGHT) + (CELL_HEIGHT - SPRITE_SIZE) / 2;
        sprite.setBounds(x, y, SPRITE_SIZE, SPRITE_SIZE);
    }
    
    private void createFruitSprites() {
        fruitSprites = new java.util.HashMap<>();
        updateFruitSprites();
    }
    
    private void updateFruitSprites() {
        // Remover sprites de frutas recolectadas
        java.util.Iterator<java.util.Map.Entry<domain.Fruit, JLabel>> iterator = fruitSprites.entrySet().iterator();
        while (iterator.hasNext()) {
            java.util.Map.Entry<domain.Fruit, JLabel> entry = iterator.next();
            if (entry.getKey().isCollected()) {
                background.remove(entry.getValue());
                iterator.remove();
            }
        }
        
        // Agregar sprites de nuevas frutas
        for (domain.Fruit fruit : game.getFruits()) {
            if (!fruit.isCollected() && !fruitSprites.containsKey(fruit)) {
                String imageName = fruit.getType().equals("Uva") ? "uva.png" : "platanos.png";
                ImageIcon icon = new ImageIcon(getClass().getResource("frutas/" + imageName));
                Image scaledImage = icon.getImage().getScaledInstance(SPRITE_SIZE - 20, SPRITE_SIZE - 20, Image.SCALE_SMOOTH);
                
                JLabel fruitSprite = new JLabel(new ImageIcon(scaledImage));
                int x = OFFSET_X + (fruit.getPosition().getCol() * CELL_WIDTH) + (CELL_WIDTH - (SPRITE_SIZE - 20)) / 2;
                int y = OFFSET_Y + (fruit.getPosition().getRow() * CELL_HEIGHT) + (CELL_HEIGHT - (SPRITE_SIZE - 20)) / 2;
                fruitSprite.setBounds(x, y, SPRITE_SIZE - 20, SPRITE_SIZE - 20);
                
                background.add(fruitSprite);
                fruitSprites.put(fruit, fruitSprite);
            }
        }
    }
    
    private void startEnemyMovement() {
        enemyTimer = new Timer(500, e -> {
            if (game.isGameOver()) {
                enemyTimer.stop();
                restartLevel();
                return;
            }
            
            if (game.isLevelCompleted()) {
                enemyTimer.stop();
                showLevelCompleted();
                return;
            }
            
            game.moveEnemies();
            
            // Actualizar posiciones de los sprites de enemigos
            java.util.List<domain.Enemy> enemies = game.getEnemies();
            for (int i = 0; i < enemies.size(); i++) {
                updateEnemyPosition(enemies.get(i), enemySprites.get(i));
            }
            
            // Actualizar sprites de frutas (remover recolectadas, agregar nuevas)
            updateFruitSprites();
            
            background.repaint();
        });
        enemyTimer.start();
    }
    
    private void showEnemyCaught() {
        String message = "¡Fuiste Atrapado!\n" +
                        "Un enemigo te ha atrapado.\n\n" +
                        "Uvas: " + game.getGrapesCollected() + "/8\n" +
                        "Plátanos: " + game.getBananasCollected() + "/8\n" +
                        "Puntuación: " + game.getPlayer1().getScore();
        
        String[] options = {"Reintentar Nivel", "Volver a Niveles", "Salir"};
        int choice = JOptionPane.showOptionDialog(this,
            message,
            "Game Over",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[0]);
        
        if (choice == 0) {
            // Reintentar nivel
            doRestartLevel();
        } else if (choice == 1) {
            // Volver a selección de niveles
            dispose();
            if (onBackToLevels != null) {
                onBackToLevels.run();
            }
        } else {
            // Salir
            dispose();
        }
    }
    
    private void restartLevel() {
        showEnemyCaught();
    }
    
    private void doRestartLevel() {
        System.out.println("Reiniciando nivel...");
        
        // Detener timers
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        
        // Limpiar sprites existentes
        background.remove(player1Sprite);
        background.remove(player2Sprite);
        for (JLabel sprite : enemySprites) {
            background.remove(sprite);
        }
        for (JLabel sprite : fruitSprites.values()) {
            background.remove(sprite);
        }
        
        // Reiniciar el juego
        game.reset();
        
        // Recrear sprites
        enemySprites.clear();
        fruitSprites.clear();
        
        createPlayerSprites();
        createEnemySprites();
        createFruitSprites();
        
        // Actualizar temporizador
        updateTimerLabel();
        
        background.repaint();
        
        // Reiniciar los timers
        startEnemyMovement();
        startGameTimer();
    }
    
    private void showTimeExpired() {
        String message = "¡Tiempo Agotado!\n" +
                        "No completaste el nivel a tiempo.\n\n" +
                        "Uvas: " + game.getGrapesCollected() + "/8\n" +
                        "Plátanos: " + game.getBananasCollected() + "/8\n" +
                        "Puntuación: " + game.getPlayer1().getScore();
        
        String[] options = {"Reintentar Nivel", "Volver a Niveles", "Salir"};
        int choice = JOptionPane.showOptionDialog(this,
            message,
            "Nivel Fallido",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            options,
            options[0]);
        
        if (choice == 0) {
            // Reintentar nivel
            doRestartLevel();
        } else if (choice == 1) {
            // Volver a selección de niveles
            dispose();
            if (onBackToLevels != null) {
                onBackToLevels.run();
            }
        } else {
            // Salir
            dispose();
        }
    }
    
    private void showLevelCompleted() {
        // Detener el temporizador
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        
        int timeUsed = 180 - game.getRemainingTimeSeconds();
        int minutes = timeUsed / 60;
        int seconds = timeUsed % 60;
        
        JOptionPane.showMessageDialog(this, 
            "¡Nivel Completado!\n" +
            "Uvas: " + game.getGrapesCollected() + "/8\n" +
            "Plátanos: " + game.getBananasCollected() + "/8\n" +
            "Tiempo: " + String.format("%02d:%02d", minutes, seconds) + "\n" +
            "Puntuación: " + game.getPlayer1().getScore(), 
            "¡Victoria!", 
            JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
