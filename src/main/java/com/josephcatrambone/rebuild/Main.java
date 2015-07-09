package com.josephcatrambone.rebuild;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.Map;


public class Main extends Application {
	// Although our editor is stateful, the root pane keeps track of states and can force sub-panes to transition from state to state.
	final String TITLE = "ReBUILD";
	final int DEFAULT_WIDTH = 800;
	final int DEFAULT_HEIGHT = 600;
	final Color BACKGROUND = Color.BLACK;
	final Color GRID = Color.DARKGRAY;
	final Color VERTS = Color.GREENYELLOW;
	final Color WALLS = Color.WHITE;
	final Color PORTALS = Color.PINK;
	final int VERT_SIZE = 4;
	final double VERTEX_SELECTION_THRESHOLD = VERT_SIZE*4;

	// Map data
	public Point2D mapSize = new Point2D(10000, 10000);
	public List <Point2D> vertices;
	public List <Wall> walls;
	public List <Sector> sectors;

	// Editor
	public static enum EditorOperation {SELECT, PLACE_VERT, SPLIT_WALL, REMOVE_VERT, DRAG_VERT, UNDO_VERT, QUIT};
	private Map <KeyCode, EditorOperation> keybindings; // 'a' -> Jump
	private GraphicsContext gc; // Yeah, this is lazy.  Whatever.
	private Point2D previousPoint = null;
	private Point2D previousMouse = null;
	private Point2D draggedVertex = null;
	private Point2D mouseDelta = null;
	private int gridSnap = 10;

	public Main() {
		vertices = new ArrayList<>();
		walls = new ArrayList<>();
		sectors = new ArrayList<>();

		keybindings = new HashMap<>();
		keybindings.put(KeyCode.Q, EditorOperation.QUIT);
		keybindings.put(KeyCode.SPACE, EditorOperation.PLACE_VERT);
		keybindings.put(KeyCode.Z, EditorOperation.UNDO_VERT);
		keybindings.put(KeyCode.X, EditorOperation.REMOVE_VERT);
	}

	@Override
	public void start(Stage stage) {
		// Build UI
		stage.setTitle(TITLE);

		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);

		Scene scene = new Scene(pane, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		Canvas canvas = new Canvas(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		gc = (GraphicsContext)canvas.getGraphicsContext2D();

		// Add handlers
		stage.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
		});

		stage.addEventHandler(KeyEvent.KEY_RELEASED, (event) -> {
			if(keybindings.containsKey(event.getCode())) {
				switch (keybindings.get(event.getCode())) {
					case PLACE_VERT:
						addVertex(previousMouse.getX(), previousMouse.getY());
						break;
					case DRAG_VERT:

						break;
					case UNDO_VERT:
						cancelVertex();
						break;
					case REMOVE_VERT:
						removeVertex(previousMouse.getX(), previousMouse.getY());
						break;
					case QUIT:
						System.exit(0); // TODO: Check unsaved map.
						break;
					default:
						break;
				}
			}
		});

		stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				updateMouseDelta(e);

				// Drag grid
				if(e.isShiftDown()) {
					Affine at = gc.getTransform();
					at.appendTranslation(mouseDelta.getX(), mouseDelta.getY());
					gc.setTransform(at);
				}
			}
		});

		stage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				Point2D clickPosition = new Point2D(t.getSceneX(), t.getSceneY());
				if(t.getClickCount() == 1) {

				}
			}
		});

		stage.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				updateMouseDelta(event);
			}
		});

		// Repeated draw.
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.0/60f), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				drawGrid(gc);
				drawMap(gc);
			}
		}));
		timeline.playFromStart();

		pane.add(canvas, 0, 0);
		stage.setScene(scene);
		stage.show();
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
		return x - gc.getTransform().getTx();
	}

	private double unprojectY(double y) {
		return y - gc.getTransform().getTy();
	}

	public void addVertex(double x, double y) {
		// Remap x, y
		x = unprojectX(x);
		y = unprojectY(y);
		if(gridSnap != 0) {
			x = Math.round(x - (x % gridSnap));
			y = Math.round(y - (y % gridSnap));
		}

		Point2D pt = new Point2D(x, y);

		// First check if this point is starting from another edge.
		int index = vertices.indexOf(pt);

		// If we are starting a new segment...
		if(previousPoint == null) {
			// Nope.  New point.
			if (index == -1) {
				System.out.println("Adding new edge.");
				previousPoint = pt;
				vertices.add(previousPoint);
			} else { // We're adding to an old wall.
				System.out.println("Starting from old edge.");
				previousPoint = vertices.get(index);
			}
		} else {
			Wall wall = new Wall();
			wall.a = previousPoint;

			// If this click is NOT on an old edge...
			if(index == -1) {
				Point2D nextPoint = pt;
				wall.b = nextPoint;
				previousPoint = nextPoint;
				vertices.add(previousPoint);
				walls.add(wall);
			} else { // We're closing this loop.
				wall.b = vertices.get(index);
				walls.add(wall);
				previousPoint = null;
			}
		}
	}

	/*** cancelVertex
	 * Remove the last vertex placed, along with all corresponding walls.
	 * Reset the previousVertex to one of the walls linking into it.
	 */
	public void cancelVertex() {
		if(previousPoint == null) { return; }

		// Select the walls that we need to destroy.
		List <Wall> candidateWalls = new ArrayList<>();
		for(Wall w : walls) {
			if(previousPoint == w.b) { // We want exact match.  Use == instead of equals.
				candidateWalls.add(w);
			}
		}
		if(candidateWalls.size() > 0) { // If we have a few candidates, remove the last one.
			Wall last = candidateWalls.get(candidateWalls.size() - 1);
			previousPoint = last.a;
			walls.remove(last);
			// TODO: Remove from all sectors.
			// If we have only one wall, remove the vertex, too.
			if(candidateWalls.size() == 1) {
				vertices.remove(last.b);
			}
		} else { // Since there are no candidates, just remove the point.
			// Don't worry about the case where there are walls STARTING from the point,
			// this can't happen because the graph is planar and a point would have to loop back upon itself.
			vertices.remove(previousPoint);
			previousPoint = null;
		}
	}

	public void removeVertex(double x, double y) {
		Point2D target = findNearestVertex(x, y, VERTEX_SELECTION_THRESHOLD);

		if(target == null) {
			System.out.println("DEBUG: No point selected.");
			return;
		}

		int targetIndex = vertices.indexOf(target);


		// Select the walls this is going to impact.
		// There are two styles we can handle:
		//  \b               a/
		// -ba-----b   a-----ba--
		// /b                a\
		List <Wall> aMatch = new ArrayList<>(); // Walls whose start changes.
		List <Wall> bMatch = new ArrayList<>(); // Walls whose end changes.
		for(Wall w : walls) {
			if(w.a == target) {
				aMatch.add(w);
			} else if(w.b == target) {
				bMatch.add(w);
			} else {
				// No match
			}
		}

		// TODO: We assume that there are no free-standing walls and that the place operation always finishes.
		// We need to handle the cases where the user starts placing a wall then bails out.

		// If we have one source and multiple-targets (fan out), we're okay.
		if(aMatch.size() == 1 && bMatch.size() > 0) {
			// Connect each wall's b-side to a's b-side, then remove a.
			for(Wall w : bMatch) {
				w.b = aMatch.get(0).b;
			}
			walls.remove(aMatch.get(0));
			vertices.remove(targetIndex);
		} else if(aMatch.size() > 0 && bMatch.size() == 1) { // Fan in
			for(Wall w : aMatch) {
				w.a = bMatch.get(0).a;
			}
			walls.remove(bMatch.get(0));
			vertices.remove(targetIndex);
		} else if(aMatch.size() < 1) { // This point has nothing coming in.

		} else { // We have multiple matches of each kind or a degenerate problem.
			/*
			for(Wall w : aMatch) {
				if(w.a == w.b) {

				}
			}
			*/
			System.err.println("Can't delete point.  Many to many problem.");
		}
	}

	public Point2D findNearestVertex(double x, double y, double selectionThreshold) {
		// Remap x, y
		x = unprojectX(x);
		y = unprojectY(y);

		Point2D pt = new Point2D(x, y);
		double bestDistance = Double.MAX_VALUE;
		Point2D bestCandidate = null;

		for(Point2D candidate : vertices) {
			double dist = pt.distance(candidate);
			if(dist < bestDistance) {
				bestDistance = dist;
				bestCandidate = candidate;
			}
		}

		if(bestDistance < selectionThreshold) {
			return bestCandidate;
		} else {
			return null;
		}
	}

	public void drawGrid(GraphicsContext gc) {
		gc.setFill(BACKGROUND);
		gc.clearRect(0, 0, mapSize.getX(), mapSize.getY());
		gc.fill();
		gc.fillRect(0, 0, mapSize.getX(), mapSize.getY());
		//gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		gc.setStroke(GRID);
		// Draw the horizontal lines.
		for(int y=0; y < mapSize.getY(); y += gridSnap) {
			gc.strokeLine(0, y, mapSize.getX(), y);
		}
		// Draw vertical lines
		for(int x=0; x < mapSize.getX(); x += gridSnap) {
			gc.strokeLine(x, 0, x, mapSize.getY());
		}
	}

	public void drawMap(GraphicsContext gc) {
		// Draw the edges first.
		gc.setStroke(WALLS);
		for(Wall w : walls) {
			gc.strokeLine(w.a.getX(), w.a.getY(), w.b.getX(), w.b.getY());
		}
		// Draw the portals
		gc.setStroke(PORTALS);
		for(Wall w : walls) {
			if(w.portal) {
				gc.strokeLine(w.a.getX(), w.a.getY(), w.b.getX(), w.b.getY());
			}
		}
		// Draw the verts
		gc.setStroke(VERTS);
		for(Point2D v : vertices) {
			gc.strokeRect(v.getX()-VERT_SIZE, v.getY()-VERT_SIZE, 2*VERT_SIZE, 2*VERT_SIZE);
		}
	}

	/*** loadMap
	 * Load all the vertex/wall/sector data.
	 * @param name
	 * @return Returns true on successful load.
	 */
	public boolean loadMap(String name) {
		return false;
	}

	public boolean saveMap(String name) {
		return false;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
