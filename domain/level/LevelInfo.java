package domain.level;

public class LevelInfo {
    private String[] fruitTypes;
    private int[] fruitCounts;
    private String[] enemyTypes;
    private int[] enemyCounts;
    
    public LevelInfo(String[] fruitTypes, int[] fruitCounts, String[] enemyTypes, int[] enemyCounts) {
        this.fruitTypes = fruitTypes;
        this.fruitCounts = fruitCounts;
        this.enemyTypes = enemyTypes;
        this.enemyCounts = enemyCounts;
    }
    
    public String[] getFruitTypes() {
        return fruitTypes;
    }
    
    public int[] getFruitCounts() {
        return fruitCounts;
    }
    
    public String[] getEnemyTypes() {
        return enemyTypes;
    }
    
    public int[] getEnemyCounts() {
        return enemyCounts;
    }
    
    public static LevelInfo getInfoForLevel(String levelName) {
        switch (levelName) {
            case "LEVEL_1":
                // Nivel 1: Oleada 1: 8 Uvas, Oleada 2: 8 Bananas
                // Enemigos: 2 Trolls
                return new LevelInfo(
                    new String[]{"GRAPE", "BANANA"},
                    new int[]{8, 8},
                    new String[]{"TROLL"},
                    new int[]{2}
                );
                
            case "LEVEL_2":
                // Nivel 2: Oleada 1: 8 Piñas, Oleada 2: 8 Cerezas
                // Enemigos: 2 Calamares, 1 Maceta
                return new LevelInfo(
                    new String[]{"PINEAPPLE", "CHERRY"},
                    new int[]{8, 8},
                    new String[]{"SQUID", "FLOWERPOT"},
                    new int[]{2, 1}
                );
                
            case "LEVEL_3":
                // Nivel 3: Oleada 1: 8 Uvas, Oleada 2: 4 Piñas + 4 Cerezas
                // Enemigos: 1 Troll, 1 Calamar, 1 Narval
                return new LevelInfo(
                    new String[]{"GRAPE", "PINEAPPLE", "CHERRY"},
                    new int[]{8, 4, 4},
                    new String[]{"TROLL", "SQUID", "NARWHAL"},
                    new int[]{1, 1, 1}
                );
                
            default:
                return new LevelInfo(
                    new String[]{"GRAPE"},
                    new int[]{8},
                    new String[]{"TROLL"},
                    new int[]{1}
                );
        }
    }
}
