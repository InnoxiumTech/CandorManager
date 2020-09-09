/*
 * Created by JFormDesigner on Wed Sep 09 19:33:10 BST 2020
 */

package uk.co.innoxium.candor.window;

import net.miginfocom.swing.MigLayout;
import uk.co.innoxium.candor.game.Game;
import uk.co.innoxium.candor.game.GamesList;
import uk.co.innoxium.candor.util.Utils;
import uk.co.innoxium.cybernize.util.ClassLoadUtil;

import javax.swing.*;

/**
 * @author Zach Piddock
 */
public class EntryScene extends JPanel {
    public EntryScene() {
        initComponents();
    }

    private void createUIComponents() {

        candoLogo = new JLabel(new ImageIcon(ClassLoadUtil.getCL().getResource("logo.png")));
        comboBox1 = new JComboBox<Game>(Utils.getVectorArrayFromList(GamesList.getGamesList()));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        button1 = new JButton();
        button2 = new JButton();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]"));
        add(candoLogo, "cell 0 0,dock center");
        add(comboBox1, "cell 0 1");

        //---- button1 ----
        button1.setText("Load New Game");
        add(button1, "cell 0 2,alignx center,growx 0");

        //---- button2 ----
        button2.setText("Load Selected Game");
        add(button2, "cell 0 2,alignx center,growx 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel candoLogo;
    private JComboBox comboBox1;
    private JButton button1;
    private JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
