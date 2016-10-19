package com.josephcatrambone.rebuild;

import java.util.ArrayList;

public class Level {
	public Level() {
	}

	class Sector {
		ArrayList <Vector3<Float>> walls;
		ArrayList <Sector> neighbors; // Null = Solid wall.
	}
}
