package domain.shared;

public enum ActionType {
    MOVE,       // Intentar moverse a la casilla adyacente
    CREATE_ICE, // Crear bloque de hielo (habilidad de helado) 
    BREAK_ICE,  // Romper bloque de hielo (habilidad de helado/calamar) [cite: 35, 51]
    WAIT        // Quedarse quieto (útil para estrategias de espera)
}