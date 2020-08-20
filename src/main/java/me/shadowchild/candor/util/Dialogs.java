package me.shadowchild.candor.util;

import com.google.common.collect.Sets;
import org.lwjgl.PointerBuffer;
import org.lwjgl.util.nfd.NFDPathSet;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Set;

import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class Dialogs {

    public static File showSingleFileDialog(String filterList) {

        // Initialize the value we will return
        File ret = null;

        // Start to open the file dialog
        PointerBuffer path = memAllocPointer(1);

        try {

            int result = checkResult(NFD_OpenDialog(filterList, null, path), path);
            if(result == NFD_OKAY) ret = new File(path.getStringUTF8(0));
        } catch(Exception e) {

            e.printStackTrace();
            showInfoDialog(
                    "Candor Mod Manager",
                    "File Selection Error.\nPlease run the application again.",
                    "ok",
                    "error",
                    true);
            memFree(path);
            System.exit(1);
        } finally {

            memFree(path);
        }

        return ret;
    }

    /**
     *
     * @param filterList - a list of strings for the file extensions to filter
     * @return - a list of all files selected by the user, can be null
     */
    public static Set<File> openMultiFileDialog(String filterList) {

        // Initialize the value we will return
        Set<File> ret = Sets.newHashSet();

        // Open the file dialog
        try (NFDPathSet pathSet = NFDPathSet.calloc()) {

            int result = NFD_OpenDialogMultiple(filterList, null, pathSet);
            switch (result) {

                case NFD_OKAY -> {

                    long count = NFD_PathSet_GetCount(pathSet);
                    for (long i = 0; i < count; i++) {

                        String path = NFD_PathSet_GetPath(pathSet, i);
                        File file = new File(path);
                        ret.add(file);
                        System.out.format("Path %d: %s\n", i, path);
                    }
                    NFD_PathSet_Free(pathSet);
                }
                case NFD_CANCEL -> System.out.println("User pressed cancel.");
                // NFD_ERROR
                default -> System.err.format("Error: %s\n", NFD_GetError());
            }
        }

        return ret;
    }

    /**
     *
     * @return Returns the folder selected by the user, can be null
     */
    public static File openPickFolder() {

        // Initialize the value we will return
        File ret = null;

        // Open the pick folder dialog
        PointerBuffer path = memAllocPointer(1);

        try {

            int result = checkResult(NFD_PickFolder((ByteBuffer)null, path), path);
            if(result == NFD_OKAY) ret = new File(path.getStringUTF8(0));
        } catch(NullPointerException e) {

            e.printStackTrace();
        } finally {

            memFree(path);
        }

        return ret;
    }

    private static int checkResult(int result, PointerBuffer path) {

        switch (result) {

            case NFD_OKAY -> {

                System.out.println("Success!");
                System.out.println(path.getStringUTF8(0));
                nNFD_Free(path.get(0));
            }
            case NFD_CANCEL -> {

                System.out.println("User pressed cancel.");
                showInfoDialog(
                        "Candor Mod Manager",
                        "Selection Error.\nPlease select a file/folder.",
                        "ok",
                        "error",
                        true);
            }
            // NFD_ERROR
            default -> System.err.format("Error: %s\n", NFD_GetError());
        }
        return result;
    }

    public static boolean showInfoDialog(String title, String message, String dialogType, String iconType, boolean defaultOption) {

        return TinyFileDialogs.tinyfd_messageBox(title, message, dialogType, iconType, defaultOption);
    }

    public static void showCandorGenericFailure() {

        showInfoDialog(
                "Candor Mod Manager",
                "Candor experienced an error.\nPlease restart or contact us at:\nhttps://discord.gg/CMG9ZtS",
                "ok",
                "error",
                true);
        System.exit(2);
    }
}
