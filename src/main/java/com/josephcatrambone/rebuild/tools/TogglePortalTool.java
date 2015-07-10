package com.josephcatrambone.rebuild.tools;

import com.josephcatrambone.rebuild.Map;

/**
 * Created by josephcatrambone on 7/9/15.
 */
public class TogglePortalTool implements Tool {

	public String getName() {
		return "Toggle Portal Tool";
	}

	@Override
	public boolean operate(Map m, ToolContext ctxt) {
		return false;
	}

	public boolean undo(Map m, ToolContext context) {
		return false;
	}
}
