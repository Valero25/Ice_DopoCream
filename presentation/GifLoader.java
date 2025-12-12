package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Cargador especializado de archivos GIF animados.
 * Permite cargar GIFs desde una carpeta base y escalarlos.
 * 
 * <p>Nota: El escalado de GIFs pierde la animacion,
 * por lo que se recomienda usar solo para imagenes estaticas
 * o cuando la animacion no es necesaria.</p>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see ImageLoader
 */
public class GifLoader {

    private final String carpetaBase;

    public GifLoader(String carpeta) {
        this.carpetaBase = "/presentation/" + carpeta + "/";
    }

    /**
     * Carga un GIF y lo escala al tamaño deseado (pierde animación)
     */
    public ImageIcon getGif(String nombre, int width, int height) {
        java.net.URL url = getClass().getResource(carpetaBase + nombre);
        if (url == null) return null;

        ImageIcon icon = new ImageIcon(url);
        Image imgEscalada = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(imgEscalada);
    }

    // Animaciones comunes
    public ImageIcon getVictoria(int width, int height) {
        return getGif("Victoria animation.gif", width, height);
    }

    public ImageIcon getCaminandoAbajo(int width, int height) {
        return getGif("Caminando Abajo animation.gif", width, height);
    }

    public ImageIcon getPatada(int width, int height) {
        return getGif("Patada animation.gif", width, height);
    }

    public ImageIcon getMuerte(int width, int height) {
        return getGif("Muerte animation.gif", width, height);
    }
}
