package uk.co.innoxium.candor;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.lwjgl.system.Platform;
import uk.co.innoxium.candor.game.GamesList;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.util.WindowUtils;

import java.awt.*;

public class CandorLauncher {
	
	public static void main(String[] args) {

		if(Platform.get() == Platform.MACOSX) {

			// We want to restart the application here with the -XstartOnFirstThread JVM argument
			// This is slightly more difficult for us as we have the java agent to deal with
		}

		SplashScreen splash = SplashScreen.getSplashScreen();
		if(splash != null) {

			Graphics2D g = splash.createGraphics();

			g.setPaintMode();
			g.setFont(new Font("Calibri", Font.PLAIN, 25));
			g.drawString("Loading Candor Resources", splash.getBounds().width / 2, splash.getBounds().height / 2);

			splash.update();
		}
		FlatDarculaLaf.install();

		try {

			ModuleSelector.checkGenericModule();
			ModuleSelector.initModules();
			GamesList.loadFromFile();
		} catch (Exception e) {

			// Probably from IDE, ignore for now
			e.printStackTrace();
		}
		ConfigHandler.handleCore();
		Runtime.getRuntime().addShutdownHook(new RuntimeHook());

		if(splash != null) splash.close();

//		if(Settings.showIntro) {
//
//			GameSelectScene gameWindow = new GameSelectScene();
//			gameWindow.initComponents();
//			gameWindow.setVisible(true);
//		} else {
//
			WindowUtils.initialiseEntryScene();
//		}
	}

	private static class RuntimeHook extends Thread {

		@Override
		public void run() {

//			CoreConfig config = (CoreConfig)ConfigHandler.getCoreConfig();
//			config.close();
		}
	}
}