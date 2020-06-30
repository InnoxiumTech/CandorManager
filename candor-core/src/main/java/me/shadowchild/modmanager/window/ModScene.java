/*
 * Created by JFormDesigner on Mon Jun 22 16:06:01 BST 2020
 */

package me.shadowchild.modmanager.window;

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
        menuBar1 = new JMenuBar();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        menuItem3 = new JMenuItem();
        label1 = new JLabel();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        list1 = new JList();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder (
        0, 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder
        . BOTTOM, new java. awt .Font ( "Dia\u006cog", java .awt . Font. BOLD ,12 ) ,java . awt. Color .
        red ) , getBorder () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java .
        beans. PropertyChangeEvent e) { if( "bord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } );
        setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //======== menuBar1 ========
        {

            //---- menuItem1 ----
            menuItem1.setText("Add Mod(s)");
            menuBar1.add(menuItem1);

            //---- menuItem2 ----
            menuItem2.setText("Play");
            menuBar1.add(menuItem2);

            //---- menuItem3 ----
            menuItem3.setText("Exit");
            menuBar1.add(menuItem3);
        }
        add(menuBar1, "cell 0 0");

        //---- label1 ----
        label1.setText("This is the game");
        add(label1, "cell 0 1");

        //---- label2 ----
        label2.setText("text");
        add(label2, "cell 0 1");

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(list1);
        }
        add(scrollPane1, "cell 0 2");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Zach Piddock
    private JMenuBar menuBar1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
    private JLabel label1;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JList list1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
