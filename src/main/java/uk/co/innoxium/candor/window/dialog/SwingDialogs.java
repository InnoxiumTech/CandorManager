package uk.co.innoxium.candor.window.dialog;

import org.intellij.lang.annotations.MagicConstant;
import uk.co.innoxium.candor.util.WindowUtils;

import javax.swing.*;


public class SwingDialogs {

    public static void showInfoMessage(String title, String message,
                                       @MagicConstant(intValues = {JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE, JOptionPane.QUESTION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, JOptionPane.WARNING_MESSAGE})
                                               int messageType) {

        JOptionPane.showMessageDialog(WindowUtils.mainFrame, message, title, messageType);
    }
}
