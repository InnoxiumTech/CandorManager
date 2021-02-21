package uk.co.innoxium.candor.window.dnd;

import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.util.NativeDialogs;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameSelectTransferHandler extends TransferHandler {

    JTextField modFolderField;
    JButton modFolderBrowse;
    JCheckBox extractCheckbox;

    public GameSelectTransferHandler(JTextField modFolderField, JButton modFolderBrowse, JCheckBox extractCheckbox) {

        this.modFolderField = modFolderField;
        this.modFolderBrowse = modFolderBrowse;
        this.extractCheckbox = extractCheckbox;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        for (DataFlavor flavor : support.getDataFlavors()) {
            if (flavor.isFlavorJavaFileListType()) {
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean importData(TransferHandler.TransferSupport support) {
        if (!this.canImport(support))
            return false;

        List<File> files;
        try {
            files = (List<File>) support.getTransferable()
                    .getTransferData(DataFlavor.javaFileListFlavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            // should never happen (or JDK is buggy)
            return false;
        }

        if (files.size() > 1) {

            NativeDialogs.showErrorMessage("Only drag one file to this location.");
        } else {

            // We only have one
            File file = files.get(0);

            JTextField field = (JTextField) support.getComponent();

            try {

                field.setText(file.getCanonicalPath());
                AbstractModule module = ModuleSelector.getModuleForGame(file.getName(), true);
                if (module.requiresModFolderSelection()) {

                    modFolderField.setEnabled(true);
                    modFolderBrowse.setEnabled(true);
                } else {

                    modFolderField.setText("Mods Folder Not Needed By Module.");
                }
                if (module.getEnableExtractOption()) {

                    extractCheckbox.setEnabled(true);
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return true;
    }
}
