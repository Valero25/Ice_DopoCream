package test;

import domain.players.BotPlayer;
import domain.players.IceCreamFlavor;
import domain.players.PlayerType;
import domain.shared.Direction;
import domain.shared.EntityInfo;

import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {
        System.out.println("Iniciando pruebas de BotPlayer...");

        boolean verifyNearest = testFindNearest();
        boolean verifyDirectionTowards = testGetDirectionTowards();
        boolean verifyDirectionAway = testGetDirectionAwayFrom();
        boolean verifyIsBot = testIsBot();

        System.out.println("\n--- Resumen de Pruebas ---");
        System.out.println("testFindNearest: " + (verifyNearest ? "PASO" : "FALLO"));
        System.out.println("testGetDirectionTowards: " + (verifyDirectionTowards ? "PASO" : "FALLO"));
        System.out.println("testGetDirectionAwayFrom: " + (verifyDirectionAway ? "PASO" : "FALLO"));
        System.out.println("testIsBot: " + (verifyIsBot ? "PASO" : "FALLO"));

        if (verifyNearest && verifyDirectionTowards && verifyDirectionAway && verifyIsBot) {
            System.out.println("\n TODAS LAS PRUEBAS PASARON CORRECTAMENTE.");
        } else {
            System.out.println("\n ALGUNAS PRUEBAS FALLARON.");
        }
    }

    // Subclase para probar métodos protegidos
    public static class TestableBot extends BotPlayer {
        public TestableBot(String id, int x, int y) {
            super(id, x, y, IceCreamFlavor.VANILLA, PlayerType.MACHINE_HUNGRY);
        }

        public EntityInfo publicFindNearest(List<EntityInfo> items) {
            return findNearest(items);
        }

        public Direction publicGetDirectionTowards(int targetX, int targetY, boolean[] canMove) {
            return getDirectionTowards(targetX, targetY, canMove);
        }

        public Direction publicGetDirectionAwayFrom(int targetX, int targetY, boolean[] canMove) {
            return getDirectionAwayFrom(targetX, targetY, canMove);
        }

        @Override
        public Direction decideMove(List<EntityInfo> fruits, List<EntityInfo> enemies, boolean[] canMove) {
            return Direction.NONE;
        }
    }

    private static boolean testFindNearest() {
        System.out.print("Ejecutando testFindNearest... ");
        TestableBot bot = new TestableBot("bot1", 10, 10);
        List<EntityInfo> fruits = new ArrayList<>();

        EntityInfo closeFruit = new EntityInfo("f1", 12, 10, "BANANA", true); // dist sq = 4
        EntityInfo farFruit = new EntityInfo("f2", 20, 20, "BANANA", true); // dist sq = 200

        fruits.add(farFruit);
        fruits.add(closeFruit);

        EntityInfo result = bot.publicFindNearest(fruits);

        if (result == closeFruit) {
            System.out.println("OK");
            return true;
        } else {
            System.out.println(
                    "ERROR. Esperaba " + closeFruit.id + " pero obtuvo " + (result != null ? result.id : "null"));
            return false;
        }
    }

    private static boolean testGetDirectionTowards() {
        System.out.print("Ejecutando testGetDirectionTowards... ");
        TestableBot bot = new TestableBot("bot1", 10, 10);

        // Target is at (15, 10) -> Right
        // canMove: UP(0), DOWN(1), LEFT(2), RIGHT(3)
        boolean[] canMove = { true, true, true, true };

        // Case 1: Target to the RIGHT
        Direction d1 = bot.publicGetDirectionTowards(15, 10, canMove);
        if (d1 != Direction.RIGHT) {
            System.out.println("ERROR Case 1. Target (15,10), Bot(10,10). Expected RIGHT, got " + d1);
            return false;
        }

        // Case 2: Target is DOWN-RIGHT but more DOWN (11, 15)
        Direction d2 = bot.publicGetDirectionTowards(11, 15, canMove);
        if (d2 != Direction.DOWN) {
            System.out.println("ERROR Case 2. Target (11,15), Bot(10,10). Expected DOWN, got " + d2);
            return false;
        }

        // Case 3: Target to RIGHT but blocked.
        boolean[] blockedRight = { true, true, true, false }; // Right blocked
        Direction d3 = bot.publicGetDirectionTowards(15, 10, blockedRight);

        // Should not be RIGHT (blocked) and should be valid (UP, DOWN, LEFT)
        if (d3 == Direction.RIGHT || d3 == Direction.NONE) {
            System.out.println("ERROR Case 3. Blocked right. Expected random valid, got " + d3);
            return false;
        }

        System.out.println("OK");
        return true;
    }

    private static boolean testGetDirectionAwayFrom() {
        System.out.print("Ejecutando testGetDirectionAwayFrom... ");
        TestableBot bot = new TestableBot("bot1", 10, 10);
        boolean[] canMove = { true, true, true, true };

        // Threat at (10, 15) -> DOWN.
        // Logic in BotPlayer tries "perpendicular evasion" when aligned.
        Direction d1 = bot.publicGetDirectionAwayFrom(10, 15, canMove);
        if (d1 != Direction.RIGHT) {
            System.out.println(
                    "ERROR Case 1. Threat (10,15), Bot(10,10). Expected RIGHT (Perpendicular evasion), got " + d1);
            return false;
        }

        System.out.println("OK");
        return true;
    }

    private static boolean testIsBot() {
        System.out.print("Ejecutando testIsBot... ");
        TestableBot bot = new TestableBot("bot1", 0, 0);
        if (bot.isBot()) {
            System.out.println("OK");
            return true;
        } else {
            System.out.println("ERROR. isBot retornó false");
            return false;
        }
    }
}
