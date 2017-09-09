package io.xoana.rebuild.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.xoana.rebuild.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.x = 10;
		config.y = 10;
		config.width = 1280;
		config.height = 640;
		new LwjglApplication(new MainGame(), config);
	}
}
