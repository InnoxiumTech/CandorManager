import java.awt.*;
import java.lang.management.ManagementFactory;

public class SplashTest {

    public static void main(String... args) {

        for(String s : ManagementFactory.getRuntimeMXBean().getInputArguments()) {

            System.out.println(s);
        }

        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }
    }
}
