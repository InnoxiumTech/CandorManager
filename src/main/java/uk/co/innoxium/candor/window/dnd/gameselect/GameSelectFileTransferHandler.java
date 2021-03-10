package uk.co.innoxium.candor.window.dnd.gameselect;

import uk.co.innoxium.candor.util.NativeDialogs;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.candor.window.GameSelectScene;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class GameSelectFileTransferHandler extends TransferHandler {

    @Override
    @SuppressWarnings("unchecked")
    public boolean importData(TransferSupport support) {

        if(!this.canImport(support))
            return false;

        List<File> files;
        try {
            files = (List<File>) support.getTransferable()
                    .getTransferData(DataFlavor.javaFileListFlavor);
        } catch(UnsupportedFlavorException | IOException ex) {
            // should never happen (or JDK is buggy)
            return false;
        }

        if(files.size() > 1) {

            NativeDialogs.showErrorMessage("Only drag one file to this location.");
        } else {

            // We only have one
            File file = files.get(0);

            if(Resources.currentScene instanceof GameSelectScene) {

                GameSelectScene scene = (GameSelectScene) Resources.currentScene;
                if(file.isFile()) {

                    scene.setGame(file);
                } else {

                    scene.setModsFolder(file);
                }
            }
        }
        return true;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {

        for(DataFlavor flavor : support.getDataFlavors()) {

            if(flavor.isFlavorJavaFileListType()) {

                return true;
            }
        }
        return false;
    }
}
