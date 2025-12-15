package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import domain.board.BoardController;
import domain.items.ItemController;
import domain.players.PlayerController;
import domain.players.IceCream;
import domain.players.IceCreamFlavor;
import domain.players.PlayerType;
import domain.shared.ActionType;
import domain.shared.Direction;
import domain.shared.BadOpoException;

/**
 * 🔥 V. Obstáculos Especiales
 * 
 * Pruebas de interacción con el entorno (Fogata, Baldosas Calientes, IceBlock).
 */
public class ObstacleTest {

    /**
     * Método main para ejecutar los tests directamente.
     */
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("test.ObstacleTest");
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
    }

    // ========================================================================
    // TEST 1: IceBlock - Se puede crear y bloquea movimiento
    // ========================================================================
    @Test
    public void testIceBlockSpawnAndBlocks() throws BadOpoException {
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);
        IceCream player = playerCtrl.getPlayer("player1");

        // Colocar bloque de hielo
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 5, 6);

        // Verificar que el obstáculo existe
        assertTrue("Debe existir un obstáculo en (5,6)", itemCtrl.isObstacleAt(5, 6));

        // Intentar mover al jugador hacia el hielo
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.DOWN);

        // El jugador no debe poder moverse
        assertEquals("Jugador bloqueado por hielo", 5, player.getY());
    }

    // ========================================================================
    // TEST 2: IceBlock es destructible
    // ========================================================================
    @Test
    public void testIceBlockIsDestructible() throws BadOpoException {
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);
        IceCream player = playerCtrl.getPlayer("player1");

        // Colocar bloque de hielo a la derecha
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 6, 5);
        assertTrue("Precondición: hielo existe", itemCtrl.isObstacleAt(6, 5));

        // Romper
        player.setFacingDirection(Direction.RIGHT);
        playerCtrl.performAction("player1", ActionType.BREAK_ICE, Direction.RIGHT);

        for (int i = 0; i < 20; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // El hielo debe haber sido destruido
        assertFalse("El hielo debe ser destruido", itemCtrl.isObstacleAt(6, 5));
    }

    // ========================================================================
    // TEST 3: Múltiples IceBlocks
    // ========================================================================
    @Test
    public void testMultipleIceBlocks() {
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 1, 1);
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice2", 2, 2);
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice3", 3, 3);

        assertTrue("Debe haber obstáculo en (1,1)", itemCtrl.isObstacleAt(1, 1));
        assertTrue("Debe haber obstáculo en (2,2)", itemCtrl.isObstacleAt(2, 2));
        assertTrue("Debe haber obstáculo en (3,3)", itemCtrl.isObstacleAt(3, 3));
    }

    // ========================================================================
    // TEST 4: Reset limpia todos los items
    // ========================================================================
    @Test
    public void testResetClearsAll() {
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 1, 1);
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice2", 2, 2);
        itemCtrl.spawnFruit("BANANA", "fruit1", 3, 3);

        assertTrue("Items existen antes del reset", itemCtrl.getItems().size() > 0);

        itemCtrl.reset();

        assertEquals("Después del reset, 0 items", 0, itemCtrl.getItems().size());
    }

    // ========================================================================
    // TEST 5: ItemInfo para la GUI muestra tipo correcto
    // ========================================================================
    @Test
    public void testItemInfoForGUI() {
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 3, 4);

        var infoList = itemCtrl.getItemInfo();

        assertEquals("Debe haber 1 info de item", 1, infoList.size());
        assertEquals("ID correcto", "ice1", infoList.get(0).id);
        assertEquals("X correcto", 3, infoList.get(0).x);
        assertEquals("Y correcto", 4, infoList.get(0).y);
        // IceBlock retorna "ICE" como tipo
        assertEquals("Tipo correcto", "ICE", infoList.get(0).type);
    }

    // ========================================================================
    // TEST 6: Cactus peligroso después de 5 segundos (SPIKE_INTERVAL)
    // ========================================================================
    @Test
    public void testDangerousCactusAfterTime() {
        // Colocar cactus
        itemCtrl.spawnFruit("CACTUS", "cactus1", 5, 5);

        // El cactus debe ser seguro inicialmente
        assertFalse("Cactus seguro inicialmente", itemCtrl.hasDangerousCactusAt(5, 5));

        // Simular 5+ segundos para que el cactus se vuelva peligroso
        for (int i = 0; i < 52; i++) { // 5.2 segundos
            itemCtrl.updateItems(0.1f);
        }

        // Verificar si hay cactus peligroso
        boolean isDangerous = itemCtrl.hasDangerousCactusAt(5, 5);
        assertTrue("El cactus debe ser peligroso después de 5 segundos", isDangerous);
    }

    // ========================================================================
    // TEST 7: Cactus seguro inicialmente
    // ========================================================================
    @Test
    public void testCactusSafeInitially() {
        itemCtrl.spawnFruit("CACTUS", "cactus1", 5, 5);

        // Sin pasar tiempo, el cactus debe ser seguro
        boolean isDangerous = itemCtrl.hasDangerousCactusAt(5, 5);

        assertFalse("El cactus debe ser seguro inicialmente", isDangerous);
    }

    // ========================================================================
    // TEST 8: Cactus vuelve a ser seguro después de las púas (ciclo cada 5s)
    // ========================================================================
    @Test
    public void testCactusCycles() {
        itemCtrl.spawnFruit("CACTUS", "cactus1", 5, 5);

        // Tiempo para completar un ciclo completo: 5s peligroso, 5s más = seguro
        // Primero simular 5s para que se vuelva peligroso
        for (int i = 0; i < 52; i++) { // 5.2 segundos
            itemCtrl.updateItems(0.1f);
        }
        assertTrue("Cactus debería ser peligroso ahora", itemCtrl.hasDangerousCactusAt(5, 5));

        // Simular otros 5s para que vuelva a ser seguro
        for (int i = 0; i < 52; i++) { // 5.2 segundos más
            itemCtrl.updateItems(0.1f);
        }

        // Debería haber ciclado de vuelta a seguro
        boolean isDangerous = itemCtrl.hasDangerousCactusAt(5, 5);
        assertFalse("El cactus debe volver a ser seguro después del ciclo", isDangerous);
    }

    // ========================================================================
    // TEST 9: HotTile - Se puede crear
    // ========================================================================
    @Test
    public void testHotTileSpawn() {
        itemCtrl.spawnObstacle("HOT_TILE", "hottile1", 5, 5);

        // Verificar que existe un item en esa posición
        boolean hasItem = false;
        for (domain.items.Item item : itemCtrl.getItems()) {
            if (item.getX() == 5 && item.getY() == 5) {
                hasItem = true;
                break;
            }
        }
        assertTrue("Debe existir HotTile en (5,5)", hasItem);
    }

    // ========================================================================
    // TEST 10: HotTile - IceBlock no puede aparecer sobre ella (se derrite)
    // ========================================================================
    @Test
    public void testHotTileMeltsIce() {
        // Primero colocar la baldosa caliente
        itemCtrl.spawnObstacle("HOT_TILE", "hottile1", 5, 5);

        // Intentar colocar hielo sobre la baldosa caliente
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 5, 5);

        // El hielo no debería poder existir sobre la baldosa caliente
        // (canSpawnAt retorna false para HotTile)
        boolean hasIce = false;
        for (domain.items.Item item : itemCtrl.getItems()) {
            if (item.getX() == 5 && item.getY() == 5 && item.getType().equals("ICE")) {
                hasIce = true;
                break;
            }
        }
        assertFalse("El hielo no puede aparecer sobre HotTile", hasIce);
    }
}
