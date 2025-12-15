package domain.items;

public class Banana extends Fruit {

    public Banana(String id, int x, int y) {
        super(id, x, y, 100); // Plátano da 100 puntos
    }

    @Override
    public void update(float dt) {
        // No hace nada porque es estática
    }

    @Override
    public String getType() {
        return "BANANA";
    }
}