package uk.co.innoxium.candor.window.component;

import uk.co.innoxium.cybernize.util.ClassLoadUtil;

import javax.swing.*;
import java.util.Objects;

public class JImage extends JLabel {

    public JImage(String iconResource) {

        this.setIcon(new ImageIcon(Objects.requireNonNull(ClassLoadUtil.getCL().getResource(iconResource))));
    }

    public JImage(ImageIcon image) {

        this.setIcon(image);
    }
}
