package domain;

/**
 * Representa un enemigo en el juego
 * SRP: Solo maneja el estado del enemigo (tipo, posición, patrón de movimiento)
 */
public class Enemy {
    private String type;
    private Position position;
    private int pathIndex; // Índice en el patrón de movimiento

    public Enemy(String type, Position startPosition) {
        this.type = type;
        this.position = startPosition;
        this.pathIndex = 0;
    }

    public String getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    /**
     * Verifica si el enemigo está en la misma posición que el jugador
     */
    public boolean collidesWith(Player player) {
        return this.position.equals(player.getPosition());
    }
}
