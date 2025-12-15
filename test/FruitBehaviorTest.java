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
 * 🍇 III. Comportamiento de las Frutas
 * 
 * Cada fruta tiene lógica única que debe probarse aisladamente.
 * Verifica recolección, puntuación, y comportamientos especiales.
 */
public class FruitBehaviorTest {

    /**
     * Método main para ejecutar los tests directamente.
     */
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("test.FruitBehaviorTest");
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
    // TEST 1: General - Jugador pisa celda con fruta -> fruta desaparece
    // ========================================================================
    @Test
    public void testPlayerCollectsFruit() throws BadOpoException {
        // Colocar jugador en (5, 5)
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);

        // Colocar fruta en (5, 6) - abajo del jugador
        itemCtrl.spawnFruit("BANANA", "fruit1", 5, 6);

        // Verificar que la fruta existe
        assertEquals("Debe haber 1 fruta", 1, itemCtrl.getFruitCount());

        // Mover jugador hacia la fruta
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.DOWN);

        // Recolectar la fruta
        int points = itemCtrl.collectItemAt(5, 6);

        // Verificar que la fruta desapareció
        assertEquals("No debe quedar ninguna fruta", 0, itemCtrl.getFruitCount());
        assertTrue("Debe haber dado puntos", points > 0);
    }

    // ========================================================================
    // TEST 2: General - Jugador pisa celda con fruta -> Puntuación aumenta
    // ========================================================================
    @Test
    public void testFruitGivesPoints() throws BadOpoException {
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);
        IceCream player = playerCtrl.getPlayer("player1");

        int initialScore = player.getScore();

        // Colocar fruta
        itemCtrl.spawnFruit("BANANA", "fruit1", 5, 6);

        // Mover hacia la fruta
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.DOWN);

        // Recolectar y sumar puntos
        int points = itemCtrl.collectItemAt(5, 6);
        player.addScore(points);

        // Verificar puntuación
        assertTrue("La puntuación debe aumentar", player.getScore() > initialScore);
    }

    // ========================================================================
    // TEST 3: Uva - Verificar que es estática (no se mueve)
    // ========================================================================
    @Test
    public void testGrapeIsStatic() throws BadOpoException {
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);

        // Colocar uva
        itemCtrl.spawnFruit("GRAPE", "grape1", 7, 7);

        // Simular paso del tiempo
        for (int i = 0; i < 100; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // Verificar que la uva sigue en su posición original
        boolean grapeStillThere = false;
        for (domain.items.Item item : itemCtrl.getItems()) {
            if (item.getId().equals("grape1") && item.getX() == 7 && item.getY() == 7) {
                grapeStillThere = true;
                break;
            }
        }
        assertTrue("La uva debe permanecer en su posición", grapeStillThere);
    }

    // ========================================================================
    // TEST 4: Uva - Verificar puntaje (+50)
    // ========================================================================
    @Test
    public void testGrapeScore() {
        itemCtrl.spawnFruit("GRAPE", "grape1", 5, 5);

        int points = itemCtrl.collectItemAt(5, 5);

        assertEquals("La uva debe dar 50 puntos", 50, points);
    }

    // ========================================================================
    // TEST 5: Plátano - Verificar que es estático
    // ========================================================================
    @Test
    public void testBananaIsStatic() {
        itemCtrl.spawnFruit("BANANA", "banana1", 3, 3);

        int initialX = 3;
        int initialY = 3;

        // Simular paso del tiempo
        for (int i = 0; i < 100; i++) {
            itemCtrl.updateItems(0.1f);
        }

        // Verificar que el plátano sigue ahí
        boolean bananaStillThere = false;
        for (domain.items.Item item : itemCtrl.getItems()) {
            if (item.getId().equals("banana1") && item.getX() == initialX && item.getY() == initialY) {
                bananaStillThere = true;
                break;
            }
        }
        assertTrue("El plátano debe permanecer en su posición", bananaStillThere);
    }

    // ========================================================================
    // TEST 6: Plátano - Verificar puntaje (+100)
    // ========================================================================
    @Test
    public void testBananaScore() {
        itemCtrl.spawnFruit("BANANA", "banana1", 5, 5);

        int points = itemCtrl.collectItemAt(5, 5);

        assertEquals("El plátano debe dar 100 puntos", 100, points);
    }

    // ========================================================================
    // TEST 7: Cereza - Verificar puntaje (+150)
    // ========================================================================
    @Test
    public void testCherryScore() {
        itemCtrl.spawnFruit("CHERRY", "cherry1", 5, 5);

        int points = itemCtrl.collectItemAt(5, 5);

        assertEquals("La cereza debe dar 150 puntos", 150, points);
    }

    // ========================================================================
    // TEST 8: Piña - Verificar puntaje (+200)
    // ========================================================================
    @Test
    public void testPineappleScore() {
        itemCtrl.spawnFruit("PINEAPPLE", "pineapple1", 5, 5);

        int points = itemCtrl.collectItemAt(5, 5);

        assertEquals("La piña debe dar 200 puntos", 200, points);
    }

    // ========================================================================
    // TEST 9: Piña - Jugador se mueve -> Piña se mueve
    // ========================================================================
    @Test
    public void testPineappleMoves() throws BadOpoException {
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);

        // Colocar piña
        itemCtrl.spawnFruit("PINEAPPLE", "pineapple1", 7, 7);

        int initialX = 7;
        int initialY = 7;

        // El jugador se mueve
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.RIGHT);

        // Notificar al ItemController que el jugador se movió
        itemCtrl.onPlayerMove();

        // Verificar que la piña se movió (a alguna posición adyacente)
        boolean pineappleMoved = false;
        for (domain.items.Item item : itemCtrl.getItems()) {
            if (item.getId().equals("pineapple1")) {
                // La piña debe haberse movido a una posición diferente
                if (item.getX() != initialX || item.getY() != initialY) {
                    pineappleMoved = true;
                }
                break;
            }
        }
        assertTrue("La piña debe moverse cuando el jugador se mueve", pineappleMoved);
    }

    // ========================================================================
    // TEST 10: Varias frutas - Conteo correcto
    // ========================================================================
    @Test
    public void testMultipleFruitsCount() {
        itemCtrl.spawnFruit("GRAPE", "grape1", 1, 1);
        itemCtrl.spawnFruit("BANANA", "banana1", 2, 2);
        itemCtrl.spawnFruit("CHERRY", "cherry1", 3, 3);
        itemCtrl.spawnFruit("PINEAPPLE", "pineapple1", 4, 4);

        assertEquals("Debe haber 4 frutas", 4, itemCtrl.getFruitCount());
    }

    // ========================================================================
    // TEST 11: Recolección reduce el conteo
    // ========================================================================
    @Test
    public void testCollectionReducesCount() {
        itemCtrl.spawnFruit("GRAPE", "grape1", 1, 1);
        itemCtrl.spawnFruit("BANANA", "banana1", 2, 2);

        assertEquals("Inicialmente 2 frutas", 2, itemCtrl.getFruitCount());

        // Recolectar una
        itemCtrl.collectItemAt(1, 1);

        assertEquals("Después de recolectar, 1 fruta", 1, itemCtrl.getFruitCount());

        // Recolectar la otra
        itemCtrl.collectItemAt(2, 2);

        assertEquals("Después de recolectar todas, 0 frutas", 0, itemCtrl.getFruitCount());
    }

    // ========================================================================
    // TEST 12: Fruta atrapada en hielo - Romper hielo -> Fruta accesible
    // ========================================================================
    @Test
    public void testFruitTrappedInIce() throws BadOpoException {
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);
        IceCream player = playerCtrl.getPlayer("player1");

        // Colocar fruta
        itemCtrl.spawnFruit("BANANA", "trapped_fruit", 5, 7);

        // Crear hielo sobre la fruta (en la celda adyacente que bloquea el paso)
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice1", 5, 6);

        // Verificar que la fruta existe
        assertEquals("Debe haber 1 fruta", 1, itemCtrl.getFruitCount());

        // El jugador no puede pasar por el hielo
        player.setFacingDirection(Direction.DOWN);
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.DOWN);
        assertEquals("Jugador bloqueado por hielo, Y=5", 5, player.getY());

        // Romper el hielo
        playerCtrl.performAction("player1", ActionType.BREAK_ICE, Direction.DOWN);
        for (int i = 0; i < 20; i++)
            itemCtrl.updateItems(0.1f);

        // Ahora el jugador puede moverse hacia la fruta
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.DOWN);
        assertEquals("Jugador puede moverse después de romper hielo", 6, player.getY());

        // La fruta sigue existiendo
        assertEquals("La fruta sigue existiendo", 1, itemCtrl.getFruitCount());
    }

    // ========================================================================
    // TEST 13: Cactus - Estado normal es recolectable
    // ========================================================================
    @Test
    public void testCactusNormalStateCollectable() {
        // Colocar cactus (estado inicial debería ser seguro)
        itemCtrl.spawnFruit("CACTUS", "cactus1", 5, 5);

        // Verificar que es recolectable
        assertEquals("Debe haber 1 fruta/cactus", 1, itemCtrl.getFruitCount());

        // Recolectar (en estado normal)
        int points = itemCtrl.collectItemAt(5, 5);

        // Debería dar puntos (250 según especificación)
        assertTrue("Cactus en estado normal debe dar puntos", points > 0);
    }

    // ========================================================================
    // TEST 14: Cactus - Verificar puntaje (+250)
    // ========================================================================
    @Test
    public void testCactusScore() {
        itemCtrl.spawnFruit("CACTUS", "cactus1", 5, 5);

        int points = itemCtrl.collectItemAt(5, 5);

        assertEquals("El cactus debe dar 250 puntos", 250, points);
    }
}
