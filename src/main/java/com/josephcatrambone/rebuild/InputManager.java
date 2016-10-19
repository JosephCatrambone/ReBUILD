package com.josephcatrambone.rebuild;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.nio.DoubleBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static org.lwjgl.glfw.GLFW.*;

class InputManager {
	HashMap <Integer, LinkedList<Runnable>> keyDownBindings; // Called on press.
	HashMap <Integer, LinkedList<Runnable>> keyUpBindings; // Called on release.
	HashSet <Integer> keyPressed; // Updated once every frame.
	HashSet <Integer> keyReleased;
	boolean[] keyDown = new boolean[256];

	// Wrappers for getting mouse position.
	DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
	DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);

	public InputManager() {
		keyDownBindings = new HashMap<>();
		keyUpBindings = new HashMap<>();
		keyPressed = new HashSet<>();
		keyReleased = new HashSet<>();
	}

	public GLFWKeyCallbackI getKeyCallback() {
		final InputManager im = this; // Finalize reference.
		return (long window, int key, int scancode, int action, int mods) -> {
			im.handleKeyEvent(key, scancode, action, mods);
		};
	}

	public GLFWCursorPosCallbackI getMousePositionCallback() {
		final InputManager im = this;
		return (long window, double xOffset, double yOffset) -> {
			im.handleCursorPositionEvent(xOffset, yOffset);
		};
	}

	public void handleKeyEvent(int key, int scancode, int action, int mods) {
		LinkedList <Runnable> actionList = null;
		if(action == GLFW_RELEASE) {
			actionList = keyDownBindings.getOrDefault(key, null);
		} else if(action == GLFW_PRESS) {
			actionList = keyUpBindings.getOrDefault(key, null);
		}

		if(actionList != null) {
			actionList.forEach(a -> a.run());
		}

		// Thanks ChernoProject.
		// Since there are multiply types of events, we just check to see if it's released.  If it's not released, it's pressed.
		if(key < 256) {
			keyDown[key] = action != GLFW_RELEASE;
		}
	}

	public void handleCursorPositionEvent(double x, double y) {

	}

	public void addKeyDownEvent(int key, Runnable f) {
		if(!keyDownBindings.containsKey(key)) {
			keyDownBindings.put(key, new LinkedList<>());
		}
		keyDownBindings.get(key).add(f);
	}
}
