package io.xoana.rebuild

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * Created by jo on 2017-09-09.
 */
class LevelEditor2D(mgr:MainGame) : GameState(mgr) {
	enum class TOOL_MODE{ SELECT, CAMERA_MOVE }

	var mode:TOOL_MODE = TOOL_MODE.SELECT

	var shapeRenderer = ShapeRenderer()
	var level = Level()

	var camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
	val cameraTarget = Vec(0f, 0f)
	var cameraSmoothing: Float = 0.9f

	var gridLevel:Int = 1 // Draw lines every 10^gridLevel pixel.

	override fun render() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

		// Apply camera projection
		camera.update(true)
		shapeRenderer.projectionMatrix = camera.combined

		// Draw sample unit squares.
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
		for(i in 0 until 10) {
			shapeRenderer.rect(0f, 0f, Math.pow(10.0, i.toDouble()).toFloat(), Math.pow(10.0, i.toDouble()).toFloat());
		}
		shapeRenderer.end()

		// Draw Grid
		shapeRenderer.color = Color.DARK_GRAY
		if(gridLevel > 0) {

		}

		// Draw Level
		level.draw(shapeRenderer)

		// Draw UI
	}

	override fun update(deltaTime: Float) {
		// Hack: To handle saving and exporting, replace the current scene with an IO scene that uses Scene2D UI.
		// Do all the keyboard input things.
		mode = TOOL_MODE.SELECT
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			mode = TOOL_MODE.CAMERA_MOVE
		}

		// Do the mouse input things.
		var dx1 = Gdx.input.getDeltaX(0)
		var dy1 = Gdx.input.getDeltaY(0)
		var dx2 = Gdx.input.getDeltaX(1)
		var dy2 = Gdx.input.getDeltaY(1)
		println("dx1: $dx1 \t dy1: $dy1 \t dx2: $dx2 \t dy2: $dy2")

		if(mode == TOOL_MODE.CAMERA_MOVE) {
			// If we flip X and leave Y alone, the camera moves to match the mouse direction.
			// If we leave X alone and flip Y, the camera moves in the opposite direction of the mouse, like dragging.
			cameraTarget.x += dx1.toFloat()
			cameraTarget.y -= dy1.toFloat()
		}

		// Update camera.
		// Smoothly move towards our target.  TIME INDEPENDENT!
		val cameraPos = Vec(camera.position.x, camera.position.y)
		val interpolaiton = cameraTarget*(1.0f-cameraSmoothing) + (cameraPos*cameraSmoothing)
		camera.position.set(interpolaiton.x, interpolaiton.y, camera.position.z)
	}

	override fun destroy() {

	}

}