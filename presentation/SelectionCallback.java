package presentation;

/**
 * Interfaz callback para la seleccion de personajes.
 * Notifica cuando ambos jugadores han completado su seleccion.
 * 
 * <p>Parametros del callback:</p>
 * <ul>
 *   <li>p1Flavor - Sabor seleccionado por jugador 1</li>
 *   <li>p1Diff - Dificultad del bot si es IA (null si es humano)</li>
 *   <li>p2Flavor - Sabor seleccionado por jugador 2</li>
 *   <li>p2Diff - Dificultad del bot si es IA (null si es humano)</li>
 *   <li>p1Name - Nombre del jugador 1</li>
 *   <li>p2Name - Nombre del jugador 2</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see CharacterSelectionPanel
 */
public interface SelectionCallback {
    void onSelectionComplete(String p1Flavor, String p1Diff, String p2Flavor, String p2Diff, String p1Name,
            String p2Name);
}
