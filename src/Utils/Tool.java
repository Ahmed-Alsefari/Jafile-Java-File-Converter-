package Utils;

import java.io.IOException;

import java.util.concurrent.TimeUnit;


public class Tool {

    public static void command(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
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
            }
        }
    }
}

