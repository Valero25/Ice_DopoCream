package domain.items;

/**
 * Baldosa caliente que derrite instantaneamente los bloques de hielo.
 * El jugador puede caminar sobre ella sin peligro.
 * Impide la creacion de bloques de hielo en su posicion.
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Obstacle
 * @see IceBlock
 */
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
