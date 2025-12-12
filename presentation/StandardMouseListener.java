package presentation;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Listener unificado para manejar interacciones de mouse estandar.
 * Soporta efectos hover (color, borde, fuente) y clics simples.
 * Reemplaza multiples clases pequenas de Listeners.
 * 
 * <p>Efectos hover soportados:</p>
 * <ul>
 *   <li>Cambio de color de fondo</li>
 *   <li>Cambio de color de texto</li>
 *   <li>Cambio de borde</li>
 *   <li>Cambio de fuente</li>
 * </ul>
 * 
 * <p>Uso tipico:</p>
 * <pre>
 * new StandardMouseListener.Builder(button)
 *     .hoverBackground(Color.BLUE, Color.CYAN)
 *     .onClick(() -> doAction())
 *     .build();
 * </pre>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 */
public class StandardMouseListener extends MouseAdapter {

    private JComponent component;
    private Runnable onClick;

    // Hover Colors
    private Color normalBg, hoverBg;
    private Color normalFg, hoverFg;

    // Hover Font
    private Font normalFont, hoverFont;

    // Hover Border
    private Border normalBorder;
    private Border hoverBorder;
    private boolean useBorderEffect;

    /**
     * Constructor privado para uso de fábrica.
     */
    private StandardMouseListener(JComponent component) {
        this.component = component;
    }

    // --- FACTORY METHODS ---

    /**
     * Crea un listener para ejecutar una acción al hacer clic.
     */
    public static StandardMouseListener onClick(JComponent c, Runnable action) {
        StandardMouseListener l = new StandardMouseListener(c);
        l.onClick = action;
        return l;
    }

    /**
     * Crea un listener para cambio de color de fondo y texto al hacer hover.
     */
    public static StandardMouseListener onHoverColor(JComponent c, Color nBg, Color hBg, Color nFg, Color hFg) {
        StandardMouseListener l = new StandardMouseListener(c);
        l.normalBg = nBg;
        l.hoverBg = hBg;
        l.normalFg = nFg;
        l.hoverFg = hFg;
        // Asignar colores iniciales
        if (nBg != null)
            c.setBackground(nBg);
        if (nFg != null)
            c.setForeground(nFg);
        return l;
    }

    /**
     * Crea un listener para cambio de fondo al hacer hover.
     */
    public static StandardMouseListener onHoverBg(JComponent c, Color nBg, Color hBg) {
        return onHoverColor(c, nBg, hBg, null, null);
    }

    /**
     * Crea un listener para cambio de texto (foreground) al hacer hover.
     */
    public static StandardMouseListener onHoverFg(JComponent c, Color nFg, Color hFg) {
        return onHoverColor(c, null, null, nFg, hFg);
    }

    /**
     * Configura el cambio de fuente al hacer hover (Chainable).
     */
    public StandardMouseListener withFontEffect(Font nFont, Font hFont) {
        this.normalFont = nFont;
        this.hoverFont = hFont;
        return this;
    }

    /**
     * Configura borde al hacer hover (Chainable).
     */
    public StandardMouseListener withBorderEffect(Color borderColor, int thickness) {
        this.useBorderEffect = true;
        this.hoverBorder = BorderFactory.createLineBorder(borderColor, thickness);
        this.normalBorder = component.getBorder(); // Guardar original
        if (component instanceof AbstractButton) {
            ((AbstractButton) component).setBorderPainted(false);
        }
        return this;
    }

    // --- EVENTS ---

    @Override
    public void mouseClicked(MouseEvent e) {
        if (onClick != null) {
            onClick.run();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (hoverBg != null)
            component.setBackground(hoverBg);
        if (hoverFg != null)
            component.setForeground(hoverFg);
        if (hoverFont != null)
            component.setFont(hoverFont);

        if (useBorderEffect) {
            if (component instanceof AbstractButton)
                ((AbstractButton) component).setBorderPainted(true);
            component.setBorder(hoverBorder);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (normalBg != null)
            component.setBackground(normalBg);
        if (normalFg != null)
            component.setForeground(normalFg);
        if (normalFont != null)
            component.setFont(normalFont);

        if (useBorderEffect) {
            if (component instanceof AbstractButton)
                ((AbstractButton) component).setBorderPainted(false);
            component.setBorder(normalBorder);
        }
    }
}
