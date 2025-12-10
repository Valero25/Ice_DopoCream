package domain.shared;

public class BadOpoException extends Exception {
    
    // Podemos agregar códigos de error si queremos ser más específicos en el log [cite: 100]
    public static final String CONFIG_ERROR = "Error en configuración de nivel";
    public static final String MOVE_ERROR = "Movimiento ilegal detectado";

    public BadOpoException(String message) {
        super(message);
    }

    public BadOpoException(String message, Throwable cause) {
        super(message, cause);
    }
}