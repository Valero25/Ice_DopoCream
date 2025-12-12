package presentation;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Cargador y cache de imagenes para la interfaz grafica.
 * Proporciona acceso centralizado a todas las imagenes del juego.
 * 
 * <p>Tipos de recursos gestionados:</p>
 * <ul>
 *   <li>Fondos de pantalla para cada estado del juego</li>
 *   <li>Sprites de jugadores (por sabor y direccion)</li>
 *   <li>Sprites de enemigos (por tipo y direccion)</li>
 *   <li>Imagenes de frutas y obstaculos</li>
 *   <li>Iconos y elementos de UI</li>
 * </ul>
 * 
 * <p>Optimizaciones:</p>
 * <ul>
 *   <li>Cache de imagenes para evitar cargas repetidas</li>
 *   <li>Escalado automatico de sprites</li>
 *   <li>Soporte para ImageIcon y Image</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 */
public class ImageLoader {

    private Map<String, Image> cache;
    private Map<String, ImageIcon> iconCache;

    public ImageLoader() {
        this.cache = new HashMap<>();
        this.iconCache = new HashMap<>();
    }

    // --- MÉTODOS PÚBLICOS ---

    public Image getImage(String type, int width, int height) {
        String key = type + "_" + width + "_" + height;
        if (!cache.containsKey(key)) {
            Image rawImg = loadRawImage(type);
            if (rawImg != null) {
                // Usamos SCALE_DEFAULT para respetar animaciones GIF si es posible
                Image scaled = rawImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
                cache.put(key, scaled);
            } else {
                return null;
            }
        }
        return cache.get(key);
    }

    public void clearCache() {
        cache.clear();
    }

    public Image getBackgroundImage(String name) {
        String path = "";

        // AQUÍ ESTÁN LAS CORRECCIONES DE NOMBRES
        switch (name) {
            case "SPLASH":
                // Tu código original usaba Fondo.gif para el Splash
                path = "/presentation/Fondo.gif";
                break;

            case "HOME":
                path = "/presentation/Fondo.gif";
                break;

            case "MODE":
                // En tu código anterior era un GIF
                path = "/presentation/FondoModo.gif";
                break;

            case "SELECT":
                path = "/presentation/fondoSelection.jpg";
                break;

            case "LEVEL":
                path = "/presentation/fondoNivel.png";
                break;

            case "MENU_BOARD":
                path = "/presentation/menu.png";
                break;

            case "LEVEL_1":
                path = "/presentation/fondogeneralnivel.png";
                break;

            case "LEVEL_2":
                path = "/presentation/fondogeneralnivel.png";
                break;

            case "LEVEL_3":
                path = "/presentation/fondogeneralnivel.png";
                break;

            default:
                return null;
        }
        return loadStartImage(path);
    }

    public ImageIcon getIcon(String characterName, String state) {
        String path = getCharacterPath(characterName, state);
        if (path == null)
            return null;

        if (!iconCache.containsKey(path)) {
            URL url = getClass().getResource(path);
            if (url != null)
                iconCache.put(path, new ImageIcon(url));
        }
        return iconCache.get(path);
    }

    public String getPath(String key, String sub) {
        // Helper para ModeSelectionPanel y LevelSelectionPanel
        if (key.equals("BTN_SINGLE"))
            return "/presentation/Boton S.png";
        if (key.equals("BTN_PVP"))
            return "/presentation/BotonJvJ.gif";
        if (key.equals("BTN_PVM"))
            return "/presentation/BotonMvJ.gif";
        if (key.equals("BTN_MVM"))
            return "/presentation/BotonMvM.gif";

        if (key.equals("LEVEL_1"))
            return "/presentation/Nivel1.gif";
        if (key.equals("LEVEL_2"))
            return "/presentation/Nivel2.gif";
        if (key.equals("LEVEL_3"))
            return "/presentation/Nivel3.gif";
        if (key.equals("LEVEL_4"))
            return "/presentation/Nivel0.gif"; // Nivel 4 a veces es Nivel0 en tus archivos

        return null;
    }

    // --- LÓGICA INTERNA ---

    private Image loadRawImage(String type) {
        String path = null;
        switch (type) {
            case "PLAYER_CHOCOLATE":
                path = "/presentation/Chocolate/Caminando Abajo animation.gif";
                break;
            case "PLAYER_VANILLA":
                path = "/presentation/Vainilla/Caminando Abajo animation.gif";
                break;
            case "PLAYER_STRAWBERRY":
                path = "/presentation/Fresa/Caminando Abajo animation.gif";
                break;

            case "TROLL":
                path = "/presentation/Troll/Caminar Abajo animation.gif";
                break;
            case "SQUID":
                path = "/presentation/Calamar Amarillo/Caminando Abajo animation.gif";
                break;
            case "FLOWERPOT":
                path = "/presentation/Maceta/movimientomaceta.gif";
                break; // Verifica el nombre exacto
            case "NARWHAL":
                path = "/presentation/Narval/DownWalk.gif"; // Caminar normal
                break;
            case "NARWHAL_DASH":
                path = "/presentation/Narval/DownDrill2.gif"; // Embestida
                break;

            case "BANANA":
                path = "/presentation/frutas/platanos.gif";
                break;
            case "GRAPE":
                path = "/presentation/frutas/uva.gif";
                break;
            case "PINEAPPLE":
                path = "/presentation/frutas/piña.gif";
                break;
            case "CHERRY":
                path = "/presentation/frutas/cerezas.gif";
                break;
            case "CACTUS":
                path = "/presentation/frutas/Cactus.gif";
                break;
            case "CACTUS_SPIKES":
                path = "/presentation/frutas/Cactusconespinas.gif";
                break;

            case "ICE":
                path = "/presentation/hielo.png";
                break;

            case "CAMPFIRE":
                path = "/presentation/fogata.png";
                break;
            case "CAMPFIRE_OFF":
                path = "/presentation/fogataazul.png";
                break;

            case "HOT_TILE":
                path = "/presentation/caliente.png";
                break;

            default:
                return null;
        }
        return loadStartImage(path);
    }

    private String getCharacterPath(String name, String state) {
        String folder = "";
        String file = "";

        switch (name) {
            case "CHOCOLATE":
                folder = "Chocolate";
                break;
            case "VANILLA":
                folder = "Vainilla";
                break;
            case "STRAWBERRY":
                folder = "Fresa";
                break;

            case "TROLL":
                folder = "Troll";
                break;
            case "SQUID":
                folder = "Calamar Amarillo";
                break;
            case "FLOWERPOT":
                folder = "Maceta";
                break;
            case "NARWHAL":
                folder = "Narval";
                break;
            default:
                return null;
        }

        switch (state) {
            case "WALK":
                if (name.equals("FLOWERPOT"))
                    file = "movimientomaceta.gif";
                else if (name.equals("NARWHAL"))
                    file = "DownWalk.gif";
                else if (name.equals("TROLL"))
                    file = "Caminar Abajo animation.gif"; // "Caminar" vs "Caminando"
                else
                    file = "Caminando Abajo animation.gif";
                break;

            case "HOVER": // Estado Victoria
                file = "Victoria animation.gif";
                if (name.equals("TROLL"))
                    file = "Perdido animation.gif";
                else if (name.equals("NARWHAL"))
                    file = "DownBreak.gif"; // Hover en menú
                break;

            case "SELECT": // Estado Ataque
                if (name.equals("TROLL") || name.equals("SQUID"))
                    file = "Ataque Abajo animation.gif";
                else if (name.equals("NARWHAL"))
                    file = "DownDrill3.gif"; // Selección en menú
                else
                    file = "Patada animation.gif";
                break;
        }
        return "/presentation/" + folder + "/" + file;
    }

    private Image loadStartImage(String path) {
        if (path == null)
            return null;
        URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("❌ ERROR: No se encontró la imagen: " + path);
            return null;
        }
        return new ImageIcon(url).getImage();
    }
}