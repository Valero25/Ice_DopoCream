package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import domain.level.LevelConfiguration;
import domain.level.LevelLoader;
import domain.board.BoardController;
import domain.items.ItemController;
import domain.enemies.EnemyController;

/**
 * ⚙️ VII. Gestión de Niveles y Configuración
 * 
 * Pruebas para la configuración personalizada de niveles.
 * Verifica LevelConfiguration, LevelLoader, y oleadas de frutas.
 */
public class LevelConfigTest {

    /**
     * Método main para ejecutar los tests directamente.
     */
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("test.LevelConfigTest");
    }

    private LevelConfiguration config;
    private LevelLoader loader;
    private BoardController boardCtrl;
    private ItemController itemCtrl;
    private EnemyController enemyCtrl;

    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 10;

    @Before
    public void setUp() {
        config = new LevelConfiguration();
        loader = new LevelLoader();
        boardCtrl = new BoardController(BOARD_WIDTH, BOARD_HEIGHT);
        itemCtrl = new ItemController(boardCtrl);
        enemyCtrl = new EnemyController(boardCtrl, itemCtrl);
    }

    // ========================================================================
    // TEST 1: LevelConfiguration - Valores por defecto
    // ========================================================================
    @Test
    public void testDefaultConfiguration() {
        // La configuración tiene valores por defecto
        assertTrue("Debe haber frutas por defecto", config.getActiveFruitTypes().size() > 0);
        assertTrue("Debe haber enemigos por defecto", config.getActiveEnemyTypes().size() > 0);
    }

    // ========================================================================
    // TEST 2: LevelConfiguration - Configurar frutas
    // ========================================================================
    @Test
    public void testSetFruitCount() {
        config.setFruitCount("BANANA", 10);

        assertEquals("Debe haber 10 bananas configuradas", 10, config.getFruitCount("BANANA"));
    }

    // ========================================================================
    // TEST 3: LevelConfiguration - Configurar enemigos
    // ========================================================================
    @Test
    public void testSetEnemyCount() {
        config.setEnemyCount("TROLL", 5);

        assertEquals("Debe haber 5 trolls configurados", 5, config.getEnemyCount("TROLL"));
    }

    // ========================================================================
    // TEST 4: LevelConfiguration - Reset a defaults
    // ========================================================================
    @Test
    public void testResetToDefaults() {
        // Cambiar configuración
        config.setFruitCount("BANANA", 100);
        config.setEnemyCount("TROLL", 50);

        // Reset
        config.resetToDefaults();

        // Debe tener valores por defecto, no los personalizados
        assertNotEquals("BANANA no debe tener 100", 100, config.getFruitCount("BANANA"));
        assertNotEquals("TROLL no debe tener 50", 50, config.getEnemyCount("TROLL"));
    }

    // ========================================================================
    // TEST 5: LevelConfiguration - Copiar configuración
    // ========================================================================
    @Test
    public void testCopyConfiguration() {
        config.setFruitCount("CHERRY", 15);

        LevelConfiguration copy = config.copy();

        assertEquals("La copia debe tener los mismos valores", 15, copy.getFruitCount("CHERRY"));

        // Modificar original no afecta la copia
        config.setFruitCount("CHERRY", 99);
        assertEquals("La copia debe ser independiente", 15, copy.getFruitCount("CHERRY"));
    }

    // ========================================================================
    // TEST 6: LevelConfiguration - Parámetros personalizados
    // ========================================================================
    @Test
    public void testCustomParameters() {
        config.setCustomParameter("difficulty", "HARD");
        config.setCustomParameter("timeLimit", 120);

        assertEquals("Parámetro difficulty", "HARD", config.getCustomParameter("difficulty"));
        assertEquals("Parámetro timeLimit", 120, config.getCustomParameter("timeLimit"));
    }

    // ========================================================================
    // TEST 7: LevelConfiguration - Parámetro no existente con default
    // ========================================================================
    @Test
    public void testCustomParameterWithDefault() {
        Object value = config.getCustomParameter("noExiste", "valorDefault");

        assertEquals("Debe retornar el valor por defecto", "valorDefault", value);
    }

    // ========================================================================
    // TEST 8: LevelLoader - Establecer configuración
    // ========================================================================
    @Test
    public void testSetConfiguration() {
        loader.setConfiguration(config);

        // Verificar que hay oleadas disponibles
        assertTrue("Debe haber oleadas con configuración", loader.hasMoreWaves(0));
    }

    // ========================================================================
    // TEST 9: LevelLoader - Spawn oleada de frutas
    // ========================================================================
    @Test
    public void testSpawnFruitWave() {
        config.setFruitCount("BANANA", 3);
        loader.setConfiguration(config);

        // Spawn primera oleada
        boolean spawned = loader.spawnFruitWave(0, boardCtrl, itemCtrl);

        assertTrue("Debe haber spawneado la oleada", spawned);
        assertTrue("Debe haber frutas en el tablero", itemCtrl.getFruitCount() > 0);
    }

    // ========================================================================
    // TEST 10: LevelLoader - Verificar más oleadas
    // ========================================================================
    @Test
    public void testHasMoreWaves() {
        loader.setConfiguration(config);

        // El número de oleadas es igual al número de tipos de fruta activos
        int totalWaves = config.getActiveFruitTypes().size();

        assertTrue("Hay oleada 0", loader.hasMoreWaves(0));
        assertTrue("Hay oleada en el medio", loader.hasMoreWaves(totalWaves / 2));
        assertFalse("No hay oleada mayor o igual al total", loader.hasMoreWaves(totalWaves));
    }

    // ========================================================================
    // TEST 11: LevelLoader - Sin configuración no hay oleadas
    // ========================================================================
    @Test
    public void testNoConfigurationNoWaves() {
        // No establecer configuración
        assertFalse("Sin config, no hay oleadas", loader.hasMoreWaves(0));
        assertFalse("Sin config, spawn retorna false", loader.spawnFruitWave(0, boardCtrl, itemCtrl));
    }

    // ========================================================================
    // TEST 12: LevelLoader - Aplicar configuración de enemigos
    // ========================================================================
    @Test
    public void testApplyCustomConfigurationEnemies() {
        config.setEnemyCount("TROLL", 2);
        config.setEnemyCount("SQUID", 1);
        loader.setConfiguration(config);

        // Aplicar configuración
        loader.applyCustomConfiguration(boardCtrl, itemCtrl, enemyCtrl);

        // Verificar que se crearon enemigos
        assertTrue("Debe haber enemigos", enemyCtrl.getEnemies().size() > 0);
    }

    // ========================================================================
    // TEST 13: LevelConfiguration - Obtener todos los tipos activos de fruta
    // ========================================================================
    @Test
    public void testGetActiveFruitTypes() {
        config.setFruitCount("BANANA", 5);
        config.setFruitCount("GRAPE", 0); // Cero = inactivo
        config.setFruitCount("CHERRY", 3);

        var activeFruits = config.getActiveFruitTypes();

        assertTrue("BANANA debe estar activa", activeFruits.contains("BANANA"));
        assertTrue("CHERRY debe estar activa", activeFruits.contains("CHERRY"));
        // GRAPE tiene 0, no debería estar activa
    }

    // ========================================================================
    // TEST 14: LevelConfiguration - Configurar obstáculos
    // ========================================================================
    @Test
    public void testSetObstacleCount() {
        config.setObstacleCount("ICE_BLOCK", 10);

        assertEquals("Debe haber 10 ICE_BLOCK configurados", 10, config.getObstacleCount("ICE_BLOCK"));
    }

    // ========================================================================
    // TEST 15: LevelConfiguration - Obtener todos los obstáculos
    // ========================================================================
    @Test
    public void testGetAllObstacles() {
        config.setObstacleCount("ICE_BLOCK", 5);
        config.setObstacleCount("CAMPFIRE", 2);

        var obstacles = config.getAllObstacles();

        assertTrue("Debe contener ICE_BLOCK", obstacles.containsKey("ICE_BLOCK"));
        assertTrue("Debe contener CAMPFIRE", obstacles.containsKey("CAMPFIRE"));
    }
}
