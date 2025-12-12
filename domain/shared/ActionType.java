package domain.shared;

/**
 * Enumeracion que define los tipos de acciones que un jugador puede realizar.
 * Utilizada para comunicar las intenciones del jugador al controlador.
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see domain.players.PlayerController
 */
public enum ActionType {
    /** Intentar moverse a la casilla adyacente en la direccion actual */
    MOVE,
    /** Crear hilera de bloques de hielo en la direccion que mira */
    CREATE_ICE,
    /** Romper hilera de bloques de hielo en la direccion que mira */
    BREAK_ICE,
    /** Quedarse quieto (util para estrategias de espera) */
    WAIT
}