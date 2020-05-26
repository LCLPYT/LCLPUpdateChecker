package work.lclpnet.lclpupdater.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public class Helper {

	private static final String WIN_EXE_NAME = "LCLPLauncher.exe",
			WIN_EXE_STANDARD = System.getProperty("user.home") + "\\AppData\\Local\\Programs\\lclplauncher\\" + WIN_EXE_NAME;

	public static void startLCLPLauncher(Consumer<Boolean> successHandler) {
		File exe = getLauncherExecuteable();
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
			Runtime.getRuntime().exec("\"" + exe.getAbsolutePath() + "\" --update=ls5");
			successHandler.accept(true);
		} catch (IOException e) {
			successHandler.accept(false);
			e.printStackTrace();
		}
	}

	private static File getLauncherExecuteable() {
		if(!OsUtils.isWindows()) {
			System.err.println("Error, the automatic LCLPLauncher start is currently only available on Windows.");
			return null;
		}
		
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
