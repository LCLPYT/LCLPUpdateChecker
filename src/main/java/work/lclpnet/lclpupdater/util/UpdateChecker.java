package work.lclpnet.lclpupdater.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.lclpupdater.event.UpdateCheckCompleteEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateChecker {

    private static final Logger logger = LogManager.getLogger();
    private static final AtomicBoolean updateRequired = new AtomicBoolean(false);

    public static boolean needsUpdate() {
        return updateRequired.get();
    }

    public static void checkForUpdates() {
        new Thread(UpdateChecker::check, "Update Checker").start();
    }

    private static void check() {
        logger.info("Checking for updates...");

        Helper.getLCLPLauncherExecutable()
                .thenAcceptAsync(UpdateChecker::checkForUpdates)
                .exceptionally(ex -> {
                    logger.error("Could not check for updates", ex);
                    return null;
                });
    }

    private static void checkForUpdates(String program) {
        if (program == null) throw new IllegalStateException("LCLPLauncher executable could not be found.");

        try {
            Process process = new ProcessBuilder(program, "check-for-app-update", "ls5")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    if ("[update-check]: Result -> NEEDS_UPDATE".equals(line)) {
                        updateRequired.set(true);
                        break;
                    }
                }
                process.waitFor();

                final boolean updateAvailable = updateRequired.get();
                logger.info(updateAvailable ? "An update is available." : "Already up-to-date.");
                UpdateCheckCompleteEvent.EVENT.invoker().onUpdateCheckComplete(updateAvailable);
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
