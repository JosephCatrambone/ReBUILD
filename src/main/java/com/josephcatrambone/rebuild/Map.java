package com.josephcatrambone.rebuild;

import com.sun.javafx.geom.Line2D;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josephcatrambone on 7/1/15.
 */
public class Map {
	public Point2D mapSize = new Point2D(10000, 10000);
	public List <Point2D> vertices;
	public List <Wall> walls;
	public List <Sector> sectors;

	public Map() {
		vertices = new ArrayList<>();
		walls = new ArrayList<>();
		sectors = new ArrayList<>();
	}

	public Point2D findNearestVertex(double x, double y, double selectionThreshold) {
		Point2D pt = new Point2D(x, y);
		double bestDistance = Double.MAX_VALUE;
		Point2D bestCandidate = null;

		for(Point2D candidate : vertices) {
			double dist = pt.distance(candidate);
			if(dist < bestDistance) {
				bestDistance = dist;
				bestCandidate = candidate;
			}
		}

		if(bestDistance < selectionThreshold) {
			return bestCandidate;
		} else {
			return null;
		}
	}

	public static Wall findNearestWall(double x, double y, List<Wall> lines, double distanceLimit) {
		Wall nearestWall = null;
		double nearestDistance = Double.MAX_VALUE;

		for(Wall candidate : lines) {
			Line2D line = new Line2D((float)candidate.a.getX(), (float)candidate.a.getY(), (float)candidate.b.getX(), (float)candidate.b.getY());
			double distance = line.ptLineDist((float)x, (float)y);
			if(distance < nearestDistance) {
				nearestDistance = distance;
				nearestWall = candidate;
			}
		}

		if(nearestDistance < distanceLimit) {
			return nearestWall;
		} else {
			return null;
		}
	}

	/*** loadMap
	 * Load all the vertex/wall/sector data.
	 * @param name
	 * @return Returns true on successful load.
	 */
	public boolean loadMap(String name) {
		return false;
	}

	public boolean saveMap(String name) {
		return false;
	}
}
