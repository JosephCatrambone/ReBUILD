package com.josephcatrambone.rebuild;

import java.lang.util.HashMap;
import java.lang.util.HashSet;
import java.lang.util.LinkedList;

public class InputManager {
/*
glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
                        if(key == GLFW_KEY_ESCAPE && ACTION == GLFW_RELEASE) {
                                glfwSetWindowShouldClose(windowHandle, true);
                        }
                });
*/

	HashMap <int, LinkedList<Function>> keyDownBindings; // Called on press.
	HashMap <int, LinkedList<Function>> keyUpBindings; // Called on release.
	HashSet <int> keyPressed; // Updated once every frame.
	HashSet <int> keyReleased;

	public InputManager() {
	}

	//public Thing getKeyCallback(window, key, scancode, action, mods) {}

	//public void addKeyPressEvent(key, Function) {}
}
