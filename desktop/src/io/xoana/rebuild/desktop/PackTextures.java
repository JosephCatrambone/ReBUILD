package io.xoana.rebuild.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import io.xoana.rebuild.MainGame;

public class PackTextures {

	public static void main(String[] arg) {
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.combineSubdirectories = true;
		settings.maxWidth=2048;
		settings.maxHeight=2048;
		TexturePacker.process(settings, "./unpacked", "./core/assets", "atlas_image");
	}
}
