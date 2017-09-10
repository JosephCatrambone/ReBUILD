package io.xoana.rebuild

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * Created by jo on 2017-09-09.
 * A sector is a polygon.  Not necessarily convex, but with no interior points.
 *
 */
/*
  Ken Silverman's original implementation had the maps in this really simple format:
```
data class Wall(var point:Vec, var nextSector:Int, var material:Int)
data class Sector(var startWall:Int, var wallCount:Int, var floorHeight:Float, var ceilingHeight:Float)
```
 */
data class Sector(
	val perimeter:Polygon,
	val neighbors:Array<Sector>,
	/*
	val wallMaterials:Material,
	var floorMaterial:Material? = null,
	var ceilingMaterial:Material? = null,
	*/
	var floorHeight:Float = 0f,
	var ceilingHeight:Float = 0f)

class Level {
	val WALL_COLOR = Color.WHITE
	val PORTAL_COLOR = Color.RED
	val POINT_COLOR = Color.GREEN
	val POINT_SIZE = 1.0f

	val sectors = mutableListOf<Sector>()

	fun draw(shapeRenderer: ShapeRenderer) {
		// Just draw all the lines.  No transforms.  Nothing special.
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
		shapeRenderer.color = WALL_COLOR
		sectors.forEach{ sec ->
			for(i in 0 until sec.perimeter.points.size) {
				val p0 = sec.perimeter.points[i]
				val p1 = sec.perimeter.points[(i+1)%sec.perimeter.points.size]
				shapeRenderer.line(p0.x, p0.y, p1.x, p1.y)
			}
		}
		shapeRenderer.end()

		// Draw the little green 'pillar' walls.
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
		shapeRenderer.color = POINT_COLOR
		sectors.forEach{ sec ->
			for(i in 0 until sec.perimeter.points.size) {
				val p0 = sec.perimeter.points[i]
				shapeRenderer.rect(p0.x-POINT_SIZE, p0.y-POINT_SIZE, POINT_SIZE, POINT_SIZE)
			}
		}
		shapeRenderer.end()
	}

	fun exportObj():String {
		TODO()
	}
}