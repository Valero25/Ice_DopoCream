package domain;

public class GameState {

    private GameMode mode;

    private String player1Character;
    private String player2Character;
    private boolean player2IsMachine;

    private int player1Score;
    private int player2Score;

    public GameState() {
        this.player1Score = 0;
        this.player2Score = 0;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public GameMode getMode() {
        return mode;
    }

    public void setPlayer1Character(String character) {
        this.player1Character = character;
    }

    public String getPlayer1Character() {
        return player1Character;
    }

    public void setPlayer2Character(String character, boolean isMachine) {
        this.player2Character = character;
        this.player2IsMachine = isMachine;
    }

    public String getPlayer2Character() {
        return player2Character;
    }

    public boolean isPlayer2Machine() {
        return player2IsMachine;
    }

    public void setPlayer1Score(int score) {
        this.player1Score = score;
    }

    public void setPlayer2Score(int score) {
        this.player2Score = score;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    @Override
    public String toString() {
        return "Mode: " + (mode != null ? mode.getDisplayName() : "none") +
               ", Player1: " + player1Character +
               ", Player2: " + (player2IsMachine ? "Machine (" : "") + player2Character + (player2IsMachine ? ")" : "");
    }
}
