package work.lclpnet.lclpupdater.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public class Helper {

	private static final String WIN_EXE_NAME = "LCLPLauncher.exe",
			WIN_EXE_STANDARD = System.getProperty("user.home") + "\\AppData\\Local\\Programs\\lclplauncher\\" + WIN_EXE_NAME;

	public static void startLCLPLauncher(Consumer<Boolean> successHandler) {
		OSHooks.startLCLPLauncher(successHandler);
	}

	public static File getLauncherWindowsExecuteable() {
		if(!OsUtils.isWindows()) return null;
		
		File standard = new File(WIN_EXE_STANDARD);
		if(standard.exists()) {
			System.out.println("Using standard installation location.");
			return standard;
		}
		
		return tryReadFromWinRegistry();
	}

	private static File tryReadFromWinRegistry() {
		if(!OsUtils.isWindows()) {
			System.err.println("Error, can't read from the windows registry on a non-windows os!");
			return null;
		}
		
		String value;
		try {
			value = WinRegistry.readString(
					WinRegistry.HKEY_CURRENT_USER,
					"Software\\lclplauncher",
					"InstallLocation");
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}

		return new File(value, WIN_EXE_NAME);
	}
	
}
