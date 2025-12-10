package domain.items;

import domain.board.BoardController;
import java.util.Random;

public class Cherry extends Fruit {

    private Random random;
    private float teleportTimer;
    private static final float TELEPORT_INTERVAL = 20.0f; // 20 segundos

    public Cherry(String id, int x, int y) {
        // La cereza otorga 150 puntos
        super(id, x, y, 150);
        this.random = new Random();
        this.teleportTimer = 0;
    }

    @Override
    public void update(float dt) {
        // Acumular tiempo
        teleportTimer += dt;
    }

    @Override
    public void onPlayerMove(BoardController board, ItemController itemCtrl) {
        // Solo intentamos teletransportarnos si ha pasado el tiempo suficiente
        if (teleportTimer >= TELEPORT_INTERVAL) {
            int attempts = 0;
            int maxAttempts = 50; // Evitar bucle infinito

            while (attempts < maxAttempts) {
                int newX = random.nextInt(board.getWidth());
                int newY = random.nextInt(board.getHeight());

                // Verificar que la posición sea válida
                if (board.isWalkable(newX, newY) && !itemCtrl.isObstacleAt(newX, newY)) {
                    this.setPosition(newX, newY);
                    teleportTimer = 0; // Reiniciar timer
                    break;
                }
                attempts++;
            }
        }
    }

    @Override
    public String getType() {
        return "CHERRY";
    }
}
