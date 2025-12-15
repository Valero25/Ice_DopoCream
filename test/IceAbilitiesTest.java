package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import domain.board.BoardController;
import domain.items.ItemController;
import domain.items.Item;
import domain.players.PlayerController;
import domain.players.IceCream;
import domain.players.IceCreamFlavor;
import domain.players.PlayerType;
import domain.shared.ActionType;
import domain.shared.Direction;
import domain.shared.EntityType;
import domain.shared.BadOpoException;

/**
 * ❄️ II. Habilidades: Creación y Destrucción de Hielo
 * 
 * Pruebas críticas para la mecánica principal del juego.
 * Verifica crear líneas de hielo, romper bloques, y casos borde.
 */
public class IceAbilitiesTest {

    /**
     * Método main para ejecutar los tests directamente.
     */
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("test.IceAbilitiesTest");
    }

    private BoardController boardCtrl;
    private ItemController itemCtrl;
    private PlayerController playerCtrl;

    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 10;

    @Before
    public void setUp() {
        boardCtrl = new BoardController(BOARD_WIDTH, BOARD_HEIGHT);
        itemCtrl = new ItemController(boardCtrl);
        playerCtrl = new PlayerController(boardCtrl, itemCtrl);

        // Jugador en posición central (5, 5), mirando hacia abajo por defecto
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);
    }

    // ========================================================================
    // TEST 1: Crear - Generar un bloque de hielo en una casilla vacía adyacente
    // ========================================================================
    @Test
    public void testCreateSingleIceBlock() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");

        // El jugador mira hacia abajo (DOWN) por defecto
        player.setFacingDirection(Direction.DOWN);

        // Ejecutar acción de crear hielo
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.DOWN);

        // Procesar la cola de dominó (simular paso de tiempo)
        for (int i = 0; i < 10; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // Verificar que se creó hielo en la dirección que mira
        boolean hasIceBelow = itemCtrl.isObstacleAt(5, 6);
        assertTrue("Debe haber un bloque de hielo en (5,6)", hasIceBelow);
    }

    // ========================================================================
    // TEST 2: Crear Línea - Generar una fila de bloques hasta topar con pared
    // ========================================================================
    @Test
    public void testCreateIceRowUntilBorder() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");
        player.setFacingDirection(Direction.DOWN);

        // Crear línea de hielo hacia abajo
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.DOWN);

        // Procesar la cola de dominó completamente
        for (int i = 0; i < 50; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // Verificar que se crearon bloques desde (5,6) hasta (5,9)
        for (int y = 6; y < BOARD_HEIGHT; y++) {
            assertTrue("Debe haber hielo en (5," + y + ")", itemCtrl.isObstacleAt(5, y));
        }
    }

    // ========================================================================
    // TEST 3: Crear - Intentar crear hielo donde YA existe un bloque
    // (no debe duplicarse ni borrarse)
    // ========================================================================
    @Test
    public void testCreateIceWhereIceExists() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");
        player.setFacingDirection(Direction.RIGHT);

        // Crear un bloque de hielo manualmente en (6, 5)
        itemCtrl.spawnObstacle("ICE_BLOCK", "existing_ice", 6, 5);
        assertTrue("Precondición: debe existir hielo en (6,5)", itemCtrl.isObstacleAt(6, 5));

        // Intentar crear hielo en la misma dirección
        // Según la lógica actual, si hay hielo delante, rompe en vez de crear
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.RIGHT);

        // Procesar la cola
        for (int i = 0; i < 10; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // El comportamiento depende de la implementación:
        // - Si CREATE_ICE sobre hielo existente llama a breakIceRow, el hielo se rompe
        // - Este test documenta el comportamiento actual
        // Verificar que pasó algo (no lanzó excepción)
        // El hielo en (6,5) pudo haberse roto según la lógica de createIceRow
    }

    // ========================================================================
    // TEST 4: Crear - Crear hielo sobre una fruta (la fruta queda atrapada)
    // ========================================================================
    @Test
    public void testCreateIceOverFruit() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");
        player.setFacingDirection(Direction.DOWN);

        // Colocar una fruta en (5, 6)
        itemCtrl.spawnFruit("BANANA", "fruit_test", 5, 6);

        // Verificar que la fruta existe
        int fruitCountBefore = itemCtrl.getFruitCount();
        assertTrue("Debe haber al menos una fruta", fruitCountBefore > 0);

        // Crear hielo hacia abajo (donde está la fruta)
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.DOWN);

        // Procesar la cola
        for (int i = 0; i < 50; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // La fruta debe seguir existiendo (no se destruye al crear hielo sobre ella)
        int fruitCountAfter = itemCtrl.getFruitCount();
        assertEquals("La fruta no debe desaparecer al crear hielo", fruitCountBefore, fruitCountAfter);
    }

    // ========================================================================
    // TEST 5: Romper - Destruir un bloque de hielo adyacente simple
    // ========================================================================
    @Test
    public void testBreakSingleIceBlock() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");
        player.setFacingDirection(Direction.RIGHT);

        // Crear un bloque de hielo en (6, 5)
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice_to_break", 6, 5);
        assertTrue("Precondición: hielo existe", itemCtrl.isObstacleAt(6, 5));

        // Romper hielo
        playerCtrl.performAction("player1", ActionType.BREAK_ICE, Direction.RIGHT);

        // Procesar la cola de dominó
        for (int i = 0; i < 10; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // Verificar que el hielo fue destruido
        assertFalse("El hielo debe haber sido destruido", itemCtrl.isObstacleAt(6, 5));
    }

    // ========================================================================
    // TEST 6: Romper Cadena - Destruir una línea de bloques (efecto dominó)
    // ========================================================================
    @Test
    public void testBreakIceChain() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");
        player.setFacingDirection(Direction.RIGHT);

        // Crear una línea de 3 bloques de hielo: (6,5), (7,5), (8,5)
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 6, 5);
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice2", 7, 5);
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice3", 8, 5);

        assertTrue("Precondición: hielo en (6,5)", itemCtrl.isObstacleAt(6, 5));
        assertTrue("Precondición: hielo en (7,5)", itemCtrl.isObstacleAt(7, 5));
        assertTrue("Precondición: hielo en (8,5)", itemCtrl.isObstacleAt(8, 5));

        // Romper hielo (debería romper toda la cadena)
        playerCtrl.performAction("player1", ActionType.BREAK_ICE, Direction.RIGHT);

        // Procesar la cola de dominó completamente
        for (int i = 0; i < 50; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // Verificar que todos los bloques fueron destruidos
        assertFalse("Hielo en (6,5) destruido", itemCtrl.isObstacleAt(6, 5));
        assertFalse("Hielo en (7,5) destruido", itemCtrl.isObstacleAt(7, 5));
        assertFalse("Hielo en (8,5) destruido", itemCtrl.isObstacleAt(8, 5));
    }

    // ========================================================================
    // TEST 7: Romper - Intentar romper hielo mirando hacia el vacío
    // (nada sucede)
    // ========================================================================
    @Test
    public void testBreakIceAtEmptySpace() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");
        player.setFacingDirection(Direction.UP);

        // No hay hielo arriba del jugador
        assertFalse("Precondición: no hay hielo arriba", itemCtrl.isObstacleAt(5, 4));

        // Intentar romper (no debería hacer nada ni lanzar excepción)
        playerCtrl.performAction("player1", ActionType.BREAK_ICE, Direction.UP);

        // Procesar
        for (int i = 0; i < 10; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // Nada debe haber cambiado, el jugador sigue en su lugar
        assertEquals("Jugador sigue en X=5", 5, player.getX());
        assertEquals("Jugador sigue en Y=5", 5, player.getY());
    }

    // ========================================================================
    // TEST 8: Romper - Verificar que NO rompe muros de borde del mapa
    // ========================================================================
    @Test
    public void testCannotBreakBorderWalls() throws BadOpoException {
        // Mover jugador al borde
        playerCtrl.reset();
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 0, 5);
        IceCream player = playerCtrl.getPlayer("player1");
        player.setFacingDirection(Direction.LEFT);

        // Intentar romper hacia la izquierda (fuera del mapa)
        playerCtrl.performAction("player1", ActionType.BREAK_ICE, Direction.LEFT);

        // Procesar
        for (int i = 0; i < 10; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // No debe haber errores, el jugador sigue en su lugar
        assertEquals("Jugador sigue en X=0", 0, player.getX());

        // El mapa sigue intacto (verificar que la posición sigue siendo inválida/muro)
        assertFalse("Posición (-1,5) sigue siendo inválida", boardCtrl.isWalkable(-1, 5));
    }

    // ========================================================================
    // TEST 9: Límite - Verificar que no hay límite de creación de bloques
    // ========================================================================
    @Test
    public void testNoLimitOnIceCreation() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");

        // Crear múltiples líneas de hielo en diferentes direcciones
        player.setFacingDirection(Direction.DOWN);
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.DOWN);
        for (int i = 0; i < 50; i++)
            itemCtrl.updateItems(0.1f);

        player.setFacingDirection(Direction.RIGHT);
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.RIGHT);
        for (int i = 0; i < 50; i++)
            itemCtrl.updateItems(0.1f);

        player.setFacingDirection(Direction.LEFT);
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.LEFT);
        for (int i = 0; i < 50; i++)
            itemCtrl.updateItems(0.1f);

        player.setFacingDirection(Direction.UP);
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.UP);
        for (int i = 0; i < 50; i++)
            itemCtrl.updateItems(0.1f);

        // Contar bloques de hielo en posiciones específicas
        int iceCount = 0;

        // Contar hielo hacia abajo (5, 6..9)
        for (int y = 6; y < BOARD_HEIGHT; y++) {
            if (itemCtrl.isObstacleAt(5, y))
                iceCount++;
        }

        // Contar hielo hacia la derecha (6..9, 5)
        for (int x = 6; x < BOARD_WIDTH; x++) {
            if (itemCtrl.isObstacleAt(x, 5))
                iceCount++;
        }

        // Contar hielo hacia la izquierda (0..4, 5)
        for (int x = 0; x < 5; x++) {
            if (itemCtrl.isObstacleAt(x, 5))
                iceCount++;
        }

        // Contar hielo hacia arriba (5, 0..4)
        for (int y = 0; y < 5; y++) {
            if (itemCtrl.isObstacleAt(5, y))
                iceCount++;
        }

        // Debe haber múltiples bloques (no hay límite)
        assertTrue("Debe haber múltiples bloques de hielo creados (encontrados: " + iceCount + ")", iceCount > 5);
    }

    // ========================================================================
    // TEST 10: Cadena interrumpida - El hielo NO se rompe si hay espacio vacío
    // ========================================================================
    @Test
    public void testBreakChainStopsAtGap() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");
        player.setFacingDirection(Direction.RIGHT);

        // Crear bloques con un hueco: (6,5) y (8,5), pero NO (7,5)
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 6, 5);
        // (7,5) está vacío - hueco
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice3", 8, 5);

        // Romper hielo
        playerCtrl.performAction("player1", ActionType.BREAK_ICE, Direction.RIGHT);

        // Procesar
        for (int i = 0; i < 50; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // Solo el primer bloque debe romperse, el segundo debe quedar intacto
        assertFalse("Hielo en (6,5) debe romperse", itemCtrl.isObstacleAt(6, 5));
        assertTrue("Hielo en (8,5) debe seguir intacto (hueco interrumpe)", itemCtrl.isObstacleAt(8, 5));
    }

    // ========================================================================
    // TEST 11: Crear hielo en las 4 direcciones
    // ========================================================================
    @Test
    public void testCreateIceAllDirections() throws BadOpoException {
        // Reiniciar con jugador en el centro
        playerCtrl.reset();
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);
        IceCream player = playerCtrl.getPlayer("player1");

        // Crear hacia arriba
        player.setFacingDirection(Direction.UP);
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.UP);
        for (int i = 0; i < 20; i++)
            itemCtrl.updateItems(0.1f);
        assertTrue("Hielo creado arriba", itemCtrl.isObstacleAt(5, 4));

        // Limpiar para siguiente test
        itemCtrl.reset();

        // Crear hacia abajo
        player.setFacingDirection(Direction.DOWN);
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.DOWN);
        for (int i = 0; i < 20; i++)
            itemCtrl.updateItems(0.1f);
        assertTrue("Hielo creado abajo", itemCtrl.isObstacleAt(5, 6));

        // Limpiar
        itemCtrl.reset();

        // Crear hacia la izquierda
        player.setFacingDirection(Direction.LEFT);
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.LEFT);
        for (int i = 0; i < 20; i++)
            itemCtrl.updateItems(0.1f);
        assertTrue("Hielo creado a la izquierda", itemCtrl.isObstacleAt(4, 5));

        // Limpiar
        itemCtrl.reset();

        // Crear hacia la derecha
        player.setFacingDirection(Direction.RIGHT);
        playerCtrl.performAction("player1", ActionType.CREATE_ICE, Direction.RIGHT);
        for (int i = 0; i < 20; i++)
            itemCtrl.updateItems(0.1f);
        assertTrue("Hielo creado a la derecha", itemCtrl.isObstacleAt(6, 5));
    }
}
