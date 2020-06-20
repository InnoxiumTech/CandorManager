package me.shadowchild.modmanager;

import me.shadowchild.modmanager.window.ManagerWindow;

public class ModManager {

	public ManagerWindow window;
	
	public ModManager() {
		
		window = new ManagerWindow();
		window.open();
	}
	
	public static void main(String[] args) {

//		new ModManager();
		ConfigHandler.handleCore();
	}
}