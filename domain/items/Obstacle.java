package domain.items;

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