package com.josephcatrambone.rebuild;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josephcatrambone on 7/2/15.
 */
public class Sector {
	List <Wall> walls;
	double floorHeight;
	double ceilingHeight;

	public Sector() {
		walls = new ArrayList<Wall>();
	}
}
