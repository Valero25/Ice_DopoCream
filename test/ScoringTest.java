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
 * ⏱️ VIII. Tiempo y Puntuación
 * 
 * Pruebas para el sistema de puntuación y tiempo de juego.
 * Verifica acumulación de puntos, recolección de frutas, y scoring.
 */
public class ScoringTest {

    /**
     * Método main para ejecutar los tests directamente.
     */
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("test.ScoringTest");
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

        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);
    }

    // ========================================================================
    // TEST 1: Puntuación inicial es 0
    // ========================================================================
    @Test
    public void testInitialScoreIsZero() {
        IceCream player = playerCtrl.getPlayer("player1");

        assertEquals("Puntuación inicial debe ser 0", 0, player.getScore());
    }

    // ========================================================================
    // TEST 2: Recolectar fruta aumenta puntuación
    // ========================================================================
    @Test
    public void testCollectFruitIncreasesScore() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");

        // Colocar fruta
        itemCtrl.spawnFruit("BANANA", "banana1", 5, 6);

        // Mover hacia la fruta
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.DOWN);

        // Recolectar y sumar puntos
        int points = itemCtrl.collectItemAt(5, 6);
        player.addScore(points);

        assertTrue("Puntuación debe aumentar", player.getScore() > 0);
    }

    // ========================================================================
    // TEST 3: Puntos de Banana = 100
    // ========================================================================
    @Test
    public void testBananaGives100Points() {
        itemCtrl.spawnFruit("BANANA", "banana1", 5, 5);
        int points = itemCtrl.collectItemAt(5, 5);

        assertEquals("Banana = 100 puntos", 100, points);
    }

    // ========================================================================
    // TEST 4: Puntos de Uva = 50
    // ========================================================================
    @Test
    public void testGrapeGives50Points() {
        itemCtrl.spawnFruit("GRAPE", "grape1", 5, 5);
        int points = itemCtrl.collectItemAt(5, 5);

        assertEquals("Uva = 50 puntos", 50, points);
    }

    // ========================================================================
    // TEST 5: Puntos de Cereza = 150
    // ========================================================================
    @Test
    public void testCherryGives150Points() {
        itemCtrl.spawnFruit("CHERRY", "cherry1", 5, 5);
        int points = itemCtrl.collectItemAt(5, 5);

        assertEquals("Cereza = 150 puntos", 150, points);
    }

    // ========================================================================
    // TEST 6: Puntos de Piña = 200
    // ========================================================================
    @Test
    public void testPineappleGives200Points() {
        itemCtrl.spawnFruit("PINEAPPLE", "pineapple1", 5, 5);
        int points = itemCtrl.collectItemAt(5, 5);

        assertEquals("Piña = 200 puntos", 200, points);
    }

    // ========================================================================
    // TEST 7: Puntos de Cactus = 250
    // ========================================================================
    @Test
    public void testCactusGives250Points() {
        itemCtrl.spawnFruit("CACTUS", "cactus1", 5, 5);
        int points = itemCtrl.collectItemAt(5, 5);

        assertEquals("Cactus = 250 puntos", 250, points);
    }

    // ========================================================================
    // TEST 8: Acumulación de puntos múltiples
    // ========================================================================
    @Test
    public void testScoreAccumulation() {
        IceCream player = playerCtrl.getPlayer("player1");

        // Recolectar múltiples frutas
        itemCtrl.spawnFruit("BANANA", "b1", 1, 1);
        itemCtrl.spawnFruit("GRAPE", "g1", 2, 2);

        int points1 = itemCtrl.collectItemAt(1, 1);
        player.addScore(points1);

        int points2 = itemCtrl.collectItemAt(2, 2);
        player.addScore(points2);

        // 100 + 50 = 150
        assertEquals("Puntuación acumulada", 150, player.getScore());
    }

    // ========================================================================
    // TEST 9: Recolectar en posición vacía da 0 puntos
    // ========================================================================
    @Test
    public void testCollectEmptyGivesZeroPoints() {
        int points = itemCtrl.collectItemAt(5, 5);

        assertEquals("Recolectar vacío = 0 puntos", 0, points);
    }

    // ========================================================================
    // TEST 10: Puntuación se acumula correctamente con addScore
    // ========================================================================
    @Test
    public void testAddScoreMultipleTimes() {
        IceCream player = playerCtrl.getPlayer("player1");

        player.addScore(100);
        player.addScore(50);
        player.addScore(25);

        assertEquals("Puntuación acumulada correctamente", 175, player.getScore());
    }

    // ========================================================================
    // TEST 11: Jugador 2 tiene puntuación independiente
    // ========================================================================
    @Test
    public void testTwoPlayersIndependentScores() {
        playerCtrl.addPlayer("player2", IceCreamFlavor.STRAWBERRY, PlayerType.HUMAN, 2, 2);

        IceCream p1 = playerCtrl.getPlayer("player1");
        IceCream p2 = playerCtrl.getPlayer("player2");

        p1.addScore(100);
        p2.addScore(200);

        assertEquals("P1 tiene 100 puntos", 100, p1.getScore());
        assertEquals("P2 tiene 200 puntos", 200, p2.getScore());
    }

    // ========================================================================
    // TEST 12: Score incrementa correctamente
    // ========================================================================
    @Test
    public void testScoreIncrements() {
        IceCream player = playerCtrl.getPlayer("player1");

        assertEquals("Inicial 0", 0, player.getScore());
        player.addScore(999);
        assertEquals("Después de addScore", 999, player.getScore());
    }

    // ========================================================================
    // TEST 13: AddScore con valor grande
    // ========================================================================
    @Test
    public void testLargeScore() {
        IceCream player = playerCtrl.getPlayer("player1");

        // Añadir muchos puntos
        player.addScore(1000000);

        assertEquals("Score grande funciona", 1000000, player.getScore());
    }

    // ========================================================================
    // TEST 14: Nombre del jugador
    // ========================================================================
    @Test
    public void testPlayerName() {
        IceCream player = playerCtrl.getPlayer("player1");

        player.setPlayerName("Diego");

        assertEquals("Nombre del jugador", "Diego", player.getPlayerName());
    }

    // ========================================================================
    // TEST 15: IceCreamFlavor se preserva
    // ========================================================================
    @Test
    public void testPlayerFlavor() {
        IceCream player = playerCtrl.getPlayer("player1");

        assertEquals("Flavor debe ser VANILLA", IceCreamFlavor.VANILLA, player.getFlavor());
    }
}
