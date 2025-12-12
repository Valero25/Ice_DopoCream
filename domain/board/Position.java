package domain.board;

import java.io.Serializable;

/**
 * Representa una posicion en el tablero de juego mediante coordenadas X e Y.
 * Clase inmutable utilizada para almacenar y comparar posiciones en el grid.
 * Implementa equals() y hashCode() para permitir su uso en colecciones.
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see BoardController
 */
class Position implements Serializable {
    private static final long serialVersionUID = 1L;
    private int x;
    private int y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
