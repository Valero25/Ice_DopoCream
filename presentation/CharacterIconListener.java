package presentation;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class CharacterIconListener extends MouseAdapter {
    private JLabel lbl;
    private ImageIcon idle;
    private ImageIcon hover;
    private ImageIcon click;
    private String name;
    private Consumer<String> handler;

    public CharacterIconListener(JLabel lbl, ImageIcon idle, ImageIcon hover, ImageIcon click, String name,
            Consumer<String> handler) {
        this.lbl = lbl;
        this.idle = idle;
        this.hover = hover;
        this.click = click;
        this.name = name;
        this.handler = handler;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (hover != null)
            lbl.setIcon(hover);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        lbl.setIcon(idle);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (click != null)
            lbl.setIcon(click);
        Timer t = new Timer(300, evt -> handler.accept(name));
        t.setRepeats(false);
        t.start();
    }
}
