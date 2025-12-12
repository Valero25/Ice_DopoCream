package domain.players;

import domain.shared.ActionType;
import domain.shared.Direction;
import domain.shared.EntityInfo;
import java.util.List;

/**
 * Bot Miedoso que prioriza huir de los enemigos.
 * Utiliza la habilidad de crear hielo como barrera defensiva.
 * 
 * <p>Estrategia:</p>
 * <ul>
 *   <li>Si hay enemigo cerca (radio 5), intenta huir</li>
 *   <li>Puede crear barreras de hielo cuando esta alineado con enemigos</li>
 *   <li>No crea hielo contra Narval (rompe hielo cargando) ni Calamar cercano</li>
 *   <li>Si es seguro, se mueve aleatoriamente</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see BotPlayer
 * @see PlayerType#MACHINE_FEARFUL
 */
public class FearfulBot extends BotPlayer {

    private boolean wantsToPlaceIce = false;
    private Direction iceDirection = Direction.NONE;

    public FearfulBot(String id, int x, int y, IceCreamFlavor flavor) {
        super(id, x, y, flavor, PlayerType.MACHINE_FEARFUL);
    }

    @Override
    public Direction decideMove(List<EntityInfo> fruits, List<EntityInfo> enemies, boolean[] canMove) {
        wantsToPlaceIce = false;

        EntityInfo nearest = findNearestEnemy(enemies);

        if (nearest != null && isClose(nearest, 5)) {
            // Verificar si deberia poner hielo
            if (shouldPlaceIce(nearest)) {
                wantsToPlaceIce = true;
                iceDirection = getDirectionTowardsSimple(nearest.x, nearest.y);
                return iceDirection;
            }

            // Huir del enemigo
            Direction runDir = getDirectionAwayFrom(nearest.x, nearest.y, canMove);
            if (runDir != Direction.NONE) {
                return runDir;
            }
        }

        // Si no hay peligro, moverse al azar
        return getRandomValidDirection(canMove);
    }

    @Override
    public ActionType getDesiredAction() {
        return wantsToPlaceIce ? ActionType.CREATE_ICE : ActionType.MOVE;
    }

    private EntityInfo findNearestEnemy(List<EntityInfo> enemies) {
        EntityInfo nearest = null;
        double minDist = Double.MAX_VALUE;
        for (EntityInfo e : enemies) {
            double dist = Math.pow(e.x - getX(), 2) + Math.pow(e.y - getY(), 2);
            if (dist < minDist) {
                minDist = dist;
                nearest = e;
            }
        }
        return nearest;
    }

    private boolean isClose(EntityInfo e, int radius) {
        double dist = Math.sqrt(Math.pow(e.x - getX(), 2) + Math.pow(e.y - getY(), 2));
        return dist <= radius;
    }

    private boolean shouldPlaceIce(EntityInfo e) {
        // El Narval rompe el hielo cargando, inutil
        if (e.type.equals("NARWHAL")) {
            return false;
        }

        double dist = Math.sqrt(Math.pow(e.x - getX(), 2) + Math.pow(e.y - getY(), 2));

        // El Calamar rompe hielo de 1 en 1. Vale solo si esta lejos
        if (e.type.equals("SQUID") && dist < 3.0) {
            return false;
        }

        // Solo poner hielo si esta a distancia media (2-4 casillas)
        if (dist > 1.5 && dist < 4.0) {
            boolean alignedX = (e.x == getX());
            boolean alignedY = (e.y == getY());
            return alignedX || alignedY;
        }
        return false;
    }

    private Direction getDirectionTowardsSimple(int targetX, int targetY) {
        int dx = targetX - getX();
        int dy = targetY - getY();
        if (Math.abs(dx) > Math.abs(dy)) {
            return (dx > 0) ? Direction.RIGHT : Direction.LEFT;
        } else {
            return (dy > 0) ? Direction.DOWN : Direction.UP;
        }
    }
}
