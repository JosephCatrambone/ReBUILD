package com.josephcatrambone.rebuild.tools;

import com.josephcatrambone.rebuild.Map;
import com.josephcatrambone.rebuild.Wall;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josephcatrambone on 7/9/15.
 */
public class RemoveVertexTool implements Tool {
	public double VERTEX_SELECTION_THRESHOLD;

	public String getName() {
		return "Remove Vertex Tool";
	}

	public boolean operate(Map map, ToolContext context) {
		double x = context.x;
		double y = context.y;
		boolean force = true;

		Point2D target = map.findNearestVertex(x, y, VERTEX_SELECTION_THRESHOLD);

		if(target == null) {
			System.out.println("DEBUG: No point selected.");
			return false;
		}

		int targetIndex = map.vertices.indexOf(target);


		// Select the walls this is going to impact.
		// There are two styles we can handle:
		//  \b               a/
		// -ba-----b   a-----ba--
		// /b                a\
		List<Wall> aMatch = new ArrayList<>(); // Walls whose start changes.
		List <Wall> bMatch = new ArrayList<>(); // Walls whose end changes.
		for(Wall w : map.walls) {
			if(w.a == target) {
				aMatch.add(w);
			} else if(w.b == target) {
				bMatch.add(w);
			} else {
				// No match
			}
		}

		// TODO: We assume that there are no free-standing walls and that the place operation always finishes.
		// We need to handle the cases where the user starts placing a wall then bails out.

		// If we have one source and multiple-targets (fan out), we're okay.
		if(aMatch.size() == 1 && bMatch.size() > 0) {
			// Connect each wall's b-side to a's b-side, then remove a.
			for(Wall w : bMatch) {
				w.b = aMatch.get(0).b;
			}
			map.walls.remove(aMatch.get(0));
			map.vertices.remove(targetIndex);
		} else if(aMatch.size() > 0 && bMatch.size() == 1) { // Fan in
			for(Wall w : aMatch) {
				w.a = bMatch.get(0).a;
			}
			map.walls.remove(bMatch.get(0));
			map.vertices.remove(targetIndex);
		} else if(aMatch.size() > 1 && bMatch.size() > 1) {  // We have multiple matches of each kind or a degenerate problem.
			if(force) {
				System.out.println("DEBUG: Force remove.");
				for(Wall w : aMatch) {
					map.walls.remove(w);
				}
				for(Wall w : bMatch) {
					map.walls.remove(w);
				}
				map.vertices.remove(targetIndex);
			} else {
				System.err.println("DEBUG: Can't delete point.  Many to many problem.");
			}
		} else { // This point has nothing coming in.
			map.vertices.remove(targetIndex);

		}

		return true;
	}

	public boolean undo(Map map, ToolContext context) { return false; }
}
