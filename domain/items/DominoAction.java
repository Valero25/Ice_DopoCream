package domain.items;

public class DominoAction implements java.io.Serializable {
    boolean isCreate;
    int x, y;

    public DominoAction(boolean isCreate, int x, int y) {
        this.isCreate = isCreate;
        this.x = x;
        this.y = y;
    }
}
