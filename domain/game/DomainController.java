package domain.game;

import domain.board.BoardController;
import domain.enemies.EnemyController;
import domain.items.ItemController;
import domain.level.LevelInfo;
import domain.level.LevelLoader;
import domain.players.IceCreamFlavor;
import domain.players.PlayerController;
import domain.players.PlayerType;
import domain.shared.ActionType;
import domain.shared.BadOpoException;
import domain.shared.Direction;
import domain.shared.EntityInfo;
import domain.shared.GameStatus;

import java.util.ArrayList;
import java.util.List;

public class DomainController implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    // --- SUB-CONTROLADORES ---
    private BoardController boardCtrl;
    private ItemController itemCtrl;
    private EnemyController enemyCtrl;
    private PlayerController playerCtrl;
    private LevelLoader levelLoader;

    // --- ESTADO DEL JUEGO ---
    private GameStatus status;
    private float timeElapsed;
    private float maxTime = 180.0f; // 3 minutos
    private int scoreP1;
    private int scoreP2;
    private String winner; // Track winner for PVP

    // --- SISTEMA DE OLEADAS DE FRUTAS ---
    private int currentWave; // Oleada actual (0 = primera oleada, 1 = segunda oleada)
    private boolean waveSpawned; // Si ya se spawnó la oleada actual

    // --- CONFIGURACIÓN DE PARTIDA (Lo que selecciona la GUI) ---
    private String gameMode; // "PVP", "PVM", "MVM", "SINGLE"
    private IceCreamFlavor p1Flavor; // Sabor del Jugador 1
    private String p2Selection; // Sabor del P2 (PVP) o Tipo de Enemigo (PVM)
    private String currentLevel; // Nivel actual ("LEVEL_1", "LEVEL_2", "LEVEL_3")
    private String p1Name = "Player 1"; // Nombre del Jugador 1
    private String p2Name = "Player 2"; // Nombre del Jugador 2

    // Dificultad para máquinas (MVM)
    private PlayerType p1Difficulty = PlayerType.MACHINE_HUNGRY;
    private PlayerType p2Difficulty = PlayerType.MACHINE_HUNGRY;

    public DomainController() {
        // Inicialización de dependencias
        this.boardCtrl = new BoardController(18, 10); // 18 columnas x 10 filas (coincide con fondo)
        this.itemCtrl = new ItemController(boardCtrl);
        this.enemyCtrl = new EnemyController(boardCtrl, itemCtrl);
        this.playerCtrl = new PlayerController(boardCtrl, itemCtrl);
        this.levelLoader = new LevelLoader();

        // Valores por defecto
        this.status = GameStatus.PAUSED;
        this.scoreP1 = 0;
        this.scoreP2 = 0;
        this.currentWave = 0;
        this.waveSpawned = false;

        // Configuración default
        this.gameMode = "PVP";
        this.p1Flavor = IceCreamFlavor.VANILLA;
        this.p2Selection = "CHOCOLATE";
    }

    /**
     * Guarda el estado actual del juego en un archivo.
     */
    public void saveGame(String fileName) throws BadOpoException {
        try (java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(fileName))) {
            out.writeObject(this);
        } catch (java.io.IOException e) {
            throw new BadOpoException("Error al guardar la partida: " + e.getMessage());
        }
    }

    /**
     * Carga un estado de juego desde un archivo.
     * Retorna una NUEVA instancia de DomainController con el estado cargado.
     */
    public static DomainController loadGame(String fileName) throws BadOpoException {
        try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(fileName))) {
            return (DomainController) in.readObject();
        } catch (java.io.IOException | ClassNotFoundException e) {
            throw new BadOpoException("Error al cargar la partida: " + e.getMessage());
        }
    }

    // =============================================================
    // CONFIGURACIÓN (Llamados por la GUI antes de jugar)
    // =============================================================

    /**
     * Establece el modo de juego.
     * 
     * @param mode "SINGLE" (Single Player), "PVP" (Player vs Player),
     *             "PVM" (Player vs Machine), "MVM" (Machine vs Machine)
     */
    public void setGameMode(String mode) {
        this.gameMode = mode.toUpperCase();
    }

    /**
     * Establece el nivel actual para el sistema de oleadas
     */
    public void setCurrentLevel(String level) {
        this.currentLevel = level;
    }

    /**
     * Configura el sabor del Jugador 1.
     * 
     * @param flavorName "CHOCOLATE", "VANILLA", "STRAWBERRY"
     */
    public void setPlayer1Flavor(String flavorName) {
        try {
            this.p1Flavor = IceCreamFlavor.valueOf(flavorName.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Sabor inválido P1: " + flavorName + ". Usando VANILLA.");
            this.p1Flavor = IceCreamFlavor.VANILLA;
        }
    }

    /**
     * Configura el sabor del jugador 2 / máquina.
     * En PVP: Es un sabor del jugador 2 humano ("CHOCOLATE", "VANILLA",
     * "STRAWBERRY").
     * En PVM: Es un sabor de la máquina controlada por IA ("CHOCOLATE", "VANILLA",
     * "STRAWBERRY").
     * En MVM: Es un sabor de la segunda máquina ("CHOCOLATE", "VANILLA",
     * "STRAWBERRY").
     */
    public void setPlayer2Flavor(String selection) {
        // Guardamos el String crudo del sabor
        this.p2Selection = selection.toUpperCase();
    }

    public void setPlayer1Difficulty(String type) {
        try {
            this.p1Difficulty = PlayerType.valueOf(type);
        } catch (Exception e) {
            this.p1Difficulty = PlayerType.MACHINE_HUNGRY;
        }
    }

    public void setPlayer2Difficulty(String type) {
        try {
            this.p2Difficulty = PlayerType.valueOf(type);
        } catch (Exception e) {
            this.p2Difficulty = PlayerType.MACHINE_HUNGRY;
        }
    }

    /**
     * Establece el nombre del Jugador 1.
     */
    public void setPlayer1Name(String name) {
        this.p1Name = name;
    }

    /**
     * Establece el nombre del Jugador 2.
     */
    public void setPlayer2Name(String name) {
        this.p2Name = name;
    }

    /**
     * Obtiene el nombre del Jugador 1.
     */
    public String getPlayer1Name() {
        return playerCtrl.getPlayerName("player1");
    }

    /**
     * Obtiene el nombre del Jugador 2.
     */
    public String getPlayer2Name() {
        return playerCtrl.getPlayerName("player2");
    }

    /**
     * Establece la configuración personalizada del nivel
     */
    public void setLevelConfiguration(domain.level.LevelConfiguration config) {
        this.levelLoader.setConfiguration(config);
    }

    // =============================================================
    // CICLO DE VIDA DEL JUEGO
    // =============================================================

    /**
     * Carga el nivel y configura los jugadores según el modo elegido.
     */
    public void loadLevel(String[] mapLayout) throws BadOpoException {
        // 1. Limpiar estado anterior
        playerCtrl.reset(); // Asegúrate de agregar este método en PlayerController para limpiar el Map
        enemyCtrl.reset(); // Igual en EnemyController
        itemCtrl.reset(); // Igual en ItemController

        // 2. Cargar mapa base (Muros y Celdas)
        // REQUISITO: Redimensionar el tablero al tamaño del mapa
        if (mapLayout != null && mapLayout.length > 0) {
            int h = mapLayout.length;
            int w = mapLayout[0].length();
            boardCtrl.reset(w, h);
        }

        levelLoader.loadLevel(mapLayout, boardCtrl, itemCtrl, enemyCtrl, playerCtrl);

        // 2.5. Aplicar configuración personalizada de enemigos y obstáculos
        // Las frutas NO se spawnean aquí, se manejan por oleadas
        levelLoader.applyCustomConfiguration(boardCtrl, itemCtrl, enemyCtrl);

        // 3. Configurar Jugadores/Oponentes manualmente según el modo
        // Buscamos puntos de spawn. Asumiremos posiciones fijas o buscamos 'P' en el
        // mapa.
        // Para simplificar, asumimos que LevelLoader no crea los jugadores finales,
        // sino que nosotros los inyectamos aquí basándonos en la configuración.

        setupEntitiesBasedOnMode();

        this.status = GameStatus.PLAYING;
        this.timeElapsed = 0;
        this.scoreP1 = 0;
        this.scoreP2 = 0;
        this.winner = null;
        this.currentWave = 0;
        this.waveSpawned = false;

        // Spawear la primera oleada de frutas al iniciar
        spawnNextWave();
    }

    private void setupEntitiesBasedOnMode() {
        // Posiciones de inicio para tablero 18x10
        int p1X = 1, p1Y = 1;
        int p2X = 16, p2Y = 8; // Esquina opuesta

        // Configuración P1
        PlayerType typeP1 = PlayerType.HUMAN;
        if (gameMode.equals("MVM")) {
            typeP1 = p1Difficulty;
        }
        playerCtrl.addPlayer("player1", p1Flavor, typeP1, p1X, p1Y);
        playerCtrl.setPlayerName("player1", p1Name);

        // Modo SINGLE: Solo jugador 1, sin oponentes adicionales (solo enemigos del
        // mapa)
        if (gameMode.equals("SINGLE")) {
            // No se crea jugador 2 ni enemigos adicionales
            // Los enemigos ya están cargados desde el mapa por LevelLoader
            return;
        }

        // Configuración P2 (Oponente)
        if (gameMode.equals("PVP") || gameMode.equals("MVM")) {
            // El oponente es otro Helado
            // El oponente es otro Helado
            PlayerType typeP2 = PlayerType.HUMAN;

            if (gameMode.equals("MVM")) {
                typeP2 = p2Difficulty;
            }

            IceCreamFlavor f2;
            try {
                f2 = IceCreamFlavor.valueOf(p2Selection);
            } catch (Exception e) {
                f2 = IceCreamFlavor.CHOCOLATE;
            } // Fallback

            playerCtrl.addPlayer("player2", f2, typeP2, p2X, p2Y);
            playerCtrl.setPlayerName("player2", p2Name);

            // REQUISITO: En PVP también debe haber un enemigo (Troll por defecto)
            if (gameMode.equals("PVP")) {
                enemyCtrl.spawnEnemy("TROLL", "pvp_bot", p2X, 1); // Spawn en esquina opuesta a P1
            }

        } else if (gameMode.equals("PVM")) {
            // En PVM, el oponente es un Helado controlado por máquina
            IceCreamFlavor f2;
            try {
                f2 = IceCreamFlavor.valueOf(p2Selection);
            } catch (Exception e) {
                f2 = IceCreamFlavor.CHOCOLATE;
            }

            // Usar la dificultad seleccionada (p2Difficulty debería estar configurada)
            PlayerType typeP2 = (p2Difficulty != null) ? p2Difficulty : PlayerType.MACHINE_HUNGRY;

            playerCtrl.addPlayer("player2", f2, typeP2, p2X, p2Y);
            playerCtrl.setPlayerName("player2", p2Name);
        }
    }

    /**
     * Spawn la siguiente oleada de frutas según la configuración personalizada
     * 
     * @return true si se spawneó una oleada, false si no hay más oleadas
     */
    private boolean spawnNextWave() {
        // Usar el LevelLoader para spawnear la oleada actual según la configuración
        // personalizada
        boolean spawned = levelLoader.spawnFruitWave(currentWave, boardCtrl, itemCtrl);
        waveSpawned = true;
        return spawned;
    }

    // =============================================================
    // GAME LOOP
    // =============================================================

    public void updateGameLoop(float dt) {
        if (status != GameStatus.PLAYING)
            return;

        timeElapsed += dt;
        if (timeElapsed >= maxTime) {
            status = GameStatus.TIMEOUT;
            determineWinnerByScore();
            return;
        }

        // Informar posición de jugadores a enemigos (solo si hay jugadores vivos)
        int[] p1Pos = playerCtrl.getPosition("player1");
        if (p1Pos != null && playerCtrl.isPlayerAlive("player1"))
            enemyCtrl.updatePlayerPos(p1Pos[0], p1Pos[1]);

        // Actualizar lógica
        itemCtrl.updateItems(dt);
        enemyCtrl.updateEnemies(dt);
        // Si hay bots (helados máquina), aquí deberías llamar a
        playerCtrl.updateBots(dt, enemyCtrl);

        checkCollisions();

        // Sistema de oleadas: cuando no hay frutas y no se ha spawneado esta oleada
        if (itemCtrl.getFruitCount() == 0) {
            if (!waveSpawned) {
                currentWave++;
                boolean moreWaves = spawnNextWave();

                // Si no hay más oleadas, determinar ganador
                if (!moreWaves) {
                    status = GameStatus.WON;
                    determineWinnerByScore();
                    return;
                }

                waveSpawned = true;
            }
        } else {
            // Hay frutas, resetear flag para permitir siguiente oleada
            waveSpawned = false;
        }
    }

    public void handlePlayerAction(String playerId, ActionType action, Direction dir) {
        if (status != GameStatus.PLAYING)
            return;
        try {
            // Solo procesar input si es humano
            // Podrías validar playerCtrl.getPlayer(playerId).getType() == HUMAN
            playerCtrl.performAction(playerId, action, dir);

            if (action == ActionType.MOVE)
                itemCtrl.onPlayerMove();
        } catch (BadOpoException e) {
            // Ignorar acción inválida
        }
    }

    public void togglePause() {
        if (status == GameStatus.PLAYING)
            status = GameStatus.PAUSED;
        else if (status == GameStatus.PAUSED)
            status = GameStatus.PLAYING;
    }

    // =============================================================
    // CONSULTAS VISTA (DTOs)
    // =============================================================

    /**
     * Retorna los objetos a dibujar: Items y Enemigos.
     * Los Jugadores NO se incluyen aqui, deben consultarse con
     * getPlayer1Info() y getPlayer2Info() por separado.
     * 
     * @return Lista de EntityInfo con items y enemigos
     */
    public List<EntityInfo> getObjectsToDraw() {
        List<EntityInfo> allObjects = new ArrayList<>();

        allObjects.addAll(itemCtrl.getItemInfo());
        allObjects.addAll(enemyCtrl.getEnemyInfo());

        return allObjects;
    }

    public boolean isWall(int x, int y) {
        return !boardCtrl.isWalkable(x, y);
    }

    public int getBoardWidth() {
        return boardCtrl.getWidth();
    }

    public int getBoardHeight() {
        return boardCtrl.getHeight();
    }

    public GameStatus getStatus() {
        return status;
    }

    public GameStatus getGameStatus() {
        return status;
    }

    public int getScore() {
        return scoreP1 + scoreP2; // Total score
    }

    public int getScoreP1() {
        return scoreP1;
    }

    public int getScoreP2() {
        return scoreP2;
    }

    public float getTimeRemaining() {
        return Math.max(0, maxTime - timeElapsed);
    }

    /**
     * Obtiene la informacion visual del Jugador 1 para la GUI.
     * La vista puede llamar directamente: EntityInfo p1 = domain.getPlayer1Info();
     * 
     * @return EntityInfo del jugador 1, o null si no existe o esta muerto
     */
    public EntityInfo getPlayer1Info() {
        return playerCtrl.getPlayer1Info();
    }

    /**
     * Obtiene la informacion visual del Jugador 2 para la GUI.
     * La vista puede llamar directamente: EntityInfo p2 = domain.getPlayer2Info();
     * 
     * @return EntityInfo del jugador 2, o null si no existe o esta muerto
     */
    public EntityInfo getPlayer2Info() {
        return playerCtrl.getPlayer2Info();
    }

    // =============================================================
    // INTERNO
    // =============================================================

    private void checkCollisions() {
        checkPlayerCollision("player1");
        checkPlayerCollision("player2");

        // Después de revisar colisiones, verificar si el juego debe terminar
        checkGameEndConditions();
    }

    private void checkPlayerCollision(String pid) {
        int[] pos = playerCtrl.getPosition(pid);
        if (pos == null)
            return;

        // Verificar si el jugador ya está muerto
        if (!playerCtrl.isPlayerAlive(pid))
            return;

        // Contra enemigos
        if (enemyCtrl.checkCollision(pos[0], pos[1])) {
            playerCtrl.killPlayer(pid);

            // Solo terminar inmediatamente en modo SINGLE
            if (gameMode.equals("SINGLE")) {
                status = GameStatus.GAME_OVER;
            }
            return; // No procesar más colisiones para este jugador
        }

        // Contra cactus con púas (peligroso)
        if (itemCtrl.hasDangerousCactusAt(pos[0], pos[1])) {
            playerCtrl.killPlayer(pid);

            // Solo terminar inmediatamente en modo SINGLE
            if (gameMode.equals("SINGLE")) {
                status = GameStatus.GAME_OVER;
            }
            return; // No procesar más colisiones para este jugador
        }

        // Contra Items
        int points = itemCtrl.collectItemAt(pos[0], pos[1]);
        if (points == -1) {
            // Item letal (fuego, etc.)
            playerCtrl.killPlayer(pid);

            // Solo terminar inmediatamente en modo SINGLE
            if (gameMode.equals("SINGLE")) {
                status = GameStatus.GAME_OVER;
            }
        } else if (points > 0) {
            if ("player1".equals(pid))
                scoreP1 += points;
            else if ("player2".equals(pid))
                scoreP2 += points;
        }
    }

    /**
     * Verifica si el juego debe terminar en modos multijugador
     */
    private void checkGameEndConditions() {
        // En modo SINGLE, el juego ya termina en checkPlayerCollision
        if (gameMode.equals("SINGLE"))
            return;

        // En modos multijugador (PVP, PVM, MVM)
        boolean p1Alive = playerCtrl.isPlayerAlive("player1");
        boolean p2Alive = playerCtrl.isPlayerAlive("player2");

        // Si ambos están muertos, Game Over
        if (!p1Alive && !p2Alive) {
            status = GameStatus.GAME_OVER;
            determineWinnerByScore();
        }
        // Si solo uno murió, establecer ganador provisional pero continuar jugando
        else if (!p1Alive && p2Alive) {
            winner = playerCtrl.getPlayerName("player2");
        } else if (p1Alive && !p2Alive) {
            winner = playerCtrl.getPlayerName("player1");
        }
    }

    /**
     * Determina el ganador basado en la puntuación.
     * Se llama cuando el juego termina por timeout o cuando se recolectan todas las
     * frutas.
     */
    private void determineWinnerByScore() {
        // En modo SINGLE no hay competencia
        if (gameMode.equals("SINGLE")) {
            winner = null;
            return;
        }

        // En modos multijugador, comparar puntuaciones
        if (scoreP1 > scoreP2) {
            winner = playerCtrl.getPlayerName("player1");
        } else if (scoreP2 > scoreP1) {
            winner = playerCtrl.getPlayerName("player2");
        } else {
            winner = "EMPATE";
        }
    }

    /**
     * Retorna una matriz de booleanos indicando dónde hay muros fijos.
     * true = Muro, false = Caminable.
     * Útil para que la Vista lo guarde y no pregunte celda por celda.
     */
    public boolean[][] getWallMap() {
        int w = boardCtrl.getWidth();
        int h = boardCtrl.getHeight();
        boolean[][] walls = new boolean[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                walls[y][x] = !boardCtrl.isWalkable(x, y);
            }
        }
        return walls;
    }

    public String getWinner() {
        return winner;
    }

    public String getGameMode() {
        return gameMode;
    }
}