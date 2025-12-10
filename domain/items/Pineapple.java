package domain.items;

import domain.board.BoardController;
import java.util.Random;

public class Pineapple extends Fruit {

    private Random random;

    public Pineapple(String id, int x, int y) {
        // La piña otorga 200 puntos
        super(id, x, y, 200);
        this.random = new Random();
    }

    @Override
    public void update(float dt) {
        // La piña no se mueve por tiempo (dt), sino por turnos/eventos del jugador.
        // Por eso este método se queda vacío.
    }

    @Override
    public void onPlayerMove(BoardController board, ItemController itemCtrl) {
        int startDir = random.nextInt(4);
        int[] dx = { 0, 0, -1, 1 }; // Arriba, Abajo, Izq, Der
        int[] dy = { -1, 1, 0, 0 };

        for (int i = 0; i < 4; i++) {
            int idx = (startDir + i) % 4; // Rotar direcciones
            int nextX = this.x + dx[idx];
            int nextY = this.y + dy[idx];

            // Verificamos si la casilla es válida (Tablero) y no tiene obstáculo
            if (board.isWalkable(nextX, nextY) && !itemCtrl.isObstacleAt(nextX, nextY)) {
                this.setPosition(nextX, nextY);
                break; // Ya se movió, terminamos con esta piña
            }
        }
    }

    @Override
    public String getType() {
        return "PINEAPPLE";
    }
}