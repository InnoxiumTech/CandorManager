package uk.co.innoxium.candor.window.modscene;

import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.mod.store.ModStore;
import uk.co.innoxium.candor.util.WindowUtils;

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

        if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2 && !e.isConsumed()) {

            // Find way to toggle mods
            modScene.toggleSelectedMods(null);
            e.consume();
            return;
        }

        if(SwingUtilities.isLeftMouseButton(e) && ((JList<?>)e.getSource()).getModel().getSize() != 0) {

            JList<?> list = (JList<?>) e.getSource();
            int index = list.locationToIndex(e.getPoint());
            Mod mod = (Mod) list.getModel().getElementAt(index);
//                    ((ListRenderer) list.getCellRenderer()).selected = !((ListRenderer) list.getCellRenderer()).selected;
            list.repaint(list.getCellBounds(index, index));
        }
        if(SwingUtilities.isRightMouseButton(e) && ((JList<?>)e.getSource()).getModel().getSize() != 0) {

            JList<?> list = (JList<?>) e.getSource();
            int index = list.locationToIndex(e.getPoint());
            list.setSelectedIndex(index);
            JPopupMenu menu = new JPopupMenu("Options");
            JMenuItem renameOption = new JMenuItem("Rename Mod");
            renameOption.addActionListener(event -> {

                Mod mod = (Mod) list.getModel().getElementAt(index);
                System.out.println("Rename clicked on mod: " + mod.getName());
                String newName = (String) JOptionPane.showInputDialog(WindowUtils.mainFrame,
                        "Please input the new name for the Mod " + mod.getReadableName(),
                        "Rename Mod",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        mod.getReadableName());

                if(newName != null && !newName.isEmpty()) {

                    mod.setReadableName(newName);
                    ModStore.updateModState(mod, mod.getState());
                    ModStore.MODS.fireChangeToListeners("rename", mod, true);
                }
            });
            menu.add(renameOption);
            menu.show(list, e.getPoint().x, e.getPoint().y);
        }
    }
}
