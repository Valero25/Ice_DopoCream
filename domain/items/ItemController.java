package domain.items;

import domain.board.BoardController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import domain.shared.EntityInfo;
import java.util.ArrayList;
import java.util.List;

public class ItemController implements java.io.Serializable {

    private BoardController boardCtrl;
    private List<Item> items;
    private Random random;

    private List<DominoAction> dominoQueue;
    private float dominoTimer;
    private static final float DOMINO_DELAY = 0.08f;

    public ItemController(BoardController boardCtrl) {
        this.boardCtrl = boardCtrl;
        this.items = new ArrayList<>();
        this.random = new Random();
        this.dominoQueue = new ArrayList<>();
        this.dominoTimer = 0;
    }

    public List<Item> getItems() {
        return items;
    }

    public void spawnFruit(String type, String id, int x, int y) {
        if (boardCtrl.isWalkable(x, y)) {
            Fruit f = Fruit.create(type, id, x, y);
            if (f != null) {
                items.add(f);
            }
        }
    }

    public void spawnObstacle(String type, String id, int x, int y) {
        if (boardCtrl.isValidPosition(x, y)) {
            Obstacle o = Obstacle.create(type, id, x, y);
            if (o != null && o.canSpawnAt(items)) {
                items.add(o);
            }
        }
    }

    /**
     * Encola una serie de operaciones de creación de hielo con efecto dominó.
     */
    public void queueIceCreation(List<int[]> positions) {
        for (int[] pos : positions) {
            dominoQueue.add(new DominoAction(true, pos[0], pos[1]));
        }
    }

    /**
     * Encola una serie de operaciones de destrucción de hielo con efecto dominó.
     */
    public void queueIceDestruction(List<int[]> positions) {
        for (int[] pos : positions) {
            dominoQueue.add(new DominoAction(false, pos[0], pos[1]));
        }
    }

    public void updateItems(float dt) {
        for (Item item : items) {
            item.update(dt);
        }

        if (!dominoQueue.isEmpty()) {
            dominoTimer += dt;
            if (dominoTimer >= DOMINO_DELAY) {
                dominoTimer = 0;
                DominoAction action = dominoQueue.remove(0);
                if (action.isCreate) {
                    spawnObstacle("ICE_BLOCK", "ice_" + System.nanoTime(), action.x, action.y);
                } else {
                    breakIceBlockImmediate(action.x, action.y);
                }
            }
        }
    }

    // Método para recolección simple
    public void collectItemsAt(int x, int y) {
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (item.getX() == x && item.getY() == y) {
                if (item.isCollectable()) {
                    // Lógica de sumar puntos iría aquí o retornando el valor
                    it.remove();
                }
            }
        }
    }

    // Método para consultar obstáculos desde fuera (usado por Enemies)
    public boolean isObstacleAt(int x, int y) {
        for (Item item : items) {
            if (item.getX() == x && item.getY() == y) {
                if (!item.isWalkable()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verifica si hay un bloque de hielo destructible en la posicion.
     * Usado para calcular la hilera antes de encolar.
     */
    public boolean hasDestructibleAt(int x, int y) {
        for (Item item : items) {
            if (item.getX() == x && item.getY() == y) {
                if (item.isDestructible()) {
                    return true;
                }
                if (!item.isWalkable()) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Rompe un bloque de hielo inmediatamente (usado por la cola dominó).
     */
    private void breakIceBlockImmediate(int x, int y) {
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (item.getX() == x && item.getY() == y) {
                if (item.isDestructible()) {
                    it.remove();
                    item.onDestroy(items);
                    return;
                }
            }
        }
    }

    /**
     * Intenta romper un bloque de hielo. Retorna true si habia hielo.
     * Retorna false si hay hueco o muro (detiene domino).
     */
    public boolean breakIceBlock(int x, int y) {
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item item = it.next();

            if (item.getX() == x && item.getY() == y) {
                if (item.isDestructible()) {
                    it.remove();
                    item.onDestroy(items);
                    return true;
                }

                if (!item.isWalkable()) {
                    return false;
                }
            }
        }
        return false;
    }

    // Método para condición de victoria
    public int getFruitCount() {
        int count = 0;
        for (Item item : items) {
            if (item.isFruit()) {
                count++;
            }
        }
        return count;
    }

    public java.util.Map<String, Integer> getRemainingFruitsByType() {
        java.util.Map<String, Integer> counts = new java.util.HashMap<>();
        for (Item item : items) {
            if (item.isFruit()) {
                counts.put(item.getType(), counts.getOrDefault(item.getType(), 0) + 1);
            }
        }
        return counts;
    }

    // Método para colisión con retorno de puntos (Si no lo tenías actualizado)
    public int collectItemAt(int x, int y) {
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (item.getX() == x && item.getY() == y) {
                if (item.isCollectable()) {
                    int p = item.getScore();
                    it.remove();
                    return p;
                }

            }
        }
        return 0;
    }

    /**
     * Verifica si hay un cactus con púas (peligroso) en la posición especificada.
     * 
     * @param x Coordenada X
     * @param y Coordenada Y
     * @return true si hay un cactus con púas que puede dañar al jugador
     */
    public boolean hasDangerousCactusAt(int x, int y) {
        for (Item item : items) {
            if (item.getX() == x && item.getY() == y) {
                if (item.isDangerous()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Se llama cada vez que el jugador realiza un movimiento exitoso.
     * Mueve todas las Piñas a una posición adyacente válida aleatoria.
     */
    public void onPlayerMove() {
        // Necesitamos importar java.util.Random si no está
        if (this.random == null)
            this.random = new Random();

        // Recorremos todos los items. Cada ítem decide si se mueve o hace algo.
        for (Item item : items) {
            item.onPlayerMove(boardCtrl, this);
        }
    }

    /**
     * Convierte los Items del dominio a DTOs simples para la vista.
     */
    public List<EntityInfo> getItemInfo() {
        List<EntityInfo> info = new ArrayList<>();
        for (Item i : items) {
            info.add(new EntityInfo(i.getId(), i.getX(), i.getY(), i.getType(), i.isDestructible()));
        }
        return info;
    }

    /**
     * Elimina todos los items (Frutas y Obstáculos) del mapa.
     */
    public void reset() {
        items.clear();
    }

}