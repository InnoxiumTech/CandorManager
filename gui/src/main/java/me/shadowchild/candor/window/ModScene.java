/*
 * Created by JFormDesigner on Mon Jun 22 16:06:01 BST 2020
 */

package me.shadowchild.candor.window;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.shadowchild.candor.mod.Mod;
import me.shadowchild.candor.mod.ModsHandler;
import me.shadowchild.candor.module.ModuleSelector;
import me.shadowchild.candor.thread.ThreadModInstaller;
import me.shadowchild.candor.util.Dialogs;
import me.shadowchild.candor.window.setting.SettingsFrame;
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
        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(e.getButton() == MouseEvent.BUTTON1 && ((JList)e.getSource()).getModel().getSize() != 0) {

                    JList list = (JList) e.getSource();
                    int index = list.locationToIndex(e.getPoint());
                    Mod mod = (Mod) list.getModel().getElementAt(index);
                    switch (mod.getState()) {

                        case ENABLED -> mod.setState(Mod.State.DISABLED);
                        case DISABLED -> mod.setState(Mod.State.ENABLED);
                    }
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

            try {

                FileUtils.copyFileToDirectory(file, modStore);
            } catch (IOException exception) {

                System.out.println("Could not copy Mod to the mod store, please retry");
                exception.printStackTrace();
                return;
            }
            File newFile = new File(modStore, file.getName());
            Mod mod = Mod.of(newFile);
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
            new ThreadModInstaller(mod).start();
        });
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
        menuItem3 = new JMenuItem();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border.
        EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax. swing. border. TitledBorder. CENTER, javax. swing
        . border. TitledBorder. BOTTOM, new java .awt .Font ("Dialo\u0067" ,java .awt .Font .BOLD ,12 ),
        java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( )
        { @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("borde\u0072" .equals (e .getPropertyName () ))
        throw new RuntimeException( ); }} );
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
                panel3.add(button2);

                //---- button3 ----
                button3.setText("Install Mod(s)");
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

                //---- menuItem1 ----
                menuItem1.setText("Apply Mod(s)");
                menu1.add(menuItem1);

                //---- menuItem2 ----
                menuItem2.setText("Load New Game");
                menu1.add(menuItem2);

                //---- menuItem4 ----
                menuItem4.setText("Settings");
                menuItem4.addActionListener(e -> settingsClicked(e));
                menu1.add(menuItem4);
            }
            menuBar1.add(menu1);

            //======== menu2 ========
            {
                menu2.setText("Game");

                //---- menuItem3 ----
                menuItem3.setText("Launch Game");
                menu2.add(menuItem3);
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
    private JMenuItem menuItem3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    class ListRenderer extends JCheckBox implements ListCellRenderer<Mod> {

        @Override
        public Component getListCellRendererComponent(JList<? extends Mod> list, Mod value, int index, boolean isSelected, boolean cellHasFocus) {

            this.setEnabled(value.getState() == Mod.State.ENABLED);
            switch(value.getState()) {

                case ENABLED -> this.setSelected(true);
                case DISABLED -> this.setSelected(false);
            }
            this.setFont(list.getFont());
            this.setBackground(list.getBackground());
            this.setForeground(list.getForeground());
            this.setText(value.getReadableName());

            return this;
        }
    }
}
