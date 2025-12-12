package domain.shared;

/**
 * Excepcion personalizada para errores especificos del juego Bad DOPO.
 * Proporciona codigos de error predefinidos para identificar problemas comunes.
 * 
 * <p>Codigos de error disponibles:</p>
 * <ul>
 *   <li>CONFIG_ERROR - Error en la configuracion del nivel</li>
 *   <li>MOVE_ERROR - Movimiento ilegal detectado</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see BadOpoLogger
 */
public class BadOpoException extends Exception {
    
    /** Codigo de error para problemas de configuracion de nivel */
    public static final String CONFIG_ERROR = "Error en configuracion de nivel";
    /** Codigo de error para movimientos no permitidos */
    public static final String MOVE_ERROR = "Movimiento ilegal detectado";

    public BadOpoException(String message) {
        super(message);
    }

    public BadOpoException(String message, Throwable cause) {
        super(message, cause);
    }
}