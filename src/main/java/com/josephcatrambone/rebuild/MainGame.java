package com.josephcatrambone.rebuild;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.util.Stack;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class MainGame {
	public static InputManager inputManager;
	public static Stack<Scene> sceneManager;

	private long windowHandle;
	private double lastUpdateTime;

	public final String SCREEN_TITLE = "ReBUILD";
	public final int SCREEN_WIDTH = 640;
	public final int SCREEN_HEIGHT = 480;

	public void run() {
		try {
			init();
			loop();
			glfwFreeCallbacks(windowHandle);
			glfwDestroyWindow(windowHandle);
		} finally {
			glfwTerminate();
			glfwSetErrorCallback(null).free();
		}
	}

	public void init() {
		// Initialize inputManager handler.
		MainGame.inputManager = new InputManager();
		MainGame.sceneManager = new Stack<>();

		// Set output.
		GLFWErrorCallback.createPrint(System.err).set();

		// Init glfw.
		if(!glfwInit()) {
			throw new IllegalStateException("Can't initialize GLFW.");
		}

		// Config window.
		glfwDefaultWindowHints();
		//glfwWindowHints(GLFW_VISIBLE, GLFW_FALSE
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		windowHandle = glfwCreateWindow(SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_TITLE, NULL, NULL);
		if(windowHandle == NULL) { // NULL, not null.
			throw new RuntimeException("Failed to create GLFW window!");
		}

		MainGame.inputManager.addKeyDownEvent(GLFW_KEY_ESCAPE, () -> { glfwSetWindowShouldClose(windowHandle, true); });
		glfwSetKeyCallback(windowHandle, MainGame.inputManager.getKeyCallback());
		glfwSetCursorPosCallback(windowHandle, MainGame.inputManager.getMousePositionCallback());

		// Center new window on screen.
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(windowHandle, (vidMode.width()-SCREEN_WIDTH)/2, (vidMode.height()-SCREEN_HEIGHT)/2);

		// Make active context and enable v-sync.
		glfwMakeContextCurrent(windowHandle);
		glfwSwapInterval(1); // VSync.

		// Show Window
		glfwShowWindow(windowHandle);

		// Some people create this in the main loop.  Don't care.
		GL.createCapabilities();
	}

	public void loop() {
		lastUpdateTime = glfwGetTime();
		while(!glfwWindowShouldClose(windowHandle)) {
			// Draw
			sceneManager.peek().draw();

			// Swap buffers and read inputs.
			glfwSwapBuffers(windowHandle);
			glfwPollEvents();

			// Handle events.
			double now = glfwGetTime();
			double timedelta = now - lastUpdateTime;
			sceneManager.peek().update(timedelta);
			lastUpdateTime = glfwGetTime();
		}
	}

	public static void main(String[] args) {
		new MainGame().run();
	}
}
