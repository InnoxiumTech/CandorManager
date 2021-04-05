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

    /**
     * Returns: an integer indicating the option selected by the user
     */
    public static int showConfirmDialog(String title, String message,
                                            @MagicConstant(intValues = {JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE, JOptionPane.QUESTION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, JOptionPane.WARNING_MESSAGE})
                                                    int messageType,
                                            @MagicConstant(intValues = {JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.OK_CANCEL_OPTION})
                                                    int dialogType) {

        return JOptionPane.showConfirmDialog(WindowUtils.mainFrame, message, title, dialogType, messageType);
    }
}
