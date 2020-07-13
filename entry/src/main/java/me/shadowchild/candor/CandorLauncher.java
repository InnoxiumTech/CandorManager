package me.shadowchild.candor;

import com.formdev.flatlaf.FlatDarculaLaf;
import me.shadowchild.modmanager.ConfigHandler;
import me.shadowchild.modmanager.window.GameSelectScene;
import me.shadowchild.modmanager.window.ManagerWindow;

public class CandorLauncher {

	public ManagerWindow window;
	
	public CandorLauncher() {
		
//		window = new ManagerWindow();
//		window.open();
	}
	
	public static void main(String[] args) {

		FlatDarculaLaf.install();

		GameSelectScene gameWindow = new GameSelectScene();
		gameWindow.initComponents();
		gameWindow.setVisible(true);
		new CandorLauncher();
		ConfigHandler.handleCore();
	}
}