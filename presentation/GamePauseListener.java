package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * Listener para el boton de pausa durante el juego.
 * Simula una pulsacion de la tecla P cuando se activa.
 * 
 * <p>Uso: Conectado al boton de pausa en el overlay
 * para permitir pausar con clic ademas de con tecla.</p>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see PauseOverlay
 * @see GamePanel
 */
public class GamePauseListener implements ActionListener {
    private Consumer<Integer> onKeyPressed;

    public GamePauseListener(Consumer<Integer> onKeyPressed) {
        this.onKeyPressed = onKeyPressed;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (onKeyPressed != null) {
            onKeyPressed.accept(KeyEvent.VK_P);
        }
    }
}
