package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón de movimiento para enemigos
 * SRP: Solo maneja los patrones de movimiento predefinidos
 * Open/Closed: Se pueden agregar nuevos patrones sin modificar código existente
 */
public class EnemyMovementPattern {
    
    /**
     * Genera el patrón de movimiento del Troll (movimiento por el borde)
     * El Troll se mueve: (0,0) -> (0,9) -> (6,9) -> (6,0) -> (0,0)
     * @param rows Número de filas del tablero
     * @param cols Número de columnas del tablero
     * @return Lista de posiciones en el patrón
     */
    public static List<Position> getTrollPattern(int rows, int cols) {
        List<Position> pattern = new ArrayList<>();
        
        // Borde superior: de (0,0) a (0,9)
        for (int col = 0; col < cols; col++) {
            pattern.add(new Position(0, col));
        }
        
        // Borde derecho: de (0,9) a (6,9)
        for (int row = 1; row < rows; row++) {
            pattern.add(new Position(row, cols - 1));
        }
        
        // Borde inferior: de (6,9) a (6,0)
        for (int col = cols - 2; col >= 0; col--) {
            pattern.add(new Position(rows - 1, col));
        }
        
        // Borde izquierdo: de (6,0) a (0,0)
        for (int row = rows - 2; row > 0; row--) {
            pattern.add(new Position(row, 0));
        }
        
        return pattern;
    }
}
