package uk.co.innoxium.candor.util;

import java.io.File;
import java.io.IOException;

public class Utils {

    private static final String[] ARCHIVE_TYPES = { "7z", "zip", "rar" };

    public static boolean isArchive(File file) {

        String ext = getExtension(file);
        for(String archiveType : ARCHIVE_TYPES) {

            if(archiveType.equalsIgnoreCase(ext)) {

                return true;
            }
        }
        return false;
    }

    public static String getExtension(File file) {

        String filePath = null;
        try {

            filePath = file.getCanonicalPath();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }
}
