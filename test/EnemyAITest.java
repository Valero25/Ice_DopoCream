package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import domain.board.BoardController;
import domain.items.ItemController;
import domain.enemies.EnemyController;
import domain.enemies.Enemy;
import domain.players.PlayerController;
import domain.players.IceCream;
import domain.players.IceCreamFlavor;
import domain.players.PlayerType;
import domain.shared.Direction;
import domain.shared.BadOpoException;

/**
 * 👾 IV. Inteligencia de Enemigos (AI)
 * 
 * Pruebas para verificar los patrones de movimiento de los enemigos.
 * Verifica Troll, Maceta (FlowerPot), Calamar (Squid) y Narval (Narwhal).
 */
public class EnemyAITest {

    /**
     * Método main para ejecutar los tests directamente.
     */
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("test.EnemyAITest");
    }

    private BoardController boardCtrl;
    private ItemController itemCtrl;
    private EnemyController enemyCtrl;
    private PlayerController playerCtrl;

    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 10;

    @Before
    public void setUp() {
        boardCtrl = new BoardController(BOARD_WIDTH, BOARD_HEIGHT);
        itemCtrl = new ItemController(boardCtrl);
        enemyCtrl = new EnemyController(boardCtrl, itemCtrl);
        playerCtrl = new PlayerController(boardCtrl, itemCtrl);
    }

    // ========================================================================
    // TEST 1: General - Enemigo toca Jugador -> Jugador muere
    // ========================================================================
    @Test
    public void testEnemyTouchesPlayerCollision() throws BadOpoException {
        // Colocar jugador en (5, 5)
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);

        // Colocar enemigo en la misma posición
        enemyCtrl.spawnEnemy("TROLL", "troll1", 5, 5);

        // Verificar colisión
        boolean collision = enemyCtrl.checkCollision(5, 5);

        assertTrue("Debe detectar colisión cuando enemigo y jugador están en la misma posición", collision);
    }

    // ========================================================================
    // TEST 2: Troll - Se mueve en línea recta
    // ========================================================================
    @Test
    public void testTrollMovesStraight() {
        // Colocar troll
        enemyCtrl.spawnEnemy("TROLL", "troll1", 5, 5);

        // Actualizar posición del jugador (para que el troll tenga un objetivo)
        enemyCtrl.updatePlayerPos(5, 8);

        // Obtener posición inicial
        Enemy troll = null;
        for (Enemy e : enemyCtrl.getEnemies()) {
            if (e.getId().equals("troll1")) {
                troll = e;
                break;
            }
        }
        assertNotNull("Troll debe existir", troll);

        int initialX = troll.getX();
        int initialY = troll.getY();

        // Simular varios updates
        for (int i = 0; i < 20; i++) {
            enemyCtrl.updateEnemies(0.5f);
        }

        // El troll debe haberse movido (en alguna dirección)
        boolean moved = (troll.getX() != initialX || troll.getY() != initialY);
        assertTrue("El troll debe moverse", moved);
    }

    // ========================================================================
    // TEST 3: Troll - NO rompe bloques de hielo
    // ========================================================================
    @Test
    public void testTrollCannotBreakIce() {
        // Colocar troll
        enemyCtrl.spawnEnemy("TROLL", "troll1", 5, 5);

        // Colocar bloque de hielo entre el troll y el jugador
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 5, 6);

        // El jugador está abajo del hielo
        enemyCtrl.updatePlayerPos(5, 9);

        // Simular varios updates
        for (int i = 0; i < 20; i++) {
            enemyCtrl.updateEnemies(0.5f);
        }

        // El hielo debe seguir existiendo
        assertTrue("El hielo debe seguir existiendo (Troll no lo rompe)", itemCtrl.isObstacleAt(5, 6));
    }

    // ========================================================================
    // TEST 4: FlowerPot (Maceta) - Persigue al jugador
    // ========================================================================
    @Test
    public void testFlowerPotChasesPlayer() {
        // Colocar maceta lejos del jugador
        enemyCtrl.spawnEnemy("FLOWERPOT", "pot1", 2, 2);

        // El jugador está en (8, 8)
        enemyCtrl.updatePlayerPos(8, 8);

        Enemy pot = null;
        for (Enemy e : enemyCtrl.getEnemies()) {
            if (e.getId().equals("pot1")) {
                pot = e;
                break;
            }
        }
        assertNotNull("Maceta debe existir", pot);

        int initialDistX = Math.abs(pot.getX() - 8);
        int initialDistY = Math.abs(pot.getY() - 8);
        int initialDist = initialDistX + initialDistY;

        // Simular varios updates
        for (int i = 0; i < 30; i++) {
            enemyCtrl.updateEnemies(0.5f);
        }

        // La maceta debe haberse acercado al jugador
        int newDistX = Math.abs(pot.getX() - 8);
        int newDistY = Math.abs(pot.getY() - 8);
        int newDist = newDistX + newDistY;

        assertTrue(
                "La maceta debe acercarse al jugador (distancia inicial: " + initialDist + ", nueva: " + newDist + ")",
                newDist < initialDist);
    }

    // ========================================================================
    // TEST 5: FlowerPot - Se detiene ante bloques de hielo
    // ========================================================================
    @Test
    public void testFlowerPotStopsAtIce() {
        // Colocar maceta
        enemyCtrl.spawnEnemy("FLOWERPOT", "pot1", 5, 3);

        // Colocar muro de hielo que bloquea el camino
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 5, 4);
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice2", 4, 4);
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice3", 6, 4);

        // El jugador está abajo del muro de hielo
        enemyCtrl.updatePlayerPos(5, 8);

        Enemy pot = null;
        for (Enemy e : enemyCtrl.getEnemies()) {
            if (e.getId().equals("pot1")) {
                pot = e;
                break;
            }
        }

        // Simular updates
        for (int i = 0; i < 30; i++) {
            enemyCtrl.updateEnemies(0.5f);
        }

        // La maceta NO debe atravesar el hielo, debe quedarse en Y <= 3
        assertTrue("La maceta no debe atravesar el hielo", pot.getY() <= 4);
    }

    // ========================================================================
    // TEST 6: Squid (Calamar) - Puede romper hielo
    // ========================================================================
    @Test
    public void testSquidBreaksIce() {
        // Colocar calamar
        enemyCtrl.spawnEnemy("SQUID", "squid1", 5, 3);

        // Colocar bloque de hielo
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 5, 4);

        // El jugador está abajo del hielo
        enemyCtrl.updatePlayerPos(5, 8);

        // Simular varios updates (el calamar debería romper el hielo)
        for (int i = 0; i < 30; i++) {
            enemyCtrl.updateEnemies(0.5f);
        }

        // El hielo debe haber sido destruido por el calamar
        assertFalse("El calamar debe romper el hielo", itemCtrl.isObstacleAt(5, 4));
    }

    // ========================================================================
    // TEST 7: Narwhal (Narval) - Puede romper hielo
    // ========================================================================
    @Test
    public void testNarwhalBreaksIce() {
        // Colocar narval
        enemyCtrl.spawnEnemy("NARWHAL", "narwhal1", 5, 3);

        // Colocar bloque de hielo
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 5, 4);

        // El jugador está abajo (alineado verticalmente para que el narval cargue)
        enemyCtrl.updatePlayerPos(5, 8);

        // Simular varios updates
        for (int i = 0; i < 30; i++) {
            enemyCtrl.updateEnemies(0.5f);
        }

        // El hielo debe haber sido destruido
        assertFalse("El narval debe romper el hielo durante su carga", itemCtrl.isObstacleAt(5, 4));
    }

    // ========================================================================
    // TEST 8: Spawn de múltiples enemigos
    // ========================================================================
    @Test
    public void testSpawnMultipleEnemies() {
        enemyCtrl.spawnEnemy("TROLL", "troll1", 1, 1);
        enemyCtrl.spawnEnemy("TROLL", "troll2", 2, 2);
        enemyCtrl.spawnEnemy("FLOWERPOT", "pot1", 3, 3);
        enemyCtrl.spawnEnemy("SQUID", "squid1", 4, 4);

        assertEquals("Debe haber 4 enemigos", 4, enemyCtrl.getEnemies().size());
    }

    // ========================================================================
    // TEST 9: Enemigo no se spawn fuera del mapa
    // ========================================================================
    @Test
    public void testEnemyCannotSpawnOutOfBounds() {
        // Intentar spawn fuera del mapa
        enemyCtrl.spawnEnemy("TROLL", "invalid_troll", -1, -1);

        // No debe haberse creado
        assertEquals("No debe haber enemigos (spawn inválido)", 0, enemyCtrl.getEnemies().size());
    }

    // ========================================================================
    // TEST 10: No colisión cuando enemigo está lejos
    // ========================================================================
    @Test
    public void testNoCollisionWhenFar() {
        enemyCtrl.spawnEnemy("TROLL", "troll1", 1, 1);

        // El jugador está lejos en (9, 9)
        boolean collision = enemyCtrl.checkCollision(9, 9);

        assertFalse("No debe haber colisión cuando están lejos", collision);
    }

    // ========================================================================
    // TEST 11: Múltiples enemigos - Uno cerca, uno lejos
    // ========================================================================
    @Test
    public void testMultipleEnemiesCollision() {
        enemyCtrl.spawnEnemy("TROLL", "troll1", 1, 1); // Lejos
        enemyCtrl.spawnEnemy("TROLL", "troll2", 5, 5); // En la posición del jugador

        // Verificar colisión en (5, 5)
        boolean collision = enemyCtrl.checkCollision(5, 5);

        assertTrue("Debe detectar colisión con troll2", collision);
    }

    // ========================================================================
    // TEST 12: Reset limpia los enemigos
    // ========================================================================
    @Test
    public void testResetClearsEnemies() {
        enemyCtrl.spawnEnemy("TROLL", "troll1", 1, 1);
        enemyCtrl.spawnEnemy("TROLL", "troll2", 2, 2);

        assertEquals("Debe haber 2 enemigos", 2, enemyCtrl.getEnemies().size());

        enemyCtrl.reset();

        assertEquals("Después del reset, 0 enemigos", 0, enemyCtrl.getEnemies().size());
    }

    // ========================================================================
    // TEST 13: EnemyInfo para la GUI
    // ========================================================================
    @Test
    public void testEnemyInfoForGUI() {
        enemyCtrl.spawnEnemy("TROLL", "troll1", 3, 4);

        var infoList = enemyCtrl.getEnemyInfo();

        assertEquals("Debe haber 1 info de enemigo", 1, infoList.size());
        assertEquals("ID correcto", "troll1", infoList.get(0).id);
        assertEquals("X correcto", 3, infoList.get(0).x);
        assertEquals("Y correcto", 4, infoList.get(0).y);
    }
}
