package com.dinosaur.dinosaurexploder.utils;

import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

public class TextUtils {
    /**
     * Summary :
     * Center the text on the screen
     */
    public static void centerText(Text text) {
        text.setX((getAppWidth() - text.getLayoutBounds().getWidth()) / 2.0);
        text.setY(getAppHeight() / 2.0);
    }
}
