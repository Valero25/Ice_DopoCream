package domain.items;

/**
 * Representa una accion en la cola de efecto domino.
 * Almacena si es una accion de creacion o destruccion de hielo
 * y la posicion donde se ejecutara.
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see ItemController
 */
public class DominoAction implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    boolean isCreate;
    int x, y;

    public DominoAction(boolean isCreate, int x, int y) {
        this.isCreate = isCreate;
        this.x = x;
        this.y = y;
    }
}
