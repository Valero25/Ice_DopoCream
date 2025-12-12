package domain.items;

/**
 * Clase abstracta base para todos los obstaculos del juego.
 * Los obstaculos bloquean el movimiento de jugadores y/o enemigos.
 * 
 * <p>Tipos de obstaculos disponibles:</p>
 * <ul>
 *   <li>IceBlock - Bloque de hielo destructible</li>
 *   <li>Campfire (Fogata) - Causa muerte al tocarlo cuando esta encendida</li>
 *   <li>HotTile (Baldosa Caliente) - Derrite bloques de hielo sobre ella</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Item
 * @see IceBlock
 * @see Campfire
 * @see HotTile
 */
public abstract class Obstacle extends Item {

    public Obstacle(String id, int x, int y) {
        super(id, x, y);
    }

    @Override
    public boolean isWalkable() {
        return false; // Por defecto bloquean el paso
    }

    public abstract boolean isDestructible();

    // --- CREADOR ESTÁTICO DE OBSTÁCULOS ---
    public static Obstacle create(String type, String id, int x, int y) {
        if (type == null)
            return null;

        switch (type.toUpperCase()) {
            case "ICE_BLOCK":
                return new IceBlock(id, x, y);
            case "CAMPFIRE":
                return new Campfire(id, x, y);
            case "HOT_TILE":
                return new HotTile(id, x, y);
            // case "STONE": return new Stone(id, x, y);
            default:
                return null;
        }
    }
}