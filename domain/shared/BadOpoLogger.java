package domain.shared;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger personalizado para registrar errores y eventos del sistema.
 * Guarda los logs en un archivo de texto persistente.
 */
public class BadOpoLogger {

    private static final String LOG_FILE = "badopo_errors.log";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Registra un mensaje informativo.
     */
    public static void log(String message) {
        writeLog("INFO", message, null);
    }

    /**
     * Registra un error con su traza.
     */
    public static void logError(String message, Throwable error) {
        writeLog("ERROR", message, error);
    }

    /**
     * Registra una excepción.
     */
    public static void logError(Throwable error) {
        writeLog("ERROR", error.getMessage(), error);
    }

    private static void writeLog(String level, String message, Throwable error) {
        // Usamos try-with-resources para asegurar el cierre del archivo
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {

            String timestamp = LocalDateTime.now().format(DATE_FORMAT);
            String logMsg = String.format("[%s] [%s] %s", timestamp, level, message);

            // Escribir en archivo
            out.println(logMsg);

            if (error != null) {
                error.printStackTrace(out);
            }

            out.println("--------------------------------------------------"); // Separador visual

            // También mostrar en consola para depuración inmediata
            System.err.println(logMsg);
            if (error != null) {
                error.printStackTrace();
            }

        } catch (IOException e) {
            System.err.println("CRITICAL: No se pudo escribir en el archivo de log.");
            e.printStackTrace();
        }
    }
}
