package uk.co.innoxium.candor.util;

import com.google.common.collect.Sets;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.nfd.NFDPathSet;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import uk.co.innoxium.candor.CandorLauncher;
import uk.co.innoxium.cybernize.net.Hastebin;
import uk.co.innoxium.swing.util.DesktopUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.util.nfd.NativeFileDialog.*;


/**
 * The dialogs class contains a few helper methods to communicate between NativeFileDialogs and TinyFileDialogs.
 */
public class NativeDialogs {

    /**
     * Shows a native file dialog allowing you to choose a single file.
     *
     * @param filterList - will show only files in the filter list, they should be comma separated without spaces. i.e. "png,jpg,svg".
     * @return - Will return a file instance if a file was chosen, else will return null.
     */
    public static File showSingleFileDialog(String filterList) {

        return showSingleFileDialog(filterList, null);
    }

    public static File showSingleFileDialog(String filterList, File defaultPath) {

        // Initialize the value we will return
        File ret = null;

        // Start to open the file dialog
        PointerBuffer path = memAllocPointer(1);

        // Open the pick folder dialog
        PointerBuffer defPath = memAllocPointer(1);
        ByteBuffer defaultPathBuffer = null;
        String defPathString = null;
        if(defaultPath != null) {

            String absolute = defaultPath.getAbsolutePath().replace("\\.", "");
            defaultPathBuffer = MemoryUtil.memUTF8Safe(absolute);
            defPathString = absolute;
        }

        try {

            int result = checkResult(NFD_OpenDialog(filterList, defPathString, path), path);
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
            CandorLauncher.safeExit(1);
        } finally {

            memFree(path);
        }

        return ret;
    }

    /**
     * @param filterList - a list of strings for the file extensions to filter
     * @return - a list of all files selected by the user, can be null
     */
    public static Set<File> openMultiFileDialog(String filterList) {

        // Initialize the value we will return
        Set<File> ret = Sets.newHashSet();

        // Open the file dialog
        try(NFDPathSet pathSet = NFDPathSet.calloc()) {

            int result = NFD_OpenDialogMultiple(filterList, null, pathSet);
            switch(result) {

                case NFD_OKAY -> {

                    long count = NFD_PathSet_GetCount(pathSet);
                    for(long i = 0; i < count; i++) {

                        String path = NFD_PathSet_GetPath(pathSet, i);
                        File file = new File(path);
                        ret.add(file);
                        System.out.format("Path %d: %s\n", i, path);
                    }
                    NFD_PathSet_Free(pathSet);
                }
                case NFD_CANCEL -> Logger.info("User pressed cancel.");
                // NFD_ERROR
                default -> System.err.format("Error: %s\n", NFD_GetError());
            }
        }

        return ret;
    }

    /**
     * @return Returns the folder selected by the user, can be null
     */
    public static File openPickFolder() {

        return openPickFolder(null);
    }

    /**
     * @param defaultPath - The default location to open to
     * @return Returns the folder selected by the user, can be null
     */
    public static File openPickFolder(File defaultPath) {

        // Initialize the value we will return
        File ret = null;

        // Open the pick folder dialog
        PointerBuffer path = memAllocPointer(1);
        ByteBuffer buffer = null;
        if(defaultPath != null) {

            String absolute = defaultPath.getAbsolutePath().replace("\\.", "");
            buffer = MemoryUtil.memUTF8Safe(absolute);
        }

        try {

            int result = checkResult(NFD_PickFolder(buffer, path), path);
            if(result == NFD_OKAY) ret = new File(path.getStringUTF8(0));
        } catch(NullPointerException e) {

            e.printStackTrace();
        } finally {

            memFree(path);
        }

        return ret;
    }

    /*
        A method used primarily in this class only, to decide what happens based on the outcome of the dialog.
     */
    private static int checkResult(int result, PointerBuffer path) {

        switch(result) {

            case NFD_OKAY -> {

                Logger.info("Success!");
                Logger.info(path.getStringUTF8(0));
                nNFD_Free(path.get(0));
            }
            case NFD_CANCEL -> {

                Logger.info("User pressed cancel.");
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

    /**
     * A tiny wrapper method to TinyFileDialogs, see @Link{TinyFileDialogs.tinyfd_messagebox} for more info.
     *
     * @return - true for ok, false for cancel.
     */
    public static boolean showInfoDialog(String title, String message, String dialogType, String iconType, boolean defaultOption) {

        return TinyFileDialogs.tinyfd_messageBox(title, message, dialogType, iconType, defaultOption);
    }

    /**
     * Shows an error message, see {showInfoDialog} for more info.
     *
     * @param message - The message to show.
     * @return - true for ok, false for cancel.
     */
    public static boolean showErrorMessage(String message) {

        return showInfoDialog(
                "Candor Mod Manager",
                message,
                "ok",
                "error",
                true
        );
    }

    public static boolean showErrorWithUpload(String message) {

        try {

            File log = new File("./debug.log");
            FileReader fr = new FileReader(log);
            BufferedReader reader = new BufferedReader(fr);
            String hastebinContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            String hastebinurl = Hastebin.post(hastebinContent, false);
            message += "\nA log has been uploaded, please copy the link for help: " + hastebinurl;
            DesktopUtil.openURL(hastebinurl.replace("https://", ""));
        } catch(IOException | URISyntaxException e) {

            e.printStackTrace();
        }

        return showInfoDialog(
                "Candor Mod Manager",
                message,
                "ok",
                "error",
                true
        );
    }

    /**
     * Shows a generic failure dialog.
     */
    public static void showCandorGenericFailure() {

        showCandorGenericFailure(true);
    }

    public static void showCandorGenericFailure(boolean hastebinUpload) {

        try {
            String message = "Candor experienced an error.\nPlease restart or contact us at:\nhttps://discord.gg/CMG9ZtS";
            if(hastebinUpload) {

                File log = new File("./debug.log");
                FileReader fr = new FileReader(log);
                BufferedReader reader = new BufferedReader(fr);
                String hastebinContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                String hastebinurl = Hastebin.post(hastebinContent, false);
                message += "\nA log has been uploaded, please copy the link for help: " + hastebinurl;
                DesktopUtil.openURL(hastebinurl.replace("https://", ""));
            }
            showErrorMessage(
                    message
            );
            CandorLauncher.safeExit(2);
        } catch(IOException | URISyntaxException e) {

            e.printStackTrace();
            CandorLauncher.safeExit(2);
        }
    }

    /**
     * Asks the user to confirm an action to be taken.
     *
     * @param action - A description of the action about to be taken, for example, "Remove Mod(s)"
     * @return - true for ok, false for cancel.
     */
    public static boolean showConfirmDialog(String action) {

        return showInfoDialog(
                "Candor Mod Manager",
                "Are you sure you wish perform:\n" + action,
                "yesno",
                "question",
                false);
    }
}
