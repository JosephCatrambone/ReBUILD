import com.josephcatrambone.rebuild.*;
import com.sun.javafx.geom.Line2D;
import javafx.geometry.Point2D;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jo on 5/28/2015.
 */
public class MainTest {
	@Test
	public void testEqualLines() {
		// Two lines should be equal even if they're facing opposite directions.
		//Wall l1 = new Wall(1, 2, 3, 4);
		//Wall l2 = new Wall(3, 4, 1, 2);
		//assertEquals("L1 != L2. Walls occupying the same space should be equal.", l1, l2);
	}

	@Test
	public void testPointEquality() {
		Point2D p1 = new Point2D(3.0, 1e6);
		Point2D p2 = new Point2D(1.0 + 2.0, 1e6);
		assertEquals("Testing P2D equality", p1, p2);
	}
}
