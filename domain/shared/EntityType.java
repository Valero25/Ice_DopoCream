package domain.shared;

/**
 * Enumeracion que define los tipos basicos de entidades en el tablero.
 * Utilizada principalmente por BoardController para gestionar el contenido de celdas.
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see domain.board.BoardController
 * @see domain.board.Cell
 */
public enum EntityType {
    /** Personajes jugadores (helados) */
    PLAYER,
    /** Personajes enemigos */
    ENEMY,
    /** Frutas coleccionables */
    FRUIT,
    /** Borde del mapa, indestructible */
    WALL,
    /** Bloque de hielo destructible */
    ICE_BLOCK,
    /** Otros obstaculos (fogatas, baldosas calientes) */
    OBSTACLE,
    /** Espacio libre donde se puede caminar */
    EMPTY
}