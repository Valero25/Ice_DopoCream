package domain;

/**
 * Representa las direcciones de movimiento en el juego
 * Principio Open/Closed: Se pueden agregar nuevas direcciones sin modificar código existente
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int rowDelta;
    private final int colDelta;

    Direction(int rowDelta, int colDelta) {
        this.rowDelta = rowDelta;
        this.colDelta = colDelta;
    }

    public int getRowDelta() {
        return rowDelta;
    }

    public int getColDelta() {
        return colDelta;
    }

    /**
     * Convierte un string o tecla a una dirección
     */
    public static Direction fromString(String input) {
        if (input == null) return null;
        
        switch (input.toLowerCase()) {
            case "w":
            case "up":
                return UP;
            case "s":
            case "down":
                return DOWN;
            case "a":
            case "left":
                return LEFT;
            case "d":
            case "right":
                return RIGHT;
            default:
                return null;
        }
    }
}
