package domain.enemies;

import domain.shared.Direction;

/**
 * Enemigo Narval (Narwhal) con capacidad de embestida.
 * Cuando se alinea horizontal o verticalmente con el jugador,
 * entra en modo embestida con alta velocidad. Puede romper hielo mientras embiste.
 * 
 * <p>Caracteristicas:</p>
 * <ul>
 *   <li>Velocidad normal: ~3.3 movimientos por segundo</li>
 *   <li>Velocidad embestida: ~16 movimientos por segundo</li>
 *   <li>Puede romper bloques de hielo solo durante la embestida</li>
 *   <li>Al chocar con un muro, termina la embestida e invierte direccion</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Enemy
 */
public class Narwhal extends Enemy {

    private Direction currentDir;
    private boolean isDashing; // Indica si está en modo embestida

    public Narwhal(String id, int x, int y) {
        super(id, x, y, 3.3f); // Velocidad normal: ~3.3 movimientos por segundo (1 casilla cada 0.3s)
        this.currentDir = Direction.LEFT;
        this.isDashing = false;
    }

    @Override
    public Direction decideMove(boolean blocked, int targetX, int targetY) {
        // Comprobar si está alineado con el jugador (misma fila o columna)
        boolean alignedHorizontally = (this.y == targetY);
        boolean alignedVertically = (this.x == targetX);

        if (alignedHorizontally && !isDashing) {
            // Alineado horizontalmente: embiste hacia el jugador
            isDashing = true;
            speed = 16.0f; // Velocidad de embestida: ~16 movimientos por segundo (muy rápido)
            currentDir = (this.x < targetX) ? Direction.RIGHT : Direction.LEFT;
            return currentDir;
        } else if (alignedVertically && !isDashing) {
            // Alineado verticalmente: embiste hacia el jugador
            isDashing = true;
            speed = 16.0f; // Velocidad de embestida: ~16 movimientos por segundo (muy rápido)
            currentDir = (this.y < targetY) ? Direction.DOWN : Direction.UP;
            return currentDir;
        }

        // Si está bloqueado (chocó con muro), termina la embestida
        if (blocked) {
            isDashing = false;
            speed = 3.3f; // Vuelve a velocidad normal
            // Rebote simple: invierte dirección
            if (currentDir == Direction.LEFT)
                currentDir = Direction.RIGHT;
            else if (currentDir == Direction.RIGHT)
                currentDir = Direction.LEFT;
            else if (currentDir == Direction.UP)
                currentDir = Direction.DOWN;
            else if (currentDir == Direction.DOWN)
                currentDir = Direction.UP;
        }

        return currentDir;
    }

    @Override
    public boolean canBreakIce() {
        return isDashing; // Solo rompe hielo cuando está embistiendo
    }

    @Override
    public String getType() {
        return "NARWHAL";
    }

    public boolean isDashing() {
        return isDashing;
    }

    @Override
    public String getVisualType() {
        return isDashing ? "NARWHAL_DASH" : "NARWHAL";
    }
}