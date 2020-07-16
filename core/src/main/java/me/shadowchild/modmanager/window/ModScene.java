/*
 * Created by JFormDesigner on Mon Jun 22 16:06:01 BST 2020
 */

package me.shadowchild.modmanager.window;

import me.shadowchild.modmanager.module.ModuleSelector;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;


public class ModScene extends JPanel {

    public ModScene() {

        initComponents();
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Zach Piddock
        panel1 = new JPanel();
        panel3 = new JPanel();
        label1 = new JLabel();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        scrollPane2 = new JScrollPane();
        list1 = new JList();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree();
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        menu2 = new JMenu();
        menuItem3 = new JMenuItem();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder( 0
        , 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM
        , new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt. Color. red) ,
         getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
        ) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
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
                label1.setText(ModuleSelector.currentModule.getReadableGameName());
                panel3.add(label1);

                //---- button1 ----
                button1.setText("Add Mod(s)");
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
    private JMenu menu2;
    private JMenuItem menuItem3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
