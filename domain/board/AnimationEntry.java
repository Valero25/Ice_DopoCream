package domain.board;

import domain.shared.EntityType;

public class AnimationEntry implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    int x, y;
    EntityType targetType; // EMPTY (destruir) o ICE_BLOCK (crear)
    float delay;

    public AnimationEntry(int x, int y, EntityType targetType, float delay) {
        this.x = x;
        this.y = y;
        this.targetType = targetType;
        this.delay = delay;
    }
}
