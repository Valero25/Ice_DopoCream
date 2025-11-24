package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una partida del juego
 * SRP: Coordina los managers especializados y las reglas generales del juego
 */
public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private int level;
    private boolean gameOver;
    private boolean levelCompleted;
    
    // Managers especializados
    private EnemyManager enemyManager;
    private FruitManager fruitManager;
    private GameTimer gameTimer;
    
    // Guardar configuración inicial para reinicio
    private String player1Character;
    private String player2Character;
    private boolean isPlayer2Machine;

    public Game(String player1Character, String player2Character, boolean isPlayer2Machine, int level) {
        this.player1Character = player1Character;
        this.player2Character = player2Character;
        this.isPlayer2Machine = isPlayer2Machine;
        
        this.board = new Board(7, 10);
        this.level = level;
        this.gameOver = false;
        this.levelCompleted = false;

        // Posiciones iniciales
        this.player1 = new Player(player1Character, new Position(6, 0), false);
        this.player2 = new Player(player2Character, new Position(6, 9), isPlayer2Machine);
        
        // Inicializar managers especializados
        this.enemyManager = new EnemyManager(board.getRows(), board.getCols());
        this.fruitManager = new FruitManager();
        this.gameTimer = new GameTimer();
        
        // Inicializar frutas
        fruitManager.initialize(getOccupiedPositions(), board.getRows(), board.getCols());
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getLevel() {
        return level;
    }

    public List<Enemy> getEnemies() {
        return enemyManager.getEnemies();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }
    
    public boolean isTimeExpired() {
        return gameTimer.isTimeExpired();
    }
    
    public int getRemainingTimeSeconds() {
        return gameTimer.getRemainingTimeSeconds();
    }

    public List<Fruit> getFruits() {
        return fruitManager.getFruits();
    }

    public int getGrapesCollected() {
        return fruitManager.getGrapesCollected();
    }

    public int getBananasCollected() {
        return fruitManager.getBananasCollected();
    }

    public boolean isGrapesPhaseComplete() {
        return fruitManager.isGrapesPhaseComplete();
    }

    /**
     * Mueve al jugador 1 en la dirección especificada
     * Verifica colisiones con enemigos y frutas después del movimiento
     * @param direction Dirección del movimiento
     * @return true si el movimiento fue exitoso
     */
    public boolean movePlayer1(Direction direction) {
        boolean moved = movePlayer(player1, direction);
        if (moved) {
            int points = fruitManager.checkCollection(player1.getPosition());
            if (points > 0) {
                player1.addScore(points);
                fruitManager.spawnBananasIfNeeded(getOccupiedPositions(), board.getRows(), board.getCols());
            }
            
            if (enemyManager.checkCollision(player1)) {
                gameOver = true;
            }
            
            checkLevelCompletion();
        }
        return moved;
    }

    /**
     * Mueve al jugador 2 en la dirección especificada
     * @param direction Dirección del movimiento
     * @return true si el movimiento fue exitoso
     */
    public boolean movePlayer2(Direction direction) {
        return movePlayer(player2, direction);
    }

    /**
     * Lógica de movimiento de un jugador
     * SRP: Solo maneja validación y actualización de posición
     */
    private boolean movePlayer(Player player, Direction direction) {
        if (direction == null) return false;

        Position currentPos = player.getPosition();
        Position newPos = new Position(
            currentPos.getRow() + direction.getRowDelta(),
            currentPos.getCol() + direction.getColDelta()
        );

        if (board.canMoveTo(newPos)) {
            player.setPosition(newPos);
            return true;
        }

        return false;
    }

    /**
     * Mueve todos los enemigos según sus patrones
     */
    public void moveEnemies() {
        if (gameOver) return;
        
        enemyManager.moveAll();
        
        if (enemyManager.checkCollision(player1)) {
            gameOver = true;
        }
    }

    /**
     * Verifica si el nivel está completo
     */
    private void checkLevelCompletion() {
        if (fruitManager.areAllFruitsCollected()) {
            levelCompleted = true;
            System.out.println("¡Nivel completado! Puntuación: " + player1.getScore());
        }
    }

    /**
     * Obtiene todas las posiciones ocupadas por jugadores y enemigos
     */
    private List<Position> getOccupiedPositions() {
        List<Position> occupied = new ArrayList<>();
        occupied.add(player1.getPosition());
        occupied.add(player2.getPosition());
        occupied.addAll(enemyManager.getOccupiedPositions());
        return occupied;
    }
    
    /**
     * Decrementa el tiempo restante en 1 segundo
     * Si el tiempo llega a 0, el nivel se considera fallido
     */
    public void decrementTime() {
        if (gameTimer.decrementTime()) {
            gameOver = true;
        }
    }
    
    /**
     * Reinicia el nivel desde el principio
     * Mantiene los mismos personajes y configuración
     */
    public void reset() {
        this.gameOver = false;
        this.levelCompleted = false;
        
        // Reiniciar posiciones de jugadores
        this.player1 = new Player(player1Character, new Position(6, 0), false);
        this.player2 = new Player(player2Character, new Position(6, 9), isPlayer2Machine);
        
        // Reiniciar managers
        enemyManager.reset();
        gameTimer.reset();
        fruitManager.initialize(getOccupiedPositions(), board.getRows(), board.getCols());
        
        System.out.println("Nivel reiniciado");
    }
}
