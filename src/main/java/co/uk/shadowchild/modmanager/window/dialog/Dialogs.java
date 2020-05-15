package co.uk.shadowchild.modmanager.window.dialog;

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.nfd.NFDPathSet;

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

           checkResult(NFD_OpenDialog(filterList, null, path), path);
           ret = new File(path.getStringUTF8(0));
        } finally {

            memFree(path);
        }

        return ret;
    }

    public static Set<File> openMultiFileDialog(String filterList) {

        // Initialize the value we will return
        Set<File> ret = null;

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

    public static File openPickFolder() {

        // Initialize the value we will return
        File ret = null;

        // Open the pick folder dialog
        PointerBuffer path = memAllocPointer(1);

        try {

            checkResult(NFD_PickFolder((ByteBuffer)null, path), path);
            ret = new File(path.getStringUTF8(0));
        } finally {

            memFree(path);
        }

        return ret;
    }

    private static void checkResult(int result, PointerBuffer path) {

        switch (result) {

            case NFD_OKAY -> {

                System.out.println("Success!");
                System.out.println(path.getStringUTF8(0));
                nNFD_Free(path.get(0));
            }
            case NFD_CANCEL -> System.out.println("User pressed cancel.");
            // NFD_ERROR
            default -> System.err.format("Error: %s\n", NFD_GetError());
        }
    }
}