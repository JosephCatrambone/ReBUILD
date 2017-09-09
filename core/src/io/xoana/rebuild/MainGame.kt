package io.xoana.rebuild

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport

import java.net.InetSocketAddress
import java.util.*

/**
 * Created by Jo on 2017-06-08.
 */

class MainGame : ApplicationAdapter() {
	companion object {
		var assets:AssetManager = AssetManager()
		var atlas:TextureAtlas = TextureAtlas()
		var stateStack = Stack<GameState>()
	}

	override fun create() {
		//MainGame.atlas = TextureAtlas(Gdx.files.internal("atlas_image.atlas"));

		// Initialize stage.
		//mainStage = Stage(ScreenViewport())
		stateStack.push(LevelEditor2D(this))
	}

	override fun render() {
		super.render()
		stateStack.peek().render()
		stateStack.peek().update(Gdx.graphics.deltaTime)
	}

	override fun dispose() {
	}

	override fun resize(width: Int, height: Int) {
		// DO NOT RECOMPUTE THE CAMERA HERE!  It will botch our config.  Leave it alone.
		//camera = new PerspectiveCamera(FOV, width, height);
	}
}

abstract class GameState(val mainGameRef:MainGame) {
	abstract fun render();
	abstract fun update(deltaTime: Float);
	abstract fun destroy();
}