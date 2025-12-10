package domain.players;

import domain.shared.ActionType;
import domain.shared.Direction;
import domain.shared.EntityInfo;
import java.util.List;

/**
 * Bot experto que combina estrategias de huida y recoleccion.
 * Estrategia: Huir si hay peligro, recolectar frutas si es seguro.
 */
public class ExpertBot extends BotPlayer {

    private boolean wantsToPlaceIce = false;
    private Direction iceDirection = Direction.NONE;

    public ExpertBot(String id, int x, int y, IceCreamFlavor flavor) {
        super(id, x, y, flavor, PlayerType.MACHINE_EXPERT);
    }

    @Override
    public Direction decideMove(List<EntityInfo> fruits, List<EntityInfo> enemies, boolean[] canMove) {
        wantsToPlaceIce = false;

        // 1. Detectar Narvales alineados (peligro de carga)
        for (EntityInfo e : enemies) {
            if (e.type.equals("NARWHAL")) {
                boolean alignedX = (e.x == getX());
                boolean alignedY = (e.y == getY());
                if (alignedX || alignedY) {
                    Direction runDir = getDirectionAwayFrom(e.x, e.y, canMove);
                    if (runDir != Direction.NONE) {
                        return runDir;
                    }
                }
            }
        }

        // 2. Verificar enemigo cercano
        EntityInfo nearestEnemy = findNearestEnemy(enemies);
        if (nearestEnemy != null && isClose(nearestEnemy, 4)) {
            // Intentar bloquear con hielo si es posible
            if (shouldPlaceIce(nearestEnemy)) {
                wantsToPlaceIce = true;
                iceDirection = getDirectionTowardsSimple(nearestEnemy.x, nearestEnemy.y);
                return iceDirection;
            }

            // Huir
            Direction runDir = getDirectionAwayFrom(nearestEnemy.x, nearestEnemy.y, canMove);
            if (runDir != Direction.NONE) {
                return runDir;
            }
        }

        // 3. Buscar fruta
        EntityInfo nearestFruit = findNearest(fruits);
        if (nearestFruit != null) {
            Direction dir = getDirectionTowards(nearestFruit.x, nearestFruit.y, canMove);
            if (dir != Direction.NONE) {
                return dir;
            }
        }

        // 4. Moverse al azar
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
        // El Narval y el Calamar rompen el hielo
        if (e.type.equals("NARWHAL") || e.type.equals("SQUID")) {
            return false;
        }

        double dist = Math.sqrt(Math.pow(e.x - getX(), 2) + Math.pow(e.y - getY(), 2));

        // Bloquear si esta a 2-3.5 casillas y alineado
        if (dist >= 2.0 && dist <= 3.5) {
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
