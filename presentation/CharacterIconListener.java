package presentation;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * Listener para iconos de personajes con estados visuales.
 * Maneja los efectos hover y click en los iconos de seleccion de personaje.
 * 
 * <p>Estados visuales:</p>
 * <ul>
 *   <li>idle - Estado normal del icono</li>
 *   <li>hover - Cuando el cursor esta sobre el icono</li>
 *   <li>click - Cuando se hace clic en el icono</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see CharacterSelectionPanel
 */
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
