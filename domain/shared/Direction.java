package domain.shared;

/**
 * Enumeracion que representa las direcciones de movimiento en el tablero.
 * Cada direccion tiene un desplazamiento asociado en los ejes X e Y.
 * 
 * <p>Sistema de coordenadas:</p>
 * <ul>
 *   <li>X aumenta hacia la derecha</li>
 *   <li>Y aumenta hacia abajo</li>
 *   <li>Origen (0,0) en esquina superior izquierda</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 */
public enum Direction {
    /** Movimiento hacia arriba (Y-1) */
    UP(0, -1),
    /** Movimiento hacia abajo (Y+1) */
    DOWN(0, 1),
    /** Movimiento hacia la izquierda (X-1) */
    LEFT(-1, 0),
    /** Movimiento hacia la derecha (X+1) */
    RIGHT(1, 0),
    /** Sin movimiento */
    NONE(0, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    // Útil para rebotes (usado por Trolls) [cite: 48]
    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return NONE;
        }
    }

    public static Direction random() {
        Direction[] dirs = { UP, DOWN, LEFT, RIGHT };
        return dirs[(int) (Math.random() * dirs.length)];
    }
}