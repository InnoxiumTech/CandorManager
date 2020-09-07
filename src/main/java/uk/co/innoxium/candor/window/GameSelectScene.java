package uk.co.innoxium.candor.window;

import uk.co.innoxium.candor.mod.store.ModStore;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.util.Dialogs;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.candor.Settings;
import uk.co.innoxium.candor.module.ModuleSelector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class GameSelectScene extends JFrame {

    private void gameExeClicked(ActionEvent e) {

        File gameExe = Dialogs.showSingleFileDialog("exe");
        try {

            gameField.setText(gameExe.getCanonicalPath());
            AbstractModule module = ModuleSelector.getModuleForGame(gameExe);
            if(module.requiresModFolderSelection()) {

                modFolderField.setEnabled(true);
                modFolderBrowse.setEnabled(true);
            }
            if(module.getEnableExtractOption()) {

                checkBox2.setEnabled(true);
            }
        } catch (IOException exception) {
            
            exception.printStackTrace();
        }
    }

    private void modsFolderClicked(ActionEvent e) {

        File modFolder = Dialogs.openPickFolder();
        try {

            modFolderField.setText(modFolder.getCanonicalPath());
        } catch (IOException exception) {

            exception.printStackTrace();
        }
    }

    private void cancelButtonClicked(ActionEvent e) {

        boolean result = Dialogs.showInfoDialog(
                "Candor Mod Manager",
                "Are you sure you wish to exit?",
                "yesno",
                "question",
                false);
        if(result) System.exit(0);
    }

    private void onButtonClicked(ActionEvent e) {
        
        boolean gameEmpty = gameField.getText().isEmpty();
        boolean folderEmpty = modFolderField.getText().isEmpty();
        boolean folderCondition = modFolderField.isEnabled() && (folderEmpty);

        if(gameEmpty || folderCondition) {

            StringBuilder builder = new StringBuilder();
            builder.append("You have not specified a:\n");
            if(gameEmpty) builder.append("-> Game Executable\n");
            if (modFolderField.isEnabled() && folderEmpty) builder.append("-> Mods Folder\n");
            builder.append("Please fill out the fields!");

            Dialogs.showInfoDialog(
                    "Candor Mod Manager",
                    builder.toString(),
                    "ok",
                    "info",
                    true);
        } else {

            // TODO: Break window loading into utility class

            Settings.gameExe = gameField.getText();
            Settings.modsFolder = modFolderField.getText();

            AbstractModule module = ModuleSelector.currentModule;
            module.setGame(new File(gameField.getText()));
            module.setModsFolder(new File(modFolderField.getText()));
            ModStore.initialise();
            this.setVisible(false);
            this.setResizable(true);
            ModScene modScene = new ModScene();
            Resources.currentScene = modScene;
            this.setContentPane(modScene);
            this.setMinimumSize(new Dimension(1200, 768));
            // TODO: Allow the window to stay on the same screen it was used on
            this.setLocationRelativeTo(null);
            this.pack();
            this.setVisible(true);
        }
    }

    private void checkBox(ActionEvent e) {

        Settings.showIntro = !Settings.showIntro;
    }

    private void extractorClicked(ActionEvent e) {

        Settings.modExtract = !Settings.modExtract;
    }

    private void thisWindowClosing(WindowEvent e) {

        boolean result = Dialogs.showInfoDialog(
                "Candor Mod Manager",
                "Are you sure you wish to exit?",
                "yesno",
                "question",
                false);
        if(result) System.exit(0);
    }

    public void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        gameLabel = new JLabel();
        gameField = new JTextField();
        gameBrowse = new JButton();
        modFolderLabel = new JLabel();
        modFolderField = new JTextField();
        modFolderBrowse = new JButton();
        buttonBar = new JPanel();
        checkBox1 = new JCheckBox();
        checkBox2 = new JCheckBox();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationByPlatform(true);
        setTitle("Candor Mod Manager");
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getResource("/logo.png")).getImage());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 39, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0, 0.0, 1.0E-4};

                //---- gameLabel ----
                gameLabel.setText("Please Select the Game Executable.");
                contentPanel.add(gameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
                contentPanel.add(gameField, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- gameBrowse ----
                gameBrowse.setText("...");
                gameBrowse.addActionListener(e -> gameExeClicked(e));
                contentPanel.add(gameBrowse, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- modFolderLabel ----
                modFolderLabel.setText("Please Locate the mods folder for this game.");
                contentPanel.add(modFolderLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- modFolderField ----
                modFolderField.setEnabled(false);
                contentPanel.add(modFolderField, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- modFolderBrowse ----
                modFolderBrowse.setText("...");
                modFolderBrowse.setEnabled(false);
                modFolderBrowse.addActionListener(e -> modsFolderClicked(e));
                contentPanel.add(modFolderBrowse, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));

                //---- checkBox1 ----
                checkBox1.setText("Skip Intro?");
                checkBox1.addActionListener(e -> checkBox(e));
                buttonBar.add(checkBox1);

                //---- checkBox2 ----
                checkBox2.setText("Mod Requires Extracting?");
                checkBox2.setEnabled(false);
                checkBox2.addActionListener(e -> extractorClicked(e));
                buttonBar.add(checkBox2);

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> onButtonClicked(e));
                buttonBar.add(okButton);

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(e -> cancelButtonClicked(e));
                buttonBar.add(cancelButton);
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(435, 300);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel gameLabel;
    private JTextField gameField;
    private JButton gameBrowse;
    private JLabel modFolderLabel;
    private JTextField modFolderField;
    private JButton modFolderBrowse;
    private JPanel buttonBar;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
