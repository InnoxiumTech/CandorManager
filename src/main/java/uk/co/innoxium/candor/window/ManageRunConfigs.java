/*
 * Created by JFormDesigner on Thu Apr 01 20:59:27 BST 2021
 */

package uk.co.innoxium.candor.window;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Zach Piddock
 */
public class ManageRunConfigs extends JDialog {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel currentConfigsLabel;
    private JLabel removeConfigsLabel;
    private JScrollPane currentScrollPane;
    private JList currentList;
    private JPanel transferButtonPanel;
    private JButton button1;
    private JButton button2;
    private JScrollPane removeScrollPane;
    private JList removeList;
    private JPanel buttonBar;
    private JButton saveButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        currentConfigsLabel = new JLabel();
        removeConfigsLabel = new JLabel();
        currentScrollPane = new JScrollPane();
        currentList = new JList();
        transferButtonPanel = new JPanel();
        button1 = new JButton();
        button2 = new JButton();
        removeScrollPane = new JScrollPane();
        removeList = new JList();
        buttonBar = new JPanel();
        saveButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "insets dialog,hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]"));

                //---- currentConfigsLabel ----
                currentConfigsLabel.setText("Configured Run Configs");
                contentPanel.add(currentConfigsLabel, "cell 0 0");

                //---- removeConfigsLabel ----
                removeConfigsLabel.setText("Removed Run Configs");
                contentPanel.add(removeConfigsLabel, "cell 2 0");

                //======== currentScrollPane ========
                {
                    currentScrollPane.setViewportView(currentList);
                }
                contentPanel.add(currentScrollPane, "cell 0 1");

                //======== transferButtonPanel ========
                {
                    transferButtonPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]",
                        // rows
                        "[]" +
                        "[]"));

                    //---- button1 ----
                    button1.setText("<");
                    transferButtonPanel.add(button1, "cell 0 0");

                    //---- button2 ----
                    button2.setText(">");
                    transferButtonPanel.add(button2, "cell 0 1");
                }
                contentPanel.add(transferButtonPanel, "cell 1 1");

                //======== removeScrollPane ========
                {
                    removeScrollPane.setViewportView(removeList);
                }
                contentPanel.add(removeScrollPane, "cell 2 1");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]" +
                    "[button,fill]",
                    // rows
                    null));

                //---- saveButton ----
                saveButton.setText("Save");
                buttonBar.add(saveButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                buttonBar.add(cancelButton, "cell 1 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public ManageRunConfigs(Window owner) {
        super(owner);
        initComponents();
    }
}
