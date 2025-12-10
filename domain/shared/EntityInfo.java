package domain.shared;

public class EntityInfo implements java.io.Serializable {
    // Datos inmutables o simples para que la vista dibuje
    public final String id;
    public final int x;
    public final int y;
    public final String type; // "TROLL", "BANANA", "PLAYER", "WALL"
    public final boolean isDestructible; // Para saber si pintar hielo azul o piedra gris

    public EntityInfo(String id, int x, int y, String type, boolean isDestructible) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.type = type;
        this.isDestructible = isDestructible;
    }
}