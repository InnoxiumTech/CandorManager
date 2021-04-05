/*
 * Created by JFormDesigner on Thu Apr 01 20:59:27 BST 2021
 */

package uk.co.innoxium.candor.window;

import com.formdev.flatlaf.FlatIconColors;
import com.google.common.collect.Lists;
import net.miginfocom.swing.MigLayout;
import uk.co.innoxium.candor.game.GamesList;
import uk.co.innoxium.candor.module.RunConfig;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.candor.util.Utils;
import uk.co.innoxium.candor.window.dialog.SwingDialogs;
import uk.co.innoxium.candor.window.modscene.ModScene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;


/**
 * Flow of the manage dialog is:
 * make copy of runConfigs list
 * if right button clicked, move to removed list - if able
 * if left button click, move to keep list - if able
 * on ok clicked - save the allowed list to the games run configs
 * @author Zach Piddock
 */
public class ManageRunConfigs extends JDialog {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel currentConfigsLabel;
    private JLabel removeConfigsLabel;
    private JScrollPane currentScrollPane;
    private JList<RunConfig> currentList;
    private JPanel transferButtonPanel;
    private JButton moveLeftButton;
    private JButton moveRightButton;
    private JScrollPane removeScrollPane;
    private JList<RunConfig> removeList;
    private JPanel buttonBar;
    private JButton saveButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private final ModScene modScene;
    private ArrayList<RunConfig> currentRunConfigsCopy = deepCopy(GamesList.getCurrentGame().customLaunchConfigs);
    private ArrayList<RunConfig> removedRunConfigs = new ArrayList<>();

    private ArrayList<RunConfig> deepCopy(ArrayList<RunConfig> customLaunchConfigs) {

        return Lists.newArrayList(customLaunchConfigs);
    }

    private void moveLeftClicked(ActionEvent e) {

        RunConfig conf = removeList.getSelectedValue();
        if(conf != null) {

            currentRunConfigsCopy.add(conf);
            removedRunConfigs.remove(conf);

            currentList.setListData(Utils.getVectorArrayFromList(currentRunConfigsCopy));
            removeList.setListData(Utils.getVectorArrayFromList(removedRunConfigs));
        }
    }

    private void moveRightClicked(ActionEvent e) {

        RunConfig conf = currentList.getSelectedValue();
        if(conf != null) {

            removedRunConfigs.add(conf);
            currentRunConfigsCopy.remove(conf);

            currentList.setListData(Utils.getVectorArrayFromList(currentRunConfigsCopy));
            removeList.setListData(Utils.getVectorArrayFromList(removedRunConfigs));
        }
    }

    private void createUIComponents() {

        currentList = new JList<>(Utils.getVectorArrayFromList(currentRunConfigsCopy));
        currentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        currentList.setCellRenderer(new ListRenderer());
        removeList = new JList<>(Utils.getVectorArrayFromList(removedRunConfigs));
        removeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        removeList.setCellRenderer(new ListRenderer());
    }

    private void cancelClicked(ActionEvent e) {

        int ret = SwingDialogs.showConfirmDialog("Cancel", "Are you sure you wish to cancel editing configs?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        if(ret == JOptionPane.YES_OPTION) {

            this.dispose();
        }
        // Else just carry on.
    }

    private void saveClicked(ActionEvent e) {

        int ret = SwingDialogs.showConfirmDialog("Confirm Changes", "Are you sure you wish to confirm?\nRun Configurations that you remove will be list forever.", JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        if(ret == JOptionPane.OK_OPTION) {

            // Save the list to the game.
            GamesList.getCurrentGame().customLaunchConfigs = Lists.newArrayList(currentRunConfigsCopy);
            this.modScene.refreshRunConfigs();
        }
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        dialogPane = new JPanel();
        contentPanel = new JPanel();
        currentConfigsLabel = new JLabel();
        removeConfigsLabel = new JLabel();
        currentScrollPane = new JScrollPane();
        transferButtonPanel = new JPanel();
        moveLeftButton = new JButton();
        moveRightButton = new JButton();
        removeScrollPane = new JScrollPane();
        buttonBar = new JPanel();
        saveButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("Manage Run Configs");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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

                    //---- moveLeftButton ----
                    moveLeftButton.setText("<");
                    moveLeftButton.addActionListener(e -> moveLeftClicked(e));
                    transferButtonPanel.add(moveLeftButton, "cell 0 1");

                    //---- moveRightButton ----
                    moveRightButton.setText(">");
                    moveRightButton.addActionListener(e -> moveRightClicked(e));
                    transferButtonPanel.add(moveRightButton, "cell 0 0");
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
                saveButton.addActionListener(e -> saveClicked(e));
                buttonBar.add(saveButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(e -> cancelClicked(e));
                buttonBar.add(cancelButton, "cell 1 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public ManageRunConfigs(ModScene modScene, Window owner) {

        super(owner);
        this.modScene = modScene;
        setModalityType(ModalityType.APPLICATION_MODAL);
        setIconImage(Resources.CANDOR_LOGO.getImage());
        initComponents();
    }

    public static class ListRenderer extends JLabel implements ListCellRenderer<RunConfig> {

        @Override
        public Component getListCellRendererComponent(JList<? extends RunConfig> list, RunConfig value, int index, boolean isSelected, boolean cellHasFocus) {

            this.setText(value.getRunConfigName());
            this.setOpaque(true);
            this.setForeground(list.getForeground());
            this.setBackground(list.getBackground());

            if(isSelected)
                this.setBackground(Color.decode(String.valueOf(FlatIconColors.OBJECTS_GREY.rgb)));

            return this;
        }
    }
}
