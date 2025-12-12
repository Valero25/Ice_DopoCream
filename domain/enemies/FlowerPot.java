package domain.enemies;

import domain.shared.Direction;

/**
 * Enemigo Maceta (FlowerPot) que persigue directamente al jugador.
 * Utiliza un algoritmo simple de persecucion: primero se mueve en el eje X
 * hacia el jugador, luego en el eje Y.
 * 
 * <p>Caracteristicas:</p>
 * <ul>
 *   <li>Velocidad: ~3.3 movimientos por segundo</li>
 *   <li>Comportamiento: Persecucion directa</li>
 *   <li>No puede romper bloques de hielo</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Enemy
 */
public class FlowerPot extends Enemy {

    public FlowerPot(String id, int x, int y) {
        super(id, x, y, 3.3f); // ~3.3 movimientos por segundo (1 casilla cada 0.3s)
    }

    @Override
    public Direction decideMove(boolean blocked, int targetX, int targetY) {
        // Lógica de persecución muy simple (Eje X primero, luego Eje Y)

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
    public String getType() {
        return "FLOWERPOT";
    }
}