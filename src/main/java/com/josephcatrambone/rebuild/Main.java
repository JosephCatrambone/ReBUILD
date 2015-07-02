package com.josephcatrambone.rebuild;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Random;


public class Main extends Application {
	// Although our editor is stateful, the root pane keeps track of states and can force sub-panes to transition from state to state.
	final String TITLE = "ReBUILD";
	final int DEFAULT_WIDTH = 800;
	final int DEFAULT_HEIGHT = 600;

	@Override
	public void start(Stage stage) {
		// Build UI
		stage.setTitle(TITLE);

		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);

		Scene scene = new Scene(pane, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		Canvas canvas = new Canvas(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		pane.add(canvas, 0, 0);

		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
