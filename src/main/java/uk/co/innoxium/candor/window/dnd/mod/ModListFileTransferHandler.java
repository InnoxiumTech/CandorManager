package uk.co.innoxium.candor.window.dnd.mod;

import uk.co.innoxium.candor.mod.store.ModStore;
import uk.co.innoxium.candor.util.NativeDialogs;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class ModListFileTransferHandler extends TransferHandler {

    @Override
    public boolean importData(TransferSupport support) {

        if(!canImport(support))
            return false;

        List<File> files;

        try {

            files = (List<File>) support.getTransferable()
                    .getTransferData(DataFlavor.javaFileListFlavor);
        } catch(IOException | UnsupportedFlavorException e) {

            // This should never happen as we already checked
            e.printStackTrace();
            return false;
        }

        files.forEach(file ->  {

            ModStore.Result result = ModStore.addModFile(file);

            switch(result) {

                case DUPLICATE -> NativeDialogs.showErrorMessage("Mod is a Duplicate and already installed.\nIf updating, please uninstall old file first.");
                case FAIL -> NativeDialogs.showErrorMessage(String.format("Mod file %s could not be added.\nPlease try again.", file.getName()));
                // Fallthrough on default
                default -> {

                }
            }
        });

        return true;
    }

    @Override
    public boolean canImport(TransferSupport support) {

        for(DataFlavor flavor : support.getDataFlavors()) {

            if(flavor.isFlavorJavaFileListType()) {

                return true;
            }
        }
        return false;
    }
}
