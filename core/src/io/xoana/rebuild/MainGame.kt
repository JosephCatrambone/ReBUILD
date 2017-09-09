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
	}

	override fun create() {
		MainGame.atlas = TextureAtlas(Gdx.files.internal("atlas_image.atlas"));

		// Initialize stage.
		mainStage = Stage(ScreenViewport())

	}

	override fun render() {
		super.render()

		//Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

	}

	override fun dispose() {
	}

	override fun resize(width: Int, height: Int) {
		// DO NOT RECOMPUTE THE CAMERA HERE!  It will botch our config.  Leave it alone.
		//camera = new PerspectiveCamera(FOV, width, height);
	}
}
