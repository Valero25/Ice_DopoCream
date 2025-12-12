package domain.items;

/**
 * Fruta Banana estatica que otorga 100 puntos al ser recolectada.
 * Es la fruta basica del juego, no tiene comportamiento especial.
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Fruit
 */
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