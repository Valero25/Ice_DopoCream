package domain;

/**
 * Representa el tablero de juego
 * SRP: Solo maneja dimensiones y validación de posiciones
 * Open/Closed: Se pueden agregar nuevas validaciones extendiendo sin modificar
 */
public class Board {
    private final int rows;
    private final int cols;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Verifica si una posición está dentro de los límites del tablero
     */
    public boolean isValidPosition(Position pos) {
        return pos.getRow() >= 0 && pos.getRow() < rows &&
               pos.getCol() >= 0 && pos.getCol() < cols;
    }

    /**
     * Verifica si un jugador puede moverse a una posición
     * En el futuro se pueden agregar más validaciones (obstáculos, etc)
     */
    public boolean canMoveTo(Position pos) {
        return isValidPosition(pos);
    }
}
