package com.josephcatrambone.rebuild.tools;

import javafx.event.Event;
import javafx.scene.input.KeyCode;

/**
 * Created by josephcatrambone on 7/9/15.
 */
public class ToolContext {
	public double x; // The x and y values in MAP space.
	public double y;
	public KeyCode key;
	public Event event;

	public ToolContext(double x, double y, KeyCode key, Event event) {
		this.x = x;
		this.y = y;
		this.key = key;
		this.event = event;
	}
}
