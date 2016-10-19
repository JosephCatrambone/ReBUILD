package com.josephcatrambone.rebuild;

import java.util.ArrayList;

public class Level {
	boolean dirty; // If the map has been modified since the last operation, it's dirty.

	public Level() {
	}

	/*** rasterize
	 * Converts the map from the sector format to a format in which it can be more efficiently displayed.
	 */
	private void rasterize() {

	}

	public void draw2d() {

	}

	public void draw3d() {
	}

	class Sector {
		ArrayList <Vector3<Float>> walls;
		ArrayList <Sector> neighbors; // Null = Solid wall.
	}
}
