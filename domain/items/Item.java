package domain.items;

import domain.board.BoardController;

/**
 * Clase abstracta base para todos los items del juego.
 * Define la estructura comun para frutas, obstaculos y otros objetos coleccionables
 * o interactivos en el tablero.
 * 
 * <p>Tipos de items:</p>
 * <ul>
 *   <li>Frutas - Coleccionables que otorgan puntos</li>
 *   <li>Obstaculos - Bloquean el paso o causan efectos</li>
 * </ul>
 * 
 * <p>Caracteristicas polimorficas:</p>
 * <ul>
 *   <li>isWalkable() - Si el jugador puede pasar sobre el item</li>
 *   <li>isCollectable() - Si el item puede ser recolectado</li>
 *   <li>isDangerous() - Si el item causa dano al jugador</li>
 *   <li>isDestructible() - Si puede ser destruido</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Fruit
 * @see Obstacle
 * @see ItemController
 */
public abstract class Item implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    protected String id;
    protected int x;
    protected int y;

    public Item(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract boolean isWalkable();

    public abstract void update(float dt);

    // --- NUEVO: POLIMORFISMO PARA LA VISTA ---
    // Cada hijo debe decir su nombre ("BANANA", "ICE", etc.)
    public abstract String getType();

    // Por defecto nada es destructible, solo los Obstáculos sobreescribirán esto
    public boolean isDestructible() {
        return false;
    }

    /**
     * Verifica si el ítem es peligroso para el jugador.
     * Reemplaza el uso de instanceof Cactus.
     */
    public boolean isDangerous() {
        return false;
    }

    // --- NUEVOS MÉTODOS POLIMÓRFICOS ---

    public boolean isCollectable() {
        return false;
    }

    public boolean isFruit() {
        return false;
    }

    public int getScore() {
        return 0;
    }

    public void onPlayerMove(BoardController board, ItemController itemCtrl) {
        // Por defecto no hace nada
    }

    public void onIceBrokenAbove() {
        // Por defecto no hace nada
    }

    public boolean isHot() {
        return false;
    }

    /**
     * Verifica si este ítem puede aparecer en la posición actual.
     * Recibe la lista de otros ítems para consultar el contexto.
     */
    public boolean canSpawnAt(java.util.List<Item> items) {
        return true; // Por defecto siempre puede
    }

    /**
     * Se invoca cuando el ítem es destruido.
     * Recibe la lista de ítems para notificar efectos colaterales.
     */
    public void onDestroy(java.util.List<Item> items) {
        // Por defecto no hace nada
    }
}
