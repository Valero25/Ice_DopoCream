package presentation;

import domain.DomainController;
import domain.Game;
import domain.GameMode;

import javax.swing.SwingUtilities;

public class App {

    private DomainController domain;
    private DopoCreamGUI gui;

    public App() {

        domain = new DomainController();

        gui = new DopoCreamGUI(
                this::onModeSelected,
                this::onPlayersSelected,
                this::startGame            // ← 🔥 tercer parámetro agregado
        );
    }

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SwingUtilities.invokeLater(App::new);
    }

    private void onModeSelected(GameMode mode) {
        domain.setGameMode(mode);
    }

    private void onPlayersSelected(String player1, String player2) {

        boolean isMachine = domain.getGameState().getMode() == GameMode.PLAYER_VS_MACHINE ||
                            domain.getGameState().getMode() == GameMode.MACHINE_VS_MACHINE;

        domain.setPlayer1Character(player1);
        domain.setPlayer2Character(player2, isMachine);

        System.out.println("Estado del juego:");
        System.out.println(domain.getGameState());
    }

    /**
     * Esta función se envía a DopoCreamGUI para que pueda iniciar el juego.
     */
    public Game startGame(int level) {
        domain.startGame(level);
        return domain.getCurrentGame();
    }

    public DomainController getDomain() {
        return domain;
    }
}
