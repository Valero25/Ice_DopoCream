package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LevelInfoPanel extends JPanel {

    private JButton startButton;
    private String levelName;
    private ActionListener mainActionListener;

    public LevelInfoPanel(ActionListener onStart) {
        setLayout(new BorderLayout());
        setBackground(new Color(135, 206, 250)); // Celeste
        this.mainActionListener = onStart;
    }

    public void setLevelInfo(String levelName, String[] fruits, int[] fruitCounts,
            String[] enemies, int[] enemyCounts, String[] obstacles, int[] obstacleCounts) {
        this.levelName = levelName;
        removeAll();

        // Panel principal con información - temática invernal
        // Panel principal con información - temática invernal
        StandardBackgroundPanel infoPanel = new StandardBackgroundPanel(StandardBackgroundPanel.Style.WINTER);

        // Título del nivel con efecto de nieve
        JLabel titleLabel = new JLabel("⭐ " + getLevelTitle(levelName) + " ⭐");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 204)); // Azul fuerte
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(titleLabel);

        infoPanel.add(Box.createVerticalStrut(20));

        // Separador decorativo
        JSeparator sep1 = new JSeparator();
        sep1.setMaximumSize(new Dimension(400, 2));
        sep1.setForeground(new Color(135, 206, 250));
        infoPanel.add(sep1);

        infoPanel.add(Box.createVerticalStrut(15));

        // Sección de frutas con panel decorativo
        JPanel fruitSection = new JPanel();
        fruitSection.setLayout(new BoxLayout(fruitSection, BoxLayout.Y_AXIS));
        fruitSection.setOpaque(false);

        JLabel fruitTitle = new JLabel("🍇 FRUTAS A RECOLECTAR 🍓");
        fruitTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        fruitTitle.setForeground(new Color(255, 105, 180)); // Hot pink
        fruitTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        fruitSection.add(fruitTitle);

        fruitSection.add(Box.createVerticalStrut(10));

        for (int i = 0; i < fruits.length; i++) {
            JPanel fruitItem = new JPanel();
            fruitItem.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 2));
            fruitItem.setOpaque(false);

            // Emoji grande con sombra
            JLabel emojiLabel = new JLabel(getFruitEmoji(fruits[i]));
            emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
            fruitItem.add(emojiLabel);

            JLabel fruitLabel = new JLabel(getFruitName(fruits[i]) + " × " + fruitCounts[i]);
            fruitLabel.setFont(new Font("Arial", Font.BOLD, 20));
            fruitLabel.setForeground(new Color(25, 25, 112)); // Midnight blue
            fruitItem.add(fruitLabel);

            fruitSection.add(fruitItem);
            fruitSection.add(Box.createVerticalStrut(4));
        }

        infoPanel.add(fruitSection);
        infoPanel.add(Box.createVerticalStrut(20));

        // Separador decorativo
        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(400, 2));
        sep2.setForeground(new Color(135, 206, 250));
        infoPanel.add(sep2);

        infoPanel.add(Box.createVerticalStrut(15));

        // Sección de enemigos con panel decorativo
        JPanel enemySection = new JPanel();
        enemySection.setLayout(new BoxLayout(enemySection, BoxLayout.Y_AXIS));
        enemySection.setOpaque(false);

        JLabel enemyTitle = new JLabel("👾 ENEMIGOS DEL NIVEL 👾");
        enemyTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        enemyTitle.setForeground(new Color(220, 20, 60)); // Crimson
        enemyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        enemySection.add(enemyTitle);

        enemySection.add(Box.createVerticalStrut(10));

        for (int i = 0; i < enemies.length; i++) {
            JPanel enemyItem = new JPanel();
            enemyItem.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 2));
            enemyItem.setOpaque(false);

            // Emoji grande
            JLabel emojiLabel = new JLabel(getEnemyEmoji(enemies[i]));
            emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
            enemyItem.add(emojiLabel);

            JLabel enemyLabel = new JLabel(getEnemyName(enemies[i]) + " × " + enemyCounts[i]);
            enemyLabel.setFont(new Font("Arial", Font.BOLD, 20));
            enemyLabel.setForeground(new Color(139, 0, 0)); // Dark red
            enemyItem.add(enemyLabel);

            enemySection.add(enemyItem);
            enemySection.add(Box.createVerticalStrut(4));
        }

        infoPanel.add(enemySection);

        // Agregar sección de obstáculos si hay alguno configurado
        if (obstacles != null && obstacles.length > 0) {
            infoPanel.add(Box.createVerticalStrut(20));

            // Separador decorativo
            JSeparator sep3 = new JSeparator();
            sep3.setMaximumSize(new Dimension(400, 2));
            sep3.setForeground(new Color(135, 206, 250));
            infoPanel.add(sep3);

            infoPanel.add(Box.createVerticalStrut(15));

            // Sección de obstáculos con panel decorativo
            JPanel obstacleSection = new JPanel();
            obstacleSection.setLayout(new BoxLayout(obstacleSection, BoxLayout.Y_AXIS));
            obstacleSection.setOpaque(false);

            JLabel obstacleTitle = new JLabel("⚠ OBSTÁCULOS ⚠");
            obstacleTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
            obstacleTitle.setForeground(new Color(255, 140, 0)); // Dark orange
            obstacleTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            obstacleSection.add(obstacleTitle);

            obstacleSection.add(Box.createVerticalStrut(10));

            for (int i = 0; i < obstacles.length; i++) {
                if (obstacleCounts[i] > 0) { // Solo mostrar si hay al menos 1
                    JPanel obstacleItem = new JPanel();
                    obstacleItem.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 2));
                    obstacleItem.setOpaque(false);

                    // Emoji grande
                    JLabel emojiLabel = new JLabel(getObstacleEmoji(obstacles[i]));
                    emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
                    obstacleItem.add(emojiLabel);

                    JLabel obstacleLabel = new JLabel(getObstacleName(obstacles[i]) + " × " + obstacleCounts[i]);
                    obstacleLabel.setFont(new Font("Arial", Font.BOLD, 20));
                    obstacleLabel.setForeground(new Color(160, 82, 45)); // Sienna brown
                    obstacleItem.add(obstacleLabel);

                    obstacleSection.add(obstacleItem);
                    obstacleSection.add(Box.createVerticalStrut(4));
                }
            }

            infoPanel.add(obstacleSection);
        }

        infoPanel.add(Box.createVerticalStrut(30)); // Espacio para el botón

        // Crear un panel contenedor para agregar scroll
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(infoPanel, BorderLayout.CENTER);

        // Agregar scroll
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        // Botón iniciar con efecto invernal en un panel inferior fijo
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBackground(new Color(135, 206, 250));

        startButton = new StartButton("🎮 ¡JUGAR! 🎮", mainActionListener);
        startButton.setPreferredSize(new Dimension(300, 60));

        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo degradado invernal
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(176, 224, 230),
                0, getHeight(), new Color(240, 248, 255));
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Copos de nieve animados (decorativos)
        g2.setColor(new Color(255, 255, 255, 120));
        java.util.Random rand = new java.util.Random(42); // Seed fijo para posiciones consistentes
        for (int i = 0; i < 30; i++) {
            int x = rand.nextInt(getWidth());
            int y = rand.nextInt(getHeight());
            int size = 3 + rand.nextInt(5);
            g2.fillOval(x, y, size, size);

            // Pequeñas líneas para simular copos
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(x - size, y, x + size, y);
            g2.drawLine(x, y - size, x, y + size);
        }
    }

    private String getLevelTitle(String level) {
        switch (level) {
            case "LEVEL_1":
                return "NIVEL 1";
            case "LEVEL_2":
                return "NIVEL 2";
            case "LEVEL_3":
                return "NIVEL 3";
            default:
                return "NIVEL";
        }
    }

    private String getFruitName(String fruit) {
        switch (fruit) {
            case "GRAPE":
                return "Uvas";
            case "BANANA":
                return "Bananas";
            case "PINEAPPLE":
                return "Piñas";
            case "CHERRY":
                return "Cerezas";
            case "CACTUS":
                return "Cactus";
            default:
                return fruit;
        }
    }

    private String getFruitEmoji(String fruit) {
        switch (fruit) {
            case "GRAPE":
                return "🍇";
            case "BANANA":
                return "🍌";
            case "PINEAPPLE":
                return "🍍";
            case "CHERRY":
                return "🍒";
            case "CACTUS":
                return "🌵";
            default:
                return "🍎";
        }
    }

    private String getEnemyName(String enemy) {
        switch (enemy) {
            case "TROLL":
                return "Troll";
            case "SQUID":
                return "Calamar";
            case "FLOWERPOT":
                return "Maceta";
            case "NARWHAL":
                return "Narval";
            default:
                return enemy;
        }
    }

    private String getEnemyEmoji(String enemy) {
        switch (enemy) {
            case "TROLL":
                return "👹";
            case "SQUID":
                return "🦑";
            case "FLOWERPOT":
                return "🪴";
            case "NARWHAL":
                return "🐋";
            default:
                return "👾";
        }
    }

    private String getObstacleName(String obstacle) {
        switch (obstacle) {
            case "CAMPFIRE":
                return "Fogata";
            case "HOT_TILE":
                return "Baldosa Caliente";
            case "ICE_BLOCK":
                return "Bloque de Hielo";
            default:
                return obstacle;
        }
    }

    private String getObstacleEmoji(String obstacle) {
        switch (obstacle) {
            case "CAMPFIRE":
                return "🔥";
            case "HOT_TILE":
                return "♨";
            case "ICE_BLOCK":
                return "🧊";
            default:
                return "⚠";
        }
    }
}
