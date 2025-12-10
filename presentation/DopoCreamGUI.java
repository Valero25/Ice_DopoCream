package presentation;

import domain.GameMode;
import domain.Game;
import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class DopoCreamGUI {

    private Dimension windowSize;
    private Consumer<GameMode> onModeSelected;
    private BiConsumer<String, String> onPlayersSelected;
    private Function<Integer, Game> onGameStart;
    private GameModeUI currentMode;
    private String player1Character;
    private String player2Character;
    private int selectedLevel;

    // 🔥 Constructor corregido: ahora recibe onGameStart
    public DopoCreamGUI(Consumer<GameMode> onModeSelected,
                        BiConsumer<String, String> onPlayersSelected,
                        Function<Integer, Game> onGameStart) {

        this.onModeSelected = onModeSelected;
        this.onPlayersSelected = onPlayersSelected;
        this.onGameStart = onGameStart; // ← IMPORTANTE PARA EVITAR EL NULL POINTER

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        windowSize = new Dimension(screen.width / 2, screen.height / 2);

        showSplashScreen();
    }

    private void showSplashScreen() {
        new SplashScreen(windowSize, this::showHome);
    }

    private void showHome() {
        new Home(windowSize,
                this::showSplashScreen,
                this::showModeSelection
        );
    }

    private void showModeSelection() {

        new ModeSelection(windowSize,
                this::showHome,

                // PvP
                () -> {
                    onModeSelected.accept(GameMode.PLAYER_VS_PLAYER);
                    showCharacterSelection(CharacterSelection.GameMode.PVP);
                },

                // PvM
                () -> {
                    onModeSelected.accept(GameMode.PLAYER_VS_MACHINE);
                    showCharacterSelection(CharacterSelection.GameMode.PVM);
                },

                // MvM
                () -> {
                    onModeSelected.accept(GameMode.MACHINE_VS_MACHINE);
                    showCharacterSelection(CharacterSelection.GameMode.MVM);
                }
        );
    }

    private void showCharacterSelection(CharacterSelection.GameMode modo) {

        new CharacterSelection(
                modo,
                (p1, p2) -> onPlayersSelected.accept(p1, p2),
                this::showLevelSelection // callback para abrir niveles
        );
    }

    private void showLevelSelection() {

        new LevelSelection(windowSize,
                levelName -> {
                    System.out.println("Nivel seleccionado: " + levelName);

                    switch (levelName) {
                        case "Nivel 1": selectedLevel = 1; break;
                        case "Nivel 2": selectedLevel = 2; break;
                        case "Nivel 3": selectedLevel = 3; break;
                        case "Nivel 4": selectedLevel = 4; break;
                    }

                    showGameScreen();
                },
                this::showHome
        );
    }

    private void showGameScreen() {
        Game game = onGameStart.apply(selectedLevel); // ← ya no da NullPointer
        new GameScreen(game, this::showLevelSelection);
    }
}
