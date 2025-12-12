package domain.items;

/**
 * Bloque de hielo destructible creado por los jugadores.
 * Puede ser destruido por jugadores o ciertos enemigos.
 * Se derrite instantaneamente sobre baldosas calientes.
 * 
 * <p>Interacciones:</p>
 * <ul>
 *   <li>Los jugadores pueden crearlo y destruirlo con habilidades</li>
 *   <li>El Calamar puede destruirlo</li>
 *   <li>El Narval puede destruirlo durante embestida</li>
 *   <li>Se derrite sobre HotTile</li>
 *   <li>Al romperse sobre una fogata, la apaga temporalmente</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Obstacle
 */
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