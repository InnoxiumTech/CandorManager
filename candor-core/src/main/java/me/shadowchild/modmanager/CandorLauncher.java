package me.shadowchild.modmanager;

import me.shadowchild.modmanager.window.ManagerWindow;

public class CandorLauncher {

	public ManagerWindow window;
	
	public CandorLauncher() {
		
		window = new ManagerWindow();
		window.open();
	}
	
	public static void main(String[] args) {

		System.out.println("Server starting...");

		new CandorLauncher();
		ConfigHandler.handleCore();
	}
}