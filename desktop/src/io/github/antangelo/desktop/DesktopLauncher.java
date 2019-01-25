package io.github.antangelo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.antangelo.GIPlatformer;

public class DesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "GI Game Jame Winter 2019 - Placeholder Title";
        config.width = 800;
        config.height = 600;
        new LwjglApplication(new GIPlatformer(), config);
    }
}
