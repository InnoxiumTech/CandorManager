package uk.co.innoxium.candor.window.component;

import uk.co.innoxium.cybernize.util.ClassLoadUtil;

import javax.swing.*;
import java.util.Objects;


/**
 * Creates a new JLabel set up for only displaying an image.
 * TODO: Merge in to @Link{github.com/InnoxiumTech/SwingExtensions}
 */
public class JImage extends JLabel {

    public JImage(String iconResource) {

        this.setIcon(new ImageIcon(Objects.requireNonNull(ClassLoadUtil.getCL().getResource(iconResource))));
    }

    public JImage(ImageIcon image) {

        this.setIcon(image);
    }
}
