package com.josephcatrambone.rebuild;

import com.sun.javafx.geom.Line2D;
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


	public Main() {

	}

	@Override
	public void start(Stage stage) {
		// Build UI
		stage.setTitle(TITLE);

		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);

		Scene scene = new Scene(pane, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		Editor editor = new Editor(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		// Add handlers
		stage.addEventHandler(KeyEvent.KEY_PRESSED, editor.getKeyPressHandler());

		stage.addEventHandler(KeyEvent.KEY_RELEASED, editor.getKeyReleaseHandler());

		stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, editor.getMouseDragHandler());

		stage.addEventHandler(MouseEvent.MOUSE_CLICKED, editor.getMouseClickHandler());

		stage.addEventHandler(MouseEvent.MOUSE_MOVED, editor.getMouseMoveHandler());

		// Repeated draw.
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.0/60f), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				editor.redraw();
			}
		}));
		timeline.playFromStart();

		pane.add(editor, 0, 0);
		stage.setScene(scene);
		stage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}
