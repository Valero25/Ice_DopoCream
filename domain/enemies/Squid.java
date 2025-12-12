package domain.enemies;

import domain.shared.Direction;

/**
 * Enemigo Calamar (Squid) con habilidad de romper hielo.
 * Similar a la Maceta, persigue al jugador directamente,
 * pero tiene la capacidad especial de destruir bloques de hielo.
 * 
 * <p>Caracteristicas:</p>
 * <ul>
 *   <li>Velocidad: ~3.3 movimientos por segundo</li>
 *   <li>Comportamiento: Persecucion directa</li>
 *   <li>Habilidad especial: Puede romper bloques de hielo</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Enemy
 */
public class Squid extends Enemy {

    public Squid(String id, int x, int y) {
        super(id, x, y, 3.3f); // ~3.3 movimientos por segundo (1 casilla cada 0.3s)
    }

    @Override
    public Direction decideMove(boolean blocked, int targetX, int targetY) {
        // Persigue igual que la maceta
        if (this.x < targetX)
            return Direction.RIGHT;
        if (this.x > targetX)
            return Direction.LEFT;

        if (this.y < targetY)
            return Direction.DOWN;
        if (this.y > targetY)
            return Direction.UP;

        return Direction.NONE;
    }

    @Override
    public boolean canBreakIce() {
        return true; // Habilidad especial
    }

    @Override
    public String getType() {
        return "SQUID";
    }
}