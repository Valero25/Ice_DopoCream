package domain.items;

public abstract class Fruit extends Item {
    protected int score;

    public Fruit(String id, int x, int y, int score) {
        super(id, x, y);
        this.score = score;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public boolean isCollectable() {
        return true;
    }

    @Override
    public boolean isFruit() {
        return true;
    }

    @Override
    public boolean isWalkable() {
        return true; // Por defecto se puede caminar sobre las frutas
    }

    // --- CREADOR ESTÁTICO DE FRUTAS ---
    public static Fruit create(String type, String id, int x, int y) {
        if (type == null)
            return null;

        switch (type.toUpperCase()) {
            case "BANANA":
                return new Banana(id, x, y);
            case "GRAPE":
                return new Grape(id, x, y);
            case "PINEAPPLE":
                return new Pineapple(id, x, y);
            case "CHERRY":
                return new Cherry(id, x, y);
            case "CACTUS":
                return new Cactus(id, x, y);
            default:
                return null;
        }
    }
}