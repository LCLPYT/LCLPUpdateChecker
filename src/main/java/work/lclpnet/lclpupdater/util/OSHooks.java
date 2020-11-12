package work.lclpnet.lclpupdater.util;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class OSHooks {

	private static class OSHandler {
        public void startLCLPLauncher(Consumer<Boolean> successHandler) {
			try {
				Runtime.getRuntime().exec("lclplauncher --update=ls5");
				successHandler.accept(true);
			} catch (IOException e) {
				successHandler.accept(false);
				e.printStackTrace();
			}
        }
    }

    private static class LinuxHandler extends OSHandler {
        // Override methods here
    }

    private static class WinHandler extends OSHandler {
    	@Override
    	public void startLCLPLauncher(Consumer<Boolean> successHandler) {
    		File exe = Helper.getLauncherWindowsExecuteable();
    		if(exe == null) {
    			System.out.println("Could not find the LCLPLauncher executeable anywhere...");
    			successHandler.accept(false);
    			return;
    		}
    		else if(!exe.exists()) {
    			System.err.println("Error, '" + exe.getAbsolutePath() + "' does not exist.");
    			successHandler.accept(false);
    			return;
    		}

    		System.out.println("Found executeable at: '" + exe.getAbsolutePath() + "'.");
    		
    		try {
    			Runtime.getRuntime().exec("cmd /C cd \"" + exe.getParentFile().getAbsolutePath() + "\" & \"" + exe.getAbsolutePath() + "\" --update=ls5");
    			successHandler.accept(true);
    		} catch (IOException e) {
    			successHandler.accept(false);
    			e.printStackTrace();
    		}
    	}
    }

    private static final OSHandler handler;

    static {
        if(System.getProperty("os.name").equalsIgnoreCase("Linux")) handler = new LinuxHandler();
        else if(System.getProperty("os.name").contains("Windows")) handler = new WinHandler();
        else handler = new OSHandler();
    }

    public static void startLCLPLauncher(Consumer<Boolean> successHandler) {
    	handler.startLCLPLauncher(successHandler);
    }
	
}
