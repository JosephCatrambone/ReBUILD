package com.josephcatrambone.rebuild;

import java.lang.Math;

public class Vector3<T extends Number> {
	public static final float EQUAL_THRESHOLD = 1e-5f;
	public T x, y, z;

	public Vector3() {
	}

	public Vector3(T x, T y, T z) {
		this.x = x; this.y = y; this.z = z;
	}

	public T[] asarray() {
		return (T[])new Object[]{x, y, z};
	}

	public boolean equals(Vector3<? extends Number> other) {
		if(other == null) { return false; }
		return distance(other) < EQUAL_THRESHOLD;
	}

	public float magnitude() {
		return (float)Math.sqrt(squaredMagnitude());
	}

	public float squaredMagnitude() {
		float X = Float.class.cast(x);
		float Y = Float.class.cast(y);
		float Z = Float.class.cast(z);
		return (float)(X*X + Y*Y + Z*Z);
	}

	public float distance(Vector3<? extends Number> other) {
		return (float)Math.sqrt(squaredDistance(other));
	}

	public float squaredDistance(Vector3<? extends Number> other ) {
		float dx = Float.class.cast(other.x) - Float.class.cast(x);
		float dy = Float.class.cast(other.y) - Float.class.cast(y);
		float dz = Float.class.cast(other.z) - Float.class.cast(z);
		return dx*dx + dy*dy + dz*dz;
	}

	public Vector3<T> normalized() {
		float mag = magnitude();
		if(mag == 0) { // TODO: Log error.
			return new Vector3<T>();
		}
		float X = Float.class.cast(x)/mag;
		float Y = Float.class.cast(y)/mag;
		float Z = Float.class.cast(z)/mag;
		return new Vector3<T>((T)(Object)X, (T)(Object)Y, (T)(Object)Z);
	}

	public void normalize() {
		float mag = magnitude();
		this.x = (T)(Object)(Float.class.cast(x)/mag);
		this.y = (T)(Object)(Float.class.cast(y)/mag);
		this.z = (T)(Object)(Float.class.cast(z)/mag);
	}

	public void add_i(Vector3<? extends Number> v) {
		this.x = (T)(Object)(Float.class.cast(x)+Float.class.cast(v.x));
		this.y = (T)(Object)(Float.class.cast(y)+Float.class.cast(v.y));
		this.z = (T)(Object)(Float.class.cast(z)+Float.class.cast(v.z));
	}

	public void sub_i(Vector3<? extends Number> v) {
		this.x = (T)(Object)(Float.class.cast(x)-Float.class.cast(v.x));
		this.y = (T)(Object)(Float.class.cast(y)-Float.class.cast(v.y));
		this.z = (T)(Object)(Float.class.cast(z)-Float.class.cast(v.z));
	}

	public void multiply_i(Vector3<? extends Number> v) {
		this.x = (T)(Object)(Float.class.cast(x)/Float.class.cast(v.x));
		this.y = (T)(Object)(Float.class.cast(y)/Float.class.cast(v.y));
		this.z = (T)(Object)(Float.class.cast(z)/Float.class.cast(v.z));
	}

	public void multiply_i(float scalar) {
		this.x = (T)(Object)(Float.class.cast(x)*scalar);
		this.y = (T)(Object)(Float.class.cast(y)*scalar);
		this.z = (T)(Object)(Float.class.cast(z)*scalar);
	}

	public T dot(Vector3<? extends Number> other) {
		float tx = Float.class.cast(x);
		float ty = Float.class.cast(y);
		float tz = Float.class.cast(z);
		float ox = Float.class.cast(other.x);
		float oy = Float.class.cast(other.y);
		float oz = Float.class.cast(other.z);
		return (T)(Object)(tx*ox + ty*oy + tz*oz);
	}

	public Vector3<T> cross(Vector3<? extends Number> other) {
		// this  x y z
		// other x y z
		float tx = Float.class.cast(x);
		float ty = Float.class.cast(y);
		float tz = Float.class.cast(z);
		float ox = Float.class.cast(other.x);
		float oy = Float.class.cast(other.y);
		float oz = Float.class.cast(other.z);
		return new Vector3<T>(
			(T)(Object)(ty*oz - tz*oy),
			(T)(Object)(-(tx*oz - tz*ox)),
			(T)(Object)(tx*oy - ty*ox)
		);
	}
}