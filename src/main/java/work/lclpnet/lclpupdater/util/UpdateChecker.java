package work.lclpnet.lclpupdater.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UpdateChecker {

    private static volatile boolean updateRequired = false;

    public static boolean needsUpdate() {
        return updateRequired;
    }

    public static void checkForUpdates() {
        new Thread(UpdateChecker::check, "Update Checker").start();
    }

    private static void check() {
        System.out.println("Checking for updates...");

        Helper.getLCLPLauncherExecutable()
                .thenAcceptAsync(UpdateChecker::checkForUpdates)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    private static void checkForUpdates(String exe) {
        if (exe == null) throw new IllegalStateException("LCLPLauncher executable could not be found.");

        try {
            Process process = new ProcessBuilder(exe, "check-for-app-update", "ls5")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    if ("[update-check]: Result -> NEEDS_UPDATE".equals(line)) {
                        updateRequired = true;
                        System.out.println("An update is available.");
                        break;
                    }
                }
                process.waitFor();
                if (!updateRequired) System.out.println("No update available.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
