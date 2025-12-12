package presentation;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * Listener para capturar input de teclado durante el juego.
 * Propaga los eventos de teclas presionadas a un Consumer.
 * 
 * <p>Teclas soportadas:</p>
 * <ul>
 *   <li>WASD + Espacio + E - Controles Jugador 1</li>
 *   <li>Flechas + Enter + Shift - Controles Jugador 2</li>
 *   <li>P - Pausar juego</li>
 *   <li>ESC - Volver al menu</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see GamePanel
 */
public class GameKeyListener extends KeyAdapter {
    private Consumer<Integer> onKeyPressed;

    public GameKeyListener(Consumer<Integer> onKeyPressed) {
        this.onKeyPressed = onKeyPressed;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (onKeyPressed != null) {
            onKeyPressed.accept(e.getKeyCode());
        }
    }
}
