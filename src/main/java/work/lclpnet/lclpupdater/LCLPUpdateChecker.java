package work.lclpnet.lclpupdater;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.lclpupdater.util.UpdateChecker;

public class LCLPUpdateChecker implements ClientModInitializer {

    public static final String MOD_ID = "lclpupdater";
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        UpdateChecker.checkForUpdates();
    }
}
