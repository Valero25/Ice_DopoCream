package domain.items;

/**
 * Fruta Uva estatica que otorga 50 puntos al ser recolectada.
 * Es la fruta de menor valor, ideal para niveles iniciales.
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Fruit
 */
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