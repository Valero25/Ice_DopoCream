package domain.players;

/**
 * Enumeracion que define los tipos de jugadores disponibles en el juego.
 * Determina si el jugador es controlado por humano o por inteligencia artificial,
 * y en caso de ser IA, que estrategia utiliza.
 * 
 * <p>Tipos disponibles:</p>
 * <ul>
 *   <li>HUMAN - Controlado por jugador humano via teclado</li>
 *   <li>MACHINE_HUNGRY - IA que prioriza recolectar frutas</li>
 *   <li>MACHINE_FEARFUL - IA que prioriza huir de enemigos</li>
 *   <li>MACHINE_EXPERT - IA con estrategia balanceada avanzada</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see IceCream
 * @see BotPlayer
 */
public enum PlayerType {

    /** Jugador humano controlado por teclado */
    HUMAN,

    /** Bot que persigue frutas activamente */
    MACHINE_HUNGRY,

    /** Bot que huye de enemigos y crea barreras */
    MACHINE_FEARFUL,

    /** Bot con estrategia combinada avanzada */
    MACHINE_EXPERT
}
