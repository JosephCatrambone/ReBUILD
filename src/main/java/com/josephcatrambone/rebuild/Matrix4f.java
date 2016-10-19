package com.josephcatrambone.rebuild;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 * Created by Jo on 2016-10-18.
 */
public class Matrix4f {
	float[] data;

	public Matrix4f() {
		data = new float[16];
	}

	public float get(int row, int column) {
		return data[column + row*4];
	}

	public void set(int row, int column, float value) {
		data[column + row*4] = value;
	}

	public static Matrix4f Identity() {
		Matrix4f mat = new Matrix4f();
		mat.set(0, 0, 1.0f);
		mat.set(1, 1, 1.0f);
		mat.set(2, 2, 1.0f);
		mat.set(3, 3, 1.0f);
		return mat;
	}

	public static Matrix4f Orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f mat = Identity();
		mat.set(0, 0, 2.0f/(right-left));
		mat.set(1, 1, 2.0f/(top-bottom));
		mat.set(2, 2, -2.0f/(far-near));
		mat.set(3, 0, -(right+left)/(right-left));
		mat.set(3, 1, -(top+bottom)/(top-bottom));
		mat.set(3, 2, -(far+near)/(far-near));
		// 3,3=1 from ident
		return mat;
	}

	public static Matrix4f Projection() {
		return null; // TODO
	}

	public static Matrix4f Translation(float dx, float dy, float dz) {
		Matrix4f m = Identity();
		m.set(0, 3, dx);
		m.set(1, 3, dy);
		m.set(2, 3, dz);
		return m;
	}

	public static Matrix4f RotationZ(float angle) {
		return null;
	}

	public static void UnaryOperation(Matrix4f src, Matrix4f target, UnaryOperator<Float> op) {
		for(int i=0; i < src.data.length; i++) {
			target.data[i] = op.apply(src.data[i]);
		}
	}

	public static void BinaryOperation(Matrix4f a, Matrix4f b, Matrix4f target, BinaryOperator<Float> op) {
		for(int i=0; i < target.data.length; i++) {
			target.data[i] = op.apply(a.data[i], b.data[i]);
		}
	}

	public static void multiply(Matrix4f a, Matrix4f b, Matrix4f target) {
		for(int row=0; row < 4; row++) {
			for(int column=0; column < 4; column++) {
				float accumulator = 0;
				for(int k=0; k < 4; k++) {
					accumulator += a.get(row, k)*b.get(k, column);
				}
				target.set(row, column, accumulator);
			}
		}
	}

	public FloatBuffer toFloatBuffer() {
		FloatBuffer buf = BufferUtils.createFloatBuffer(16);
		buf.put(data);
		return buf;
	}
}
