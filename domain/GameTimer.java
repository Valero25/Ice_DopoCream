package domain;

/**
 * Gestiona el temporizador del juego
 * SRP: Solo se encarga de la lógica del tiempo
 */
public class GameTimer {
    private static final int TIME_LIMIT_SECONDS = 180; // 3 minutos
    private int remainingTimeSeconds;
    private boolean timeExpired;
    
    public GameTimer() {
        this.remainingTimeSeconds = TIME_LIMIT_SECONDS;
        this.timeExpired = false;
    }
    
    public int getRemainingTimeSeconds() {
        return remainingTimeSeconds;
    }
    
    public boolean isTimeExpired() {
        return timeExpired;
    }
    
    /**
     * Decrementa el tiempo restante en 1 segundo
     * Si el tiempo llega a 0, marca el tiempo como expirado
     */
    public boolean decrementTime() {
        if (remainingTimeSeconds > 0) {
            remainingTimeSeconds--;
            
            if (remainingTimeSeconds == 0) {
                timeExpired = true;
                System.out.println("¡Tiempo agotado! Nivel fallido");
                return true;
            }
        }
        return false;
    }
    
    /**
     * Reinicia el temporizador
     */
    public void reset() {
        this.remainingTimeSeconds = TIME_LIMIT_SECONDS;
        this.timeExpired = false;
    }
}
