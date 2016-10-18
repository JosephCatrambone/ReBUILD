package com.josephcatrambone.rebuild;

import java.io.File;

public class MainGame {

	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
		System.setProperty("java.library.path", new File("native").getAbsolutePath() + File.pathSeparator + "lib");
		System.out.println("What's up?");
	}
}