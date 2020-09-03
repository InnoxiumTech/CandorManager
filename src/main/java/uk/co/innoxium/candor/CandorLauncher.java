package uk.co.innoxium.candor;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.lwjgl.system.Platform;
import uk.co.innoxium.candor.mod.store.ModStore;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.util.Dialogs;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.candor.window.GameSelectScene;
import uk.co.innoxium.candor.window.ModScene;
import uk.co.innoxium.cybernize.util.ClassLoadUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

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
		} catch (Exception e) {

			// Probably from IDE, ignore for now
			e.printStackTrace();
		}
		ConfigHandler.handleCore();
		Runtime.getRuntime().addShutdownHook(new RuntimeHook());

		if(splash != null) splash.close();

		if(Settings.showIntro) {

			GameSelectScene gameWindow = new GameSelectScene();
			gameWindow.initComponents();
			gameWindow.setVisible(true);
		} else {

			JFrame frame = new JFrame();

			File game = new File(Settings.gameExe);
			AbstractModule module = ModuleSelector.getModuleForGame(game);
			module.setGame(game);
			module.setModsFolder(new File(Settings.modsFolder));
			ModStore.initialise();
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {

					boolean result = Dialogs.showInfoDialog(
							"Candor Mod Manager",
							"Are you sure you wish to exit?",
							"yesno",
							"question",
							false);
					if(result) System.exit(0);
				}
			});
			frame.setResizable(true);
			frame.setTitle("Candor Mod Manager");
			frame.setIconImage(new ImageIcon(ClassLoadUtil.getCL().getResource("logo.png")).getImage());
			ModScene modScene = new ModScene();
			Resources.currentScene = modScene;
			frame.setContentPane(modScene);
			frame.setMinimumSize(new Dimension(1200, 768));
			// TODO: Allow the window to stay on the same screen it was used on
			frame.setLocationRelativeTo(null);
			frame.pack();
			frame.setVisible(true);
		}
	}

	private static class RuntimeHook extends Thread {

		@Override
		public void run() {

//			CoreConfig config = (CoreConfig)ConfigHandler.getCoreConfig();
//			config.close();
		}
	}
}