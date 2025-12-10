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

    // Guardamos la posición del jugador para que los enemigos perseguidores
    // (Maceta/Calamar) sepan a dónde ir
    private int playerX;
    private int playerY;

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
     * Actualiza la referencia de dónde está el jugador.
     * Debe llamarse en cada ciclo del juego antes de updateEnemies.
     */
    public void updatePlayerPos(int x, int y) {
        this.playerX = x;
        this.playerY = y;
    }

    /**
     * Ciclo principal de actualización de la IA.
     */
    public void updateEnemies(float dt) {
        for (Enemy e : enemies) {
            e.updateTimer(dt);

            if (!e.canMove())
                continue;

            // 1. El enemigo decide a dónde QUIERE ir (polimorfismo)
            Direction intent = e.decideMove(false, playerX, playerY);

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
                e.decideMove(true, playerX, playerY); // Notifica bloqueo al enemigo
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
        // Opcional: Resetear coordenadas del target para evitar comportamientos raros
        // en el primer frame
        this.playerX = -1;
        this.playerY = -1;
    }
}