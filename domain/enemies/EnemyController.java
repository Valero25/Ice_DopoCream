package domain.enemies;

import domain.board.BoardController;
import domain.items.ItemController;
import domain.shared.Direction;
import domain.shared.EntityInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class EnemyController implements java.io.Serializable {

    private BoardController boardCtrl;
    private ItemController itemCtrl; // Necesario para gestionar rotura de hielo
    private List<Enemy> enemies;

    // Guardamos las posiciones de ambos jugadores
    private int player1X = -1;
    private int player1Y = -1;
    private int player2X = -1;
    private int player2Y = -1;
    private boolean player1Alive = false;
    private boolean player2Alive = false;

    public EnemyController(BoardController boardCtrl, ItemController itemCtrl) {
        this.boardCtrl = boardCtrl;
        this.itemCtrl = itemCtrl;
        this.enemies = new ArrayList<>();
    }

    /**
     * Crea un enemigo usando el método estático de la clase padre Enemy.
     */
    public void spawnEnemy(String type, String id, int x, int y) {
        // Validación básica para no crear enemigos fuera del mapa
        if (boardCtrl.isValidPosition(x, y)) {
            Enemy e = Enemy.create(type, id, x, y);
            if (e != null) {
                enemies.add(e);
            }
        }
    }

    /**
     * Actualiza la posición del jugador 1.
     */
    public void updatePlayerPos(int x, int y) {
        this.player1X = x;
        this.player1Y = y;
        this.player1Alive = true;
    }

    /**
     * Actualiza las posiciones de ambos jugadores.
     */
    public void updatePlayerPositions(int p1x, int p1y, boolean p1Alive, int p2x, int p2y, boolean p2Alive) {
        this.player1X = p1x;
        this.player1Y = p1y;
        this.player1Alive = p1Alive;
        this.player2X = p2x;
        this.player2Y = p2y;
        this.player2Alive = p2Alive;
    }

    /**
     * Obtiene la posición del jugador más cercano a la posición dada.
     */
    private int[] getClosestPlayerPos(int fromX, int fromY) {
        int targetX = -1;
        int targetY = -1;
        double minDist = Double.MAX_VALUE;

        if (player1Alive && player1X >= 0) {
            double dist1 = Math.abs(player1X - fromX) + Math.abs(player1Y - fromY);
            if (dist1 < minDist) {
                minDist = dist1;
                targetX = player1X;
                targetY = player1Y;
            }
        }

        if (player2Alive && player2X >= 0) {
            double dist2 = Math.abs(player2X - fromX) + Math.abs(player2Y - fromY);
            if (dist2 < minDist) {
                targetX = player2X;
                targetY = player2Y;
            }
        }

        return new int[] { targetX, targetY };
    }

    /**
     * Ciclo principal de actualización de la IA.
     */
    public void updateEnemies(float dt) {
        for (Enemy e : enemies) {
            e.updateTimer(dt);

            if (!e.canMove())
                continue;

            // Obtener posición del jugador más cercano a este enemigo
            int[] targetPos = getClosestPlayerPos(e.getX(), e.getY());
            int targetX = targetPos[0];
            int targetY = targetPos[1];

            // Si no hay jugadores válidos, el enemigo no hace nada
            if (targetX < 0 || targetY < 0) {
                e.resetTimer();
                continue;
            }

            // 1. El enemigo decide a dónde QUIERE ir (polimorfismo)
            Direction intent = e.decideMove(false, targetX, targetY);

            if (intent == Direction.NONE) {
                e.resetTimer();
                continue;
            }

            int nextX = e.getX() + intent.getDx();
            int nextY = e.getY() + intent.getDy();

            // 2. El Controller valida el mundo
            boolean blocked = !boardCtrl.isWalkable(nextX, nextY);
            boolean obstacle = itemCtrl.isObstacleAt(nextX, nextY);

            // 3. Si hay obstáculo, pregunta al enemigo si puede romperlo
            if (obstacle && e.canBreakIce()) {
                if (itemCtrl.breakIceBlock(nextX, nextY)) {
                    e.resetTimer();
                    continue; // Consumió turno rompiendo
                }
            }

            // 4. Ejecutar movimiento o notificar bloqueo
            if (!blocked && !obstacle) {
                e.setPosition(nextX, nextY);
            } else {
                e.decideMove(true, targetX, targetY); // Notifica bloqueo al enemigo
            }
            e.resetTimer();
        }
    }

    /**
     * Verifica si algún enemigo ha tocado al jugador en la posición dada.
     * Retorna true si hay colisión (Game Over).
     */
    public boolean checkCollision(int pX, int pY) {
        for (Enemy e : enemies) {
            if (e.getX() == pX && e.getY() == pY) {
                return true;
            }
        }
        return false;
    }

    // Método auxiliar para obtener la lista (útil para la vista/GUI)
    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<EntityInfo> getEnemyInfo() {
        List<EntityInfo> infoList = new ArrayList<>();

        for (Enemy e : enemies) {
            // SIN INSTANCEOF - Usamos polimorfismo
            String type = e.getVisualType();

            infoList.add(new EntityInfo(e.getId(), e.getX(), e.getY(), type, false));
        }
        return infoList;
    }

    /**
     * Elimina todos los enemigos activos.
     */
    public void reset() {
        enemies.clear();
        // Resetear coordenadas de ambos jugadores
        this.player1X = -1;
        this.player1Y = -1;
        this.player2X = -1;
        this.player2Y = -1;
        this.player1Alive = false;
        this.player2Alive = false;
    }
}