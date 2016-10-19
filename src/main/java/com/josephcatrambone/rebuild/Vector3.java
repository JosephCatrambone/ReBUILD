package com.josephcatrambone.rebuild;

import java.lang.Math;

public class Vector3<T extends Number> {
	public static final float EQUAL_THRESHOLD = 1e-5;
	public T x, y, z;

	public Vector3() {
	}

	public Vector3(T x, T y, T z) {
		this.x = x; this.y = y; this.z = z;
	}

	public T[] asarray() {
		return new T[]{x, y, z};
	}

	@Override
	public boolean equals(Vector3<? extends Number> other) {
		if(other == null) { return false; }
		return distance(other) < EQUAL_THRESHOLD;
	}

	public float magnitude() {
		return Math.sqrt(squaredMagnitude());
	}

	public float squaredMagnitude() {
		return x*x + y*y + z*z;
	}

	public float distance(Vector3<? extends Number> other) {
		return Math.sqrt(squaredDistance(other));
	}

	public float squaredDistance(Vector3<? extends Number> other ) {
		float dx = other.x - x;
		float dy = other.y - y;
		float dz = other.z - z;
		return dx*dx + dy*dy + dz*dz;
	}

	public Vector3<T> normalized() {
		float mag = magnitude();
		if(mag == 0) { // TODO: Log error.
			return new Vector3<T>();
		}
		return new Vector3<T>(x/mag, y/mag, z/mag);
	}

	public void normalize() {
		float mag = magnitude();
		this.x /= mag;
		this.y /= mag;
		this.z /= mag;
	}

	public void add_i(Vector3<? extends Number> v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}

	public void sub_i(Vector3<? extends Number> v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}

	public void multiply_i(Vector3<? extends Number> v) {
		this.x *= v.x;
		this.y *= v.y;
		this.z *= v.z;
	}

	public void multiply_i(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
	}

	public float dot(Vector3<? extends Number> other) {
		return this.x*other.x + this.y*other.y + this.z*other.z;
	}

	public Vector3<T> cross(Vector3<? extends Number> other) {
		// this  x y z
		// other x y z
		return new Vector3<T>(
			y*other.z - z*other.y,
			-(x*other.z - z*other.x),
			x*other.y - y*other.x
		);
	}
}
