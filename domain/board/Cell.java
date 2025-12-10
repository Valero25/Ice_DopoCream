package domain.board;

import domain.shared.EntityType;

import java.io.Serializable;

class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private EntityType content; // WALL, ICE_BLOCK, EMPTY
    private float animationProgress; // 0.0 a 1.0 para efecto dominó
    private boolean isAnimating;

    Cell(EntityType content) {
        this.content = content;
        this.animationProgress = 1.0f; // Completamente visible por defecto
        this.isAnimating = false;
    }

    void setContent(EntityType content) {
        this.content = content;
    }

    EntityType getContent() {
        return content;
    }

    // Un obstáculo solido por el cual no pueden pasar [cite: 58]
    boolean isWalkable() {
        return content == EntityType.EMPTY;
    }

    float getAnimationProgress() {
        return animationProgress;
    }

    void setAnimationProgress(float progress) {
        this.animationProgress = Math.max(0.0f, Math.min(1.0f, progress));
    }

    boolean isAnimating() {
        return isAnimating;
    }

    void setAnimating(boolean animating) {
        this.isAnimating = animating;
    }
}