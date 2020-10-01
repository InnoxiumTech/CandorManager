package uk.co.innoxium.candor.util;

import net.harawata.appdirs.AppDirsFactory;
import uk.co.innoxium.cybernize.util.ClassLoadUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A class containing resources that we will query, as well as a few helper methods.
 */
public class Resources {

    // The current scene we are on, potentially removable?
    public static JComponent currentScene;

    /* The path to our stored data. */
    public static final File CANDOR_DATA_PATH = new File(
            AppDirsFactory.getInstance().getUserDataDir(
                    "Innoxium/Candor",
                    null,
                    null,
                    true));

    // Instances of the folders inside the data folder we use.
    public static final File STORE_PATH = new File(CANDOR_DATA_PATH, "ModStore");
    public static final File CONFIG_PATH = new File(CANDOR_DATA_PATH, "Config");

    // Our logos, currently only used in the about dialog.
    public static final ImageIcon CANDOR_LOGO = new ImageIcon(Objects.requireNonNull(ClassLoadUtil.getCL().getResource("logo.png")));
    public static final ImageIcon INNOXIUM_LOGO = getScaledInnoxiumLogo();

    // The font we use for the mods list.
    public static Font fantasque;

    /**
     * Installs the Fantasque Font that we use for the Mods list.
     * @throws IOException
     * @throws FontFormatException
     */
    public static void installFonts() throws IOException, FontFormatException {

        String fontResource = "fonts/FantasqueSansMono-Regular.ttf";
        InputStream is = ClassLoadUtil.getCL().getResourceAsStream(fontResource);
        assert is != null;
        fantasque = Font.createFont(Font.TRUETYPE_FONT, is);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fantasque);
        Logger.info(fantasque.getFamily());
        Logger.info(fantasque.getFontName());
        is.close();
    }

    /**
     *
     * @return Returns a scaled version of the Innoxium logo, in relation to the Candor Logo, onl y used in the about dialog.
     */
    private static ImageIcon getScaledInnoxiumLogo() {

        ImageIcon initialIcon = new ImageIcon(Objects.requireNonNull(ClassLoadUtil.getCL().getResource("innoxiumlogo.png")));
        Image theImage = initialIcon.getImage();
        Dimension dimension = new Dimension(initialIcon.getIconWidth(), initialIcon.getIconHeight());
        float aspectRatio = (float)dimension.width / (float)dimension.height;
        int newWidth = (int)(CANDOR_LOGO.getIconHeight() * aspectRatio);
        Image scaled = theImage.getScaledInstance(newWidth, CANDOR_LOGO.getIconHeight(), Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * We use this font to get an instance of the Fantasque Mono font, at the desired font size.
     * @param fontSize - The size of the font you wish to derive.
     * @return - Will return a font instance of either Fantasque Mono, or Calibri if the font failed to install.
     */
    public static Font getFantasque(float fontSize) {

        if(fantasque == null) {

            fantasque = Font.getFont("Calibri");
        }
        return fantasque.deriveFont(fontSize);
    }
}
