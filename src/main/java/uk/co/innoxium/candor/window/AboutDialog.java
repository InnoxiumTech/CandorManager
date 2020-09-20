/*
 * Created by JFormDesigner on Wed Aug 26 21:08:39 BST 2020
 */

package uk.co.innoxium.candor.window;

import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.candor.window.component.JImage;
import uk.co.innoxium.swing.component.JHyperlink;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Zach Piddock
 */
public class AboutDialog extends JDialog {

    public AboutDialog(Window owner) {
        super(owner);
        initComponents();
    }

    private void createUIComponents() {

        Font linkFont = Resources.fantasque.deriveFont(24f);

        candorLogo = new JImage(Resources.CANDOR_LOGO);
        innoxiumLogo = new JImage(Resources.INNOXIUM_LOGO);

        discordLink = new JHyperlink("Discord", "discord.gg/CMG9ZtS");
        discordLink.setFont(linkFont);
        githubLink = new JHyperlink("Github", "github.com/InnoxiumTech/CandorManager");
        githubLink.setFont(linkFont);
        websiteLink = new JHyperlink("Website", "innoxium.co.uk");
        websiteLink.setFont(linkFont);
        twitterLink = new JHyperlink("Twitter", "twitter.com/InnoxiumTech");
        twitterLink.setFont(linkFont);
        itchLink = new JHyperlink("Itch.io", "innoxium.itch.io/candor-mod-manager");
        itchLink.setFont(linkFont);

//        editorPane1 = new JTextPane();
//        try {
//
//            editorPane1.setContentType("text/html");
//            editorPane1.setPage(AboutDialog.class.getResource("/html/about.html"));
//        } catch (IOException e) {
//
//            e.printStackTrace();
//            editorPane1.setContentType("text/html");
//            editorPane1.setText("<html>Page not found.</html>");
//        }
    }

    private void okClicked(ActionEvent e) {

        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        dialogPane = new JPanel();
        contentPanel = new JPanel();
        panel1 = new JPanel();
        panel2 = new JPanel();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setTitle("About Candor!");
        setModal(true);
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new BorderLayout());

                //======== panel1 ========
                {
                    panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));

                    //---- discordLink ----
                    discordLink.setText("Discord");
                    panel1.add(discordLink);

                    //---- githubLink ----
                    githubLink.setText("Github");
                    panel1.add(githubLink);

                    //---- websiteLink ----
                    websiteLink.setText("Website");
                    panel1.add(websiteLink);

                    //---- twitterLink ----
                    twitterLink.setText("Twitter");
                    panel1.add(twitterLink);

                    //---- itchLink ----
                    itchLink.setText("Itch.io");
                    panel1.add(itchLink);
                }
                contentPanel.add(panel1, BorderLayout.SOUTH);

                //======== panel2 ========
                {
                    panel2.setLayout(new FlowLayout());
                    panel2.add(candorLogo);
                    panel2.add(innoxiumLogo);
                }
                contentPanel.add(panel2, BorderLayout.NORTH);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> okClicked(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.PAGE_END);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel panel1;
    private JLabel discordLink;
    private JLabel githubLink;
    private JLabel websiteLink;
    private JLabel twitterLink;
    private JLabel itchLink;
    private JPanel panel2;
    private JLabel candorLogo;
    private JLabel innoxiumLogo;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
