package domain.players;

import domain.shared.Direction;
import domain.shared.EntityInfo;
import java.util.List;

/**
 * Bot que persigue la fruta mas cercana.
 * Estrategia: Ir hacia la fruta mas proxima o moverse al azar si no hay.
 */
public class HungryBot extends BotPlayer {

    public HungryBot(String id, int x, int y, IceCreamFlavor flavor) {
        super(id, x, y, flavor, PlayerType.MACHINE_HUNGRY);
    }

    @Override
    public Direction decideMove(List<EntityInfo> fruits, List<EntityInfo> enemies, boolean[] canMove) {
        // Buscar fruta mas cercana
        EntityInfo nearest = findNearest(fruits);

        if (nearest != null) {
            return getDirectionTowards(nearest.x, nearest.y, canMove);
        }

        // Si no hay fruta, moverse al azar
        return getRandomValidDirection(canMove);
    }
}
