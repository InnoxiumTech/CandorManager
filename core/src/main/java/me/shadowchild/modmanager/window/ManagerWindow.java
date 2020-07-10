package me.shadowchild.modmanager.window;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.pushingpixels.substance.api.SubstanceCortex;
import org.pushingpixels.substance.api.skin.GraphiteGlassSkin;

import javax.swing.*;
import java.awt.*;

public class ManagerWindow {

	private final JFrame frame;

	public ManagerWindow() {
		FlatDarculaLaf.install();
		frame =  new JFrame("Cybernize Mod Manager - By ShadowChild");
		frame.setVisible(false);
		setUpComponents();
	}

	private void setUpComponents() {

//		LayoutManager manager = new BorderLayout(1, 1);
//		frame.setLayout(manager);
		frame.setMinimumSize(new Dimension(1200, 768));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(new ModScene());
	}

	public void open() {

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
