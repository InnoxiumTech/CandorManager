/*
 * Created by JFormDesigner on Mon Oct 12 19:01:26 BST 2020
 */

package uk.co.innoxium.candor.window.install;

import javax.swing.*;
import java.awt.*;

/**
 * @author Zach Piddock
 */
public class ModInstallDialog extends JDialog {

    public ModInstallDialog(Window owner) {

        super(owner);
        initComponents();
    }

    private void createUIComponents() {

    }

    private void initComponents() {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        panel1 = new JPanel();
        hideButton = new JButton();
        panel2 = new JPanel();

        //======== this ========
        setTitle("Mod Install Progress");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new BorderLayout(5, 5));

            //---- installProgress ----
            installProgress.setStringPainted(true);
            panel1.add(installProgress, BorderLayout.CENTER);

            //---- hideButton ----
            hideButton.setText("Hide");
            panel1.add(hideButton, BorderLayout.SOUTH);

            //======== panel2 ========
            {
                panel2.setLayout(new BorderLayout());

                //---- modLabel ----
                modLabel.setText("Installing Mod: $Mod");
                modLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel2.add(modLabel, BorderLayout.NORTH);
                panel2.add(modStateLabel, BorderLayout.SOUTH);
            }
            panel1.add(panel2, BorderLayout.NORTH);
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JProgressBar installProgress;
    private JButton hideButton;
    private JPanel panel2;
    private JLabel modLabel;
    private JLabel modStateLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
