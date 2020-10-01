/*
 * Created by JFormDesigner on Mon Jun 22 16:06:01 BST 2020
 */

package uk.co.innoxium.candor.window;

import com.formdev.flatlaf.FlatIconColors;
import com.github.f4b6a3.uuid.util.UuidConverter;
import net.miginfocom.swing.MigLayout;
import uk.co.innoxium.candor.game.Game;
import uk.co.innoxium.candor.game.GamesList;
import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.mod.ModList;
import uk.co.innoxium.candor.mod.store.ModStore;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.module.RunConfig;
import uk.co.innoxium.candor.thread.ThreadModInstaller;
import uk.co.innoxium.candor.util.Dialogs;
import uk.co.innoxium.candor.util.Logger;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.candor.util.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;


public class ModScene extends JPanel {

    public ModScene(String gameUuid) {

        // Set game stuff
        Game game = GamesList.getGameFromUUID(UuidConverter.fromString(gameUuid));
        assert game != null;
        AbstractModule module = ModuleSelector.getModuleForGame(game);
        module.setGame(new File(game.getGameExe()));
        module.setModsFolder(new File(game.getModsFolder()));
        ModStore.initialise();

        try {

            WindowUtils.mainFrame.setMinimumSize(new Dimension(1200, 768));
            ModStore.determineInstalledMods();
        } catch (IOException e) {

            Logger.info("This shouldn't happen, likely a corrupt mods.json :(");
            e.printStackTrace();
            System.exit(-1);
        }
        initComponents();
        ModStore.MODS.addListener(new ModList.ListChangeListener<Mod>() {

            @Override
            public void handleChange(String identifier, Mod mod, boolean result) {

                installedModsJList.setListData(ModStore.MODS.toArray());
            }

            @Override
            public void handleChange(String identifier, Collection<? extends Mod> c, boolean result) {

                installedModsJList.setListData(ModStore.MODS.toArray());
            }
        });
    }

    private void createUIComponents() {


        installedModsJList = new JList(ModStore.MODS.toArray());

        installedModsJList.setCellRenderer(new ListRenderer());
        installedModsJList.setFont(Resources.fantasque.deriveFont(24f));
        installedModsJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        installedModsJList.setSelectionModel(new DefaultListSelectionModel() {

            private static final long serialVersionUID = 1L;

            boolean gestureStarted = false;

            @Override
            public void setSelectionInterval(int index0, int index1) {

                if(!gestureStarted) {

                    if (index0 == index1) {

                        if (isSelectedIndex(index0)) {

                            removeSelectionInterval(index0, index0);
                            return;
                        }
                    }
                    super.setSelectionInterval(index0, index1);
                }
                gestureStarted = true;
            }

            @Override
            public void addSelectionInterval(int index0, int index1) {

                if (index0==index1) {

                    if (isSelectedIndex(index0)) {

                        removeSelectionInterval(index0, index0);
                        return;
                    }
                    super.addSelectionInterval(index0, index1);
                }
            }

            @Override
            public void setValueIsAdjusting(boolean isAdjusting) {

                if (!isAdjusting) {

                    gestureStarted = false;
                }
            }
        });
        installedModsJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(e.getButton() == MouseEvent.BUTTON1 && ((JList)e.getSource()).getModel().getSize() != 0) {

                    JList list = (JList) e.getSource();
                    int index = list.locationToIndex(e.getPoint());
                    Mod mod = (Mod) list.getModel().getElementAt(index);
//                    ((ListRenderer) list.getCellRenderer()).selected = !((ListRenderer) list.getCellRenderer()).selected;
                    list.repaint(list.getCellBounds(index, index));
                }
            }
        });
    }

    private void settingsClicked(ActionEvent e) {

        // Disable while we don't have enough settings
//        JDialog dialog = new SettingsFrame();
//        dialog.setAlwaysOnTop(true);
//        dialog.setLocationRelativeTo(this);
//        dialog.setVisible(true);
    }

    private void addModClicked(ActionEvent e) {

        Dialogs.openMultiFileDialog(ModuleSelector.currentModule.getModFileFilterList()).forEach(file -> {

            ModStore.Result result = ModStore.addModFile(file);

            switch(result) {

                case DUPLICATE -> Dialogs.showErrorMessage("Mod is a Duplicate and already installed.\nIf updating, please uninstall old file first.");
                case FAIL -> Dialogs.showErrorMessage(String.format("Mod file %s could not be added.\nPlease try again.", file.getName()));
                // Fallthrough on default
                default -> {}
            }
        });
    }

    private void removeModsSelected(ActionEvent e) {

        if(Dialogs.showConfirmDialog("Remove Selected Mods")) {

            if(installedModsJList.getSelectedValuesList().isEmpty()) {
                Dialogs.showInfoDialog(
                        "Candor Mod Manager",
                        "You have not selected any mods to remove.",
                        "ok",
                        "warning",
                        false);
            }
            installedModsJList.getSelectedValuesList().forEach(o -> {

                try {

                    ModStore.removeModFile((Mod) o, true);
                } catch (IOException exception) {

                    exception.printStackTrace();
                }
            });
        }
    }

    private void installModsClicked(ActionEvent e) {
        
        installedModsJList.getSelectedValuesList().forEach(o -> {

            Mod mod = (Mod)o;
            new ThreadModInstaller(mod).start();
        });
    }

    private void runGameClicked(ActionEvent e) {
        
        RunConfig runConfig = ModuleSelector.currentModule.getDefaultRunConfig();
        
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(runConfig.getStartCommand() + " " + runConfig.getProgramArgs());
        String workingDir = runConfig.getWorkingDir();
        if(workingDir != null && !workingDir.isEmpty()) {

            builder.directory(new File(workingDir));
        }
        try {

            Logger.info(builder.command().toString());
            Process process = builder.start();
        } catch (IOException ioException) {

            ioException.printStackTrace();
        }
    }


    // TODO: Add support for modules to determine how to toggle mods, e.g. via a plugin list for GameBryo games
    private void toggleSelectedMods(ActionEvent e) {

        if(Dialogs.showConfirmDialog("Toggle Selected Mods")) {

            if(installedModsJList.getSelectedValuesList().isEmpty()) {

                Dialogs.showInfoDialog(
                        "Candor Mod Manager",
                        "You have not selected any mods to toggle.",
                        "ok",
                        "warning",
                        false);
            }
            installedModsJList.getSelectedValuesList().forEach(o -> {

                try {

                    ModStore.removeModFile((Mod) o, false);
                } catch (IOException exception) {

                    exception.printStackTrace();
                }
            });
        }
    }

    private void newGameClicked(ActionEvent e) {

        WindowUtils.setupGameSelectScene();
    }

    private void aboutClicked(ActionEvent e) {

        AboutDialog dialog = new AboutDialog(WindowUtils.mainFrame);
        dialog.setResizable(true);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        managerPanel = new JPanel();
        managerPaneMenu = new JPanel();
        gameLabel = new JLabel();
        addModButton = new JButton();
        removeModsButton = new JButton();
        installModsButton = new JButton();
        toggleButton = new JButton();
        listScrollPane = new JScrollPane();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        applyModsMenuItem = new JMenuItem();
        loadNewGameMenuItem = new JMenuItem();
        settingsMenuItem = new JMenuItem();
        aboutMenuItem = new JMenuItem();
        gameMenu = new JMenu();
        openGameFolderMenuItem = new JMenuItem();
        opemModsFolderMenuItem = new JMenuItem();
        launchGameMenuItem = new JMenuItem();
        runConfigsMenuItem = new JMenuItem();

        //======== this ========
        setLayout(new BorderLayout());

        //======== managerPanel ========
        {
            managerPanel.setLayout(new MigLayout(
                "fill,insets panel,hidemode 3",
                // columns
                "[fill]",
                // rows
                "[]" +
                "[]"));

            //======== managerPaneMenu ========
            {
                managerPaneMenu.setLayout(new FlowLayout(FlowLayout.LEFT));

                //---- gameLabel ----
                gameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                gameLabel.setText(ModuleSelector.currentModule.getReadableGameName().toUpperCase());
                managerPaneMenu.add(gameLabel);

                //---- addModButton ----
                addModButton.setText("Add Mod(s)");
                addModButton.setIcon(UIManager.getIcon("Tree.leafIcon"));
                addModButton.addActionListener(e -> addModClicked(e));
                managerPaneMenu.add(addModButton);

                //---- removeModsButton ----
                removeModsButton.setText("Remove Selected");
                removeModsButton.setIcon(null);
                removeModsButton.addActionListener(e -> removeModsSelected(e));
                managerPaneMenu.add(removeModsButton);

                //---- installModsButton ----
                installModsButton.setText("Install Selected Mod(s)");
                installModsButton.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
                installModsButton.addActionListener(e -> installModsClicked(e));
                managerPaneMenu.add(installModsButton);

                //---- toggleButton ----
                toggleButton.setText("Toggle Enabled");
                toggleButton.addActionListener(e -> toggleSelectedMods(e));
                managerPaneMenu.add(toggleButton);
            }
            managerPanel.add(managerPaneMenu, "cell 0 0");

            //======== listScrollPane ========
            {
                listScrollPane.setViewportView(installedModsJList);
            }
            managerPanel.add(listScrollPane, "cell 0 1,dock center");
        }
        add(managerPanel, BorderLayout.CENTER);

        //======== menuBar ========
        {

            //======== fileMenu ========
            {
                fileMenu.setText("File");
                fileMenu.setMnemonic('F');

                //---- applyModsMenuItem ----
                applyModsMenuItem.setText("Apply Mod(s)");
                applyModsMenuItem.setMnemonic('A');
                fileMenu.add(applyModsMenuItem);

                //---- loadNewGameMenuItem ----
                loadNewGameMenuItem.setText("Load New Game");
                loadNewGameMenuItem.setMnemonic('L');
                loadNewGameMenuItem.addActionListener(e -> newGameClicked(e));
                fileMenu.add(loadNewGameMenuItem);

                //---- settingsMenuItem ----
                settingsMenuItem.setText("Settings");
                settingsMenuItem.setMnemonic('S');
                settingsMenuItem.addActionListener(e -> settingsClicked(e));
                fileMenu.add(settingsMenuItem);

                //---- aboutMenuItem ----
                aboutMenuItem.setText("About");
                aboutMenuItem.addActionListener(e -> aboutClicked(e));
                fileMenu.add(aboutMenuItem);
            }
            menuBar.add(fileMenu);

            //======== gameMenu ========
            {
                gameMenu.setText("Game");
                gameMenu.setMnemonic('G');

                //---- openGameFolderMenuItem ----
                openGameFolderMenuItem.setText("Open Game Folder");
                gameMenu.add(openGameFolderMenuItem);

                //---- opemModsFolderMenuItem ----
                opemModsFolderMenuItem.setText("Open Mods Folder");
                gameMenu.add(opemModsFolderMenuItem);

                //---- launchGameMenuItem ----
                launchGameMenuItem.setText("Launch Game");
                launchGameMenuItem.addActionListener(e -> runGameClicked(e));
                gameMenu.add(launchGameMenuItem);
                gameMenu.addSeparator();

                //---- runConfigsMenuItem ----
                runConfigsMenuItem.setText("Custom Run Config(s)");
                gameMenu.add(runConfigsMenuItem);
            }
            menuBar.add(gameMenu);
        }
        add(menuBar, BorderLayout.NORTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel managerPanel;
    private JPanel managerPaneMenu;
    private JLabel gameLabel;
    private JButton addModButton;
    private JButton removeModsButton;
    private JButton installModsButton;
    private JButton toggleButton;
    private JScrollPane listScrollPane;
    private JList installedModsJList;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem applyModsMenuItem;
    private JMenuItem loadNewGameMenuItem;
    private JMenuItem settingsMenuItem;
    private JMenuItem aboutMenuItem;
    private JMenu gameMenu;
    private JMenuItem openGameFolderMenuItem;
    private JMenuItem opemModsFolderMenuItem;
    private JMenuItem launchGameMenuItem;
    private JMenuItem runConfigsMenuItem;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    static class ListRenderer extends JCheckBox implements ListCellRenderer<Mod> {

//        public boolean selected = false;

        @Override
        public Component getListCellRendererComponent(JList<? extends Mod> list, Mod value, int index, boolean isSelected, boolean cellHasFocus) {

            this.setEnabled(value.getState() == Mod.State.ENABLED);
            this.setSelected(value.getState() == Mod.State.ENABLED);
            this.setFont(list.getFont());
            this.setBackground(list.getBackground());
            this.setForeground(list.getForeground());

            if(isSelected) {

                this.setBackground(Color.decode(String.valueOf(FlatIconColors.OBJECTS_GREY.rgb)));
            }
            this.setText(value.getReadableName() + " [" + value.getState().name() + "]");

            return this;
        }
    }
}
