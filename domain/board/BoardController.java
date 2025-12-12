package domain.board;

import domain.shared.Direction;
import domain.shared.EntityType;
import domain.shared.BadOpoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador principal del tablero de juego.
 * Gestiona la matriz de celdas, validaciones de posiciones,
 * creacion/destruccion de bloques de hielo y animaciones domino.
 * 
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Mantener el estado del tablero (matriz de celdas)</li>
 *   <li>Validar movimientos y posiciones</li>
 *   <li>Gestionar bloques de hielo (crear/romper)</li>
 *   <li>Controlar animaciones visuales del efecto domino</li>
 * </ul>
 * 
 * @author Diego Montes y Juan David Valero
 * @version 1.0
 * @see Cell
 * @see AnimationEntry
 */
public class BoardController implements java.io.Serializable {

    private int width;
    private int height;
    private Cell[][] grid; // Matriz interna oculta
    private List<AnimationEntry> animationQueue; // Cola de animaciones dominó
    private float animationTimer;
    private static final float ANIMATION_DELAY = 0.3f; // Retardo entre celdas (muy visible)
    private static final float ANIMATION_SPEED = 4.0f; // Velocidad de aparición/desaparición (más lento)

    public BoardController(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];
        this.animationQueue = new ArrayList<>();
        this.animationTimer = 0f;
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new Cell(EntityType.EMPTY);
            }
        }
    }
    // Aquí luego llamarás a un LevelLoader para poner paredes reales
    // --- MODIFICACIÓN DEL ENTORNO (Habilidades) ---

    public void reset(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];
        initializeEmptyBoard();
    }

    // --- CONSULTAS DE ESTADO (Para IAs y Validaciones) ---

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public EntityType getEntityAt(int x, int y) {
        if (!isValidPosition(x, y))
            return EntityType.WALL; // Bordes son muros
        return grid[y][x].getContent();
    }

    // Usado por Players y Enemies para saber si pueden avanzar
    public boolean isWalkable(int x, int y) {
        if (!isValidPosition(x, y))
            return false;
        return grid[y][x].isWalkable();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // --- MODIFICACIÓN DEL ENTORNO (Habilidades) ---

    // Requisito: Crear bloques de hielo [cite: 5, 32]
    public boolean createIceBlock(int x, int y) {
        if (!isValidPosition(x, y))
            return false;

        Cell cell = grid[y][x];

        // Regla: "Si ya existe un bloque, no se reemplaza" [cite: 33]
        if (cell.getContent() != EntityType.EMPTY) {
            return false;
        }

        cell.setContent(EntityType.ICE_BLOCK);
        return true;
    }

    // Requisito: Romper bloques de hielo [cite: 5, 35]
    public boolean breakIceBlock(int x, int y) {
        if (!isValidPosition(x, y))
            return false;

        Cell cell = grid[y][x];
        if (cell.getContent() == EntityType.ICE_BLOCK) {
            cell.setContent(EntityType.EMPTY);
            return true;
        }
        return false;
    }

    // Añadir animación dominó desde un punto
    private void addDominoAnimation(int startX, int startY, EntityType targetType) {
        animationQueue.clear(); // Limpiar animaciones previas
        animationTimer = 0f; // Reiniciar timer

        // Calcular distancia Manhattan desde el punto inicial
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (targetType == EntityType.ICE_BLOCK) {
                    // Solo animar celdas vacías que NO sean muros
                    if (grid[y][x].getContent() == EntityType.EMPTY) {
                        int distance = Math.abs(x - startX) + Math.abs(y - startY);
                        animationQueue.add(new AnimationEntry(x, y, targetType, distance * ANIMATION_DELAY));
                        grid[y][x].setAnimationProgress(0f);
                        grid[y][x].setAnimating(true);
                    }
                } else {
                    // Solo animar bloques de hielo existentes
                    if (grid[y][x].getContent() == EntityType.ICE_BLOCK) {
                        int distance = Math.abs(x - startX) + Math.abs(y - startY);
                        animationQueue.add(new AnimationEntry(x, y, targetType, distance * ANIMATION_DELAY));
                        grid[y][x].setAnimationProgress(1.0f); // Empezar completamente visible
                        grid[y][x].setAnimating(true);
                    }
                }
            }
        }
    }

    // Actualizar animaciones (llamar desde DomainController)
    public void updateAnimations(float deltaTime) {
        if (animationQueue.isEmpty())
            return;

        animationTimer += deltaTime;

        List<AnimationEntry> toRemove = new ArrayList<>();

        for (AnimationEntry entry : animationQueue) {
            // Solo comenzar la animación si el timer ha llegado al delay de esta celda
            if (animationTimer >= entry.delay) {
                Cell cell = grid[entry.y][entry.x];

                // Calcular cuánto tiempo ha pasado desde que esta celda comenzó
                float timeSinceStart = animationTimer - entry.delay;

                if (entry.targetType == EntityType.ICE_BLOCK) {
                    // Animación de creación
                    float progress = timeSinceStart * ANIMATION_SPEED;

                    if (progress >= 1.0f) {
                        progress = 1.0f;
                        cell.setContent(EntityType.ICE_BLOCK);
                        cell.setAnimating(false);
                        toRemove.add(entry);
                    } else {
                        // Durante la animación, marcar como ICE_BLOCK para que se dibuje
                        if (cell.getContent() == EntityType.EMPTY) {
                            cell.setContent(EntityType.ICE_BLOCK);
                        }
                        cell.setAnimating(true);
                    }
                    cell.setAnimationProgress(progress);
                } else {
                    // Animación de destrucción
                    float progress = 1.0f - (timeSinceStart * ANIMATION_SPEED);

                    if (progress <= 0.0f) {
                        progress = 0.0f;
                        cell.setContent(EntityType.EMPTY);
                        cell.setAnimating(false);
                        toRemove.add(entry);
                    } else {
                        cell.setAnimating(true);
                    }
                    cell.setAnimationProgress(progress);
                }
            }
        }

        animationQueue.removeAll(toRemove);

        // Si terminaron todas las animaciones, reiniciar el timer
        if (animationQueue.isEmpty()) {
            animationTimer = 0f;
        }
    }

    // Obtener progreso de animación de una celda
    public float getCellAnimationProgress(int x, int y) {
        if (!isValidPosition(x, y))
            return 1.0f;
        return grid[y][x].getAnimationProgress();
    }

    // Obtener todas las posiciones de bloques de hielo
    public List<int[]> getIceBlockPositions() {
        List<int[]> positions = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x].getContent() == EntityType.ICE_BLOCK) {
                    positions.add(new int[] { x, y });
                }
            }
        }
        return positions;
    }

    // --- CONFIGURACIÓN (Para Carga de Niveles) ---

    // Este método lo usará tu cargador de niveles
    public void setMapObstacle(int x, int y, EntityType type) throws BadOpoException {
        if (!isValidPosition(x, y))
            throw new BadOpoException(BadOpoException.CONFIG_ERROR);
        grid[y][x].setContent(type);
    }

}