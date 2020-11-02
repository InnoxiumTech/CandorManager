package uk.co.innoxium.candor.window.tool;

import net.miginfocom.swing.MigLayout;
import uk.co.innoxium.candor.module.RunConfig;
import uk.co.innoxium.candor.tool.Tool;
import uk.co.innoxium.candor.tool.ToolsList;
import uk.co.innoxium.candor.util.NativeDialogs;
import uk.co.innoxium.candor.util.WindowUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class ToolAddWindow extends JDialog {

    public ToolAddWindow() {

        this.setResizable(false);
        initComponents();
        this.pack();
        this.setLocationRelativeTo(WindowUtils.mainFrame);
    }

    private void toolSearchClicked(ActionEvent e) {

        File toolExe = NativeDialogs.showSingleFileDialog(null);
        if(toolExe != null) {

            try {

                toolExeField.setText(toolExe.getCanonicalPath());
            } catch (IOException ioException) {

                ioException.printStackTrace();
            }
        }
    }

    private void workingDirClicked(ActionEvent e) {

        File workingDir = NativeDialogs.openPickFolder();
        if(workingDir != null) {

            try {

                workingDirField.setText(workingDir.getCanonicalPath());
            } catch (IOException ioException) {

                ioException.printStackTrace();
            }
        }
    }

    private void confirmClicked(ActionEvent e) {

        boolean nameEmpty = nameField.getText().isEmpty();
        boolean toolEmpty = toolExeField.getText().isEmpty();

        if(nameEmpty || toolEmpty) {

            StringBuilder builder = new StringBuilder();
            builder.append("You have not specified a:\n");
            if(toolEmpty) builder.append("-> Tool Executable\n");
            if(nameEmpty) builder.append("-> Tool Name\n");
            builder.append("Please fill out the fields!");

            NativeDialogs.showInfoDialog(
                    "Candor Mod Manager",
                    builder.toString(),
                    "ok",
                    "info",
                    true);
        } else {

            Tool tool = new Tool();
            tool.name = nameField.getText();
            RunConfig runConfig = new RunConfig(tool.name);
            runConfig.startCommand = toolExeField.getText();
            runConfig.programArgs = argsField.getText();
            runConfig.workingDir = workingDirField.getText();
            tool.runConfig = runConfig;
            ToolsList.addTool(tool);
            this.dispose();
        }
    }

    private void cancelClicked(ActionEvent e) {

        boolean result = NativeDialogs.showInfoDialog(
                "Candor Mod Manager",
                "Are you sure you wish to cancel adding a tool?",
                "yesno",
                "question",
                false);
        if(result) this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        toolExeLabel = new JLabel();
        toolExeField = new JTextField();
        toolFieldSearch = new JButton();
        argsLabel = new JLabel();
        argsField = new JTextField();
        workingDirLabel = new JLabel();
        workingDirField = new JTextField();
        workingDirSearch = new JButton();
        nameLabel = new JLabel();
        nameField = new JTextField();
        confirmButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("Add Tool!");
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- toolExeLabel ----
        toolExeLabel.setText("Please Select The Tool Executable");
        contentPane.add(toolExeLabel, "cell 0 0");
        contentPane.add(toolExeField, "cell 0 1");

        //---- toolFieldSearch ----
        toolFieldSearch.setText("...");
        toolFieldSearch.addActionListener(e -> toolSearchClicked(e));
        contentPane.add(toolFieldSearch, "cell 1 1");

        //---- argsLabel ----
        argsLabel.setText("Optional Program Arguments");
        contentPane.add(argsLabel, "cell 0 2");
        contentPane.add(argsField, "cell 0 3 2 1");

        //---- workingDirLabel ----
        workingDirLabel.setText("Working Directory (defaults to executable location)");
        contentPane.add(workingDirLabel, "cell 0 4");
        contentPane.add(workingDirField, "cell 0 5");

        //---- workingDirSearch ----
        workingDirSearch.setText("...");
        workingDirSearch.addActionListener(e -> workingDirClicked(e));
        contentPane.add(workingDirSearch, "cell 1 5");

        //---- nameLabel ----
        nameLabel.setText("Please Enter A Name For This Tool");
        contentPane.add(nameLabel, "cell 0 6");
        contentPane.add(nameField, "cell 0 7 2 1");

        //---- confirmButton ----
        confirmButton.setText("Confirm!");
        confirmButton.addActionListener(e -> confirmClicked(e));
        contentPane.add(confirmButton, "cell 0 8 2 1");

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(e -> cancelClicked(e));
        contentPane.add(cancelButton, "cell 0 8 2 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel toolExeLabel;
    private JTextField toolExeField;
    private JButton toolFieldSearch;
    private JLabel argsLabel;
    private JTextField argsField;
    private JLabel workingDirLabel;
    private JTextField workingDirField;
    private JButton workingDirSearch;
    private JLabel nameLabel;
    private JTextField nameField;
    private JButton confirmButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
