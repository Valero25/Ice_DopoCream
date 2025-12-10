package domain.shared;

public enum GameStatus {
    PLAYING,
    PAUSED,     // Requisito funcional: Permitir pausar [cite: 84]
    WON,        // Mensaje de victoria [cite: 86]
    GAME_OVER,  // Si un enemigo toca al helado [cite: 76]
    TIMEOUT     // Si se agotan los 3 minutos [cite: 77]
}
