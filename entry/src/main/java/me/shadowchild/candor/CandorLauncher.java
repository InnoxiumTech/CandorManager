package me.shadowchild.candor;

import com.formdev.flatlaf.FlatDarculaLaf;
import me.shadowchild.candor.module.ModuleSelector;
import me.shadowchild.candor.window.GameSelectScene;
import me.shadowchild.candor.window.ManagerWindow;

public class CandorLauncher {

	public ManagerWindow window;
	
	public CandorLauncher() {
		
//		window = new ManagerWindow();
//		window.open();
	}
	
	public static void main(String[] args) {

		FlatDarculaLaf.install();

		ModuleSelector.initModules();

		GameSelectScene gameWindow = new GameSelectScene();
		gameWindow.initComponents();
		gameWindow.setVisible(true);
		new CandorLauncher();
		ConfigHandler.handleCore();
	}
}