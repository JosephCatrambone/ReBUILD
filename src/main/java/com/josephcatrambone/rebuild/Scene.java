package com.josephcatrambone.rebuild;

/**
 * Created by Jo on 2016-10-18.
 */
public abstract class Scene {
	public abstract void create();
	public abstract void draw();
	public abstract void update(double timedelta);
	public abstract void destroy();
	public abstract void pause();
	public abstract void resume();
}
