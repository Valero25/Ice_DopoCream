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
 * 📦 I. Pruebas de Movimiento y Física del Jugador (Dominio)
 * 
 * Estas pruebas verifican que el IceCream se mueva según las reglas básicas.
 * Todas las pruebas corren SIN necesidad de abrir ventana gráfica.
 */
public class PlayerMovementTest {

    /**
     * Método main para ejecutar los tests directamente.
     * Permite correr la clase como aplicación Java normal.
     */
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("test.PlayerMovementTest");
    }

    private BoardController boardCtrl;
    private ItemController itemCtrl;
    private PlayerController playerCtrl;

    // Tablero de prueba: 10x10
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 10;

    /**
     * Configuración inicial antes de cada test.
     * Crea un tablero vacío y un jugador en la posición central (5, 5).
     */
    @Before
    public void setUp() {
        boardCtrl = new BoardController(BOARD_WIDTH, BOARD_HEIGHT);
        itemCtrl = new ItemController(boardCtrl);
        playerCtrl = new PlayerController(boardCtrl, itemCtrl);

        // Agregar jugador en posición central (5, 5)
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 5, 5);
    }

    // ========================================================================
    // TEST 1: Verificar que el helado se mueve una casilla al NORTE (UP)
    // ========================================================================
    @Test
    public void testMoveNorth() throws BadOpoException {
        // Posición inicial
        IceCream player = playerCtrl.getPlayer("player1");
        assertEquals("Posición inicial X", 5, player.getX());
        assertEquals("Posición inicial Y", 5, player.getY());

        // Mover hacia el norte (arriba, Y decrece)
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.UP);

        // Verificar nueva posición
        assertEquals("Después de mover NORTE, X debe ser igual", 5, player.getX());
        assertEquals("Después de mover NORTE, Y debe decrementar", 4, player.getY());
    }

    // ========================================================================
    // TEST 2: Verificar que el helado se mueve una casilla al SUR (DOWN)
    // ========================================================================
    @Test
    public void testMoveSouth() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");

        playerCtrl.performAction("player1", ActionType.MOVE, Direction.DOWN);

        assertEquals("Después de mover SUR, X debe ser igual", 5, player.getX());
        assertEquals("Después de mover SUR, Y debe incrementar", 6, player.getY());
    }

    // ========================================================================
    // TEST 3: Verificar que el helado se mueve una casilla al ESTE (RIGHT)
    // ========================================================================
    @Test
    public void testMoveEast() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");

        playerCtrl.performAction("player1", ActionType.MOVE, Direction.RIGHT);

        assertEquals("Después de mover ESTE, X debe incrementar", 6, player.getX());
        assertEquals("Después de mover ESTE, Y debe ser igual", 5, player.getY());
    }

    // ========================================================================
    // TEST 4: Verificar que el helado se mueve una casilla al OESTE (LEFT)
    // ========================================================================
    @Test
    public void testMoveWest() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");

        playerCtrl.performAction("player1", ActionType.MOVE, Direction.LEFT);

        assertEquals("Después de mover OESTE, X debe decrementar", 4, player.getX());
        assertEquals("Después de mover OESTE, Y debe ser igual", 5, player.getY());
    }

    // ========================================================================
    // TEST 5: Borde - Intentar mover el helado fuera de los límites del mapa
    // (debe quedarse quieto)
    // ========================================================================
    @Test
    public void testMoveOutOfBoundsNorth() throws BadOpoException {
        // Colocar jugador en el borde superior (0, 0)
        playerCtrl.reset();
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 0, 0);
        IceCream player = playerCtrl.getPlayer("player1");

        // Intentar mover hacia el norte (fuera del mapa)
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.UP);

        // Debe quedarse en la misma posición
        assertEquals("No debe moverse fuera del borde NORTE, X", 0, player.getX());
        assertEquals("No debe moverse fuera del borde NORTE, Y", 0, player.getY());
    }

    @Test
    public void testMoveOutOfBoundsSouth() throws BadOpoException {
        // Colocar jugador en el borde inferior
        playerCtrl.reset();
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 0, BOARD_HEIGHT - 1);
        IceCream player = playerCtrl.getPlayer("player1");

        playerCtrl.performAction("player1", ActionType.MOVE, Direction.DOWN);

        assertEquals("No debe moverse fuera del borde SUR, Y", BOARD_HEIGHT - 1, player.getY());
    }

    @Test
    public void testMoveOutOfBoundsWest() throws BadOpoException {
        playerCtrl.reset();
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 0, 5);
        IceCream player = playerCtrl.getPlayer("player1");

        playerCtrl.performAction("player1", ActionType.MOVE, Direction.LEFT);

        assertEquals("No debe moverse fuera del borde OESTE, X", 0, player.getX());
    }

    @Test
    public void testMoveOutOfBoundsEast() throws BadOpoException {
        playerCtrl.reset();
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, BOARD_WIDTH - 1, 5);
        IceCream player = playerCtrl.getPlayer("player1");

        playerCtrl.performAction("player1", ActionType.MOVE, Direction.RIGHT);

        assertEquals("No debe moverse fuera del borde ESTE, X", BOARD_WIDTH - 1, player.getX());
    }

    // ========================================================================
    // TEST 6: Colisión - Intentar mover el helado hacia una celda con IceBlock
    // (no debe moverse)
    // ========================================================================
    @Test
    public void testCollisionWithIceBlock() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");

        // Crear un bloque de hielo en (5, 4) - al norte del jugador
        itemCtrl.spawnObstacle("ICE_BLOCK", "ice_test", 5, 4);

        // Intentar mover hacia el norte donde está el bloque de hielo
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.UP);

        // El jugador debe quedarse en su lugar
        assertEquals("No debe atravesar bloque de hielo, X", 5, player.getX());
        assertEquals("No debe atravesar bloque de hielo, Y", 5, player.getY());
    }

    // ========================================================================
    // TEST 7: Colisión - Intentar mover el helado hacia una celda con muro de
    // borde
    // ========================================================================
    @Test
    public void testCollisionWithBorderWall() throws BadOpoException {
        // Colocar jugador cerca del borde
        playerCtrl.reset();
        playerCtrl.addPlayer("player1", IceCreamFlavor.VANILLA, PlayerType.HUMAN, 0, 0);
        IceCream player = playerCtrl.getPlayer("player1");

        // Mover hacia arriba (muro de borde implícito en Y=-1)
        int initialX = player.getX();
        int initialY = player.getY();

        playerCtrl.performAction("player1", ActionType.MOVE, Direction.UP);

        assertEquals("Colisión con borde debe mantener X", initialX, player.getX());
        assertEquals("Colisión con borde debe mantener Y", initialY, player.getY());
    }

    // ========================================================================
    // TEST 8: Verificar que el helado NO se puede mover en diagonal
    // ========================================================================
    @Test
    public void testNoDiagonalMovement() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");

        // La dirección NONE no debería mover al jugador
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.NONE);

        assertEquals("Dirección NONE no debe cambiar X", 5, player.getX());
        assertEquals("Dirección NONE no debe cambiar Y", 5, player.getY());

        // Nota: El enum Direction no tiene diagonales (UP_RIGHT, etc.)
        // por lo que el movimiento diagonal es imposible por diseño.
        // Este test verifica que NONE no mueve al jugador.
    }

    // ========================================================================
    // TEST 9: Verificar colisión entre dos jugadores en modo PvP
    // (¿chocan o se traspasan?)
    // ========================================================================
    @Test
    public void testPvPPlayerCollision() throws BadOpoException {
        // Agregar segundo jugador adyacente
        playerCtrl.addPlayer("player2", IceCreamFlavor.STRAWBERRY, PlayerType.HUMAN, 5, 4);

        IceCream player1 = playerCtrl.getPlayer("player1");
        IceCream player2 = playerCtrl.getPlayer("player2");

        // Player1 en (5,5), Player2 en (5,4)
        // Intentar que Player1 se mueva al norte hacia Player2
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.UP);

        // Según la implementación actual, los jugadores pueden ocupar la misma casilla
        // (se traspasan). Si esto cambia, este test detectará el cambio de
        // comportamiento.
        // Actualizar el assert según la regla que definas:

        // OPCIÓN A: Se traspasan (comportamiento actual esperado)
        assertEquals("Player1 se mueve a posición de Player2", 5, player1.getX());
        assertEquals("Player1 se mueve a posición de Player2", 4, player1.getY());

        // OPCIÓN B: Chocan (descomentar si decides implementar colisión entre
        // jugadores)
        // assertEquals("Player1 no debe moverse si hay otro jugador", 5,
        // player1.getX());
        // assertEquals("Player1 no debe moverse si hay otro jugador", 5,
        // player1.getY());
    }

    // ========================================================================
    // TEST 10: Verificar que el movimiento actualiza la dirección de cara
    // ========================================================================
    @Test
    public void testFacingDirectionUpdatesOnMove() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");

        // Dirección inicial es DOWN por defecto
        assertEquals("Dirección inicial es DOWN", Direction.DOWN, player.getFacingDirection());

        // Mover hacia la derecha
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.RIGHT);
        assertEquals("Después de moverse a la derecha", Direction.RIGHT, player.getFacingDirection());

        // Mover hacia arriba
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.UP);
        assertEquals("Después de moverse arriba", Direction.UP, player.getFacingDirection());

        // Mover hacia la izquierda
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.LEFT);
        assertEquals("Después de moverse a la izquierda", Direction.LEFT, player.getFacingDirection());

        // Mover hacia abajo
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.DOWN);
        assertEquals("Después de moverse abajo", Direction.DOWN, player.getFacingDirection());
    }

    // ========================================================================
    // TEST 11: Verificar secuencia de movimientos
    // ========================================================================
    @Test
    public void testMultipleMovements() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");

        // Mover en un patrón: derecha, derecha, arriba, izquierda
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.RIGHT);
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.RIGHT);
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.UP);
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.LEFT);

        // Posición final esperada: (5+2-1, 5-1) = (6, 4)
        assertEquals("Secuencia de movimientos - X final", 6, player.getX());
        assertEquals("Secuencia de movimientos - Y final", 4, player.getY());
    }

    // ========================================================================
    // TEST 12: Verificar que jugador muerto no se mueve
    // ========================================================================
    @Test
    public void testDeadPlayerCannotMove() throws BadOpoException {
        IceCream player = playerCtrl.getPlayer("player1");
        int initialX = player.getX();
        int initialY = player.getY();

        // Matar al jugador
        playerCtrl.killPlayer("player1");
        assertFalse("Jugador debe estar muerto", player.isAlive());

        // Intentar mover
        playerCtrl.performAction("player1", ActionType.MOVE, Direction.RIGHT);

        // No debe haberse movido
        assertEquals("Jugador muerto no se mueve - X", initialX, player.getX());
        assertEquals("Jugador muerto no se mueve - Y", initialY, player.getY());
    }
}
