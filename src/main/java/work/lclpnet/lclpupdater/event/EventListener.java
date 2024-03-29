package work.lclpnet.lclpupdater.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import work.lclpnet.lclpupdater.LCLPUpdateChecker;
import work.lclpnet.lclpupdater.misc.UpdateScreen;
import work.lclpnet.lclpupdater.util.MainScreenChecker;
import work.lclpnet.lclpupdater.util.UpdateChecker;

@EventBusSubscriber(modid = LCLPUpdateChecker.MOD_ID, bus = Bus.FORGE)
public class EventListener {

    private static boolean updateScreenShown = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent e) {
        if (UpdateChecker.needsUpdate() && e.phase == Phase.START && isMainScreen() && !updateScreenShown) {
            updateScreenShown = true;
            Minecraft.getInstance().displayGuiScreen(new UpdateScreen());
        }
    }

    private static boolean isMainScreen() {
        Screen screen = Minecraft.getInstance().currentScreen;
        return screen instanceof MainMenuScreen || MainScreenChecker.isMainScreen(screen);
    }

}
