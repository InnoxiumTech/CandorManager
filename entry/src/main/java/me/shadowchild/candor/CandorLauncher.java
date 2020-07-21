package me.shadowchild.candor;

import com.formdev.flatlaf.FlatDarculaLaf;
import me.shadowchild.candor.module.AbstractModule;
import me.shadowchild.candor.module.ModuleSelector;
import me.shadowchild.candor.window.GameSelectScene;
import me.shadowchild.candor.window.ManagerWindow;
import me.shadowchild.candor.window.ModScene;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CandorLauncher {

	public ManagerWindow window;
	
	public CandorLauncher() {
		
//		window = new ManagerWindow();
//		window.open();
	}
	
	public static void main(String[] args) {

		FlatDarculaLaf.install();

		ModuleSelector.initModules();
		ConfigHandler.handleCore();
		Runtime.getRuntime().addShutdownHook(new RuntimeHook());

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
			frame.setContentPane(new ModScene());
			frame.setMinimumSize(new Dimension(1200, 768));
			// TODO: Allow the window to stay on the same screen it was used on
			frame.setLocationRelativeTo(null);
			frame.pack();
			frame.setVisible(true);
		}
		new CandorLauncher();
	}

	private static class RuntimeHook extends Thread {

		@Override
		public void run() {

			CoreConfig config = (CoreConfig)ConfigHandler.getCoreConfig();
			config.close();
		}
	}
}