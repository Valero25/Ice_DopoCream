package domain;

/**
 * Representa una fruta en el juego
 * SRP: Solo maneja el estado de la fruta (tipo, posición, si fue recolectada)
 */
public class Fruit {
    private String type;
    private Position position;
    private boolean collected;

    public Fruit(String type, Position position) {
        this.type = type;
        this.position = position;
        this.collected = false;
    }

    public String getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        this.collected = true;
    }

    /**
     * Verifica si el jugador está en la misma posición que la fruta
     */
    public boolean isAt(Position pos) {
        return !collected && position.equals(pos);
    }
}
