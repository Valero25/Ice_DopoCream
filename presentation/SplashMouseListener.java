package presentation;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SplashMouseListener extends MouseAdapter {
    private Runnable onClick;

    public SplashMouseListener(Runnable onClick) {
        this.onClick = onClick;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
