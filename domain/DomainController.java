package domain;

/**
 * Controlador principal del dominio (Facade Pattern)
 * SRP: Coordina todas las operaciones del dominio
 * Todos los llamados entre clases del dominio pasan por aquí
 */
public class DomainController {

    private GameState gameState;
    private Game currentGame;

    public DomainController() {
        this.gameState = new GameState();
    }

    // ===== Configuración del juego =====
    
    public void setGameMode(GameMode mode) {
        gameState.setMode(mode);
    }

    public void setPlayer1Character(String character) {
        gameState.setPlayer1Character(character);
    }

    public void setPlayer2Character(String character, boolean isMachine) {
        gameState.setPlayer2Character(character, isMachine);
    }

    public void startGame(int level) {
        currentGame = new Game(
            gameState.getPlayer1Character(),
            gameState.getPlayer2Character(),
            gameState.isPlayer2Machine(),
            level
        );
    }

    // ===== Consultas =====
    
    public Game getCurrentGame() {
        return currentGame;
    }

    public GameState getGameState() {
        return gameState;
    }

    // ===== Acciones del juego =====
    
    /**
     * Mueve al jugador 1 en la dirección especificada
     * @param direction Dirección del movimiento
     * @return true si el movimiento fue exitoso
     */
    public boolean movePlayer1(Direction direction) {
        if (currentGame == null) return false;
        return currentGame.movePlayer1(direction);
    }

    /**
     * Mueve al jugador 2 en la dirección especificada
     * @param direction Dirección del movimiento
     * @return true si el movimiento fue exitoso
     */
    public boolean movePlayer2(Direction direction) {
        if (currentGame == null) return false;
        return currentGame.movePlayer2(direction);
    }
}
