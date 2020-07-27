package me.shadowchild.candor;

import com.formdev.flatlaf.FlatDarculaLaf;
import me.shadowchild.candor.module.AbstractModule;
import me.shadowchild.candor.module.ModuleSelector;
import me.shadowchild.candor.window.GameSelectScene;
import me.shadowchild.candor.window.ModScene;
import me.shadowchild.cybernize.util.ClassLoadUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CandorLauncher {
	
	public static void main(String[] args) {

		SplashScreen splash = SplashScreen.getSplashScreen();
		if(splash != null) {

			Graphics2D g = splash.createGraphics();

			g.setPaintMode();
			g.drawString("Loading Candor Resources", splash.getBounds().width / 2, splash.getBounds().height / 2);

			splash.update();
		}
		FlatDarculaLaf.install();

		try {

			ModuleSelector.initModules();
		} catch (Exception e) {

			// Probably from IDE, ignore for now
			e.printStackTrace();
		}
		ConfigHandler.handleCore();
		Runtime.getRuntime().addShutdownHook(new RuntimeHook());

		if(splash != null) splash.close();

		if(CoreConfig.showIntro) {

			GameSelectScene gameWindow = new GameSelectScene();
			gameWindow.initComponents();
			gameWindow.setVisible(true);
		} else {

			JFrame frame = new JFrame();

			File game = new File(CoreConfig.game);
			AbstractModule module = ModuleSelector.getModuleForGame(game);
			module.setGame(game);
			module.setModsFolder(new File(CoreConfig.modsFolder));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(true);
			frame.setTitle("Candor Mod Manager");
			frame.setIconImage(new ImageIcon(ClassLoadUtil.getCL().getResource("logo.png")).getImage());
			frame.setContentPane(new ModScene());
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

			CoreConfig config = (CoreConfig)ConfigHandler.getCoreConfig();
			config.close();
		}
	}
}