package com.josephcatrambone.rebuild.tools;

import com.josephcatrambone.rebuild.Map;
import javafx.event.Event;
import javafx.scene.input.KeyCode;

/**
 * Created by josephcatrambone on 7/9/15.
 */
public interface Tool {
	String getName();
	boolean operate(Map m, ToolContext ctxt);
	boolean undo(Map m, ToolContext ctxt);
}
