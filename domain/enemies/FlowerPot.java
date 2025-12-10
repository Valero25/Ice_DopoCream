package domain.enemies;

import domain.shared.Direction;

public class FlowerPot extends Enemy {

    public FlowerPot(String id, int x, int y) {
        super(id, x, y, 3.3f); // ~3.3 movimientos por segundo (1 casilla cada 0.3s)
    }

    @Override
    public Direction decideMove(boolean blocked, int targetX, int targetY) {
        // Lógica de persecución muy simple (Eje X primero, luego Eje Y)

        if (this.x < targetX)
            return Direction.RIGHT;
        if (this.x > targetX)
            return Direction.LEFT;

        if (this.y < targetY)
            return Direction.DOWN;
        if (this.y > targetY)
            return Direction.UP;

        return Direction.NONE;
    }

    @Override
    public String getType() {
        return "FLOWERPOT";
    }
}