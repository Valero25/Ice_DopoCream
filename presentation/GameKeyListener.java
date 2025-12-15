package presentation;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

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
