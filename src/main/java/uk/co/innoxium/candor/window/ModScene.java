/*
 * Created by JFormDesigner on Mon Jun 22 16:06:01 BST 2020
 */

package uk.co.innoxium.candor.window;

import com.formdev.flatlaf.FlatIconColors;
import net.miginfocom.swing.MigLayout;
import uk.co.innoxium.candor.Settings;
import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.mod.ModList;
import uk.co.innoxium.candor.mod.store.ModStore;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.module.RunConfig;
import uk.co.innoxium.candor.thread.ThreadModInstaller;
import uk.co.innoxium.candor.util.Dialogs;
import uk.co.innoxium.candor.util.WindowUtils;
import uk.co.innoxium.candor.window.setting.SettingsFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;


public class ModScene extends JPanel {

    public ModScene() {

        try {

            WindowUtils.mainFrame.setMinimumSize(new Dimension(1200, 768));
            determineInstalledMods();
        } catch (IOException e) {

            System.out.println("This shouldn't happen, likely a corrupt mods.json :(");
            e.printStackTrace();
            System.exit(-1);
        }
        initComponents();
        ModStore.MODS.addListener(new ModList.ListChangeListener<Mod>() {

            @Override
            public void handleChange(String identifier, Mod mod, boolean result) {

                list1.setListData(ModStore.MODS.toArray());
            }

            @Override
            public void handleChange(String identifier, Collection<? extends Mod> c, boolean result) {

                list1.setListData(ModStore.MODS.toArray());
            }
        });
    }

    private void determineInstalledMods() throws IOException {

        ModStore.determineInstalledMods();

        // This is an example of how the mods will be stored in data
        /*
         mods: [
            {
                name: "example",
                file: "path/to/file.ext", // The original file will be stored somewhere so we can compare loose files when disabling/removing
                state: "enabled"
            },
            {
                name: "anotherExample",
                file: "another/path/to/file.ext",
                state: "disabled"
            }
         ]
         */
    }

    private void createUIComponents() {


        list1 = new JList(ModStore.MODS.toArray());
        list1.setCellRenderer(new ListRenderer());
        list1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list1.setSelectionModel(new DefaultListSelectionModel() {

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
        list1.addMouseListener(new MouseAdapter() {
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

        JDialog dialog = new SettingsFrame();
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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

            if(list1.getSelectedValuesList().isEmpty()) {
                Dialogs.showInfoDialog(
                        "Candor Mod Manager",
                        "You have not selected any mods to remove.",
                        "ok",
                        "warning",
                        false);
            }
            list1.getSelectedValuesList().forEach(o -> {

                try {

                    ModStore.removeModFile((Mod) o);
                } catch (IOException exception) {

                    exception.printStackTrace();
                }
            });
        }
    }

    private void installModsClicked(ActionEvent e) {
        
        list1.getSelectedValuesList().forEach(o -> {

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

            System.out.println(builder.command().toString());
            Process process = builder.start();
        } catch (IOException ioException) {

            ioException.printStackTrace();
        }
    }

    private void toggleSelectedMods(ActionEvent e) {
    }

    private void newGameClicked(ActionEvent e) {

        Settings.showIntro = true;
//        Settings.gameExe = "";
//        Settings.modsFolder = "";
//        Settings.modExtract = false;
        WindowUtils.setupGameSelectScene();
    }

    private void aboutClicked(ActionEvent e) {

        AboutDialog dialog = new AboutDialog(WindowUtils.mainFrame);
        dialog.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        panel1 = new JPanel();
        panel3 = new JPanel();
        label1 = new JLabel();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        button4 = new JButton();
        scrollPane2 = new JScrollPane();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree();
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        menuItem4 = new JMenuItem();
        menuItem8 = new JMenuItem();
        menu2 = new JMenu();
        menuItem5 = new JMenuItem();
        menuItem6 = new JMenuItem();
        menuItem3 = new JMenuItem();
        menuItem7 = new JMenuItem();

        //======== this ========
        setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "fill,insets panel,hidemode 3",
                // columns
                "[fill]",
                // rows
                "[]" +
                "[]"));

            //======== panel3 ========
            {
                panel3.setLayout(new FlowLayout(FlowLayout.LEFT));

                //---- label1 ----
                label1.setFont(new Font("Segoe UI", Font.BOLD, 16));
                label1.setText(ModuleSelector.currentModule.getReadableGameName().toUpperCase());
                panel3.add(label1);

                //---- button1 ----
                button1.setText("Add Mod(s)");
                button1.setIcon(UIManager.getIcon("Tree.leafIcon"));
                button1.addActionListener(e -> addModClicked(e));
                panel3.add(button1);

                //---- button2 ----
                button2.setText("Remove Selected");
                button2.setIcon(null);
                button2.addActionListener(e -> removeModsSelected(e));
                panel3.add(button2);

                //---- button3 ----
                button3.setText("Install Selected Mod(s)");
                button3.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
                button3.addActionListener(e -> installModsClicked(e));
                panel3.add(button3);

                //---- button4 ----
                button4.setText("Toggle Enabled");
                button4.addActionListener(e -> toggleSelectedMods(e));
                panel3.add(button4);
            }
            panel1.add(panel3, "cell 0 0");

            //======== scrollPane2 ========
            {
                scrollPane2.setViewportView(list1);
            }
            panel1.add(scrollPane2, "cell 0 1,dock center");

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(tree1);
            }
            panel1.add(scrollPane1, "cell 0 1,dock center");
        }
        add(panel1, BorderLayout.CENTER);

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("File");
                menu1.setMnemonic('F');

                //---- menuItem1 ----
                menuItem1.setText("Apply Mod(s)");
                menuItem1.setMnemonic('A');
                menu1.add(menuItem1);

                //---- menuItem2 ----
                menuItem2.setText("Load New Game");
                menuItem2.setMnemonic('L');
                menuItem2.addActionListener(e -> newGameClicked(e));
                menu1.add(menuItem2);

                //---- menuItem4 ----
                menuItem4.setText("Settings");
                menuItem4.setMnemonic('S');
                menuItem4.addActionListener(e -> settingsClicked(e));
                menu1.add(menuItem4);

                //---- menuItem8 ----
                menuItem8.setText("About");
                menuItem8.addActionListener(e -> aboutClicked(e));
                menu1.add(menuItem8);
            }
            menuBar1.add(menu1);

            //======== menu2 ========
            {
                menu2.setText("Game");
                menu2.setMnemonic('G');

                //---- menuItem5 ----
                menuItem5.setText("Open Game Folder");
                menu2.add(menuItem5);

                //---- menuItem6 ----
                menuItem6.setText("Open Mods Folder");
                menu2.add(menuItem6);

                //---- menuItem3 ----
                menuItem3.setText("Launch Game");
                menuItem3.addActionListener(e -> runGameClicked(e));
                menu2.add(menuItem3);
                menu2.addSeparator();

                //---- menuItem7 ----
                menuItem7.setText("Custom Run Config(s)");
                menu2.add(menuItem7);
            }
            menuBar1.add(menu2);
        }
        add(menuBar1, BorderLayout.NORTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JPanel panel3;
    private JLabel label1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JScrollPane scrollPane2;
    private JList list1;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem4;
    private JMenuItem menuItem8;
    private JMenu menu2;
    private JMenuItem menuItem5;
    private JMenuItem menuItem6;
    private JMenuItem menuItem3;
    private JMenuItem menuItem7;
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
