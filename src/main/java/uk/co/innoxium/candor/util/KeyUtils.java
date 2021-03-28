package uk.co.innoxium.candor.util;

import java.awt.*;
import java.awt.event.KeyEvent;


public class KeyUtils {

    private static volatile boolean ctrlDown = false;
    private static volatile boolean shiftDown = false;

    public static void init() {

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {

            synchronized(KeyUtils.class) {

                switch(e.getID()) {

                    case KeyEvent.KEY_PRESSED: {

                        if(e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {

                            ctrlDown = true;
                        } else if (e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {

                            shiftDown = true;
                        }
                        break;
                    }
                    case KeyEvent.KEY_RELEASED: {

                        if(e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {

                            ctrlDown = false;
                        } else if (e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {

                            shiftDown = false;
                        }
                        break;
                    }
                }
            }
            return false;
        });
    }

    public static boolean isCtrlDown() {

        return ctrlDown;
    }

    public static boolean isShiftDown() {

        return shiftDown;
    }
}
