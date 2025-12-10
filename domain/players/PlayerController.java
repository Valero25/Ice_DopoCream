package domain.players;

import domain.board.BoardController;
import domain.enemies.EnemyController;
import domain.items.ItemController;
import domain.shared.ActionType;
import domain.shared.BadOpoException;
import domain.shared.Direction;
import domain.shared.EntityInfo;

import java.util.HashMap;
import java.util.Map;

public class PlayerController {

    private BoardController boardCtrl;
    private ItemController itemCtrl;
    private Map<String, IceCream> players;

    // Guardamos la configuración elegida por el usuario (Por defecto Vainilla)
    private IceCreamFlavor selectedFlavorP1 = IceCreamFlavor.VANILLA;

    public PlayerController(BoardController boardCtrl, ItemController itemCtrl) {
        this.boardCtrl = boardCtrl;
        this.itemCtrl = itemCtrl;
        this.players = new HashMap<>();
    }

    /**
     * Permite configurar el sabor antes de iniciar el nivel.
     * La GUI llamará a esto a través del DomainController.
     */
    public void setPlayer1Flavor(IceCreamFlavor flavor) {
        this.selectedFlavorP1 = flavor;
    }

    /**
     * Crea un jugador.
     * Si no se especifica sabor (null), usa el seleccionado globalmente.
     */
    public void addPlayer(String id, IceCreamFlavor flavor, PlayerType type, int startX, int startY) {
        if (boardCtrl.isValidPosition(startX, startY)) {
            // Si el nivel dice "crea un jugador" pero no especifica sabor (null),
            // usamos el que el usuario eligió en el menú.
            IceCreamFlavor finalFlavor = (flavor != null) ? flavor : selectedFlavorP1;

            IceCream player;
            switch (type) {
                case MACHINE_HUNGRY:
                    player = new HungryBot(id, startX, startY, finalFlavor);
                    break;
                case MACHINE_FEARFUL:
                    player = new FearfulBot(id, startX, startY, finalFlavor);
                    break;
                case MACHINE_EXPERT:
                    player = new ExpertBot(id, startX, startY, finalFlavor);
                    break;
                default:
                    player = new IceCream(id, startX, startY, finalFlavor, type);
                    break;
            }
            players.put(id, player);
        }
    }

    /**
     * Establece el nombre personalizado de un jugador.
     * 
     * @param id ID del jugador
     * @param name Nombre a asignar
     */
    public void setPlayerName(String id, String name) {
        IceCream p = players.get(id);
        if (p != null) {
            p.setPlayerName(name);
        }
    }

    /**
     * Obtiene el nombre personalizado de un jugador.
     * 
     * @param id ID del jugador
     * @return El nombre del jugador, o el ID si no tiene nombre asignado
     */
    public String getPlayerName(String id) {
        IceCream p = players.get(id);
        if (p != null) {
            return p.getPlayerName();
        }
        return id;
    }

    /**
     * Actualiza la IA de los bots.
     * El Controller construye el contexto y el bot decide la accion.
     */
    public void updateBots(float dt, EnemyController enemyCtrl) {
        // Construir listas de contexto una sola vez
        java.util.List<EntityInfo> fruits = getFruitInfoList();
        java.util.List<EntityInfo> enemies = enemyCtrl.getEnemyInfo();

        for (IceCream p : players.values()) {
            // Sin instanceof: usamos polimorfismo
            if (!p.isBot())
                continue;

            p.updateBotTimer(dt);

            if (!p.canBotMove())
                continue;

            // Calcular movimientos validos para este bot
            boolean[] canMove = getValidMoves(p.getX(), p.getY());

            // El bot decide (polimorfismo)
            Direction dir = p.decideMove(fruits, enemies, canMove);
            ActionType action = p.getDesiredAction();

            // El Controller ejecuta
            if (dir != Direction.NONE) {
                try {
                    performAction(p.getId(), action, dir);
                } catch (BadOpoException e) {
                    // Ignorar si la accion falla
                }
            }
            p.resetBotTimer();
        }
    }

    private java.util.List<EntityInfo> getFruitInfoList() {
        java.util.List<EntityInfo> list = new java.util.ArrayList<>();
        for (domain.items.Item i : itemCtrl.getItems()) {
            if (i.isFruit()) {
                list.add(new EntityInfo(i.getId(), i.getX(), i.getY(), i.getType(), false));
            }
        }
        return list;
    }

    private boolean[] getValidMoves(int x, int y) {
        return new boolean[] {
                boardCtrl.isWalkable(x, y - 1) && !itemCtrl.isObstacleAt(x, y - 1), // UP
                boardCtrl.isWalkable(x, y + 1) && !itemCtrl.isObstacleAt(x, y + 1), // DOWN
                boardCtrl.isWalkable(x - 1, y) && !itemCtrl.isObstacleAt(x - 1, y), // LEFT
                boardCtrl.isWalkable(x + 1, y) && !itemCtrl.isObstacleAt(x + 1, y) // RIGHT
        };
    }

    public void performAction(String playerId, ActionType action, Direction dir) throws BadOpoException {
        IceCream p = players.get(playerId);
        if (p == null || !p.isAlive())
            return;

        if (dir != Direction.NONE)
            p.setFacingDirection(dir);

        switch (action) {
            case MOVE:
                movePlayer(p, dir);
                break;
            case CREATE_ICE:
                createIceRow(p);
                break;
            case BREAK_ICE:
                breakIceRow(p);
                break;
            default:
                break;
        }
    }

    // --- Movimiento y Habilidades (Simplificado para brevedad, lógica igual a la
    // anterior) ---
    private void movePlayer(IceCream p, Direction dir) {
        int nextX = p.getX() + dir.getDx();
        int nextY = p.getY() + dir.getDy();
        // Verifica muros y obstáculos
        if (boardCtrl.isWalkable(nextX, nextY) && !itemCtrl.isObstacleAt(nextX, nextY)) {
            p.setPosition(nextX, nextY);
        }
    }

    private void createIceRow(IceCream p) {
        Direction dir = p.getFacingDirection();
        int cx = p.getX() + dir.getDx();
        int cy = p.getY() + dir.getDy();

        if (boardCtrl.isValidPosition(cx, cy) && itemCtrl.isObstacleAt(cx, cy)) {
            breakIceRow(p);
        } else {
            java.util.List<int[]> positions = new java.util.ArrayList<>();
            while (boardCtrl.isValidPosition(cx, cy)) {
                if (!boardCtrl.isWalkable(cx, cy) || itemCtrl.isObstacleAt(cx, cy)) {
                    break;
                }
                positions.add(new int[] { cx, cy });
                cx += dir.getDx();
                cy += dir.getDy();
            }
            itemCtrl.queueIceCreation(positions);
        }
    }

    private void breakIceRow(IceCream p) {
        Direction dir = p.getFacingDirection();
        int cx = p.getX() + dir.getDx();
        int cy = p.getY() + dir.getDy();

        java.util.List<int[]> positions = new java.util.ArrayList<>();
        while (boardCtrl.isValidPosition(cx, cy)) {
            if (itemCtrl.hasDestructibleAt(cx, cy)) {
                positions.add(new int[] { cx, cy });
                cx += dir.getDx();
                cy += dir.getDy();
            } else {
                break;
            }
        }
        itemCtrl.queueIceDestruction(positions);
    }

    /**
     * Obtiene la informacion visual del Jugador 1 para la GUI.
     * Retorna un EntityInfo con posicion, tipo y estado.
     * 
     * @return EntityInfo del jugador 1, o null si no existe o esta muerto
     */
    public EntityInfo getPlayer1Info() {
        return getPlayerInfo("player1");
    }

    /**
     * Obtiene la informacion visual del Jugador 2 para la GUI.
     * Retorna un EntityInfo con posicion, tipo y estado.
     * 
     * @return EntityInfo del jugador 2, o null si no existe o esta muerto
     */
    public EntityInfo getPlayer2Info() {
        return getPlayerInfo("player2");
    }

    /**
     * Obtiene la informacion visual de un jugador especifico por su ID.
     * 
     * @param id Identificador del jugador (ej: "player1", "player2")
     * @return EntityInfo con los datos visuales, o null si no existe o esta muerto
     */
    public EntityInfo getPlayerInfo(String id) {
        IceCream p = players.get(id);
        if (p == null || !p.isAlive()) {
            return null;
        }
        return new EntityInfo(p.getId(), p.getX(), p.getY(), p.getType(), false);
    }

    /**
     * Obtiene el objeto IceCream de un jugador.
     * Uso interno del dominio.
     * 
     * @param id Identificador del jugador
     * @return El objeto IceCream, o null si no existe
     */
    public IceCream getPlayer(String id) {
        return players.get(id);
    }

    /**
     * Obtiene la posicion de un jugador como arreglo [x, y].
     * 
     * @param id Identificador del jugador
     * @return Arreglo con coordenadas [x, y], o null si no existe
     */
    public int[] getPosition(String id) {
        IceCream p = players.get(id);
        if (p == null) {
            return null;
        }
        return new int[] { p.getX(), p.getY() };
    }

    /**
     * Obtiene la puntuacion del Jugador 1.
     * 
     * @return Puntuacion actual del jugador 1, o 0 si no existe
     */
    public int getPlayer1Score() {
        IceCream p = players.get("player1");
        return (p != null) ? p.getScore() : 0;
    }

    /**
     * Obtiene la puntuacion del Jugador 2.
     * 
     * @return Puntuacion actual del jugador 2, o 0 si no existe
     */
    public int getPlayer2Score() {
        IceCream p = players.get("player2");
        return (p != null) ? p.getScore() : 0;
    }

    /**
     * Verifica si un jugador está vivo.
     * 
     * @param playerId ID del jugador
     * @return true si el jugador existe y está vivo
     */
    public boolean isPlayerAlive(String playerId) {
        IceCream p = players.get(playerId);
        return p != null && p.isAlive();
    }

    /**
     * Mata a un jugador (lo marca como muerto).
     * 
     * @param playerId ID del jugador
     */
    public void killPlayer(String playerId) {
        IceCream p = players.get(playerId);
        if (p != null) {
            p.setAlive(false);
        }
    }

    /**
     * Limpia la lista de jugadores.
     * Se llama antes de cargar un nuevo nivel.
     */
    public void reset() {
        players.clear();
    }
}