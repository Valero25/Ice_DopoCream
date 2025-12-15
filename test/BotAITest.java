package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import domain.board.BoardController;
import domain.items.ItemController;
import domain.enemies.EnemyController;
import domain.players.PlayerController;
import domain.players.IceCream;
import domain.players.BotPlayer;
import domain.players.HungryBot;
import domain.players.FearfulBot;
import domain.players.ExpertBot;
import domain.players.IceCreamFlavor;
import domain.players.PlayerType;
import domain.shared.Direction;
import domain.shared.EntityInfo;
import domain.shared.BadOpoException;

import java.util.ArrayList;
import java.util.List;

/**
 * 🤖 VI. Modos de Juego y Máquinas (Helados AI)
 * 
 * Pruebas para los controladores automáticos de los helados (Bots).
 * Verifica HungryBot, FearfulBot, ExpertBot y sus comportamientos.
 */
public class BotAITest {

    /**
     * Método main para ejecutar los tests directamente.
     */
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("test.BotAITest");
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
    // TEST 1: HungryBot - Se crea correctamente como bot
    // ========================================================================
    @Test
    public void testHungryBotIsBot() {
        playerCtrl.addPlayer("bot1", IceCreamFlavor.VANILLA, PlayerType.MACHINE_HUNGRY, 5, 5);
        IceCream bot = playerCtrl.getPlayer("bot1");

        assertNotNull("Bot debe existir", bot);
        assertTrue("HungryBot debe ser un bot", bot.isBot());
    }

    // ========================================================================
    // TEST 2: FearfulBot - Se crea correctamente como bot
    // ========================================================================
    @Test
    public void testFearfulBotIsBot() {
        playerCtrl.addPlayer("bot1", IceCreamFlavor.STRAWBERRY, PlayerType.MACHINE_FEARFUL, 5, 5);
        IceCream bot = playerCtrl.getPlayer("bot1");

        assertNotNull("Bot debe existir", bot);
        assertTrue("FearfulBot debe ser un bot", bot.isBot());
    }

    // ========================================================================
    // TEST 3: ExpertBot - Se crea correctamente como bot
    // ========================================================================
    @Test
    public void testExpertBotIsBot() {
        playerCtrl.addPlayer("bot1", IceCreamFlavor.CHOCOLATE, PlayerType.MACHINE_EXPERT, 5, 5);
        IceCream bot = playerCtrl.getPlayer("bot1");

        assertNotNull("Bot debe existir", bot);
        assertTrue("ExpertBot debe ser un bot", bot.isBot());
    }

    // ========================================================================
    // TEST 4: Jugador humano NO es bot
    // ========================================================================
    @Test
    public void testHumanIsNotBot() {
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);
        IceCream player = playerCtrl.getPlayer("player1");

        assertNotNull("Jugador debe existir", player);
        assertFalse("Jugador humano NO debe ser bot", player.isBot());
    }

    // ========================================================================
    // TEST 5: HungryBot - Detecta fruta más cercana
    // ========================================================================
    @Test
    public void testHungryBotFindNearestFruit() {
        HungryBot bot = new HungryBot("hungry1", 5, 5, IceCreamFlavor.VANILLA);

        // Crear lista de frutas simuladas
        List<EntityInfo> fruits = new ArrayList<>();
        fruits.add(new EntityInfo("f1", 8, 8, "BANANA", false)); // Lejos
        fruits.add(new EntityInfo("f2", 6, 5, "GRAPE", false)); // Cerca (distancia 1)
        fruits.add(new EntityInfo("f3", 9, 9, "CHERRY", false)); // Muy lejos

        List<EntityInfo> enemies = new ArrayList<>();
        boolean[] canMove = { true, true, true, true }; // Puede moverse en todas direcciones

        // El bot debe moverse hacia la fruta más cercana (6, 5) = derecha
        Direction decision = bot.decideMove(fruits, enemies, canMove);

        assertEquals("HungryBot debe moverse hacia la fruta más cercana (derecha)",
                Direction.RIGHT, decision);
    }

    // ========================================================================
    // TEST 6: FearfulBot - Huye de enemigos muy cercanos
    // ========================================================================
    @Test
    public void testFearfulBotFleesFromEnemy() {
        FearfulBot bot = new FearfulBot("fearful1", 5, 5, IceCreamFlavor.STRAWBERRY);

        List<EntityInfo> fruits = new ArrayList<>();
        fruits.add(new EntityInfo("f1", 8, 8, "BANANA", false));

        // Enemigo MUY cerca del bot (distancia 1, no pone hielo, huye)
        List<EntityInfo> enemies = new ArrayList<>();
        enemies.add(new EntityInfo("e1", 6, 5, "TROLL", false));

        boolean[] canMove = { true, true, true, true };

        // El bot debe huir del enemigo (no ir hacia la derecha donde está)
        Direction decision = bot.decideMove(fruits, enemies, canMove);

        // No debe moverse hacia el enemigo (derecha)
        assertNotEquals("FearfulBot no debe moverse hacia el enemigo cercano",
                Direction.RIGHT, decision);
    }

    // ========================================================================
    // TEST 7: ExpertBot - Prioriza supervivencia cuando hay peligro
    // ========================================================================
    @Test
    public void testExpertBotPrioritizesSurvival() {
        ExpertBot bot = new ExpertBot("expert1", 5, 5, IceCreamFlavor.CHOCOLATE);

        // Fruta a la derecha
        List<EntityInfo> fruits = new ArrayList<>();
        fruits.add(new EntityInfo("f1", 7, 5, "BANANA", false));

        // Enemigo MUY cerca a la derecha (entre el bot y la fruta)
        List<EntityInfo> enemies = new ArrayList<>();
        enemies.add(new EntityInfo("e1", 6, 5, "TROLL", false));

        boolean[] canMove = { true, true, true, true };

        Direction decision = bot.decideMove(fruits, enemies, canMove);

        // El ExpertBot no debe moverse hacia el enemigo aunque la fruta esté allí
        assertNotEquals("ExpertBot prioriza supervivencia, no va hacia enemigo cercano",
                Direction.RIGHT, decision);
    }

    // ========================================================================
    // TEST 8: Bot timer - Actualiza correctamente
    // ========================================================================
    @Test
    public void testBotTimerUpdate() {
        HungryBot bot = new HungryBot("bot1", 5, 5, IceCreamFlavor.VANILLA);

        // Inicialmente no puede moverse
        assertFalse("Bot no puede moverse inmediatamente", bot.canBotMove());

        // Simular tiempo
        for (int i = 0; i < 10; i++) {
            bot.updateBotTimer(0.1f);
        }

        // Después de suficiente tiempo, debe poder moverse
        assertTrue("Bot puede moverse después de suficiente tiempo", bot.canBotMove());
    }

    // ========================================================================
    // TEST 9: Bot timer reset
    // ========================================================================
    @Test
    public void testBotTimerReset() {
        HungryBot bot = new HungryBot("bot1", 5, 5, IceCreamFlavor.VANILLA);

        // Simular tiempo hasta que pueda moverse
        for (int i = 0; i < 10; i++) {
            bot.updateBotTimer(0.1f);
        }
        assertTrue("Precondición: bot puede moverse", bot.canBotMove());

        // Reset
        bot.resetBotTimer();

        // Ya no puede moverse
        assertFalse("Después del reset, bot no puede moverse", bot.canBotMove());
    }

    // ========================================================================
    // TEST 10: UpdateBots - Bots se mueven automáticamente
    // ========================================================================
    @Test
    public void testUpdateBotsMovesHungryBot() throws BadOpoException {
        // Crear bot hambriento
        playerCtrl.addPlayer("bot1", IceCreamFlavor.VANILLA, PlayerType.MACHINE_HUNGRY, 5, 5);
        IceCream bot = playerCtrl.getPlayer("bot1");

        // Colocar fruta cerca
        itemCtrl.spawnFruit("BANANA", "fruit1", 7, 5);

        int initialX = bot.getX();

        // Simular varios updates del bot
        for (int i = 0; i < 20; i++) {
            playerCtrl.updateBots(0.1f, enemyCtrl);
        }

        // El bot debe haberse movido hacia la fruta
        assertTrue("Bot debe haberse movido hacia la fruta",
                bot.getX() != initialX || bot.getY() != 5);
    }

    // ========================================================================
    // TEST 11: Bot con movimiento bloqueado
    // ========================================================================
    @Test
    public void testBotWithBlockedMovement() {
        HungryBot bot = new HungryBot("bot1", 5, 5, IceCreamFlavor.VANILLA);

        List<EntityInfo> fruits = new ArrayList<>();
        fruits.add(new EntityInfo("f1", 6, 5, "BANANA", false)); // Fruta a la derecha

        List<EntityInfo> enemies = new ArrayList<>();

        // Todas las direcciones bloqueadas excepto izquierda
        boolean[] canMove = { false, false, true, false }; // Solo LEFT disponible

        Direction decision = bot.decideMove(fruits, enemies, canMove);

        // El bot debe elegir la única dirección disponible o NONE
        assertTrue("Bot debe elegir dirección válida",
                decision == Direction.LEFT || decision == Direction.NONE);
    }

    // ========================================================================
    // TEST 12: Múltiples bots pueden coexistir
    // ========================================================================
    @Test
    public void testMultipleBotsCoexist() {
        playerCtrl.addPlayer("bot1", IceCreamFlavor.VANILLA, PlayerType.MACHINE_HUNGRY, 2, 2);
        playerCtrl.addPlayer("bot2", IceCreamFlavor.STRAWBERRY, PlayerType.MACHINE_FEARFUL, 8, 8);

        IceCream bot1 = playerCtrl.getPlayer("bot1");
        IceCream bot2 = playerCtrl.getPlayer("bot2");

        assertNotNull("Bot1 debe existir", bot1);
        assertNotNull("Bot2 debe existir", bot2);
        assertTrue("Bot1 es bot", bot1.isBot());
        assertTrue("Bot2 es bot", bot2.isBot());
    }

    // ========================================================================
    // TEST 13: Bot y humano pueden coexistir (modo PvM)
    // ========================================================================
    @Test
    public void testBotAndHumanCoexist() {
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 2, 2);
        playerCtrl.addPlayer("bot1", IceCreamFlavor.STRAWBERRY, PlayerType.MACHINE_HUNGRY, 8, 8);

        IceCream human = playerCtrl.getPlayer("player1");
        IceCream bot = playerCtrl.getPlayer("bot1");

        assertNotNull("Humano debe existir", human);
        assertNotNull("Bot debe existir", bot);
        assertFalse("Humano no es bot", human.isBot());
        assertTrue("Bot es bot", bot.isBot());
    }

    // ========================================================================
    // TEST 14: FearfulBot sin enemigos cerca va hacia frutas
    // ========================================================================
    @Test
    public void testFearfulBotGoesToFruitWhenSafe() {
        FearfulBot bot = new FearfulBot("fearful1", 5, 5, IceCreamFlavor.STRAWBERRY);

        // Fruta a la derecha
        List<EntityInfo> fruits = new ArrayList<>();
        fruits.add(new EntityInfo("f1", 7, 5, "BANANA", false));

        // Sin enemigos
        List<EntityInfo> enemies = new ArrayList<>();

        boolean[] canMove = { true, true, true, true };

        Direction decision = bot.decideMove(fruits, enemies, canMove);

        // Cuando no hay enemigos cerca, el FearfulBot debería ir hacia las frutas
        assertEquals("FearfulBot debe ir hacia fruta cuando está seguro",
                Direction.RIGHT, decision);
    }
}
