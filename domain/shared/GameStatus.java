package domain.shared;

/**
 * Enumeracion que representa los posibles estados del juego.
 * Utilizada para controlar el flujo del juego y determinar acciones disponibles.
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see domain.game.DomainController
 */
public enum GameStatus {
    /** Juego en curso, procesando logica y input */
    PLAYING,
    /** Juego pausado, no procesa logica ni tiempo */
    PAUSED,
    /** Victoria: Se recolectaron todas las frutas */
    WON,
    /** Game Over: Un enemigo toco al jugador */
    GAME_OVER,
    /** Tiempo agotado: Se acabaron los 3 minutos */
    TIMEOUT
}
