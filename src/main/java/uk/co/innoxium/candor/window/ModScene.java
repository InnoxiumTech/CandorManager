/*
 * Created by JFormDesigner on Mon Jun 22 16:06:01 BST 2020
 */

package uk.co.innoxium.candor.window;

import com.formdev.flatlaf.FlatIconColors;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import uk.co.innoxium.candor.util.Dialogs;
import uk.co.innoxium.candor.module.RunConfig;
import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.mod.ModUtils;
import uk.co.innoxium.candor.mod.ModsHandler;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.thread.ThreadModInstaller;
import uk.co.innoxium.candor.window.setting.SettingsFrame;
import me.shadowchild.cybernize.util.JsonUtil;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class ModScene extends JPanel {

    File installedModsConfig = new File("config/" + ModuleSelector.currentModule.getExeName() + "/mods.json");

    public ModScene() {

        try {

            determineInstalledMods();
        } catch (IOException e) {

            System.out.println("This shouldn't happen, likely a corrupt mods.json :(");
            e.printStackTrace();
            System.exit(-1);
        }
        initComponents();
        ModsHandler.MODS.addListener(new ModsHandler.ListChangeListener<Mod>() {

            @Override
            public void handleChange(String identifier, Mod mod, boolean result) {

                list1.setListData(ModsHandler.MODS.toArray());
            }

            @Override
            public void handleChange(String identifier, Collection<? extends Mod> c, boolean result) {

                list1.setListData(ModsHandler.MODS.toArray());
            }
        });
    }

    private void determineInstalledMods() throws IOException {

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
        if(!installedModsConfig.getParentFile().exists()) installedModsConfig.getParentFile().mkdirs();
        if(!installedModsConfig.exists()) {

            installedModsConfig.createNewFile();
            Gson gson = new Gson();
            JsonObject obj = new JsonObject();
            obj.add("mods", new JsonArray());
            FileWriter writer = new FileWriter(installedModsConfig);
            gson.toJson(obj, writer);

            writer.close();
        }

        JsonObject contents = JsonUtil.getObjectFromUrl(installedModsConfig.toURI().toURL());
        if(contents.has("mods")) {

            JsonArray array = JsonUtil.getArray(contents, "mods");
            array.forEach(element -> {

                Mod mod = Mod.of((JsonObject)element);
                // TODO: check if mods is already in the list
                ModsHandler.MODS.add(mod);
            });
        }
    }

    private void createUIComponents() {


        list1 = new JList(ModsHandler.MODS.toArray());
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

        File modStore = new File("config/" + ModuleSelector.currentModule.getExeName() + "/mods");
        if(!modStore.exists()) modStore.mkdirs();

        Dialogs.openMultiFileDialog(ModuleSelector.currentModule.getModFileFilterList()).forEach(file -> {

            if(!ModUtils.checkAlreadyInstalled(file)) {

                try {

                    FileUtils.copyFileToDirectory(file, modStore);
                } catch (IOException exception) {

                    System.out.println("Could not copy Mod to the mod store, please retry");
                    exception.printStackTrace();
                    return;
                }
                File newFile = new File(modStore, file.getName());
                Mod mod = Mod.of(newFile);
                mod.setReadableName(JOptionPane.showInputDialog("Please Enter a name for this mod: ", mod.getName()));
                try {

                    JsonObject contents = JsonUtil.getObjectFromUrl(installedModsConfig.toURI().toURL());
                    contents.get("mods").getAsJsonArray().add(Mod.from(mod));

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    FileWriter writer = new FileWriter(installedModsConfig);
                    gson.toJson(contents, writer);

                    writer.close();
                } catch (IOException exception) {

                    exception.printStackTrace();
                }
                ModsHandler.MODS.add(mod);
//                new ThreadModInstaller(mod).start();
            }
        });
    }

    private void removeModsSelected(ActionEvent e) {

        ArrayList<Mod> removedMods = Lists.newArrayList();

        list1.getSelectedValuesList().forEach(o -> {
            
            File modsFolder = ModuleSelector.currentModule.getModsFolder();
            File modStore = new File("/config" + ModuleSelector.currentModule.getExeName() + "/mods");
            Mod mod = (Mod)o;

            // We let the module decide how to delete the files
            if(ModuleSelector.currentModule.getModInstaller().uninstall(mod)) {

                // Once the module has deleted the files, remove the mod from the mods config
                mod.getAssociatedFiles().forEach(element -> {

                    try {

                        JsonObject contents = JsonUtil.getObjectFromUrl(installedModsConfig.toURI().toURL());
                        JsonArray array = contents.get("mods").getAsJsonArray();
                        JsonArray newArray = array.deepCopy();

                        array.forEach(object -> {

                            JsonObject obj = (JsonObject)object;
                            if(mod.getName().equals(obj.get("name").getAsString())) {

                                newArray.remove(obj);
                            }
                        });

                        contents.remove("mods");
                        contents.add("mods", newArray);

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        FileWriter writer = new FileWriter(installedModsConfig);
                        gson.toJson(contents, writer);

                        writer.close();

                        FileUtils.deleteQuietly(mod.getFile());
                    } catch (IOException exception) {

                        exception.printStackTrace();
                    }
                });
                removedMods.add(mod);
            }
        });

        removedMods.forEach(ModsHandler.MODS::remove);
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
    
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Zach Piddock
        createUIComponents();

        panel1 = new JPanel();
        panel3 = new JPanel();
        label1 = new JLabel();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        scrollPane2 = new JScrollPane();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree();
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        menuItem4 = new JMenuItem();
        menu2 = new JMenu();
        menuItem5 = new JMenuItem();
        menuItem6 = new JMenuItem();
        menuItem3 = new JMenuItem();
        menuItem7 = new JMenuItem();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder
        ( 0, 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border
        .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog", java .awt . Font. BOLD ,12 ) ,java . awt
        . Color .red ) , getBorder () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void
        propertyChange (java . beans. PropertyChangeEvent e) { if( "bord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException( )
        ;} } );
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
                menu1.add(menuItem2);

                //---- menuItem4 ----
                menuItem4.setText("Settings");
                menuItem4.setMnemonic('S');
                menuItem4.addActionListener(e -> settingsClicked(e));
                menu1.add(menuItem4);
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
    // Generated using JFormDesigner Evaluation license - Zach Piddock
    private JPanel panel1;
    private JPanel panel3;
    private JLabel label1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JScrollPane scrollPane2;
    private JList list1;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem4;
    private JMenu menu2;
    private JMenuItem menuItem5;
    private JMenuItem menuItem6;
    private JMenuItem menuItem3;
    private JMenuItem menuItem7;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    class ListRenderer extends JCheckBox implements ListCellRenderer<Mod> {

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
