package uk.co.innoxium.candor.window.modscene;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ModSceneMouseAdapter extends MouseAdapter {

    private final ModScene modScene;

    public ModSceneMouseAdapter(ModScene modScene) {

        this.modScene = modScene;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if(e.getSource() instanceof JTable) {

            // Handle double click
            if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2 && !e.isConsumed()) {

                // Find way to toggle mods
                modScene.toggleSelectedModsTable(null);
                e.consume();
                return;
            }
        }
    }
}
