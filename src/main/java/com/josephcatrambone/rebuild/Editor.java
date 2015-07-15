package com.josephcatrambone.rebuild;

import com.josephcatrambone.rebuild.tools.*;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.util.*;

/**
 * Created by josephcatrambone on 7/9/15.
 */
public class Editor extends Canvas  {
	final Color BACKGROUND = Color.BLACK;
	final Color GRID = Color.DARKGRAY;
	final Color VERTS = Color.GREENYELLOW;
	final Color WALLS = Color.WHITE;
	final Color PORTALS = Color.DEEPPINK;
	final int VERT_SIZE = 4;
	final double VERTEX_SELECTION_THRESHOLD = VERT_SIZE*4;

	// Editor
	private Tool activeTool = null;
	private java.util.Map<KeyCode, Tool> keyboardBindings;
	private Point2D previousMouse = null;
	private Point2D mouseDelta = null;
	private int gridSnap = 10;
	private Map map;

	public Editor(int width, int height) {
		super(width, height);

		keyboardBindings = new HashMap<>();
		keyboardBindings.put(KeyCode.ESCAPE, null);
		keyboardBindings.put(KeyCode.Q, null);
		keyboardBindings.put(KeyCode.C, new AddVertexTool());
		keyboardBindings.put(KeyCode.X, new RemoveVertexTool());
		keyboardBindings.put(KeyCode.P, new TogglePortalTool());

		map = new Map();
	}

	private void updateMouseDelta(MouseEvent event) {
		if(previousMouse == null) {
			previousMouse = new Point2D(event.getX(), event.getY());
		}
		double x = event.getX();
		double y = event.getY();
		double dx = x - previousMouse.getX();
		double dy = y - previousMouse.getY();
		mouseDelta = new Point2D(dx, dy);
		previousMouse = new Point2D(x, y);
	}

	private double unprojectX(double x) {
		//Untransform x
		try {
			x = this.getGraphicsContext2D().getTransform().inverseTransform(x, 0).getX();
		} catch(javafx.scene.transform.NonInvertibleTransformException nite) {
			x -= this.getGraphicsContext2D().getTransform().getTx();
		}

		// Snap to grid.
		if(gridSnap > 1) {
			x -= (Math.round(x) % gridSnap);
		}

		return x;
	}

	private double unprojectY(double y) {
		// Invert transform
		try {
			y = this.getGraphicsContext2D().getTransform().inverseTransform(0, y).getY();
		} catch(javafx.scene.transform.NonInvertibleTransformException nite) {
			y -= this.getGraphicsContext2D().getTransform().getTy();
		}

		// Snap to grid.
		if(gridSnap > 1) {
			y -= (Math.round(y) % gridSnap);
		}

		return y;
	}

	public void drawGrid() {
		GraphicsContext gc = this.getGraphicsContext2D();

		gc.setFill(BACKGROUND);
		gc.clearRect(0, 0, map.mapSize.getX(), map.mapSize.getY());
		gc.fill();
		gc.fillRect(0, 0, map.mapSize.getX(), map.mapSize.getY());
		//gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		gc.setStroke(GRID);
		// Draw the horizontal lines.
		for(int y=0; y < map.mapSize.getY(); y += gridSnap) {
			gc.strokeLine(0, y, map.mapSize.getX(), y);
		}
		// Draw vertical lines
		for(int x=0; x < map.mapSize.getX(); x += gridSnap) {
			gc.strokeLine(x, 0, x, map.mapSize.getY());
		}
	}

	public void drawMap() {
		GraphicsContext gc = this.getGraphicsContext2D();

		// Draw the edges first.
		gc.setStroke(WALLS);
		for(Wall w : map.walls) {
			gc.strokeLine(w.a.getX(), w.a.getY(), w.b.getX(), w.b.getY());
		}
		// Draw the portals
		gc.setStroke(PORTALS);
		for(Wall w : map.walls) {
			if(w.portal) {
				gc.strokeLine(w.a.getX(), w.a.getY(), w.b.getX(), w.b.getY());
			}
		}
		// Draw the verts
		gc.setStroke(VERTS);
		for(Point2D v : map.vertices) {
			gc.strokeRect(v.getX()-VERT_SIZE, v.getY()-VERT_SIZE, 2*VERT_SIZE, 2*VERT_SIZE);
		}
	}

	public void redraw() {
		drawGrid();
		drawMap();
	}

	public EventHandler<KeyEvent> getKeyPressHandler() {
		return (event) -> {};
	}

	public EventHandler<KeyEvent> getKeyReleaseHandler() {
		return (KeyEvent event) -> {
			if(keyboardBindings.containsKey(event.getCode())) {
				activeTool = keyboardBindings.get(event.getCode());
			}
		};
	}

	public EventHandler<MouseEvent> getMouseMoveHandler() {
		return (MouseEvent event) -> {
			updateMouseDelta(event);
		};
	}

	public EventHandler<MouseEvent> getMouseDragHandler() {
		return (MouseEvent event) -> {
			updateMouseDelta(event);

			// Handle map scrolling.
			if(event.isShiftDown()) {
				Affine at = this.getGraphicsContext2D().getTransform();
				at.appendTranslation(mouseDelta.getX(), mouseDelta.getY());
				this.getGraphicsContext2D().setTransform(at);
			}
		};
	}

	public EventHandler<MouseEvent> getMouseClickHandler() {
		return (event) -> {
			double x = unprojectX(event.getX());
			double y = unprojectY(event.getSceneY());
			if(event.getClickCount() == 1 && activeTool != null) {
				activeTool.operate(map, new ToolContext(x, y, null, event));
			}
		};
	}
}
