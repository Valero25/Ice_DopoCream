package domain.enemies;

import domain.shared.Direction;

public class Troll extends Enemy {

    private Direction currentDir;

    public Troll(String id, int x, int y) {
        super(id, x, y, 3.3f); // ~3.3 movimientos por segundo (1 casilla cada 0.3s)
        this.currentDir = Direction.RIGHT; // Dirección inicial por defecto
    }

    @Override
    public Direction decideMove(boolean blocked, int targetX, int targetY) {
        // Regla: Si chocó, cambia de dirección (gira 90 grados a la derecha)
        if (blocked) {
            currentDir = invert(currentDir);
        }
        return currentDir;
    }

    // Ahora rota 90 grados a la derecha en lugar de invertir 180 grados
    private Direction invert(Direction d) {
        if (d == Direction.RIGHT)
            return Direction.DOWN;
        if (d == Direction.DOWN)
            return Direction.LEFT;
        if (d == Direction.LEFT)
            return Direction.UP;
        if (d == Direction.UP)
            return Direction.RIGHT;
        return Direction.NONE;
    }

    @Override
    public String getType() {
        return "TROLL";
    }
}