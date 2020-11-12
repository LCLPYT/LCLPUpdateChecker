package work.lclpnet.lclpupdater.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

public class UpdateChecker {

	private static final String INSTALLATION_URL = "https://lclpnet.work/lclplauncher/installations/ls5/info";

	private static volatile Installation updateInfo = null;
	private static volatile boolean updateRequired = false;
	
	public static Installation getUpdateInfo() {
		return updateInfo;
	}

	public static boolean needsUpdate() {
		return updateRequired;
	}

	public static void checkForUpdates() {
		new Thread(UpdateChecker::check, "Update Checker").run();
	}
	
	private static void check() {
		System.out.println("Checking for updates...");
		
		try (InputStream in = new URL(INSTALLATION_URL).openStream()) {
			updateInfo = Installation.fromInputStream(in);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		File local = new File(".installation");
		if(!local.exists()) {
			updateRequired = true;
			System.out.println("Update is required. (local installation no longer valid)");
			return;
		}
		
		String base64;
		try (InputStream in = new FileInputStream(local);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			IOUtils.copy(in, out);
			base64 = new String(out.toByteArray(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		String decoded = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
		Installation localInstall = new Gson().fromJson(decoded, Installation.class);
		
		updateRequired = localInstall.getVersionNumber() < updateInfo.getVersionNumber();
		System.out.println("An update to version " + updateInfo.getVersion() + " is required.");
	}
	
}
