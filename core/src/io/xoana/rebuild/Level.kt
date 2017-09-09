package io.xoana.rebuild

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * Created by jo on 2017-09-09.
 */
/*
  Ken Silverman's original implementation had the maps in this really simple format:
```
data class Wall(var point:Vec, var nextSector:Int, var material:Int)
data class Sector(var startWall:Int, var wallCount:Int, var floorHeight:Float, var ceilingHeight:Float)
```
 */
data class Wall(var start:Vec, var nextWall:Wall?, var material:Int, var nextSector: Sector?)
data class Sector(val startWall:Wall, var floorMaterial:Int, var ceilingMaterial:Int, var floorHeight:Float, var ceilingHeight:Float)

class Level {
	val WALL_COLOR = Color.WHITE
	val PORTAL_COLOR = Color.RED
	val sectors = mutableListOf<Sector>()

	fun draw(shapeRenderer: ShapeRenderer) {
		// Just draw all the lines.  No transforms.  Nothing special.
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
		// Draw all the wall edges.
		sectors.forEach { sec ->
			val startWall = sec.startWall
			var currentWall:Wall? = sec.startWall
			while(currentWall != null) {
				// Draw a line from this to the next.
				val nextWall = currentWall.nextWall ?: startWall
				if(currentWall.nextSector == null) {
					shapeRenderer.color = WALL_COLOR
				} else {
					shapeRenderer.color = PORTAL_COLOR
				}
				shapeRenderer.line(currentWall.start.x, currentWall.start.y, nextWall.start.x, nextWall.start.y)
				currentWall = currentWall.nextWall
			}
		}
		// Draw all the edges
		shapeRenderer.end()
	}

	fun exportObj():String {
		TODO()
	}
}