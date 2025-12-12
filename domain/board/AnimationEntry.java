package domain.board;

import domain.shared.EntityType;

/**
 * Representa una entrada de animacion para el efecto domino en el tablero.
 * Almacena la posicion, el tipo de entidad objetivo y el retraso de animacion.
 * Se utiliza para crear animaciones secuenciales de creacion o destruccion
 * de bloques de hielo con efecto visual de propagacion.
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see BoardController
 */
public class AnimationEntry implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    /** Coordenada X de la celda a animar */
    int x;
    /** Coordenada Y de la celda a animar */
    int y;
    /** Tipo de entidad objetivo: EMPTY (destruir) o ICE_BLOCK (crear) */
    EntityType targetType;
    /** Retraso en segundos antes de iniciar esta animacion */
    float delay;

    /**
     * Crea una nueva entrada de animacion.
     * 
     * @param x          Coordenada X de la celda
     * @param y          Coordenada Y de la celda
     * @param targetType Tipo de entidad objetivo (EMPTY para destruir, ICE_BLOCK para crear)
     * @param delay      Retraso en segundos antes de iniciar la animacion
     */
    public AnimationEntry(int x, int y, EntityType targetType, float delay) {
        this.x = x;
        this.y = y;
        this.targetType = targetType;
        this.delay = delay;
    }
}
