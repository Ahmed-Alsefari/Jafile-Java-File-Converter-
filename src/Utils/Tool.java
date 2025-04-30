package Utils;

import java.io.IOException;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;


public class Tool {

    public static void command(String command) {
        Process process = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
            process.waitFor(120, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            System.err.println("Cannot execute command: " + e);
        } finally {
            killProcessIfRunning(process);
        }
    }

    public static void killProcessIfRunning(Process process) {
        if (process != null && process.isAlive()) {
            process.destroy();
            try {
                if (!process.waitFor(3, java.util.concurrent.TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                }
            } catch (InterruptedException e) {
                System.err.println("Error while waiting for process to end: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
    public static String getFileExtension(Path file) {
        String fileName = file.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}

