package domain.items;

public class Grape extends Fruit {

    public Grape(String id, int x, int y) {
        super(id, x, y, 50); // Uva da 50 puntos
    }

    @Override
    public void update(float dt) {
        // No hace nada porque es estática
    }

    @Override
    public String getType() {
        return "GRAPE";
    }
}