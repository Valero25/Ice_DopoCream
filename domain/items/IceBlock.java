package domain.items;

public class IceBlock extends Obstacle {

    public IceBlock(String id, int x, int y) {
        super(id, x, y);
    }

    @Override
    public boolean isDestructible() {
        return true; // El hielo sí se rompe
    }

    @Override
    public void update(float dt) {
        // Se deja vacío por ahora (a futuro podría derretirse)
    }

    @Override
    public String getType() {
        return "ICE";
    }

    @Override
    public boolean canSpawnAt(java.util.List<Item> items) {
        // El hielo no puede aparecer en baldosas calientes
        for (Item item : items) {
            if (item.getX() == this.x && item.getY() == this.y && item.isHot()) {
                return false; // Se derrite inmediatamente
            }
        }
        return true;
    }

    @Override
    public void onDestroy(java.util.List<Item> items) {
        // Al romperse, apaga fogatas en la misma posición
        for (Item item : items) {
            if (item.getX() == this.x && item.getY() == this.y) {
                item.onIceBrokenAbove();
            }
        }
    }
}