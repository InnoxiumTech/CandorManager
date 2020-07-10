/*
 * Created by JFormDesigner on Mon Jun 22 16:06:01 BST 2020
 */

package me.shadowchild.modmanager.window;

import java.awt.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * @author unknown
 */
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
        scrollPane1 = new JScrollPane();
        scrollPane2 = new JScrollPane();
        tree1 = new JTree();
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        menu2 = new JMenu();
        menuItem3 = new JMenuItem();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder( 0
        , 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM
        , new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ), java. awt. Color. red) ,
         getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
        ) {if ("bord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
        setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "fill,insets panel,hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[]"));

            //======== scrollPane1 ========
            {
                scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            }
            panel1.add(scrollPane1, "cell 0 0,dock center");

            //======== scrollPane2 ========
            {
                scrollPane2.setViewportView(tree1);
            }
            panel1.add(scrollPane2, "cell 1 0,dock center");
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
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private JTree tree1;
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenu menu2;
    private JMenuItem menuItem3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
