package com.josephcatrambone.rebuild;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager {
	HashMap <Integer, LinkedList<Runnable>> keyDownBindings; // Called on press.
	HashMap <Integer, LinkedList<Runnable>> keyUpBindings; // Called on release.
	HashSet <Integer> keyPressed; // Updated once every frame.
	HashSet <Integer> keyReleased;

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
	}

	public void addKeyDownEvent(int key, Runnable f) {
		if(!keyDownBindings.containsKey(key)) {
			keyDownBindings.put(key, new LinkedList<>());
		}
		keyDownBindings.get(key).add(f);
	}
}
