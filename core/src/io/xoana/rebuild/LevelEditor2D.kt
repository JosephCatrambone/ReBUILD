package io.xoana.rebuild

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3

/**
 * Created by jo on 2017-09-09.
 */
class LevelEditor2D(mgr:MainGame) : GameState(mgr) {

	var activeTool: LevelTool? = null

	var shapeRenderer = ShapeRenderer()
	var level = Level()

	var camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
	val cameraTarget = Vec(0f, 0f)
	var cameraSmoothing: Float = 0.9f

	var gridLevel:Int = 10 // Draw lines every 10^gridLevel pixel.

	override fun render() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

		// Apply camera projection
		camera.update(true)
		shapeRenderer.projectionMatrix = camera.combined

		// Draw Grid
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
		shapeRenderer.color = Color.DARK_GRAY
		// Unfortunately, we've got top-left screen as (0,0) and bottom right as (width,height)
		val cameraTopLeftProjection = camera.unproject(Vector3(0f, 0f, 0f))
		val cameraBottomRightProjection = camera.unproject(Vector3(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), 0f))
		println("Camera top left: ${cameraTopLeftProjection.x} ${cameraTopLeftProjection.y} ${cameraTopLeftProjection.z}")
		println("Camera bottom right: ${cameraBottomRightProjection.x} ${cameraBottomRightProjection.y} ${cameraBottomRightProjection.z}")
		val gridStart = snapToGrid(Vec(cameraTopLeftProjection.x, cameraTopLeftProjection.y))
		val gridEnd = snapToGrid(Vec(cameraBottomRightProjection.x, cameraBottomRightProjection.y))
		for(i in gridStart.x.toInt() until gridEnd.x.toInt() step gridLevel) {
			shapeRenderer.line(i.toFloat(), -camera.viewportHeight, i.toFloat(), camera.viewportHeight);
		}
		for(i in gridEnd.y.toInt() until gridStart.y.toInt() step gridLevel) {
			shapeRenderer.line(-camera.viewportWidth, i.toFloat(), camera.viewportWidth, i.toFloat());
		}
		shapeRenderer.end()

		// Draw Level
		level.draw(shapeRenderer)

		// Draw UI
	}

	override fun update(deltaTime: Float) {
		// Hack: To handle saving and exporting, replace the current scene with an IO scene that uses Scene2D UI.
		// Do all the keyboard input things.
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			//mode = TOOL_MODE.CAMERA_MOVE
			activeTool = CameraMoveTool(this)
		}

		activeTool?.onUpdate(deltaTime)

		// Update camera.
		// Smoothly move towards our target.  TIME INDEPENDENT!
		val cameraPos = Vec(camera.position.x, camera.position.y)
		val interpolaiton = cameraTarget*(1.0f-cameraSmoothing) + (cameraPos*cameraSmoothing)
		camera.position.set(interpolaiton.x, interpolaiton.y, camera.position.z)
	}

	override fun dispose() {

	}

	override fun resize(width: Int, height: Int) {
		camera.setToOrtho(false, width.toFloat(), height.toFloat())
	}

	fun snapToGrid(vec:Vec): Vec {
		return Vec((vec.x*gridLevel).toInt().toFloat()/gridLevel, (vec.y*gridLevel).toInt().toFloat()/gridLevel)
	}
}

abstract class LevelTool(val editorRef: LevelEditor2D) {
	abstract fun onUpdate(deltaTime: Float);
}

class SelectTool(editorRef:LevelEditor2D) : LevelTool(editorRef) {
	override fun onUpdate(deltaTime: Float) {

	}
}

class CameraMoveTool(editorRef:LevelEditor2D) : LevelTool(editorRef) {
	override fun onUpdate(deltaTime: Float) {
		if(Gdx.input.isTouched(0) || Gdx.input.isButtonPressed(0)) {
			var dx1 = Gdx.input.getDeltaX(0)
			var dy1 = Gdx.input.getDeltaY(0)
			var dx2 = Gdx.input.getDeltaX(1)
			var dy2 = Gdx.input.getDeltaY(1)

			// If we flip X and leave Y alone, the camera moves to match the mouse direction.
			// If we leave X alone and flip Y, the camera moves in the opposite direction of the mouse, like dragging.
			editorRef.cameraTarget.x -= dx1.toFloat()
			editorRef.cameraTarget.y += dy1.toFloat()
		}
	}
}

class DrawTool(editorRef: LevelEditor2D) : LevelTool(editorRef) {
	
	override fun onUpdate(deltaTime: Float) {

	}

}
