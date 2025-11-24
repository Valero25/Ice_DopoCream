package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Representa una partida del juego
 * SRP: Coordina el tablero, jugadores, enemigos, frutas y reglas del juego
 */
public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private int level;
    private List<Enemy> enemies;
    private List<Position> trollPattern;
    private List<Fruit> fruits;
    private boolean gameOver;
    private boolean levelCompleted;
    private boolean timeExpired;
    private int grapesCollected;
    private int bananasCollected;
    private boolean grapesPhaseComplete;
    
    private static final int TOTAL_GRAPES = 8;
    private static final int TOTAL_BANANAS = 8;
    private static final int TIME_LIMIT_SECONDS = 180; // 3 minutos
    
    private int remainingTimeSeconds;
    
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
        this.timeExpired = false;
        this.grapesCollected = 0;
        this.bananasCollected = 0;
        this.grapesPhaseComplete = false;
        this.remainingTimeSeconds = TIME_LIMIT_SECONDS;

        // Posiciones iniciales
        this.player1 = new Player(player1Character, new Position(6, 0), false);
        this.player2 = new Player(player2Character, new Position(6, 9), isPlayer2Machine);
        
        // Inicializar enemigos
        this.enemies = new ArrayList<>();
        this.trollPattern = EnemyMovementPattern.getTrollPattern(board.getRows(), board.getCols());
        
        // Crear dos Trolls con diferentes posiciones iniciales en el patrón
        Enemy troll1 = new Enemy("Troll", new Position(0, 0));
        troll1.setPathIndex(0);
        enemies.add(troll1);
        
        Enemy troll2 = new Enemy("Troll", trollPattern.get(trollPattern.size() / 2));
        troll2.setPathIndex(trollPattern.size() / 2);
        enemies.add(troll2);
        
        // Inicializar frutas (primero solo uvas)
        this.fruits = new ArrayList<>();
        spawnGrapes();
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
        return enemies;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }
    
    public boolean isTimeExpired() {
        return timeExpired;
    }
    
    public int getRemainingTimeSeconds() {
        return remainingTimeSeconds;
    }

    public List<Fruit> getFruits() {
        return fruits;
    }

    public int getGrapesCollected() {
        return grapesCollected;
    }

    public int getBananasCollected() {
        return bananasCollected;
    }

    public boolean isGrapesPhaseComplete() {
        return grapesPhaseComplete;
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
            checkFruitCollection();
            checkCollisions();
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
        
        for (Enemy enemy : enemies) {
            if (enemy.getType().equals("Troll")) {
                moveTroll(enemy);
            }
        }
        
        checkCollisions();
    }

    /**
     * Mueve un Troll siguiendo su patrón de borde
     */
    private void moveTroll(Enemy troll) {
        int nextIndex = (troll.getPathIndex() + 1) % trollPattern.size();
        troll.setPosition(trollPattern.get(nextIndex));
        troll.setPathIndex(nextIndex);
    }

    /**
     * Verifica colisiones entre jugadores y enemigos
     */
    private void checkCollisions() {
        for (Enemy enemy : enemies) {
            if (enemy.collidesWith(player1)) {
                gameOver = true;
                System.out.println("¡Game Over! El jugador 1 fue atrapado por un " + enemy.getType());
                return;
            }
        }
    }

    /**
     * Verifica si el jugador recolectó una fruta
     */
    private void checkFruitCollection() {
        Position playerPos = player1.getPosition();
        
        for (Fruit fruit : fruits) {
            if (fruit.isAt(playerPos)) {
                fruit.collect();
                
                if (fruit.getType().equals("Uva")) {
                    grapesCollected++;
                    player1.addScore(10);
                    System.out.println("¡Uva recolectada! (" + grapesCollected + "/" + TOTAL_GRAPES + ")");
                    
                    if (grapesCollected == TOTAL_GRAPES && !grapesPhaseComplete) {
                        grapesPhaseComplete = true;
                        System.out.println("¡Todas las uvas recolectadas! Ahora aparecen los plátanos");
                        spawnBananas();
                    }
                } else if (fruit.getType().equals("Platano")) {
                    bananasCollected++;
                    player1.addScore(15);
                    System.out.println("¡Plátano recolectado! (" + bananasCollected + "/" + TOTAL_BANANAS + ")");
                }
                break;
            }
        }
    }

    /**
     * Verifica si el nivel está completo
     */
    private void checkLevelCompletion() {
        if (grapesCollected == TOTAL_GRAPES && bananasCollected == TOTAL_BANANAS) {
            levelCompleted = true;
            System.out.println("¡Nivel completado! Puntuación: " + player1.getScore());
        }
    }

    /**
     * Genera 8 uvas en posiciones aleatorias
     */
    private void spawnGrapes() {
        Random random = new Random();
        List<Position> occupiedPositions = getOccupiedPositions();
        
        for (int i = 0; i < TOTAL_GRAPES; i++) {
            Position pos = getRandomFreePosition(random, occupiedPositions);
            fruits.add(new Fruit("Uva", pos));
            occupiedPositions.add(pos);
        }
    }

    /**
     * Genera 8 plátanos en posiciones aleatorias
     */
    private void spawnBananas() {
        Random random = new Random();
        List<Position> occupiedPositions = getOccupiedPositions();
        
        for (int i = 0; i < TOTAL_BANANAS; i++) {
            Position pos = getRandomFreePosition(random, occupiedPositions);
            fruits.add(new Fruit("Platano", pos));
            occupiedPositions.add(pos);
        }
    }

    /**
     * Obtiene todas las posiciones ocupadas por jugadores y enemigos
     */
    private List<Position> getOccupiedPositions() {
        List<Position> occupied = new ArrayList<>();
        occupied.add(player1.getPosition());
        occupied.add(player2.getPosition());
        
        for (Enemy enemy : enemies) {
            occupied.add(enemy.getPosition());
        }
        
        // Agregar posiciones del patrón de troll para evitar spawn en el borde
        occupied.addAll(trollPattern);
        
        return occupied;
    }

    /**
     * Genera una posición aleatoria que no esté ocupada
     */
    private Position getRandomFreePosition(Random random, List<Position> occupiedPositions) {
        Position pos;
        do {
            int row = random.nextInt(board.getRows());
            int col = random.nextInt(board.getCols());
            pos = new Position(row, col);
        } while (isPositionOccupied(pos, occupiedPositions));
        
        return pos;
    }

    /**
     * Verifica si una posición está ocupada
     */
    private boolean isPositionOccupied(Position pos, List<Position> occupiedPositions) {
        for (Position occupied : occupiedPositions) {
            if (occupied.equals(pos)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Decrementa el tiempo restante en 1 segundo
     * Si el tiempo llega a 0, el nivel se considera fallido
     */
    public void decrementTime() {
        if (remainingTimeSeconds > 0) {
            remainingTimeSeconds--;
            
            if (remainingTimeSeconds == 0) {
                timeExpired = true;
                gameOver = true;
                System.out.println("¡Tiempo agotado! Nivel fallido");
            }
        }
    }
    
    /**
     * Reinicia el nivel desde el principio
     * Mantiene los mismos personajes y configuración
     */
    public void reset() {
        this.gameOver = false;
        this.levelCompleted = false;
        this.timeExpired = false;
        this.grapesCollected = 0;
        this.bananasCollected = 0;
        this.grapesPhaseComplete = false;
        this.remainingTimeSeconds = TIME_LIMIT_SECONDS;
        
        // Reiniciar posiciones de jugadores
        this.player1 = new Player(player1Character, new Position(6, 0), false);
        this.player2 = new Player(player2Character, new Position(6, 9), isPlayer2Machine);
        
        // Reiniciar enemigos
        this.enemies = new ArrayList<>();
        
        Enemy troll1 = new Enemy("Troll", new Position(0, 0));
        troll1.setPathIndex(0);
        enemies.add(troll1);
        
        Enemy troll2 = new Enemy("Troll", trollPattern.get(trollPattern.size() / 2));
        troll2.setPathIndex(trollPattern.size() / 2);
        enemies.add(troll2);
        
        // Reiniciar frutas
        this.fruits = new ArrayList<>();
        spawnGrapes();
        
        System.out.println("Nivel reiniciado");
    }
}
