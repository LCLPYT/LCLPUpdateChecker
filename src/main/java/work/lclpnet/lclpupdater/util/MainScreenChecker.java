package work.lclpnet.lclpupdater.util;

import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.InterModComms.IMCMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.lclpupdater.LCLPUpdateChecker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainScreenChecker {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Set<Class<?>> mainScreenClasses = new HashSet<>();

    public static void tryAccept(IMCMessage msg) {
        if (!"defineStartingScreens".equals(msg.getMethod())
                || !LCLPUpdateChecker.MODID.equals(msg.getModId())) return;

        Object o = msg.getMessageSupplier().get();
        if (!(o instanceof Class<?>[])) return;

        Class<?>[] arr = (Class<?>[]) o;
        Arrays.stream(arr).filter(c -> !mainScreenClasses.contains(c)).forEach(cl -> {
            mainScreenClasses.add(cl);
            LOGGER.debug("Registered main screen class {}", cl.getName());
        });
    }

    public static boolean isMainScreen(Screen screen) {
        return mainScreenClasses.stream().anyMatch(c -> c.isInstance(screen));
    }

}
