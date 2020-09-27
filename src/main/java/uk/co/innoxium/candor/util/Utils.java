package uk.co.innoxium.candor.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

/**
 * This class contains a bunch of misc helper methods.
 */
public class Utils {

    /* Supported archive types */
    private static final String[] ARCHIVE_TYPES = { "7z", "zip", "rar" };

    /**
     * checks if the file is an archive - to be improved in the future.
     * @param file - The file to check.
     * @return - true if it is an archive, false if not.
     */
    public static boolean isArchive(File file) {

        String ext = getExtension(file);
        for(String archiveType : ARCHIVE_TYPES) {

            if(archiveType.equalsIgnoreCase(ext)) {

                return true;
            }
        }
        return false;
    }

    /**
     * Gets the extension of the file passed.
     * @param file - The file to get the extension of.
     * @return - A string of the file extension. returns an empty string or null on error.
     */
    public static String getExtension(File file) {

        String filePath = null;
        try {

            filePath = file.getCanonicalPath();
        } catch (IOException e) {

            e.printStackTrace();
            return "";
        }
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }

    /**
     * Creates a vector array from an arraylist.
     * @param list - the list to convert
     * @param <T> - the object type
     * @return - A Vector list with a copy from the arraylist.
     */
    public static <T> Vector<T> getVectorArrayFromList(ArrayList<T> list) {

        return new Vector<>(list);
    }

    // TODO: Remove
    public static UUID getUUIDFromGame(String gameExe) {

        return UUID.nameUUIDFromBytes(gameExe.getBytes(Charset.defaultCharset()));
    }
}
