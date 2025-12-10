package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorHoverListener extends MouseAdapter {
    private JComponent component;
    private Color normalColor;
    private Color hoverColor;
    private boolean isForeground;
    private Font normalFont;
    private Font hoverFont;

    /**
     * Constructor para solo cambios de color.
     */
    public ColorHoverListener(JComponent component, Color normalColor, Color hoverColor, boolean isForeground) {
        this(component, normalColor, hoverColor, isForeground, null, null);
    }

    /**
     * Constructor para cambios de color y fuente.
     */
    public ColorHoverListener(JComponent component, Color normalColor, Color hoverColor, boolean isForeground,
            Font normalFont, Font hoverFont) {
        this.component = component;
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.isForeground = isForeground;
        this.normalFont = normalFont;
        this.hoverFont = hoverFont;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (isForeground) {
            component.setForeground(hoverColor);
        } else {
            component.setBackground(hoverColor);
        }
        if (hoverFont != null) {
            component.setFont(hoverFont);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (isForeground) {
            component.setForeground(normalColor);
        } else {
            component.setBackground(normalColor);
        }
        if (normalFont != null) {
            component.setFont(normalFont);
        }
    }
}
