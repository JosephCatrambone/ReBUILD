package com.josephcatrambone.rebuild;

import com.sun.javafx.geom.Line2D;
import javafx.geometry.Point2D;

import java.util.List;

/**
 * Created by josephcatrambone on 7/6/15.
 */
public class Utils {
	/*** projectAB
	 * Given points A, B, and C which make lines AB, AC,
	 * Project AB onto AC.
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static Point2D projectAB(Point2D a, Point2D b, Point2D c) {
		Point2D bPrime = b.subtract(a);
		Point2D cPrime = c.subtract(a);

		// Project the point onto the line.
		//     B
		//    ^^ y
		//   / |
		//  /  |
		// A---x---------->C

		// Assume both lines are at the origin.
		double dxBC = bPrime.getX() - cPrime.getX();
		double dyBC = bPrime.getY() - cPrime.getY();
		double dotBC = Math.sqrt(dxBC*dxBC + dyBC*dyBC);
		double magnitudeCSquared = cPrime.getX()*cPrime.getX() + cPrime.getY()*cPrime.getY();

		return new Point2D((float)(((dotBC/magnitudeCSquared)*(cPrime.getX())) + a.getX()), ((float)((dotBC/magnitudeCSquared)*(cPrime.getY())) + a.getY()));
	}
}
