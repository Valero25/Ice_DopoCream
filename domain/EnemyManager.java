package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona los enemigos del juego (creación, movimiento, colisiones)
 * SRP: Solo se encarga de la lógica de enemigos
 */
public class EnemyManager {
    private List<Enemy> enemies;
    private List<Position> trollPattern;
    
    public EnemyManager(int rows, int cols) {
        this.enemies = new ArrayList<>();
        this.trollPattern = EnemyMovementPattern.getTrollPattern(rows, cols);
        initializeEnemies();
    }
    
    public List<Enemy> getEnemies() {
        return enemies;
    }
    
    /**
     * Inicializa los enemigos del nivel
     */
    private void initializeEnemies() {
        enemies.clear();
        
        // Crear dos Trolls con diferentes posiciones iniciales en el patrón
        Enemy troll1 = new Enemy("Troll", new Position(0, 0));
        troll1.setPathIndex(0);
        enemies.add(troll1);
        
        Enemy troll2 = new Enemy("Troll", trollPattern.get(trollPattern.size() / 2));
        troll2.setPathIndex(trollPattern.size() / 2);
        enemies.add(troll2);
    }
    
    /**
     * Mueve todos los enemigos según sus patrones
     */
    public void moveAll() {
        for (Enemy enemy : enemies) {
            if (enemy.getType().equals("Troll")) {
                moveTroll(enemy);
            }
        }
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
     * Verifica si algún enemigo colisiona con un jugador
     */
    public boolean checkCollision(Player player) {
        for (Enemy enemy : enemies) {
            if (enemy.collidesWith(player)) {
                System.out.println("¡Game Over! El jugador fue atrapado por un " + enemy.getType());
                return true;
            }
        }
        return false;
    }
    
    /**
     * Reinicia los enemigos a sus posiciones iniciales
     */
    public void reset() {
        initializeEnemies();
    }
    
    /**
     * Obtiene las posiciones ocupadas por los enemigos
     */
    public List<Position> getOccupiedPositions() {
        List<Position> positions = new ArrayList<>();
        for (Enemy enemy : enemies) {
            positions.add(enemy.getPosition());
        }
        // Agregar también las posiciones del patrón para evitar spawn en el borde
        positions.addAll(trollPattern);
        return positions;
    }
}
