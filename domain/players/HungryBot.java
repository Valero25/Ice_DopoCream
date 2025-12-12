package domain.players;

import domain.shared.Direction;
import domain.shared.EntityInfo;
import java.util.List;

/**
 * Bot Hambriento que persigue la fruta mas cercana.
 * Estrategia simple: Ir hacia la fruta mas proxima sin considerar enemigos.
 * Si no hay frutas disponibles, se mueve aleatoriamente.
 * 
 * <p>Caracteristicas:</p>
 * <ul>
 *   <li>Prioridad: Recolectar frutas</li>
 *   <li>No evita enemigos activamente</li>
 *   <li>Movimiento aleatorio cuando no hay frutas</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see BotPlayer
 * @see PlayerType#MACHINE_HUNGRY
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
