package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gestiona las frutas del juego (generación, recolección)
 * SRP: Solo se encarga de la lógica de frutas
 */
public class FruitManager {
    private List<Fruit> fruits;
    private int grapesCollected;
    private int bananasCollected;
    private boolean grapesPhaseComplete;
    
    private static final int TOTAL_GRAPES = 8;
    private static final int TOTAL_BANANAS = 8;
    
    public FruitManager() {
        this.fruits = new ArrayList<>();
        this.grapesCollected = 0;
        this.bananasCollected = 0;
        this.grapesPhaseComplete = false;
    }
    
    public List<Fruit> getFruits() {
        return fruits;
    }
    
    public int getGrapesCollected() {
        return grapesCollected;
    }
    
    public int getBananasCollected() {
        return bananasCollected;
    }
    
    public boolean isGrapesPhaseComplete() {
        return grapesPhaseComplete;
    }
    
    /**
     * Inicializa las frutas del nivel (solo uvas al inicio)
     */
    public void initialize(List<Position> occupiedPositions, int rows, int cols) {
        fruits.clear();
        grapesCollected = 0;
        bananasCollected = 0;
        grapesPhaseComplete = false;
        spawnGrapes(occupiedPositions, rows, cols);
    }
    
    /**
     * Verifica si el jugador recolectó una fruta y actualiza el estado
     */
    public int checkCollection(Position playerPosition) {
        for (Fruit fruit : fruits) {
            if (fruit.isAt(playerPosition)) {
                fruit.collect();
                
                if (fruit.getType().equals("Uva")) {
                    grapesCollected++;
                    System.out.println("¡Uva recolectada! (" + grapesCollected + "/" + TOTAL_GRAPES + ")");
                    
                    if (grapesCollected == TOTAL_GRAPES && !grapesPhaseComplete) {
                        grapesPhaseComplete = true;
                        System.out.println("¡Todas las uvas recolectadas! Ahora aparecen los plátanos");
                    }
                    return 10; // Puntos por uva
                } else if (fruit.getType().equals("Platano")) {
                    bananasCollected++;
                    System.out.println("¡Plátano recolectado! (" + bananasCollected + "/" + TOTAL_BANANAS + ")");
                    return 15; // Puntos por plátano
                }
                break;
            }
        }
        return 0;
    }
    
    /**
     * Genera plátanos cuando se completa la fase de uvas
     */
    public void spawnBananasIfNeeded(List<Position> occupiedPositions, int rows, int cols) {
        if (grapesPhaseComplete && bananasCollected == 0) {
            spawnBananas(occupiedPositions, rows, cols);
        }
    }
    
    /**
     * Verifica si todas las frutas fueron recolectadas
     */
    public boolean areAllFruitsCollected() {
        return grapesCollected == TOTAL_GRAPES && bananasCollected == TOTAL_BANANAS;
    }
    
    private void spawnGrapes(List<Position> occupiedPositions, int rows, int cols) {
        Random random = new Random();
        List<Position> occupied = new ArrayList<>(occupiedPositions);
        
        for (int i = 0; i < TOTAL_GRAPES; i++) {
            Position pos = getRandomFreePosition(random, occupied, rows, cols);
            fruits.add(new Fruit("Uva", pos));
            occupied.add(pos);
        }
    }
    
    private void spawnBananas(List<Position> occupiedPositions, int rows, int cols) {
        Random random = new Random();
        List<Position> occupied = new ArrayList<>(occupiedPositions);
        
        for (int i = 0; i < TOTAL_BANANAS; i++) {
            Position pos = getRandomFreePosition(random, occupied, rows, cols);
            fruits.add(new Fruit("Platano", pos));
            occupied.add(pos);
        }
    }
    
    private Position getRandomFreePosition(Random random, List<Position> occupiedPositions, int rows, int cols) {
        Position pos;
        do {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            pos = new Position(row, col);
        } while (isPositionOccupied(pos, occupiedPositions));
        
        return pos;
    }
    
    private boolean isPositionOccupied(Position pos, List<Position> occupiedPositions) {
        for (Position occupied : occupiedPositions) {
            if (occupied.equals(pos)) {
                return true;
            }
        }
        return false;
    }
}
