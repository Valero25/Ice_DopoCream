package domain.items;

/**
 * Clase abstracta base para todas las frutas coleccionables del juego.
 * Las frutas otorgan puntos al ser recolectadas por los jugadores.
 * 
 * <p>Tipos de frutas disponibles:</p>
 * <ul>
 *   <li>Banana - 100 puntos, estatica</li>
 *   <li>Grape (Uva) - 50 puntos, estatica</li>
 *   <li>Pineapple (Piña) - 200 puntos, se mueve cuando el jugador se mueve</li>
 *   <li>Cherry (Cereza) - 150 puntos, se teletransporta cada 20 segundos</li>
 *   <li>Cactus - 250 puntos, alterna entre puas (peligroso) y seguro cada 30s</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Item
 * @see Banana
 * @see Grape
 * @see Pineapple
 * @see Cherry
 * @see Cactus
 */
public abstract class Fruit extends Item {
    protected int score;

    public Fruit(String id, int x, int y, int score) {
        super(id, x, y);
        this.score = score;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public boolean isCollectable() {
        return true;
    }

    @Override
    public boolean isFruit() {
        return true;
    }

    @Override
    public boolean isWalkable() {
        return true; // Por defecto se puede caminar sobre las frutas
    }

    // --- CREADOR ESTÁTICO DE FRUTAS ---
    public static Fruit create(String type, String id, int x, int y) {
        if (type == null)
            return null;

        switch (type.toUpperCase()) {
            case "BANANA":
                return new Banana(id, x, y);
            case "GRAPE":
                return new Grape(id, x, y);
            case "PINEAPPLE":
                return new Pineapple(id, x, y);
            case "CHERRY":
                return new Cherry(id, x, y);
            case "CACTUS":
                return new Cactus(id, x, y);
            default:
                return null;
        }
    }
}