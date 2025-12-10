package presentation;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class CharacterSelection extends JFrame {

    public enum GameMode { PVP, PVM, MVM }

    private GameMode gameMode;
    private BiConsumer<String, String> onPlayersSelected;
    private Runnable onReadyForLevel; // Callback para abrir LevelSelection

    private Map<String, JLabel> gifLabelsPlayer1 = new HashMap<>();
    private Map<String, GifLoader> loadersPlayer1 = new HashMap<>();

    private Map<String, JLabel> gifLabelsPlayer2 = new HashMap<>();
    private Map<String, GifLoader> loadersPlayer2 = new HashMap<>();

    private JLabel background;

    private final double SCALE_P1 = 1.0;
    private final double SCALE_P2 = 1.0;

    private int playerTurn = 1;
    private String player1Selection;

    public CharacterSelection(GameMode mode,
                              BiConsumer<String, String> onPlayersSelected,
                              Runnable onReadyForLevel) {

        this.gameMode = mode;
        this.onPlayersSelected = onPlayersSelected;
        this.onReadyForLevel = onReadyForLevel;

        // Si es MVM → abrir LevelSelection directo sin crear esta ventana
        if (gameMode == GameMode.MVM) {
            SwingUtilities.invokeLater(() -> {
                onReadyForLevel.run();
                dispose(); // cerrar esta ventana si se creó
            });
            return;
        }

        setTitle("Select Your Character");
        setSize(900, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Background
        ImageIcon imgBackground = new ImageIcon(getClass().getResource("fondoSelection.jpg"));
        background = new JLabel(imgBackground);
        background.setBounds(0, 0, imgBackground.getIconWidth(), imgBackground.getIconHeight());
        setContentPane(background);
        background.setLayout(null);

        // Personajes jugador 1
        loadersPlayer1.put("Chocolate", new GifLoader("Chocolate"));
        loadersPlayer1.put("Vainilla", new GifLoader("Vainilla"));
        loadersPlayer1.put("Fresa", new GifLoader("Fresa"));

        createPlayer1Gif("Chocolate", 290, 300);
        createPlayer1Gif("Vainilla", 400, 300);
        createPlayer1Gif("Fresa", 520, 300);

        setVisible(true);
    }

    private void createPlayer1Gif(String name, int x, int y) {

        int width = (int) (100 * SCALE_P1);
        int height = (int) (100 * SCALE_P1);

        JLabel gif = new JLabel(loadersPlayer1.get(name).getCaminandoAbajo(width, height));
        gif.setBounds(x, y, width, height);
        gifLabelsPlayer1.put(name, gif);
        background.add(gif);

        gif.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                gif.setIcon(loadersPlayer1.get(name).getVictoria(width, height));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                gif.setIcon(loadersPlayer1.get(name).getCaminandoAbajo(width, height));
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                player1Selection = name;
                selectPlayer1(name);

                if (gameMode == GameMode.PVM) {

                    // Máquina selecciona personaje automático después de 2 segundos
                    new javax.swing.Timer(2000, evt -> {

                        String machineChoice = "Troll";

                        selectPlayer2(machineChoice);
                        onPlayersSelected.accept(player1Selection, machineChoice);

                        // Después de seleccionar personajes → abrir niveles y cerrar esta ventana
                        new javax.swing.Timer(2000, e2 -> {
                            onReadyForLevel.run();
                            dispose();
                        }) {{
                            setRepeats(false);
                            start();
                        }};

                    }) {{
                        setRepeats(false);
                        start();
                    }};
                }

                if (gameMode == GameMode.PVP) {

                    // Pasar a jugador 2 después de 2 segundos
                    new javax.swing.Timer(2000, evt -> {
                        playerTurn = 2;
                        loadPlayer2Characters();
                    }) {{
                        setRepeats(false);
                        start();
                    }};
                }
            }
        });
    }

    private void selectPlayer1(String chosen) {

        for (String name : gifLabelsPlayer1.keySet()) {
            JLabel gif = gifLabelsPlayer1.get(name);
            if (name.equals(chosen)) {
                gif.setIcon(loadersPlayer1.get(name).getPatada(100, 100));
            } else {
                gif.setIcon(loadersPlayer1.get(name).getMuerte(100, 100));
            }
        }
    }

    private void loadPlayer2Characters() {

        for (JLabel l : gifLabelsPlayer1.values())
            background.remove(l);

        background.repaint();

        loadersPlayer2.put("Troll", new GifLoader("Troll"));
        loadersPlayer2.put("Calamar Amarillo", new GifLoader("Calamar Amarillo"));

        createPlayer2Gif("Troll", 300, 300);
        createPlayer2Gif("Calamar Amarillo", 500, 300);
    }

    private void createPlayer2Gif(String name, int x, int y) {

        int width = (int) (100 * SCALE_P2);
        int height = (int) (100 * SCALE_P2);

        JLabel gif;

        if (name.equals("Troll"))
            gif = new JLabel(loadersPlayer2.get(name).getGif("Caminar Abajo animation.gif", width, height));
        else
            gif = new JLabel(loadersPlayer2.get(name).getGif("Caminando Abajo animation.gif", width, height));

        gif.setBounds(x, y, width, height);
        gifLabelsPlayer2.put(name, gif);
        background.add(gif);
        background.repaint();

        gif.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (name.equals("Troll"))
                    gif.setIcon(loadersPlayer2.get(name).getGif("Perdido animation.gif", width, height));
                else
                    gif.setIcon(loadersPlayer2.get(name).getGif("Ataque Abajo animation.gif", width, height));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (name.equals("Troll"))
                    gif.setIcon(loadersPlayer2.get(name).getGif("Caminar Abajo animation.gif", width, height));
                else
                    gif.setIcon(loadersPlayer2.get(name).getGif("Caminando Abajo animation.gif", width, height));
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                selectPlayer2(name);
                onPlayersSelected.accept(player1Selection, name);

                // Después de seleccionar P2 → esperar 2 segundos → abrir niveles y cerrar esta ventana
                new javax.swing.Timer(2000, e2 -> {
                    onReadyForLevel.run();
                    dispose();
                }) {{
                    setRepeats(false);
                    start();
                }};
            }
        });
    }

    private void selectPlayer2(String chosen) {

        for (String name : gifLabelsPlayer2.keySet()) {
            JLabel gif = gifLabelsPlayer2.get(name);
            int width = gif.getWidth();
            int height = gif.getHeight();

            if (name.equals("Troll"))
                gif.setIcon(loadersPlayer2.get(name).getGif("Perdido animation.gif", width, height));
            else
                gif.setIcon(loadersPlayer2.get(name).getGif("Ataque Abajo animation.gif", width, height));
        }
    }
}
