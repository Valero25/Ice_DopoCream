package presentation;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.Arrays;
import java.util.List;

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

        charactersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 200));
        charactersPanel.setOpaque(false);
        add(charactersPanel, BorderLayout.CENTER);

        // Iniciar flujo
        startSelectionFlow();
    }

    private void startSelectionFlow() {
        // Paso 1: Nombre + Helado en la misma pantalla
        if (mode.equals("MVM")) {
            showNameAndFlavor("PLAYER 1 (Machine): ENTER NAME", this::handleP1Complete);
        } else {
            showNameAndFlavor("PLAYER 1: ENTER NAME", this::handleP1Complete);
        }
    }

    private void handleP1Complete(String name, String flavor) {
        p1Name = name;
        p1Flavor = flavor;

        if (mode.equals("MVM")) {
            // P1 es máquina, necesita elegir dificultad
            titleLabel.setText(p1Name + ": CHOOSE DIFFICULTY");
            showOptions(Arrays.asList("HUNGRY", "FEARFUL", "EXPERT"), false, this::handleP1Diff);
        } else {
            // SINGLE, PVP, PVM - P1 es humano, pasar al siguiente paso
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

        // Si no, toca elegir P2 (nombre + helado en la misma pantalla)
        if (p2Flavor == null) {
            if (mode.equals("PVM")) {
                showNameAndFlavor("MACHINE: ENTER NAME", this::handleP2Complete);
            } else if (mode.equals("MVM")) {
                showNameAndFlavor("PLAYER 2 (Machine): ENTER NAME", this::handleP2Complete);
            } else {
                // PVP
                showNameAndFlavor("PLAYER 2: ENTER NAME", this::handleP2Complete);
            }
        }
    }

    private void handleP2Complete(String name, String flavor) {
        p2Name = name;
        p2Flavor = flavor;

        if (mode.equals("MVM") || mode.equals("PVM")) {
            // La máquina necesita elegir dificultad
            titleLabel.setText(p2Name + ": CHOOSE DIFFICULTY");
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

    // Variable temporal para guardar el nombre mientras se elige helado
    private String tempName = "";
    private javax.swing.Timer blinkTimer;

    private void showNameAndFlavor(String promptText, BiConsumer<String, String> handler) {
        charactersPanel.removeAll();
        titleLabel.setText(promptText);

        // Resetear nombre temporal para este jugador
        tempName = "";

        // Panel principal con layout vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Label grande para mostrar las letras "volando"
        JLabel flyingLabel = new JLabel("_");
        flyingLabel.setFont(new Font("Monospaced", Font.BOLD, 50));
        flyingLabel.setForeground(new Color(101, 67, 33));
        flyingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo de texto oculto para capturar teclas
        JTextField hiddenField = new JTextField(15);
        hiddenField.setOpaque(false);
        hiddenField.setBorder(null);
        hiddenField.setForeground(new Color(0, 0, 0, 0));
        hiddenField.setCaretColor(new Color(0, 0, 0, 0));
        hiddenField.setFont(new Font("Arial", Font.PLAIN, 1));
        hiddenField.setMaximumSize(new Dimension(1, 1));

        // Timer para efecto de parpadeo del cursor
        if (blinkTimer != null)
            blinkTimer.stop();
        blinkTimer = new javax.swing.Timer(500, null);
        final boolean[] showCursor = { true };

        blinkTimer.addActionListener(e -> {
            showCursor[0] = !showCursor[0];
            String text = hiddenField.getText();
            flyingLabel.setText(text.isEmpty() ? "_" : text + (showCursor[0] ? "_" : ""));
        });
        blinkTimer.start();

        // Panel para los helados
        JPanel iceCreamPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        iceCreamPanel.setOpaque(false);

        // Lista para poder cambiar los otros helados cuando se selecciona uno
        java.util.List<JLabel> allLabels = new java.util.ArrayList<>();
        java.util.Map<JLabel, ImageIcon> deathIcons = new java.util.HashMap<>();

        // Crear botones de helado
        for (String flavor : Arrays.asList("CHOCOLATE", "VANILLA", "STRAWBERRY")) {
            ImageIcon idle = loader.getIcon(flavor, "WALK");
            ImageIcon hover = loader.getIcon(flavor, "HOVER");
            ImageIcon click = loader.getIcon(flavor, "SELECT");
            ImageIcon death = loader.getIcon(flavor, "DEATH");

            int size = 110;
            ImageIcon scaledIdle = scaleIcon(idle, size, size);
            ImageIcon scaledHover = scaleIcon(hover, size, size);
            ImageIcon scaledClick = scaleIcon(click, size, size);
            ImageIcon scaledDeath = scaleIcon(death, size, size);

            JLabel lbl = new JLabel(scaledIdle);
            lbl.setPreferredSize(new Dimension(size, size));
            lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Guardar icono de muerte para este label
            deathIcons.put(lbl, scaledDeath);
            allLabels.add(lbl);

            // Al hacer click, guardar nombre + helado (solo si hay nombre)
            lbl.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (tempName.trim().isEmpty())
                        return; // No permitir si no hay nombre
                    if (blinkTimer != null)
                        blinkTimer.stop();

                    // Mostrar animación de selección para el elegido
                    if (scaledClick != null)
                        lbl.setIcon(scaledClick);

                    // Mostrar animación de muerte para los otros
                    for (JLabel other : allLabels) {
                        if (other != lbl) {
                            ImageIcon otherDeath = deathIcons.get(other);
                            if (otherDeath != null)
                                other.setIcon(otherDeath);
                        }
                    }

                    // Guardar el nombre antes del delay
                    final String selectedName = tempName.trim();

                    // Esperar 3 segundos para mostrar la animación antes de continuar
                    javax.swing.Timer delayTimer = new javax.swing.Timer(3000, evt -> {
                        handler.accept(selectedName, flavor);
                    });
                    delayTimer.setRepeats(false);
                    delayTimer.start();
                }

                public void mouseEntered(java.awt.event.MouseEvent e) {
                    // Hover siempre activo
                    if (scaledHover != null)
                        lbl.setIcon(scaledHover);
                }

                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (scaledIdle != null)
                        lbl.setIcon(scaledIdle);
                }

                public void mousePressed(java.awt.event.MouseEvent e) {
                    if (tempName.trim().isEmpty())
                        return; // No cambiar sin nombre
                    if (scaledClick != null)
                        lbl.setIcon(scaledClick);
                }
            });

            iceCreamPanel.add(lbl);
        }

        // DocumentListener para actualizar el nombre mostrado
        hiddenField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                String text = hiddenField.getText().toUpperCase();
                if (text.length() > 10) {
                    text = text.substring(0, 10);
                    hiddenField.setText(text);
                }
                tempName = text;
                flyingLabel.setText(text.isEmpty() ? "_" : text + "_");
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }
        });

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(flyingLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(hiddenField);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(iceCreamPanel);

        charactersPanel.add(mainPanel);
        revalidate();
        repaint();

        // Dar foco al campo oculto
        SwingUtilities.invokeLater(() -> hiddenField.requestFocus());
    }

    // --- GUI HELPERS ---

    private void showOptions(List<String> options, boolean isIceCream, Consumer<String> handler) {
        charactersPanel.removeAll();

        // Si son opciones de dificultad, usar layout vertical
        boolean isDifficultyOptions = options.stream().anyMatch(this::isDifficulty);

        if (isDifficultyOptions) {
            // Layout vertical para botones de dificultad
            charactersPanel.setLayout(new BoxLayout(charactersPanel, BoxLayout.Y_AXIS));
            charactersPanel.add(Box.createVerticalGlue());
        } else {
            charactersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 200));
        }

        for (String opt : options) {
            boolean useIcon = isIceCream || isEnemy(opt);

            if (useIcon) {
                addIconBtn(opt, handler);
            } else {
                addTextBtn(opt, handler, isDifficultyOptions);
            }

            if (isDifficultyOptions) {
                charactersPanel.add(Box.createVerticalStrut(10));
            }
        }

        if (isDifficultyOptions) {
            charactersPanel.add(Box.createVerticalGlue());
        }

        revalidate();
        repaint();
    }

    private boolean isEnemy(String s) {
        return Arrays.asList("TROLL", "SQUID", "FLOWERPOT", "NARWHAL").contains(s);
    }

    private boolean isDifficulty(String s) {
        return Arrays.asList("HUNGRY", "FEARFUL", "EXPERT").contains(s);
    }

    private void addIconBtn(String name, Consumer<String> handler) {
        ImageIcon idle = loader.getIcon(name, "WALK");
        ImageIcon hover = loader.getIcon(name, "HOVER");
        ImageIcon click = loader.getIcon(name, "SELECT");

        // Escalar los GIF a un tamaño más grande
        int size = 110;
        ImageIcon scaledIdle = scaleIcon(idle, size, size);
        ImageIcon scaledHover = scaleIcon(hover, size, size);
        ImageIcon scaledClick = scaleIcon(click, size, size);

        JLabel lbl = new JLabel(scaledIdle);
        lbl.setPreferredSize(new Dimension(size, size));

        lbl.addMouseListener(new CharacterIconListener(lbl, scaledIdle, scaledHover, scaledClick, name, handler));

        charactersPanel.add(lbl);
    }

    private ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
        if (icon == null)
            return null;
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(scaled);
    }

    private void addTextBtn(String text, Consumer<String> handler, boolean largeSize) {
        JButton btn;

        // Tamaño: grande (350x120) para dificultad, normal (200x80) para otros
        int width = largeSize ? 350 : 200;
        int height = largeSize ? 120 : 80;

        // Intentar cargar imagen si es un tipo de dificultad
        if (isDifficulty(text)) {
            String imagePath = "/presentation/" + text.toLowerCase() + ".png";
            java.net.URL url = getClass().getResource(imagePath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                btn = new JButton(new ImageIcon(scaled));
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);
            } else {
                btn = new JButton(text);
                btn.setFont(new Font("Arial", Font.BOLD, 20));
            }
        } else {
            btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
        }

        btn.setPreferredSize(new Dimension(width, height));
        btn.setMaximumSize(new Dimension(width, height));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover: borde amarillo
        if (isDifficulty(text)) {
            btn.addMouseListener(StandardMouseListener.onHoverBg(btn, null, null)
                    .withBorderEffect(Color.YELLOW, 3));
        }

        btn.addActionListener(e -> handler.accept(text));
        charactersPanel.add(btn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
    }
}