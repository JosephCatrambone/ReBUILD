package com.josephcatrambone.rebuild.tools;

import com.josephcatrambone.rebuild.Map;
import com.josephcatrambone.rebuild.Wall;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josephcatrambone on 7/9/15.
 */
public class AddVertexTool implements Tool {
	Point2D previousPoint;

	public String getName() {
		return "Add Vertex Tool";
	}

	public boolean operate(Map map, ToolContext context) {
		double x = context.x;
		double y = context.y;

		Point2D pt = new Point2D(x, y);

		// First check if this point is starting from another edge.
		int index = map.vertices.indexOf(pt);

		// If we are starting a new segment...
		if(previousPoint == null) {
			// Nope.  New point.
			if (index == -1) {
				System.out.println("Adding new edge.");
				previousPoint = pt;
				map.vertices.add(previousPoint);
			} else { // We're adding to an old wall.
				System.out.println("Starting from old edge.");
				previousPoint = map.vertices.get(index);
			}
		} else {
			Wall wall = new Wall();
			wall.a = previousPoint;

			// If this click is NOT on an old edge...
			if(index == -1) {
				Point2D nextPoint = pt;
				wall.b = nextPoint;
				previousPoint = nextPoint;
				map.vertices.add(previousPoint);
				map.walls.add(wall);
			} else { // We're closing this loop.
				wall.b = map.vertices.get(index);
				map.walls.add(wall);
				previousPoint = null;
			}
		}

		return true;
	}

	/*** cancelVertex
	 * Remove the last vertex placed, along with all corresponding walls.
	 * Reset the previousVertex to one of the walls linking into it.
	 */
	public boolean undo(Map map, ToolContext ctxt) {
		if(previousPoint == null) { return false; }

		// Select the walls that we need to destroy.
		List<Wall> candidateWalls = new ArrayList<>();
		for(Wall w : map.walls) {
			if(previousPoint == w.b) { // We want exact match.  Use == instead of equals.
				candidateWalls.add(w);
			}
		}
		if(candidateWalls.size() > 0) { // If we have a few candidates, remove the last one.
			Wall last = candidateWalls.get(candidateWalls.size() - 1);
			previousPoint = last.a;
			map.walls.remove(last);
			// TODO: Remove from all sectors.
			// If we have only one wall, remove the vertex, too.
			if(candidateWalls.size() == 1) {
				map.vertices.remove(last.b);
			}
		} else { // Since there are no candidates, just remove the point.
			// Don't worry about the case where there are walls STARTING from the point,
			// this can't happen because the graph is planar and a point would have to loop back upon itself.
			map.vertices.remove(previousPoint);
			previousPoint = null;
		}

		return true;
	}

}
