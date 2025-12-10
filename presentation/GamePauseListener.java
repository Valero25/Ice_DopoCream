package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

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
