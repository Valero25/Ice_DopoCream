package domain.level;

import domain.board.BoardController;
import domain.enemies.EnemyController;
import domain.items.ItemController;
import domain.players.PlayerController;
import domain.shared.BadOpoException;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Cargador de niveles que parsea mapas de texto y configura el juego.
 * Gestiona la creacion de entidades segun la configuracion personalizada
 * y el sistema de oleadas de frutas.
 * 
 * <p>Codigos del mapa de texto:</p>
 * <ul>
 *   <li># - Muro indestructible</li>
 *   <li>P - Posicion inicial del jugador</li>
 *   <li>. - Espacio vacio</li>
 *   <li>I - Bloque de hielo inicial</li>
 *   <li>C - Fogata</li>
 *   <li>H - Baldosa caliente</li>
 * </ul>
 * 
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Parsear el mapa de texto y crear la estructura del tablero</li>
 *   <li>Aplicar configuracion personalizada de enemigos y obstaculos</li>
 *   <li>Gestionar oleadas de frutas segun la configuracion</li>
 *   <li>Validar posiciones de spawn para evitar superposiciones</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see LevelConfiguration
 * @see BoardController
 */
public class LevelLoader implements java.io.Serializable {

    private Random random;
    private LevelConfiguration config;

    public LevelLoader() {
        this.random = new Random();
        this.config = null; // Se establece antes de cargar
    }

    /**
     * Establece la configuración personalizada para el siguiente nivel a cargar
     */
    public void setConfiguration(LevelConfiguration config) {
        this.config = config;
    }

    /**
     * Parsea un mapa de texto y puebla los controladores.
     * 
     * @param mapLayout: Array de Strings donde cada String es una fila del mapa.
     */
    public void loadLevel(String[] mapLayout,
            BoardController board,
            ItemController items,
            EnemyController enemies,
            PlayerController players) throws BadOpoException {

        if (mapLayout == null || mapLayout.length == 0) {
            throw new BadOpoException("El mapa está vacío.");
        }

        int height = mapLayout.length;
        int width = mapLayout[0].length();

        // Recorremos la matriz de texto
        for (int y = 0; y < height; y++) {
            String row = mapLayout[y];

            // Validación básica de dimensiones
            if (row.length() != width) {
                throw new BadOpoException(
                        "El mapa no es rectangular en la fila " + y + ". Esperado: " + width + ", Recibido: "
                                + row.length());
            }

            for (int x = 0; x < width; x++) {
                char code = row.charAt(x);
                processCell(code, x, y, board, items, enemies, players);
            }
        }
    }

    private void processCell(char code, int x, int y,
            BoardController board,
            ItemController items,
            EnemyController enemies,
            PlayerController players) {

        // ID único simple basado en coordenadas (ej: "item_5_5")
        String id = x + "_" + y;

        switch (code) {
            // --- ESTRUCTURA ---
            case '#': // Muro indestructible (Borde)
                // Aquí asumimos que BoardController tiene un método para poner muros fijos
                // O simplemente lo marcamos como NO caminable si tu Board lo soporta.
                // board.setWall(x, y);
                break;

            // --- JUGADOR ---
            case 'P':
                // No creamos jugadores aquí, se crean en setupEntitiesBasedOnMode
                // con el sabor elegido por el usuario
                break;

            // --- FRUTAS ---
            // Las frutas YA NO se spawnean del mapa, se manejan por oleadas
            case 'B':
            case 'G':
            case 'A':
            case 'R':
                // Ignorar, las frutas se spawnean por oleadas según configuración
                break;

            // --- OBSTÁCULOS ---
            case 'I':
                items.spawnObstacle("ICE_BLOCK", "obs_" + id, x, y);
                break;
            case 'C':
                items.spawnObstacle("CAMPFIRE", "obs_" + id, x, y);
                break;

            // --- TERRENO ---
            case 'H':
                items.spawnObstacle("HOT_TILE", "obs_" + id, x, y);
                break;

            // --- ENEMIGOS ---
            // Los enemigos YA NO se spawnean del mapa, se manejan por configuración
            case 'T':
            case 'F':
            case 'S':
            case 'N':
                // Ignorar, los enemigos se spawnean según configuración personalizada
                break;

            // --- ESPACIO VACÍO ---
            case '.':
            default:
                break;
        }
    }

    /**
     * Spawn enemigos y obstáculos según la configuración personalizada.
     * Las frutas NO se spawnean aquí, se manejan por oleadas.
     */
    public void applyCustomConfiguration(BoardController board,
            ItemController items,
            EnemyController enemies) {
        if (config == null) {
            return; // No hay configuración personalizada
        }

        // Obtener todas las posiciones válidas (vacías y caminables)
        // Para enemigos y obstáculos no necesitamos verificar items de calor
        List<int[]> validPositions = getValidSpawnPositionsSimple(board);

        if (validPositions.isEmpty()) {
            System.err.println("No hay posiciones válidas para spawn personalizado");
            return;
        }

        // NO spawneamos frutas aquí - se manejan por oleadas en DomainController

        // Spawn enemigos según configuración
        for (String enemyType : config.getActiveEnemyTypes()) {
            int count = config.getEnemyCount(enemyType);
            for (int i = 0; i < count && !validPositions.isEmpty(); i++) {
                int[] pos = getRandomPosition(validPositions);
                String id = "custom_enemy_" + enemyType + "_" + i;
                enemies.spawnEnemy(enemyType, id, pos[0], pos[1]);
            }
        }

        // Extensibilidad: obstáculos personalizados
        for (String obstacleType : config.getAllObstacles().keySet()) {
            int count = config.getObstacleCount(obstacleType);
            for (int i = 0; i < count && !validPositions.isEmpty(); i++) {
                int[] pos = getRandomPosition(validPositions);
                String id = "custom_obs_" + obstacleType + "_" + i;
                items.spawnObstacle(obstacleType, id, pos[0], pos[1]);
            }
        }
    }

    /**
     * Spawnea una oleada específica de frutas según la configuración.
     * 
     * @param waveIndex índice de la oleada (0 = primera fruta, 1 = segunda, etc.)
     * @return true si se spawneó una oleada, false si no hay más oleadas
     */
    public boolean spawnFruitWave(int waveIndex, BoardController board, ItemController items) {
        if (config == null) {
            return false;
        }

        List<String> activeFruits = config.getActiveFruitTypes();
        if (waveIndex >= activeFruits.size()) {
            return false; // No hay más oleadas
        }

        String fruitType = activeFruits.get(waveIndex);
        int count = config.getFruitCount(fruitType);

        // Obtener posiciones válidas (evitando items de calor)
        List<int[]> validPositions = getValidSpawnPositions(board, items);

        // Spawn todas las frutas de este tipo
        for (int i = 0; i < count && !validPositions.isEmpty(); i++) {
            int[] pos = getRandomPosition(validPositions);
            String id = "wave" + waveIndex + "_" + fruitType + "_" + i;
            items.spawnFruit(fruitType, id, pos[0], pos[1]);
        }

        return true; // Se spawneó correctamente
    }

    /**
     * Verifica si hay más oleadas disponibles
     */
    public boolean hasMoreWaves(int currentWave) {
        if (config == null) {
            return false;
        }
        List<String> activeFruits = config.getActiveFruitTypes();
        return currentWave < activeFruits.size();
    }

    /**
     * Obtiene todas las posiciones válidas para spawn de frutas.
     * Las frutas NO pueden aparecer sobre:
     * - Items de calor (CAMPFIRE o HOT_TILE)
     * - Bloques de hielo iniciales (ICE_BLOCK)
     * Las frutas pueden quedar DEBAJO de hielo creado por el jugador durante el
     * juego.
     */
    private List<int[]> getValidSpawnPositions(BoardController board, ItemController items) {
        List<int[]> positions = new ArrayList<>();
        int width = board.getWidth();
        int height = board.getHeight();

        // Evitar bordes y posiciones iniciales de jugadores
        for (int y = 2; y < height - 2; y++) {
            for (int x = 2; x < width - 2; x++) {
                // Evitar esquinas donde spawneam jugadores
                if ((x <= 2 && y <= 2) || (x >= width - 3 && y >= height - 3)) {
                    continue;
                }

                // Verificar que la celda sea caminable
                if (!board.isWalkable(x, y)) {
                    continue;
                }

                // Verificar que NO haya items de calor u obstáculos en esta posición
                if (hasBlockingItemAt(x, y, items)) {
                    continue;
                }

                // La posición es válida
                positions.add(new int[] { x, y });
            }
        }

        return positions;
    }

    /**
     * Verifica si hay un item que bloquea el spawn de frutas en la posición dada.
     * Incluye: CAMPFIRE, HOT_TILE, ICE_BLOCK (bloques de hielo iniciales)
     */
    private boolean hasBlockingItemAt(int x, int y, ItemController itemCtrl) {
        List<domain.items.Item> allItems = itemCtrl.getItems();
        for (domain.items.Item item : allItems) {
            if (item.getX() == x && item.getY() == y) {
                String type = item.getType();
                // Verificar si es un item que bloquea spawn de frutas
                if ("HOT_TILE".equals(type) || "CAMPFIRE".equals(type) ||
                        "CAMPFIRE_OFF".equals(type) || "ICE".equals(type) || "ICE_BLOCK".equals(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Obtiene posiciones válidas simples (para enemigos y obstáculos)
     * sin verificar items de calor.
     */
    private List<int[]> getValidSpawnPositionsSimple(BoardController board) {
        List<int[]> positions = new ArrayList<>();
        int width = board.getWidth();
        int height = board.getHeight();

        // Evitar bordes y posiciones iniciales de jugadores
        for (int y = 2; y < height - 2; y++) {
            for (int x = 2; x < width - 2; x++) {
                // Evitar esquinas donde spawneam jugadores
                if ((x <= 2 && y <= 2) || (x >= width - 3 && y >= height - 3)) {
                    continue;
                }

                if (board.isWalkable(x, y)) {
                    positions.add(new int[] { x, y });
                }
            }
        }

        return positions;
    }

    /**
     * Obtiene y remueve una posición aleatoria de la lista
     */
    private int[] getRandomPosition(List<int[]> positions) {
        if (positions.isEmpty()) {
            return new int[] { 5, 5 }; // Fallback
        }
        int index = random.nextInt(positions.size());
        return positions.remove(index);
    }
}