package presentation;

import domain.level.LevelConfiguration;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel de configuracion personalizada del nivel.
 * Permite ajustar parametros del juego antes de iniciar.
 * Extiende StandardBackgroundPanel para el fondo visual.
 * 
 * <p>Parametros configurables:</p>
 * <ul>
 *   <li>Duracion de la partida</li>
 *   <li>Cantidad y tipos de frutas (con checkboxes y spinners)</li>
 *   <li>Cantidad y tipos de enemigos</li>
 *   <li>Cantidad de obstaculos</li>
 *   <li>Archivo de mapa personalizado</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see LevelConfiguration
 * @see StandardBackgroundPanel
 */
public class LevelConfigPanel extends StandardBackgroundPanel {

    private LevelConfiguration config;
    private String levelName;

    // Componentes para frutas
    private Map<String, JSpinner> fruitSpinners;
    private Map<String, JCheckBox> fruitCheckboxes;

    // Componentes para enemigos
    private Map<String, JSpinner> enemySpinners;
    private Map<String, JCheckBox> enemyCheckboxes;

    // Componentes para obstáculos
    private Map<String, JSpinner> obstacleSpinners;
    private Map<String, JCheckBox> obstacleCheckboxes;

    public LevelConfigPanel(String levelName,
            domain.level.LevelConfiguration existingConfig,
            java.util.function.Consumer<LevelConfiguration> onContinue,
            Runnable onBack) {
        super(StandardBackgroundPanel.Style.WINTER);
        this.levelName = levelName;
        // Si nos pasan config existente, la usamos (clonada o directa).
        // Si no, creamos nueva.
        this.config = (existingConfig != null) ? existingConfig : new LevelConfiguration();

        this.fruitSpinners = new HashMap<>();
        this.fruitCheckboxes = new HashMap<>();
        this.enemySpinners = new HashMap<>();
        this.enemyCheckboxes = new HashMap<>();
        this.obstacleSpinners = new HashMap<>();
        this.obstacleCheckboxes = new HashMap<>();

        setLayout(new BorderLayout());
        // setBackground(new Color(135, 206, 250)); // REMOVED

        // Panel principal con scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // Transparente para ver la nieve
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Título
        JLabel title = new JLabel("Configurar Nivel: " + levelName);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
        title.setForeground(new Color(0, 51, 102));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(title);

        contentPanel.add(Box.createVerticalStrut(10));

        JLabel subtitle = new JLabel("Personaliza frutas y enemigos");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(new Color(51, 51, 51));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subtitle);

        contentPanel.add(Box.createVerticalStrut(30));

        // Sección FRUTAS
        contentPanel.add(createSectionPanel("FRUTAS", createFruitsPanel()));
        contentPanel.add(Box.createVerticalStrut(20));

        // Sección ENEMIGOS
        contentPanel.add(createSectionPanel("ENEMIGOS", createEnemiesPanel()));
        contentPanel.add(Box.createVerticalStrut(20));

        // Sección OBSTÁCULOS
        contentPanel.add(createSectionPanel("OBSTÁCULOS", createObstaclesPanel()));
        contentPanel.add(Box.createVerticalStrut(30));

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false); // Importante para ver el fondo
        scrollPane.setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);

        JButton backButton = createStyledButton("VOLVER", new Color(200, 100, 100));
        backButton.addActionListener(e -> onBack.run());

        JButton continueButton = createStyledButton("CONTINUAR", new Color(100, 200, 100));
        continueButton.addActionListener(e -> {
            applyConfiguration();
            onContinue.accept(config);
        });

        buttonPanel.add(backButton);
        buttonPanel.add(continueButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createSectionPanel(String title, JPanel content) {
        JPanel section = new JPanel();
        section.setLayout(new BorderLayout());

        // Fondo semi-transparente blanco/azulado para que se lea bien sobre la nieve
        section.setBackground(new Color(255, 255, 255, 200));

        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Centrar el panel en el BoxLayout padre
        section.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.setMaximumSize(new Dimension(600, 400)); // Un poco más estrecho para centrar mejor

        JLabel titleLabel = new JLabel("☐ " + title, SwingConstants.CENTER); // Centrar texto
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Asegurar centrado horizontal
        section.add(titleLabel, BorderLayout.NORTH);

        section.add(content, BorderLayout.CENTER);

        return section;
    }

    private JPanel createFruitsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addConfigRow(panel, "Plátano", "BANANA", 5, fruitCheckboxes, fruitSpinners, 0);
        addConfigRow(panel, "Uva", "GRAPE", 5, fruitCheckboxes, fruitSpinners, 1);
        addConfigRow(panel, "Piña", "PINEAPPLE", 3, fruitCheckboxes, fruitSpinners, 2);
        addConfigRow(panel, "Cereza", "CHERRY", 3, fruitCheckboxes, fruitSpinners, 3);
        addConfigRow(panel, "Cactus", "CACTUS", 2, fruitCheckboxes, fruitSpinners, 4);

        return panel;
    }

    private JPanel createEnemiesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addConfigRow(panel, "Troll", "TROLL", 2, enemyCheckboxes, enemySpinners, 0);
        addConfigRow(panel, "Calamar", "SQUID", 1, enemyCheckboxes, enemySpinners, 1);
        addConfigRow(panel, "Maceta", "FLOWERPOT", 1, enemyCheckboxes, enemySpinners, 2);
        addConfigRow(panel, "Narval", "NARWHAL", 1, enemyCheckboxes, enemySpinners, 3);

        return panel;
    }

    private JPanel createObstaclesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addConfigRow(panel, "Fogata", "CAMPFIRE", 2, obstacleCheckboxes, obstacleSpinners, 0);
        addConfigRow(panel, "Baldosa Caliente", "HOT_TILE", 4, obstacleCheckboxes, obstacleSpinners, 1);

        return panel;
    }

    private void addConfigRow(JPanel parent, String displayName, String configKey,
            int defaultValue, Map<String, JCheckBox> checkboxMap,
            Map<String, JSpinner> spinnerMap, int gridy) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = gridy;
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado
        gbc.fill = GridBagConstraints.VERTICAL;

        // Initial value logic
        int initialValue = defaultValue;
        boolean isSelected = true;

        if (config.getActiveFruitTypes().contains(configKey)) {
            initialValue = config.getFruitCount(configKey);
        } else if (config.getActiveEnemyTypes().contains(configKey)) {
            initialValue = config.getEnemyCount(configKey);
        } else if (config.getAllObstacles().containsKey(configKey) && config.getObstacleCount(configKey) > 0) {
            initialValue = config.getObstacleCount(configKey);
        } else {
            if (!isConfigEmpty(config)) {
                initialValue = 0;
                isSelected = false;
            }
        }

        // 1. Checkbox (Columna 0)
        JCheckBox checkbox = new JCheckBox(displayName);
        checkbox.setSelected(isSelected || initialValue > 0);
        checkbox.setFont(new Font("Arial", Font.PLAIN, 18));
        checkbox.setOpaque(false);
        // checkbox.setPreferredSize(new Dimension(150, 30)); // No necesario con
        // GridBag, pero ayuda
        checkboxMap.put(configKey, checkbox);

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST; // Alineado a la izquierda
        gbc.weightx = 0; // No estirar
        parent.add(checkbox, gbc);

        // 2. Spinner (Columna 1)
        SpinnerNumberModel model = new SpinnerNumberModel(initialValue, 0, 20, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(new Font("Arial", Font.PLAIN, 16));
        spinner.setPreferredSize(new Dimension(60, 30));
        spinnerMap.put(configKey, spinner);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        parent.add(spinner, gbc);

        // 3. Botón Menos (Columna 2)
        JButton minusBtn = createSmallButton("-");
        gbc.gridx = 2;
        parent.add(minusBtn, gbc);

        // 4. Botón Mas (Columna 3)
        JButton plusBtn = createSmallButton("+");
        gbc.gridx = 3;
        parent.add(plusBtn, gbc);

        // Logica de botones
        minusBtn.addActionListener(e -> {
            int val = (Integer) spinner.getValue();
            if (val > 0)
                spinner.setValue(val - 1);
        });

        plusBtn.addActionListener(e -> {
            int val = (Integer) spinner.getValue();
            if (val < 20)
                spinner.setValue(val + 1);
        });

        checkbox.addActionListener(e -> {
            if (!checkbox.isSelected()) {
                spinner.setValue(0);
            } else {
                spinner.setValue(defaultValue);
            }
        });
    }

    private JButton createSmallButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(50, 30));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(220, 220, 220));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(StandardMouseListener.onHoverBg(
                btn,
                bgColor,
                bgColor.brighter()));

        return btn;
    }

    private void applyConfiguration() {
        // Aplicar frutas
        for (Map.Entry<String, JSpinner> entry : fruitSpinners.entrySet()) {
            int count = (Integer) entry.getValue().getValue();
            config.setFruitCount(entry.getKey(), count);
        }

        // Aplicar enemigos
        for (Map.Entry<String, JSpinner> entry : enemySpinners.entrySet()) {
            int count = (Integer) entry.getValue().getValue();
            config.setEnemyCount(entry.getKey(), count);
        }

        // Aplicar obstáculos
        for (Map.Entry<String, JSpinner> entry : obstacleSpinners.entrySet()) {
            int count = (Integer) entry.getValue().getValue();
            config.setObstacleCount(entry.getKey(), count);
        }
    }

    private boolean isConfigEmpty(domain.level.LevelConfiguration c) {
        return c.getActiveFruitTypes().isEmpty() &&
                c.getActiveEnemyTypes().isEmpty() &&
                c.getAllObstacles().isEmpty();
    }

}
