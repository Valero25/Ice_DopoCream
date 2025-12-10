package domain.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que representa la configuración personalizable de un nivel.
 * Permite definir qué frutas y enemigos aparecerán y en qué cantidad.
 * Diseñada para ser extensible a futuro (nuevos tipos de entidades).
 */
public class LevelConfiguration {
    
    // Configuración de frutas: tipo -> cantidad
    private Map<String, Integer> fruitConfig;
    
    // Configuración de enemigos: tipo -> cantidad
    private Map<String, Integer> enemyConfig;
    
    // Configuración de obstáculos: tipo -> cantidad (para extensibilidad)
    private Map<String, Integer> obstacleConfig;
    
    // Parámetros adicionales extensibles
    private Map<String, Object> customParameters;
    
    public LevelConfiguration() {
        this.fruitConfig = new HashMap<>();
        this.enemyConfig = new HashMap<>();
        this.obstacleConfig = new HashMap<>();
        this.customParameters = new HashMap<>();
        
        // Configuración por defecto
        initializeDefaults();
    }
    
    /**
     * Inicializa valores por defecto
     */
    private void initializeDefaults() {
        // Frutas por defecto
        fruitConfig.put("BANANA", 5);
        fruitConfig.put("GRAPE", 5);
        fruitConfig.put("PINEAPPLE", 3);
        fruitConfig.put("CHERRY", 3);
        fruitConfig.put("CACTUS", 2);
        
        // Enemigos por defecto
        enemyConfig.put("TROLL", 2);
        enemyConfig.put("SQUID", 1);
        enemyConfig.put("FLOWERPOT", 1);
        enemyConfig.put("NARWHAL", 1);
        
        // Obstáculos por defecto
        obstacleConfig.put("ICE_BLOCK", 3);
        obstacleConfig.put("CAMPFIRE", 2);
        obstacleConfig.put("HOT_TILE", 4);
    }
    
    // ==================== FRUTAS ====================
    
    /**
     * Establece la cantidad de una fruta específica
     */
    public void setFruitCount(String fruitType, int count) {
        if (count < 0) count = 0;
        fruitConfig.put(fruitType.toUpperCase(), count);
    }
    
    /**
     * Obtiene la cantidad configurada de una fruta
     */
    public int getFruitCount(String fruitType) {
        return fruitConfig.getOrDefault(fruitType.toUpperCase(), 0);
    }
    
    /**
     * Obtiene todas las frutas configuradas
     */
    public Map<String, Integer> getAllFruits() {
        return new HashMap<>(fruitConfig);
    }
    
    /**
     * Obtiene lista de tipos de frutas que tienen cantidad > 0
     */
    public List<String> getActiveFruitTypes() {
        List<String> active = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : fruitConfig.entrySet()) {
            if (entry.getValue() > 0) {
                active.add(entry.getKey());
            }
        }
        return active;
    }
    
    // ==================== ENEMIGOS ====================
    
    /**
     * Establece la cantidad de un enemigo específico
     */
    public void setEnemyCount(String enemyType, int count) {
        if (count < 0) count = 0;
        enemyConfig.put(enemyType.toUpperCase(), count);
    }
    
    /**
     * Obtiene la cantidad configurada de un enemigo
     */
    public int getEnemyCount(String enemyType) {
        return enemyConfig.getOrDefault(enemyType.toUpperCase(), 0);
    }
    
    /**
     * Obtiene todos los enemigos configurados
     */
    public Map<String, Integer> getAllEnemies() {
        return new HashMap<>(enemyConfig);
    }
    
    /**
     * Obtiene lista de tipos de enemigos que tienen cantidad > 0
     */
    public List<String> getActiveEnemyTypes() {
        List<String> active = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : enemyConfig.entrySet()) {
            if (entry.getValue() > 0) {
                active.add(entry.getKey());
            }
        }
        return active;
    }
    
    // ==================== OBSTÁCULOS (EXTENSIBILIDAD) ====================
    
    /**
     * Establece la cantidad de un obstáculo específico
     */
    public void setObstacleCount(String obstacleType, int count) {
        if (count < 0) count = 0;
        obstacleConfig.put(obstacleType.toUpperCase(), count);
    }
    
    /**
     * Obtiene la cantidad configurada de un obstáculo
     */
    public int getObstacleCount(String obstacleType) {
        return obstacleConfig.getOrDefault(obstacleType.toUpperCase(), 0);
    }
    
    /**
     * Obtiene todos los obstáculos configurados
     */
    public Map<String, Integer> getAllObstacles() {
        return new HashMap<>(obstacleConfig);
    }
    
    // ==================== PARÁMETROS PERSONALIZADOS (EXTENSIBILIDAD) ====================
    
    /**
     * Establece un parámetro personalizado
     * Útil para extensiones futuras sin modificar la clase
     */
    public void setCustomParameter(String key, Object value) {
        customParameters.put(key, value);
    }
    
    /**
     * Obtiene un parámetro personalizado
     */
    public Object getCustomParameter(String key) {
        return customParameters.get(key);
    }
    
    /**
     * Obtiene un parámetro personalizado con valor por defecto
     */
    public Object getCustomParameter(String key, Object defaultValue) {
        return customParameters.getOrDefault(key, defaultValue);
    }
    
    // ==================== UTILIDADES ====================
    
    /**
     * Resetea la configuración a valores por defecto
     */
    public void resetToDefaults() {
        fruitConfig.clear();
        enemyConfig.clear();
        obstacleConfig.clear();
        customParameters.clear();
        initializeDefaults();
    }
    
    /**
     * Crea una copia de esta configuración
     */
    public LevelConfiguration copy() {
        LevelConfiguration copy = new LevelConfiguration();
        copy.fruitConfig = new HashMap<>(this.fruitConfig);
        copy.enemyConfig = new HashMap<>(this.enemyConfig);
        copy.obstacleConfig = new HashMap<>(this.obstacleConfig);
        copy.customParameters = new HashMap<>(this.customParameters);
        return copy;
    }
    
    /**
     * Obtiene el total de frutas configuradas
     */
    public int getTotalFruits() {
        return fruitConfig.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Obtiene el total de enemigos configurados
     */
    public int getTotalEnemies() {
        return enemyConfig.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Valida que la configuración sea válida
     */
    public boolean isValid() {
        // Debe haber al menos una fruta o un enemigo
        return getTotalFruits() > 0 || getTotalEnemies() > 0;
    }
    
    @Override
    public String toString() {
        return String.format("LevelConfig[Frutas: %d, Enemigos: %d, Obstáculos: %d]",
                getTotalFruits(), getTotalEnemies(), 
                obstacleConfig.values().stream().mapToInt(Integer::intValue).sum());
    }
}
