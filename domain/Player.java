package domain;

/**
 * Representa un jugador en el juego
 * SRP: Solo maneja el estado del jugador (personaje, posición, puntuación)
 */
public class Player {
    private String character;
    private Position position;
    private boolean isMachine;
    private int score;

    public Player(String character, Position startPosition, boolean isMachine) {
        this.character = character;
        this.position = startPosition;
        this.isMachine = isMachine;
        this.score = 0;
    }

    public String getCharacter() {
        return character;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isMachine() {
        return isMachine;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }
}
