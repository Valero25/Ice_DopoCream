package domain.shared;

/**
 * DTO (Data Transfer Object) que contiene informacion visual de una entidad.
 * Utilizado para transferir datos del dominio a la capa de presentacion
 * sin exponer objetos internos del dominio.
 * 
 * <p>Informacion incluida:</p>
 * <ul>
 *   <li>ID unico de la entidad</li>
 *   <li>Posicion (x, y) en el tablero</li>
 *   <li>Tipo de entidad (determina el sprite a usar)</li>
 *   <li>Si es destructible (para efectos visuales)</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 */
public class EntityInfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    /** Identificador unico de la entidad */
    public final String id;
    /** Coordenada X en el tablero */
    public final int x;
    /** Coordenada Y en el tablero */
    public final int y;
    /** Tipo de entidad (ej: "TROLL", "BANANA", "PLAYER_VANILLA") */
    public final String type;
    /** Indica si la entidad puede ser destruida */
    public final boolean isDestructible;

    public EntityInfo(String id, int x, int y, String type, boolean isDestructible) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.type = type;
        this.isDestructible = isDestructible;
    }
}
