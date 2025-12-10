package domain;

public enum GameMode {
    PLAYER_VS_PLAYER("Player vs Player"),
    PLAYER_VS_MACHINE("Player vs Machine"),
    MACHINE_VS_MACHINE("Machine vs Machine");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
