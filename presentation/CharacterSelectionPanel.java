package presentation;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.List;

/**
 * Panel para la seleccion de personajes (sabores de helado).
 * Permite a los jugadores elegir su sabor antes de iniciar la partida.
 * 
 * <p>Sabores disponibles:</p>
 * <ul>
 *   <li>VANILLA - Vainilla</li>
 *   <li>CHOCOLATE - Chocolate</li>
 *   <li>STRAWBERRY - Fresa</li>
 * </ul>
 * 
 * <p>Caracteristicas:</p>
 * <ul>
 *   <li>Muestra iconos de cada sabor con hover effects</li>
 *   <li>Soporta seleccion para uno o dos jugadores</li>
 *   <li>Diferencia entre jugadores humanos y bots</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see SelectionCallback
 */
public class CharacterSelectionPanel extends JPanel {
    private ImageLoader loader;
    private Image bgImg;
    private String mode;

    // Callbacks para notificar a BadOpoGUI
    // onComplete: (p1Flavor, p1Diff, p2Flavor, p2Diff)
    // Usaremos un listener custom o una interfaz simple
    private SelectionCallback callback;

    // Estado de selección
    private String p1Flavor = null;
    private String p1Difficulty = null;
    private String p2Flavor = null;
    private String p2Difficulty = null;
    private String p1Name = null;
    private String p2Name = null;

    private JLabel titleLabel;
    private JPanel charactersPanel;

    public CharacterSelectionPanel(ImageLoader loader, String mode, SelectionCallback callback) {
        this.loader = loader;
        this.mode = mode;
        this.callback = callback;
        this.bgImg = loader.getBackgroundImage("SELECT");

        setLayout(new BorderLayout());

        titleLabel = new JLabel("PLAYER 1 SELECT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(titleLabel, BorderLayout.NORTH);

        charactersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 100));
        charactersPanel.setOpaque(false);
        add(charactersPanel, BorderLayout.CENTER);

        // Iniciar flujo
        startSelectionFlow();
    }

    private void startSelectionFlow() {
        // Paso 1: P1 Flavor (Siempre se pide)
        titleLabel.setText("PLAYER 1: CHOOSE FLAVOR");
        showOptions(Arrays.asList("CHOCOLATE", "VANILLA", "STRAWBERRY"), true, this::handleP1Flavor);
    }

    private void handleP1Flavor(String selection) {
        p1Flavor = selection;

        // Pedir nombre para P1
        if (mode.equals("MVM")) {
            // En MVM, P1 es máquina, pedir nombre
            showNameInput("PLAYER 1 (Machine): ENTER NAME", this::handleP1Name);
        } else {
            // P1 es humano
            showNameInput("PLAYER 1: ENTER YOUR NAME", this::handleP1Name);
        }
    }

    private void handleP1Name(String name) {
        p1Name = name;

        if (mode.equals("MVM")) {
            // Paso 2: P1 Difficulty
            titleLabel.setText("PLAYER 1: CHOOSE DIFFICULTY");
            showOptions(Arrays.asList("HUNGRY", "FEARFUL", "EXPERT"), false, this::handleP1Diff);
        } else {
            // SINGLE, PVP, PVM (P1 es humano, no hay dificultad)
            checkNextStep();
        }
    }

    private void handleP1Diff(String selection) {
        p1Difficulty = "MACHINE_" + selection; // Convertir a enum name
        checkNextStep();
    }

    private void checkNextStep() {
        if (mode.equals("SINGLE")) {
            finish();
            return;
        }

        // Si ya elegimos P2, terminamos
        if (p2Flavor != null && (p2Difficulty != null || mode.equals("PVP"))) {
            finish();
            return;
        }

        // Si no, toca elegir P2
        if (p2Flavor == null) {
            if (mode.equals("PVM")) {
                // En PVM, la máquina es un helado, no un enemigo
                titleLabel.setText("MACHINE: CHOOSE FLAVOR");
                showOptions(Arrays.asList("CHOCOLATE", "VANILLA", "STRAWBERRY"), true, this::handleP2Flavor);
            } else {
                titleLabel.setText("PLAYER 2: CHOOSE FLAVOR");
                showOptions(Arrays.asList("CHOCOLATE", "VANILLA", "STRAWBERRY"), true, this::handleP2Flavor);
            }
        }
    }

    private void handleP2Flavor(String selection) {
        p2Flavor = selection;

        // Pedir nombre para P2
        if (mode.equals("PVM")) {
            showNameInput("MACHINE: ENTER NAME", this::handleP2Name);
        } else if (mode.equals("MVM")) {
            showNameInput("PLAYER 2 (Machine): ENTER NAME", this::handleP2Name);
        } else {
            // PVP
            showNameInput("PLAYER 2: ENTER YOUR NAME", this::handleP2Name);
        }
    }

    private void handleP2Name(String name) {
        p2Name = name;

        if (mode.equals("MVM") || mode.equals("PVM")) {
            // En MVM y PVM, la máquina necesita elegir dificultad
            titleLabel.setText(mode.equals("PVM") ? "MACHINE: CHOOSE DIFFICULTY" : "PLAYER 2: CHOOSE DIFFICULTY");
            showOptions(Arrays.asList("HUNGRY", "FEARFUL", "EXPERT"), false, this::handleP2Diff);
        } else {
            finish();
        }
    }

    private void handleP2Diff(String selection) {
        p2Difficulty = "MACHINE_" + selection;
        finish();
    }

    private void finish() {
        callback.onSelectionComplete(p1Flavor, p1Difficulty, p2Flavor, p2Difficulty, p1Name, p2Name);
    }

    private void showNameInput(String promptText, Consumer<String> handler) {
        charactersPanel.removeAll();
        titleLabel.setText(promptText);

        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JTextField nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 24));
        nameField.setMaximumSize(new Dimension(300, 50));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setHorizontalAlignment(JTextField.CENTER);

        JButton confirmBtn = new JButton("CONFIRM");
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 20));
        confirmBtn.setPreferredSize(new Dimension(200, 50));
        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                name = "Player";
            }
            handler.accept(name);
        });

        // También permitir Enter para confirmar
        nameField.addActionListener(e -> confirmBtn.doClick());

        inputPanel.add(Box.createVerticalStrut(50));
        inputPanel.add(nameField);
        inputPanel.add(Box.createVerticalStrut(20));
        inputPanel.add(confirmBtn);

        charactersPanel.add(inputPanel);
        revalidate();
        repaint();

        // Dar foco al campo de texto
        SwingUtilities.invokeLater(() -> nameField.requestFocus());
    }

    // --- GUI HELPERS ---

    private void showOptions(List<String> options, boolean isIceCream, Consumer<String> handler) {
        charactersPanel.removeAll();

        for (String opt : options) {
            // Si es dificultad, usamos un icono genérico o texto,
            // pero por simplicidad reusaremos iconos si existen o texto.
            // Asumiremos que para HUNGRY/FEARFUL/EXPERT no hay iconos aun,
            // asi que usaremos botones de texto o iconos de helado genéricos.
            // Para mantenerlo visual, usaremos iconos de helado (Vainilla) para dificultad
            // por ahora
            // O mejor, botones con texto si no hay assets.

            // INTENTO: Cargar icono. Si falla, mostrar texto.
            // Pero como no tengo assets de dificultad, usaré iconos de helado para
            // representar dificultad?
            // No, mejor botones de texto simples para dificultad si no es IceCream/Enemy.

            boolean useIcon = isIceCream || isEnemy(opt);

            if (useIcon) {
                addIconBtn(opt, handler);
            } else {
                addTextBtn(opt, handler);
            }
        }
        revalidate();
        repaint();
    }

    private boolean isEnemy(String s) {
        return Arrays.asList("TROLL", "SQUID", "FLOWERPOT", "NARWHAL").contains(s);
    }

    private void addIconBtn(String name, Consumer<String> handler) {
        ImageIcon idle = loader.getIcon(name, "WALK");
        ImageIcon hover = loader.getIcon(name, "HOVER");
        ImageIcon click = loader.getIcon(name, "SELECT");

        JLabel lbl = new JLabel(idle);
        lbl.setPreferredSize(new Dimension(150, 150));

        lbl.addMouseListener(new CharacterIconListener(lbl, idle, hover, click, name, handler));

        charactersPanel.add(lbl);
    }

    private void addTextBtn(String text, Consumer<String> handler) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(200, 80));
        btn.addActionListener(e -> handler.accept(text));
        charactersPanel.add(btn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
    }
}