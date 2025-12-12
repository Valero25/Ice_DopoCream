package presentation;

import domain.game.DomainController;
import domain.shared.ActionType;
import domain.shared.BadOpoException;
import domain.shared.Direction;
import domain.shared.GameStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import domain.shared.BadOpoLogger;

/**
 * Ventana principal de la interfaz grafica del juego Bad DOPO Cream.
 * Gestiona la navegacion entre pantallas y el ciclo de juego (game loop).
 * 
 * <p>Pantallas gestionadas:</p>
 * <ul>
 *   <li>SPLASH - Pantalla de inicio con animacion</li>
 *   <li>HOME - Menu principal con opciones</li>
 *   <li>MODE - Seleccion de modo de juego</li>
 *   <li>CHAR - Seleccion de personajes</li>
 *   <li>LEVEL - Seleccion de nivel</li>
 *   <li>LEVEL_CONFIG - Configuracion personalizada del nivel</li>
 *   <li>GAME - Pantalla de juego activo</li>
 *   <li>GAME_OVER - Pantalla de resultados</li>
 * </ul>
 * 
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Coordinar navegacion entre pantallas usando CardLayout</li>
 *   <li>Ejecutar el game loop a 60 FPS</li>
 *   <li>Traducir input de teclado a acciones del dominio</li>
 *   <li>Gestionar persistencia (guardar/cargar partidas)</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see DomainController
 * @see GamePanel
 */
public class BadOpoGUI extends JFrame {

    private DomainController domain;
    private ImageLoader loader;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // El Timer vive AQUI, no en GamePanel
    private Timer gameLoopTimer;
    private GamePanel activeGamePanel; // Referencia al panel actual
    private String currentLevel; // Nivel actual para siguiente nivel
    private domain.level.LevelConfiguration currentLevelConfig; // Configuración del nivel actual

    // Mapa de configuraciones por nivel (para mantener configuraciones al
    // avanzar/retroceder)
    private Map<String, domain.level.LevelConfiguration> levelConfigurations;

    public BadOpoGUI(DomainController domain) {
        this.domain = domain;
        this.loader = new ImageLoader();
        this.levelConfigurations = new HashMap<>();

        setTitle("Bad DOPO Cream");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        // Inicializamos las pantallas estáticas
        initScreens();

        // Configurar el Game Loop (60 FPS)
        // La acción del timer actualiza el dominio y luego la vista
        gameLoopTimer = new Timer(16, e -> updateGameStep());

        cardLayout.show(mainPanel, "SPLASH");
    }

    private void initScreens() {
        mainPanel.add(new SplashPanel(loader, () -> cardLayout.show(mainPanel, "HOME")), "SPLASH");

        mainPanel.add(new HomePanel(loader,
                () -> cardLayout.show(mainPanel, "MODE"),
                () -> loadGame(),
                () -> System.exit(0)), "HOME");

        mainPanel.add(new ModeSelectionPanel(loader, mode -> {
            domain.setGameMode(mode);
            if (mode.equals("MVM")) {
                showCharSelection(mode);
            } else if (mode.equals("SINGLE")) {
                // En modo Single, ir directo a selección de personaje (solo P1)
                showCharSelection(mode);
            } else {
                showCharSelection(mode);
            }
        }, () -> cardLayout.show(mainPanel, "HOME")), "MODE");
    }

    private void showCharSelection(String mode) {
        CharacterSelectionPanel p = new CharacterSelectionPanel(loader, mode,
                (p1Flavor, p1Diff, p2Flavor, p2Diff, p1Name, p2Name) -> {
                    domain.setPlayer1Flavor(p1Flavor);
                    domain.setPlayer1Name(p1Name != null ? p1Name : "Player 1");

                    if (p1Diff != null) {
                        domain.setPlayer1Difficulty(p1Diff);
                    }

                    // En modo SINGLE, p2Flavor puede ser null
                    if (p2Flavor != null) {
                        domain.setPlayer2Flavor(p2Flavor);
                        domain.setPlayer2Name(p2Name != null ? p2Name : "Player 2");
                    }
                    if (p2Diff != null) {
                        domain.setPlayer2Difficulty(p2Diff);
                    }

                    showLevelSelection();
                });
        // Truco para refrescar el panel
        mainPanel.add(p, "CHAR");
        cardLayout.show(mainPanel, "CHAR");
    }

    private void showLevelSelection() {
        LevelSelectionPanel p = new LevelSelectionPanel(loader, level -> showLevelConfig(level),
                () -> cardLayout.show(mainPanel, "MODE"));
        mainPanel.add(p, "LEVEL");
        cardLayout.show(mainPanel, "LEVEL");
    }

    private void showLevelConfig(String levelFile) {
        // Recuperar conf existente o null/default
        domain.level.LevelConfiguration existing = getConfigurationForLevel(levelFile);

        // Mostrar panel de configuración personalizada
        LevelConfigPanel configPanel = new LevelConfigPanel(
                levelFile,
                existing,
                config -> showLevelInfo(levelFile, config),
                () -> showLevelSelection());
        mainPanel.add(configPanel, "LEVEL_CONFIG");
        cardLayout.show(mainPanel, "LEVEL_CONFIG");
    }

    private void showLevelInfo(String levelFile, domain.level.LevelConfiguration config) {
        // Guardar la configuración para poder reiniciar o retroceder niveles
        this.currentLevelConfig = config;

        // Guardar la configuración por nivel para reutilizarla después
        levelConfigurations.put(levelFile, config);

        // Guardar la configuración en el dominio para usarla después
        domain.setLevelConfiguration(config);

        // Crear panel de información basado en la configuración personalizada
        LevelInfoPanel infoPanel = new LevelInfoPanel(e -> startGame(levelFile));

        // Convertir configuración a arrays para mostrar
        java.util.List<String> fruitTypes = config.getActiveFruitTypes();
        String[] fruits = fruitTypes.toArray(new String[0]);
        int[] fruitCounts = new int[fruits.length];
        for (int i = 0; i < fruits.length; i++) {
            fruitCounts[i] = config.getFruitCount(fruits[i]);
        }

        java.util.List<String> enemyTypes = config.getActiveEnemyTypes();
        String[] enemies = enemyTypes.toArray(new String[0]);
        int[] enemyCounts = new int[enemies.length];
        for (int i = 0; i < enemies.length; i++) {
            enemyCounts[i] = config.getEnemyCount(enemies[i]);
        }

        // Obtener obstáculos configurados
        java.util.Map<String, Integer> obstacleMap = config.getAllObstacles();
        java.util.List<String> obstacleList = new java.util.ArrayList<>();
        java.util.List<Integer> obstacleCountList = new java.util.ArrayList<>();
        for (java.util.Map.Entry<String, Integer> entry : obstacleMap.entrySet()) {
            if (entry.getValue() > 0) {
                obstacleList.add(entry.getKey());
                obstacleCountList.add(entry.getValue());
            }
        }
        String[] obstacles = obstacleList.toArray(new String[0]);
        int[] obstacleCounts = new int[obstacleCountList.size()];
        for (int i = 0; i < obstacleCountList.size(); i++) {
            obstacleCounts[i] = obstacleCountList.get(i);
        }

        infoPanel.setLevelInfo(levelFile, fruits, fruitCounts, enemies, enemyCounts, obstacles, obstacleCounts);

        mainPanel.add(infoPanel, "LEVEL_INFO");
        cardLayout.show(mainPanel, "LEVEL_INFO");
    }

    private void startGame(String levelFile) {
        try {
            // Guardar nivel actual
            this.currentLevel = levelFile;

            // 0. Setear nivel actual para sistema de oleadas
            domain.setCurrentLevel(levelFile);

            // 1. Cargar Nivel en Dominio
            String[] map = getMockMap(levelFile);
            domain.loadLevel(map);

            // 2. Crear GamePanel nuevo (Pasamos lambda para inputs)
            activeGamePanel = new GamePanel(keyCode -> handleGameInput(keyCode));

            // 3. Configurar datos estáticos del panel (Muros y Baldosas Calientes)
            boolean[][] walls = domain.getWallMap();
            activeGamePanel.setupBoard(walls);

            // 3.5. Configurar nombres de los jugadores
            activeGamePanel.setPlayerNames(domain.getPlayer1Name(), domain.getPlayer2Name());

            // 4. Configurar fondo específico del nivel
            Image levelBg = loader.getBackgroundImage(levelFile);
            if (levelBg != null) {
                activeGamePanel.setLevelBackground(levelBg);
            }

            // 4. Mostrar Panel
            mainPanel.add(activeGamePanel, "GAME");
            cardLayout.show(mainPanel, "GAME");
            activeGamePanel.requestFocus();

            // 5. Arrancar el Timer
            gameLoopTimer.start();

        } catch (BadOpoException e) {
            BadOpoLogger.logError("Error al iniciar el juego", e);
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    /**
     * ESTE MÉTODO SE EJECUTA 60 VECES POR SEGUNDO
     * Es el puente entre Dominio y Vista
     */
    private void updateGameStep() {
        if (activeGamePanel == null)
            return;

        // A. Actualizar Lógica
        GameStatus status = domain.getStatus();

        // Solo actualizar si está jugando (no en pausa)
        if (status == GameStatus.PLAYING) {
            domain.updateGameLoop(0.016f);
        }

        // B. Verificar Estados de Fin
        if (status == GameStatus.GAME_OVER || status == GameStatus.WON || status == GameStatus.TIMEOUT) {
            gameLoopTimer.stop();
            showGameOverScreen(status);
            return;
        }

        // C. EMPUJAR DATOS A LA VISTA (Push)
        // Jugadores se pasan por separado (requisito del profesor)
        activeGamePanel.renderFrame(
                domain.getObjectsToDraw(),
                domain.getPlayer1Info(),
                domain.getPlayer2Info(),
                domain.getScoreP1(),
                domain.getScoreP2(),
                domain.getTimeRemaining(),
                domain.getRemainingFruits());
    }

    private void showGameOverScreen(GameStatus status) {
        String result = status.toString();
        String winnerName = domain.getWinner();
        String gameMode = getGameMode();
        int finalScore = domain.getScore();
        String playerName = domain.getPlayer1Name(); // Nombre del jugador 1 para modo SINGLE

        // Determinar ganador y puntaje
        if (winnerName == null) {
            int s1 = domain.getScoreP1();
            int s2 = domain.getScoreP2();
            if (s1 > s2) {
                winnerName = domain.getPlayer1Name();
                finalScore = s1;
            } else if (s2 > s1) {
                winnerName = domain.getPlayer2Name();
                finalScore = s2;
            } else {
                winnerName = "EMPATE";
                finalScore = s1;
            }
        } else {
            // El ganador ya está definido, obtener el puntaje correspondiente
            String p1Name = domain.getPlayer1Name();
            String p2Name = domain.getPlayer2Name();
            if (p1Name != null && p1Name.equals(winnerName))
                finalScore = domain.getScoreP1();
            else if (p2Name != null && p2Name.equals(winnerName))
                finalScore = domain.getScoreP2();
        }

        // Verificar si hay siguiente nivel
        boolean hasNext = hasNextLevel(currentLevel);

        GameOverPanel gameOverPanel = new GameOverPanel(
                gameMode,
                result,
                winnerName,
                finalScore,
                () -> {
                    // Limpiar configuraciones al volver al menú
                    levelConfigurations.clear();
                    cardLayout.show(mainPanel, "HOME");
                },
                () -> {
                    // Limpiar configuraciones al ir a selección de niveles
                    levelConfigurations.clear();
                    showLevelSelection();
                },
                () -> {
                    // SIGUIENTE NIVEL - usar configuración guardada o heredada
                    advanceToNextLevel();
                },
                () -> {
                    // REINICIAR NIVEL
                    restartCurrentLevel();
                },
                () -> {
                    System.exit(0);
                },
                hasNext,
                playerName);

        mainPanel.add(gameOverPanel, "GAME_OVER");
        cardLayout.show(mainPanel, "GAME_OVER");
    }

    private String getGameMode() {
        return domain.getGameMode();
    }

    private boolean hasNextLevel(String level) {
        if ("LEVEL_1".equals(level))
            return true;
        if ("LEVEL_2".equals(level))
            return true;
        if ("LEVEL_3".equals(level))
            return false;
        return false;
    }

    private String getNextLevel(String level) {
        if ("LEVEL_1".equals(level))
            return "LEVEL_2";
        if ("LEVEL_2".equals(level))
            return "LEVEL_3";
        // Extensible: agregar más niveles aquí
        return null;
    }

    /**
     * Obtiene la configuración para un nivel específico.
     * Si no existe configuración para ese nivel, usa la última configuración
     * disponible.
     */
    /**
     * Obtiene la configuración para un nivel específico.
     * Si no existe configuración para ese nivel, devuelve null o una nueva
     * configuracion
     * (NO hereda del nivel anterior para evitar bugs de reinicio con config
     * incorrecta).
     */
    private domain.level.LevelConfiguration getConfigurationForLevel(String level) {
        // Si hay configuración específica para este nivel, usarla
        if (levelConfigurations.containsKey(level)) {
            return levelConfigurations.get(level);
        }

        /*
         * LÓGICA ANTERIOR ELIMINADA:
         * No queremos heredar configuración del nivel anterior automáticamente al
         * configurar,
         * porque eso causaba que si morías en Nivel 2 y reiniciabas Nivel 1,
         * el sistema podía confundirse. Mejor, cada nivel empieza limpio si no se ha
         * configurado.
         */

        // Fallback: crear configuración por defecto y guardarla
        domain.level.LevelConfiguration defaultConfig = new domain.level.LevelConfiguration();
        // Opcional: Podríamos pre-cargar defaults aquí si quisiéramos

        levelConfigurations.put(level, defaultConfig);
        return defaultConfig;
    }

    /**
     * Reinicia desde el nivel 1 con todas las configuraciones guardadas.
     * Se usa cuando el jugador muere en modo SINGLE.
     */
    private void restartFromLevel1() {
        // Obtener la configuración para el nivel 1
        domain.level.LevelConfiguration config = getConfigurationForLevel("LEVEL_1");
        this.currentLevelConfig = config;
        this.currentLevel = "LEVEL_1";

        domain.setLevelConfiguration(config);
        startGame("LEVEL_1");
    }

    /**
     * Avanza al siguiente nivel mostrando la pantalla de configuración.
     */
    private void advanceToNextLevel() {
        String nextLevel = getNextLevel(currentLevel);
        if (nextLevel == null) {
            // No hay más niveles, mostrar selección
            showLevelSelection();
            return;
        }

        // Mostrar la pantalla de configuración para el siguiente nivel
        showLevelConfig(nextLevel);
    }

    /**
     * Reinicia el nivel actual con la misma configuración.
     * En modo SINGLE: Si se perdió, vuelve a LEVEL_1.
     */
    private void restartCurrentLevel() {
        String gameMode = domain.getGameMode();
        GameStatus lastStatus = domain.getGameStatus();

        // En modo SINGLE, si perdiste vuelves al nivel 1
        if ("SINGLE".equals(gameMode) && lastStatus == GameStatus.GAME_OVER) {
            restartFromLevel1();
            return;
        }

        // En otros modos o si ganaste, reinicia el mismo nivel
        if (currentLevelConfig == null) {
            currentLevelConfig = getConfigurationForLevel(currentLevel);
        }

        domain.setLevelConfiguration(currentLevelConfig);
        startGame(currentLevel);
    }

    /**
     * Maneja los inputs que vienen del GamePanel
     */
    private void handleGameInput(int keyCode) {
        // --- JUGADOR 1 (WASD + Espacio + E) ---
        String p1 = "player1";
        switch (keyCode) {
            case KeyEvent.VK_W:
                domain.handlePlayerAction(p1, ActionType.MOVE, Direction.UP);
                break;
            case KeyEvent.VK_S:
                domain.handlePlayerAction(p1, ActionType.MOVE, Direction.DOWN);
                break;
            case KeyEvent.VK_A:
                domain.handlePlayerAction(p1, ActionType.MOVE, Direction.LEFT);
                break;
            case KeyEvent.VK_D:
                domain.handlePlayerAction(p1, ActionType.MOVE, Direction.RIGHT);
                break;
            case KeyEvent.VK_SPACE:
                domain.handlePlayerAction(p1, ActionType.CREATE_ICE, Direction.NONE);
                break;
            case KeyEvent.VK_E:
                domain.handlePlayerAction(p1, ActionType.BREAK_ICE, Direction.NONE);
                break;
        }

        // --- JUGADOR 2 (Flechas + Enter + Shift) ---
        String p2 = "player2";
        switch (keyCode) {
            case KeyEvent.VK_UP:
                domain.handlePlayerAction(p2, ActionType.MOVE, Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                domain.handlePlayerAction(p2, ActionType.MOVE, Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                domain.handlePlayerAction(p2, ActionType.MOVE, Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                domain.handlePlayerAction(p2, ActionType.MOVE, Direction.RIGHT);
                break;
            case KeyEvent.VK_ENTER:
                domain.handlePlayerAction(p2, ActionType.CREATE_ICE, Direction.NONE);
                break;
            case KeyEvent.VK_SHIFT:
                domain.handlePlayerAction(p2, ActionType.BREAK_ICE, Direction.NONE);
                break;
        }

        // --- GENERAL ---
        switch (keyCode) {
            case KeyEvent.VK_P:
                GameStatus currentStatus = domain.getStatus();
                if (currentStatus == GameStatus.PLAYING) {
                    domain.togglePause();
                    showPauseOverlay();
                } else if (currentStatus == GameStatus.PAUSED) {
                    domain.togglePause();
                    hidePauseOverlay();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                gameLoopTimer.stop();
                cardLayout.show(mainPanel, "HOME");
                break;
        }
    }

    private void showPauseOverlay() {
        PauseOverlay pauseOverlay = new PauseOverlay(
                () -> {
                    domain.togglePause();
                    hidePauseOverlay();
                },
                () -> saveGame(),
                () -> {
                    gameLoopTimer.stop();
                    hidePauseOverlay();
                    cardLayout.show(mainPanel, "HOME");
                });

        pauseOverlay.setBounds(0, 0, activeGamePanel.getWidth(), activeGamePanel.getHeight());
        activeGamePanel.add(pauseOverlay);
        activeGamePanel.revalidate();
        activeGamePanel.repaint();
    }

    /**
     * Oculta el overlay de pausa removiendolo del panel de juego.
     */
    private void hidePauseOverlay() {
        Component[] components = activeGamePanel.getComponents();
        for (Component comp : components) {
            if (comp.getClass().getSimpleName().equals("PauseOverlay")) {
                activeGamePanel.remove(comp);
            }
        }
        activeGamePanel.revalidate();
        activeGamePanel.repaint();
        activeGamePanel.requestFocus();
    }

    // =============================================================
    // PERSISTENCIA
    // =============================================================

    public void saveGame() {
        if (domain.getStatus() != GameStatus.PAUSED && domain.getStatus() != GameStatus.PLAYING) {
            JOptionPane.showMessageDialog(this, "Solo puedes guardar durante una partida.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Partida");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try {
                domain.saveGame(fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Partida guardada correctamente.");
            } catch (BadOpoException e) {
                BadOpoLogger.logError("Error al guardar la partida", e);
                JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
            }
        }
    }

    public void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Abrir Partida");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToLoad = fileChooser.getSelectedFile();
            try {
                // 1. Cargar nuevo dominio
                DomainController loadedDomain = DomainController.loadGame(fileToLoad.getAbsolutePath());

                // 2. Reemplazar dominio actual
                this.domain = loadedDomain;

                // 3. Restaurar estado visual (Re-crear GamePanel)
                restoreGameState();

                JOptionPane.showMessageDialog(this, "Partida cargada correctamente.");

            } catch (BadOpoException e) {
                BadOpoLogger.logError("Error al cargar la partida", e);
                JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage());
            }
        }
    }

    private void restoreGameState() {
        // Detener timer anterior si existe
        if (gameLoopTimer != null && gameLoopTimer.isRunning()) {
            gameLoopTimer.stop();
        }

        // Crear nuevo GamePanel
        activeGamePanel = new GamePanel(keyCode -> handleGameInput(keyCode));

        // Configurar el tablero con el nuevo dominio
        boolean[][] walls = domain.getWallMap();
        activeGamePanel.setupBoard(walls);
        activeGamePanel.setPlayerNames(domain.getPlayer1Name(), domain.getPlayer2Name());

        // Cargar fondo usando el nivel guardado en el dominio
        String levelFile = domain.getCurrentLevel();
        if (levelFile != null) {
            Image levelBg = loader.getBackgroundImage(levelFile);
            if (levelBg != null) {
                activeGamePanel.setLevelBackground(levelBg);
            }
        }

        // Mostrar Panel
        mainPanel.add(activeGamePanel, "GAME");
        cardLayout.show(mainPanel, "GAME");
        activeGamePanel.requestFocus();

        // Re-iniciar Timer
        gameLoopTimer.start();

        // Asegurar estado correcto
        if (domain.getStatus() == GameStatus.PAUSED) {
            showPauseOverlay();
        }
    }

    private String[] getMockMap(String level) {
        if ("LEVEL_2".equals(level)) {
            // Nivel 2: Oleada 1: 8 Piñas, Oleada 2: 8 Cerezas
            // Enemigos: 2 Calamares, 1 Maceta
            return new String[] {
                    "##################",
                    "#P.A.A.A.A.......#",
                    "#................#",
                    "#....S...........#",
                    "#................#",
                    "#.A.A.A.A....F...#",
                    "#................#",
                    "#........S.......#",
                    "#................#",
                    "##################"
            };
        }

        if ("LEVEL_3".equals(level)) {
            // Nivel 3: Oleada 1: 8 Uvas, Oleada 2: 4 Piñas + 4 Cerezas
            // Enemigos: 1 Troll, 1 Calamar, 1 Narval
            return new String[] {
                    "##################",
                    "#P.G.G.G.G.......#",
                    "#................#",
                    "#....T...........#",
                    "#................#",
                    "#.G.G.G.G....S...#",
                    "#................#",
                    "#........N.......#",
                    "#................#",
                    "##################"
            };
        }

        // Nivel 1: Oleada 1: 8 Uvas, Oleada 2: 8 Bananas
        // Enemigos: 2 Trolls
        return new String[] {
                "##################",
                "#P.G.G.G.G.......#",
                "#................#",
                "#.......T........#",
                "#................#",
                "#.G.G.G.G........#",
                "#................#",
                "#............T...#",
                "#................#",
                "##################"
        };
    }
}