package domain.players;

import domain.shared.ActionType;
import domain.shared.Direction;

/**
 * Clase base que representa un jugador (helado) en el juego.
 * Contiene la posicion, puntuacion, estado de vida y apariencia del personaje.
 */
public class IceCream implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private int x;
    private int y;
    private IceCreamFlavor flavor;
    private PlayerType type;
    private Direction facingDirection;
    private int score;
    private boolean isAlive;
    private String playerName;

    /**
     * Construye un nuevo jugador IceCream.
     * 
     * @param id     Identificador unico del jugador
     * @param x      Posicion inicial en el eje X
     * @param y      Posicion inicial en el eje Y
     * @param flavor Sabor que determina la apariencia visual
     * @param type   Tipo de jugador (humano o bot)
     */
    public IceCream(String id, int x, int y, IceCreamFlavor flavor, PlayerType type) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.flavor = flavor;
        this.type = type;
        this.facingDirection = Direction.DOWN;
        this.score = 0;
        this.isAlive = true;
        this.playerName = id; // Por defecto, el nombre es el ID
    }

    /**
     * Obtiene el nombre personalizado del jugador.
     * 
     * @return El nombre del jugador
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Establece el nombre personalizado del jugador.
     * 
     * @param name El nombre a asignar al jugador
     */
    public void setPlayerName(String name) {
        this.playerName = name;
    }

    /**
     * Obtiene el identificador unico del jugador.
     * 
     * @return El ID del jugador
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene la posicion actual en el eje X.
     * 
     * @return Coordenada X actual
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene la posicion actual en el eje Y.
     * 
     * @return Coordenada Y actual
     */
    public int getY() {
        return y;
    }

    /**
     * Establece una nueva posicion para el jugador.
     * 
     * @param x Nueva coordenada X
     * @param y Nueva coordenada Y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Obtiene la direccion hacia la que mira el jugador.
     * 
     * @return Direccion actual del jugador
     */
    public Direction getFacingDirection() {
        return facingDirection;
    }

    /**
     * Establece la direccion hacia la que mira el jugador.
     * Ignora la direccion NONE para mantener la ultima direccion valida.
     * 
     * @param d Nueva direccion (se ignora si es NONE)
     */
    public void setFacingDirection(Direction d) {
        if (d != Direction.NONE)
            this.facingDirection = d;
    }

    /**
     * Obtiene la puntuacion actual del jugador.
     * 
     * @return Puntuacion acumulada
     */
    public int getScore() {
        return score;
    }

    /**
     * Agrega puntos a la puntuacion del jugador.
     * 
     * @param points Cantidad de puntos a agregar
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Verifica si el jugador esta vivo.
     * 
     * @return true si el jugador esta vivo, false si esta muerto
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Establece el estado de vida del jugador.
     * 
     * @param alive true para marcar como vivo, false para marcar como muerto
     */
    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    /**
     * Obtiene el sabor del jugador.
     * 
     * @return El sabor que determina la apariencia visual
     */
    public IceCreamFlavor getFlavor() {
        return flavor;
    }

    /**
     * Obtiene el tipo de jugador (humano o tipo de bot).
     * 
     * @return El tipo de jugador
     */
    public PlayerType getPlayerType() {
        return type;
    }

    /**
     * Genera un identificador de tipo para la GUI.
     * Concatena "PLAYER_" con el nombre del sabor para que la interfaz
     * grafica sepa que color o sprite usar.
     * 
     * @return String en formato "PLAYER_SABOR" (ej: "PLAYER_CHOCOLATE")
     */
    public String getType() {
        return "PLAYER_" + flavor.name();
    }

    /**
     * Indica si este jugador es un bot controlado por IA.
     * 
     * @return false por defecto, los BotPlayer sobrescriben con true
     */
    public boolean isBot() {
        return false;
    }

    // --- Métodos de Bot (implementación por defecto para humanos) ---
    // Los BotPlayer sobrescriben estos métodos

    public void updateBotTimer(float dt) {
        // No hace nada para jugadores humanos
    }

    public boolean canBotMove() {
        return false; // Humanos no usan este método
    }

    public void resetBotTimer() {
        // No hace nada para jugadores humanos
    }

    public Direction decideMove(java.util.List<domain.shared.EntityInfo> fruits,
            java.util.List<domain.shared.EntityInfo> enemies,
            boolean[] canMove) {
        return Direction.NONE; // Humanos no usan este método
    }

    public ActionType getDesiredAction() {
        return ActionType.MOVE; // Por defecto
    }
}
