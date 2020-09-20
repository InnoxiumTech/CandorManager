package uk.co.innoxium.candor.util;

import net.harawata.appdirs.AppDirsFactory;
import uk.co.innoxium.cybernize.util.ClassLoadUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Resources {

    public static JComponent currentScene;

    public static final File CANDOR_DATA_PATH = new File(
            AppDirsFactory.getInstance().getUserDataDir(
                    "Innoxium/Candor",
                    null,
                    null,
                    true));

    public static final File STORE_PATH = new File(CANDOR_DATA_PATH, "ModStore");
    public static final File CONFIG_PATH = new File(CANDOR_DATA_PATH, "Config");

    public static final ImageIcon CANDOR_LOGO = new ImageIcon(Objects.requireNonNull(ClassLoadUtil.getCL().getResource("logo.png")));
    public static final ImageIcon INNOXIUM_LOGO = getInnoxiumLogo();

    public static Font fantasque;

    public static void installFonts() throws IOException, FontFormatException {

        String fontResource = "fonts/FantasqueSansMono-Regular.ttf";
        InputStream is = ClassLoadUtil.getCL().getResourceAsStream(fontResource);
        assert is != null;
        fantasque = Font.createFont(Font.TRUETYPE_FONT, is);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fantasque);
        System.out.println(fantasque.getFamily());
        System.out.println(fantasque.getFontName());
        is.close();
    }

    private static ImageIcon getInnoxiumLogo() {

        ImageIcon initialIcon = new ImageIcon(Objects.requireNonNull(ClassLoadUtil.getCL().getResource("innoxiumlogo.png")));
        Image theImage = initialIcon.getImage();
        Dimension dimension = new Dimension(initialIcon.getIconWidth(), initialIcon.getIconHeight());
        float aspectRatio = (float)dimension.width / (float)dimension.height;
        int newWidth = (int)(CANDOR_LOGO.getIconHeight() * aspectRatio);
        Image scaled = theImage.getScaledInstance(newWidth, CANDOR_LOGO.getIconHeight(), Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}
