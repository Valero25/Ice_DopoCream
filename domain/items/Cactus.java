package domain.items;

import domain.board.BoardController;

/**
 * Cactus: Fruta especial con puas que alternan entre estado peligroso y seguro.
 * Otorga 250 puntos al ser recolectado cuando no tiene puas.
 * 
 * <p>Comportamiento:</p>
 * <ul>
 *   <li>Alterna entre puas (peligroso) y sin puas (seguro) cada 30 segundos</li>
 *   <li>Cuando tiene puas, elimina al jugador que lo toque</li>
 *   <li>Solo puede ser recolectado cuando NO tiene puas</li>
 *   <li>Inicia sin puas (seguro para recolectar)</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Fruit
 */
public class Cactus extends Fruit {

    private static final float SPIKE_INTERVAL = 30.0f; // 30 segundos entre cambios de estado
    private static final int CACTUS_SCORE = 250;

    private float stateTimer;
    private boolean hasSpikes; // true = tiene púas (peligroso), false = seguro para recolectar

    public Cactus(String id, int x, int y) {
        super(id, x, y, CACTUS_SCORE);
        this.stateTimer = 0;
        this.hasSpikes = false; // Inicia sin púas (seguro)
    }

    @Override
    public void update(float dt) {
        // Acumular tiempo
        stateTimer += dt;

        // Cada 30 segundos, alternar estado de púas
        if (stateTimer >= SPIKE_INTERVAL) {
            hasSpikes = !hasSpikes;
            stateTimer = 0; // Reiniciar timer
        }
    }

    @Override
    public void onPlayerMove(BoardController board, ItemController itemCtrl) {
        // El cactus es estático, no hace nada cuando el jugador se mueve
    }

    /**
     * Verifica si el cactus tiene púas actualmente.
     * Si tiene púas, es peligroso y puede eliminar al jugador.
     * 
     * @return true si tiene púas (peligroso), false si está seguro
     */
    public boolean hasSpikes() {
        return hasSpikes;
    }

    /**
     * Solo se puede recolectar cuando NO tiene púas.
     */
    @Override
    public boolean isCollectable() {
        return !hasSpikes;
    }

    /**
     * Verifica si el cactus es peligroso (tiene púas).
     * 
     * @return true si puede dañar al jugador
     */
    @Override
    public boolean isDangerous() {
        return hasSpikes;
    }

    /**
     * Obtiene el tiempo restante hasta el próximo cambio de estado.
     * 
     * @return segundos restantes
     */
    public float getTimeUntilStateChange() {
        return SPIKE_INTERVAL - stateTimer;
    }

    @Override
    public String getType() {
        // Retorna tipo diferente según si tiene púas o no (para visualización)
        return hasSpikes ? "CACTUS_SPIKES" : "CACTUS";
    }
}
