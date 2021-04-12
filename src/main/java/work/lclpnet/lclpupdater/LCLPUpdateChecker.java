package work.lclpnet.lclpupdater;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.lclpupdater.event.EventListener;
import work.lclpnet.lclpupdater.util.MainScreenChecker;
import work.lclpnet.lclpupdater.util.UpdateChecker;

@Mod(LCLPUpdateChecker.MODID)
public class LCLPUpdateChecker {

    public static final String MODID = "lclpupdater";
    private static final Logger LOGGER = LogManager.getLogger();

    public LCLPUpdateChecker() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setup);
        modBus.addListener(this::onIMCProcess);

        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.register(this);
        bus.register(new EventListener());
    }

    private void setup(final FMLCommonSetupEvent event) { //preinit
        LOGGER.info("LCLPUpdateChecker initializing...");

        UpdateChecker.checkForUpdates();

        LOGGER.info("LCLPUpdateChecker initialized.");
    }

    public void onIMCProcess(InterModProcessEvent e) {
        e.getIMCStream().forEach(MainScreenChecker::tryAccept);
    }

}
