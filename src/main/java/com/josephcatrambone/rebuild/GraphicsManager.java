package com.josephcatrambone.rebuild;

import org.lwjgl.opengl.GL20;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Jo on 2016-10-19.
 */
public class GraphicsManager {

	HashMap<String, Object> assets;
	HashMap<String, String> sourceCode;

	public GraphicsManager() {
		sourceCode = new HashMap<>();
	}

	public int buildShaders(String vertShader, String fragShader) {
		int program = glCreateProgram();

		int vertShaderId = loadShader(vertShader, GL_VERTEX_SHADER);
		int fragShaderId = loadShader(fragShader, GL_FRAGMENT_SHADER);

		glAttachShader(program, vertShaderId);
		glAttachShader(program, fragShaderId);
		glLinkProgram(program);
		glValidateProgram(program);

		// We should delete the shaders after we compiled them.
		glDeleteShader(vertShaderId);
		glDeleteShader(fragShaderId);

		// TODO: Add glUseProgram(id) in a method and glUniform1i(prog, val).

		return program;
	}

	public int loadShader(String shaderFilename, int shaderType) {
		// Try to load the file from a local directory.
		String source = "";
		sourceCode.put(shaderFilename, source);

		int shaderId = glCreateShader(shaderType);
		glShaderSource(shaderId, source);
		glCompileShader(shaderId);
		if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile shader " + shaderFilename);
			return -1;
		}

		return shaderId;
	}
}
