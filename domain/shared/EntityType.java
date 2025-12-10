package domain.shared;

public enum EntityType {
    PLAYER,         // Helados
    ENEMY,          // Enemigos
    FRUIT,          // Frutas recolectables
    WALL,           // Borde indestructible
    ICE_BLOCK,      // Bloque destructible [cite: 58]
    OBSTACLE,       // Otros (fogatas, etc.)
    EMPTY           // Espacio libre
}