package domain.items;

/**
 * Fogata que alterna entre encendida (peligrosa) y apagada (segura).
 * Cuando esta encendida, elimina al jugador que la toque.
 * Se puede apagar temporalmente rompiendo hielo sobre ella.
 * 
 * <p>Comportamiento:</p>
 * <ul>
 *   <li>Inicia encendida (peligrosa)</li>
 *   <li>Se apaga cuando se rompe hielo sobre ella</li>
 *   <li>Se reenciende automaticamente despues de 10 segundos</li>
 *   <li>El jugador puede caminar sobre ella pero muere si esta encendida</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Obstacle
 */
public class Campfire extends Obstacle {

    private boolean isOn;
    private float timer;
    private static final float REIGNITE_TIME = 10.0f;

    public Campfire(String id, int x, int y) {
        super(id, x, y);
        this.isOn = true;
        this.timer = 0;
    }

    @Override
    public boolean isWalkable() {
        return true; // Se puede caminar sobre ella (pero te mata si está encendida)
    }

    @Override
    public boolean isCollectable() {
        return isOn; // Si está encendida, "se recolecta" (causa daño)
    }

    @Override
    public int getScore() {
        return -1; // Código de muerte
    }

    @Override
    public boolean isDestructible() {
        return false; // No se destruye con el golpe del jugador
    }

    @Override
    public void update(float dt) {
        if (!isOn) {
            timer += dt;
            if (timer >= REIGNITE_TIME) {
                isOn = true;
                timer = 0;
            }
        }
    }

    @Override
    public void onIceBrokenAbove() {
        if (isOn) {
            isOn = false;
            timer = 0;
        }
    }

    @Override
    public String getType() {
        return isOn ? "CAMPFIRE" : "CAMPFIRE_OFF";
    }
}
