package domain.items;

public class HotTile extends Obstacle {

    public HotTile(String id, int x, int y) {
        super(id, x, y);
    }

    @Override
    public boolean isWalkable() {
        return true;
    }

    @Override
    public boolean isDestructible() {
        return false;
    }

    @Override
    public boolean isHot() {
        return true;
    }

    @Override
    public String getType() {
        return "HOT_TILE";
    }

    @Override
    public void update(float dt) {
        // No hace nada
    }
}
