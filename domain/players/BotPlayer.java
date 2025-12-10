package domain.players;

import domain.shared.ActionType;
import domain.shared.Direction;
import domain.shared.EntityInfo;
import java.util.List;
import java.util.Random;

/**
 * Clase abstracta base para todos los jugadores controlados por IA.
 * Extiende IceCream y proporciona funcionalidad comun para los bots.
 */
public abstract class BotPlayer extends IceCream {

    protected float moveTimer;
    protected static final float MOVE_INTERVAL = 0.25f;
    protected Random random;

    public BotPlayer(String id, int x, int y, IceCreamFlavor flavor, PlayerType type) {
        super(id, x, y, flavor, type);
        this.random = new Random();
        this.moveTimer = 0;
    }

    @Override
    public boolean isBot() {
        return true;
    }

    /**
     * Decide la direccion de movimiento basada en el contexto.
     */
    @Override
    public abstract Direction decideMove(List<EntityInfo> fruits, List<EntityInfo> enemies, boolean[] canMove);

    /**
     * Retorna la accion deseada por el bot.
     */
    @Override
    public ActionType getDesiredAction() {
        return ActionType.MOVE;
    }

    @Override
    public void updateBotTimer(float dt) {
        moveTimer += dt;
    }

    @Override
    public boolean canBotMove() {
        return moveTimer >= MOVE_INTERVAL;
    }

    @Override
    public void resetBotTimer() {
        moveTimer = 0;
    }

    // --- Metodos utilitarios para los hijos ---

    protected EntityInfo findNearest(List<EntityInfo> items) {
        EntityInfo nearest = null;
        double minDist = Double.MAX_VALUE;
        for (EntityInfo e : items) {
            double dist = Math.pow(e.x - getX(), 2) + Math.pow(e.y - getY(), 2);
            if (dist < minDist) {
                minDist = dist;
                nearest = e;
            }
        }
        return nearest;
    }

    protected Direction getDirectionTowards(int targetX, int targetY, boolean[] canMove) {
        int dx = targetX - getX();
        int dy = targetY - getY();
        // canMove: [0]=UP, [1]=DOWN, [2]=LEFT, [3]=RIGHT
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0 && canMove[3])
                return Direction.RIGHT;
            if (dx < 0 && canMove[2])
                return Direction.LEFT;
            if (dy > 0 && canMove[1])
                return Direction.DOWN;
            if (dy < 0 && canMove[0])
                return Direction.UP;
        } else {
            if (dy > 0 && canMove[1])
                return Direction.DOWN;
            if (dy < 0 && canMove[0])
                return Direction.UP;
            if (dx > 0 && canMove[3])
                return Direction.RIGHT;
            if (dx < 0 && canMove[2])
                return Direction.LEFT;
        }
        return getRandomValidDirection(canMove);
    }

    protected Direction getDirectionAwayFrom(int targetX, int targetY, boolean[] canMove) {
        int dx = getX() - targetX;
        int dy = getY() - targetY;

        // Evasion perpendicular si esta alineado
        boolean alignedX = (targetX == getX());
        boolean alignedY = (targetY == getY());

        if (alignedY) {
            if (canMove[1])
                return Direction.DOWN;
            if (canMove[0])
                return Direction.UP;
        }
        if (alignedX) {
            if (canMove[3])
                return Direction.RIGHT;
            if (canMove[2])
                return Direction.LEFT;
        }

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0 && canMove[3])
                return Direction.RIGHT;
            if (dx < 0 && canMove[2])
                return Direction.LEFT;
            if (dy > 0 && canMove[1])
                return Direction.DOWN;
            if (dy < 0 && canMove[0])
                return Direction.UP;
        } else {
            if (dy > 0 && canMove[1])
                return Direction.DOWN;
            if (dy < 0 && canMove[0])
                return Direction.UP;
            if (dx > 0 && canMove[3])
                return Direction.RIGHT;
            if (dx < 0 && canMove[2])
                return Direction.LEFT;
        }
        return getRandomValidDirection(canMove);
    }

    protected Direction getRandomValidDirection(boolean[] canMove) {
        Direction[] dirs = { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT };
        for (int i = 0; i < 10; i++) {
            int idx = random.nextInt(4);
            if (canMove[idx])
                return dirs[idx];
        }
        for (int i = 0; i < 4; i++) {
            if (canMove[i])
                return dirs[i];
        }
        return Direction.NONE;
    }
}
