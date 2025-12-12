package domain.enemies;

import domain.shared.Direction;

/**
 * Clase abstracta base para todos los enemigos del juego.
 * Define la estructura comun y comportamiento basico de los enemigos,
 * incluyendo posicion, velocidad y sistema de temporizador de movimiento.
 * 
 * <p>Tipos de enemigos disponibles:</p>
 * <ul>
 *   <li>Troll - Patrulla en linea recta, gira al chocar</li>
 *   <li>FlowerPot (Maceta) - Persigue al jugador directamente</li>
 *   <li>Squid (Calamar) - Persigue y puede romper hielo</li>
 *   <li>Narwhal (Narval) - Embiste al alinearse con el jugador</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see EnemyController
 * @see Direction
 */
public abstract class Enemy implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    protected String id;
    protected int x;
    protected int y;
    protected float speed;
    protected float moveTimer;

    public Enemy(String id, int x, int y, float speed) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.moveTimer = 0;
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

    // Método abstracto: El "cerebro" del enemigo.
    // Recibe información del entorno y decide hacia dónde quiere ir.
    // blocked: ¿Me bloqueó un muro en el frame anterior?
    // targetX/Y: Dónde está el jugador (para los que persiguen)
    public abstract Direction decideMove(boolean blocked, int targetX, int targetY);

    public abstract String getType();

    /**
     * Retorna el tipo visual actual del enemigo.
     * Por defecto es igual a getType(), pero las subclases pueden cambiarlo
     * según su estado (ej. Narval embistiendo).
     */
    public String getVisualType() {
        return getType();
    }

    // --- CREADOR ESTÁTICO (Sin Factory externa) ---
    public static Enemy create(String type, String id, int x, int y) {
        if (type == null)
            return null;

        switch (type.toUpperCase()) {
            case "TROLL":
                return new Troll(id, x, y);
            case "FLOWERPOT": // La Maceta
                return new FlowerPot(id, x, y);
            case "SQUID":
                return new Squid(id, x, y);
            case "NARWHAL":
                return new Narwhal(id, x, y);
            default:
                return null;
        }
    }

    // Método helper para saber si este enemigo puede romper hielo (Default: No)
    public boolean canBreakIce() {
        return false;
    }

    // --- SISTEMA DE VELOCIDAD ---

    /**
     * Actualiza el timer del enemigo.
     * speed representa movimientos por segundo.
     * Cuando moveTimer >= 1.0, el enemigo puede moverse.
     */
    public void updateTimer(float dt) {
        moveTimer += speed * dt;
    }

    /**
     * Verifica si el enemigo puede moverse en este frame.
     */
    public boolean canMove() {
        return moveTimer >= 1.0f;
    }

    /**
     * Resetea el timer después de un movimiento.
     */
    public void resetTimer() {
        moveTimer -= 1.0f; // Restamos 1.0 en lugar de poner a 0 para mantener precisión
    }
}
