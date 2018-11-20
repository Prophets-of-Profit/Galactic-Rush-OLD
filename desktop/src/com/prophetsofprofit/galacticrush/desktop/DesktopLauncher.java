package com.prophetsofprofit.galacticrush.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.prophetsofprofit.galacticrush.Main;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Galactic Rush";
        //config.addIcon("high-res-icon.png", Files.FileType.Internal);
        //config.addIcon("icon.png", Files.FileType.Internal);
        new LwjglApplication(new Main(), config);
    }
}
