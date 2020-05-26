package work.lclpnet.lclpupdater;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import work.lclpnet.lclpupdater.event.EventListener;
import work.lclpnet.lclpupdater.util.UpdateChecker;

@Mod(LCLPUpdateChecker.MODID)
public class LCLPUpdateChecker {
	
	public static final String MODID = "lclpupdater";
	private static final Logger LOGGER = LogManager.getLogger();

	public LCLPUpdateChecker() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		IEventBus bus = MinecraftForge.EVENT_BUS;
		bus.register(this);
		bus.register(new EventListener());
	}

	private void setup(final FMLCommonSetupEvent event) { //preinit
		LOGGER.info("LCLPUpdateChecker initializing...");

		UpdateChecker.checkForUpdates();
		
		LOGGER.info("LCLPUpdateChecker initialized.");
	}
	
}
