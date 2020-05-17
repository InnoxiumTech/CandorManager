package co.uk.shadowchild.modmanager.util.config;

import com.electronwill.nightconfig.core.conversion.PreserveNotNull;

public class Configuration {

    @PreserveNotNull
    public static class DefaultData {

        public static String game = "";
        public static String modsFolder = "";
        public static int windowHeight = 900, windowWidth = 1600;
        public static int targetFrameRate = 60;
        public static boolean enableVsync = true;
    }
}
