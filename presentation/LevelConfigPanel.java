package presentation;

import domain.level.LevelConfiguration;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LevelConfigPanel extends JPanel {

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
            java.util.function.Consumer<LevelConfiguration> onContinue,
            Runnable onBack) {
        this.levelName = levelName;
        this.config = new LevelConfiguration();
        this.fruitSpinners = new HashMap<>();
        this.fruitCheckboxes = new HashMap<>();
        this.enemySpinners = new HashMap<>();
        this.enemyCheckboxes = new HashMap<>();
        this.obstacleSpinners = new HashMap<>();
        this.obstacleCheckboxes = new HashMap<>();

        setLayout(new BorderLayout());
        setBackground(new Color(135, 206, 250)); // Celeste

        // Panel principal con scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(135, 206, 250));
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
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(135, 206, 250));

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
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        section.setMaximumSize(new Dimension(800, 400));

        JLabel titleLabel = new JLabel("☐ " + title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        section.add(titleLabel, BorderLayout.NORTH);

        section.add(content, BorderLayout.CENTER);

        return section;
    }

    private JPanel createFruitsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addConfigRow(panel, "Plátano", "BANANA", 5, fruitCheckboxes, fruitSpinners);
        addConfigRow(panel, "Uva", "GRAPE", 5, fruitCheckboxes, fruitSpinners);
        addConfigRow(panel, "Piña", "PINEAPPLE", 3, fruitCheckboxes, fruitSpinners);
        addConfigRow(panel, "Cereza", "CHERRY", 3, fruitCheckboxes, fruitSpinners);
        addConfigRow(panel, "Cactus", "CACTUS", 2, fruitCheckboxes, fruitSpinners);

        return panel;
    }

    private JPanel createEnemiesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addConfigRow(panel, "Troll", "TROLL", 2, enemyCheckboxes, enemySpinners);
        addConfigRow(panel, "Calamar", "SQUID", 1, enemyCheckboxes, enemySpinners);
        addConfigRow(panel, "Maceta", "FLOWERPOT", 1, enemyCheckboxes, enemySpinners);
        addConfigRow(panel, "Narval", "NARWHAL", 1, enemyCheckboxes, enemySpinners);

        return panel;
    }

    private JPanel createObstaclesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addConfigRow(panel, "Fogata", "CAMPFIRE", 2, obstacleCheckboxes, obstacleSpinners);
        addConfigRow(panel, "Baldosa Caliente", "HOT_TILE", 4, obstacleCheckboxes, obstacleSpinners);

        return panel;
    }

    private void addConfigRow(JPanel parent, String displayName, String configKey,
            int defaultValue, Map<String, JCheckBox> checkboxMap,
            Map<String, JSpinner> spinnerMap) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(700, 50));

        // Checkbox
        JCheckBox checkbox = new JCheckBox(displayName);
        checkbox.setSelected(true);
        checkbox.setFont(new Font("Arial", Font.PLAIN, 16));
        checkbox.setBackground(Color.WHITE);
        checkbox.setPreferredSize(new Dimension(200, 30));
        checkboxMap.put(configKey, checkbox);

        // Spinner con el valor por defecto
        SpinnerNumberModel model = new SpinnerNumberModel(defaultValue, 0, 20, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(new Font("Arial", Font.PLAIN, 14));
        spinner.setPreferredSize(new Dimension(80, 30));
        spinnerMap.put(configKey, spinner);

        // Botones + y -
        JButton minusBtn = createSmallButton("-");
        JButton plusBtn = createSmallButton("+");

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

        // Cuando se desmarca el checkbox, poner el spinner en 0
        checkbox.addActionListener(e -> {
            if (!checkbox.isSelected()) {
                spinner.setValue(0);
            } else {
                spinner.setValue(defaultValue);
            }
        });

        row.add(checkbox);
        row.add(spinner);
        row.add(minusBtn);
        row.add(plusBtn);

        parent.add(row);
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

        btn.addMouseListener(new ColorHoverListener(
                btn,
                bgColor,
                bgColor.brighter(),
                false));

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
