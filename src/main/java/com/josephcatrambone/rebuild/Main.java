package com.josephcatrambone.rebuild;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

	@Override
	public void start(Stage stage) {
		imageDemo(stage);
	}

	public void sinDemo(Stage stage) {
		// Build UI
		stage.setTitle("Aij Test UI");

		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);

		Scene scene = new Scene(pane, WIDTH, HEIGHT);

		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		pane.add(canvas, 0, 0);

		stage.setScene(scene);
		stage.show();

		// Build data
		final int RESOLUTION = 1000;
		final Matrix x = new Matrix(RESOLUTION, 1);
		for(int i=0; i < RESOLUTION; i++) {
			x.set(i, 0, Math.PI * i * 2.0 / (float) RESOLUTION);
		}
		final Matrix y = x.elementOp(v -> Math.sin(v));

		// Build backend
		final NeuralNetwork nn = new NeuralNetwork(new int[]{1, 10, 1}, new String[]{"linear", "tanh", "linear"});
		BackpropTrainer trainer = new BackpropTrainer();
		trainer.batchSize = 1;
		trainer.momentum = 0.9;
		trainer.learningRate = 0.01;
		trainer.notificationIncrement = 1000;
		trainer.maxIterations = 1001;

		Runnable updateFunction = new Runnable() {
			@Override
			public void run() {
				Matrix prediction = nn.predict(x);
				GraphicsContext gc = canvas.getGraphicsContext2D();
				gc.setFill(Color.BLACK);
				gc.fillRect(0, 0, WIDTH, HEIGHT);
				gc.setFill(Color.WHITE);
				for(int i=0; i < RESOLUTION-1; i++) {
					gc.fillOval(i*WIDTH/RESOLUTION, (1+y.get(i, 0))*HEIGHT/2.0, 1.0, 1.0);
				}
				gc.setFill(Color.BLUE);
				for(int i=0; i < RESOLUTION-1; i++) {
					gc.fillOval(i*WIDTH/RESOLUTION, (1+prediction.get(i, 0))*HEIGHT/2.0, 1.0, 1.0);
				}
			}
		};

		// Redraw the UI in the main thread.
		// We're abusing Java animation because we can only redraw from the main thread and need to do so with events.
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				trainer.train(nn, x, y, updateFunction);
			}
		}));
		timeline.playFromStart();
	}

	/*** visualizeRBM
	 * Given an RBM as input, return an image which shows the sensitivity of each pathway.
	 * Attempts to produce a square image.
	 * @param rbm
	 * @param normalizeIntensity
	 * @return
	 */
	public Image visualizeRBM(RestrictedBoltzmannMachine rbm, boolean normalizeIntensity) {
		int outputNeurons = rbm.getNumOutputs();
		int inputNeurons = rbm.getNumInputs();
		int subImgWidth = (int)Math.ceil(Math.sqrt(inputNeurons));
		int imgWidth = (int)Math.ceil(Math.sqrt(outputNeurons))*subImgWidth;
		WritableImage output = new WritableImage(imgWidth, imgWidth);
		PixelWriter pw = output.getPixelWriter();

		for(int i=0; i < outputNeurons; i++) {
			int subImgOffsetX = subImgWidth*(i%((int)Math.ceil(Math.sqrt(outputNeurons))));
			int subImgOffsetY = subImgWidth*(i/((int)Math.ceil(Math.sqrt(outputNeurons))));

			// Set one item hot and reconstruct
			Matrix stim = new Matrix(1, outputNeurons);
			stim.set(0, i, 1.0);
			Matrix reconstruction = rbm.reconstruct(stim);

			// Normalize data if needed
			double low = 0;
			double high = 1;
			if(normalizeIntensity) {
				low = Double.MAX_VALUE;
				high = Double.MIN_VALUE;
				for(int j=0; j < reconstruction.numColumns(); j++) {
					double val = reconstruction.get(0, j);
					if (val < low) { low = val; }
					if (val > high) { high = val; }
				}
			}

			// Rebuild and draw input to image
			for(int j=0; j < reconstruction.numColumns(); j++) {
				double val = reconstruction.get(0, j);
				val = (val-low)/(high-low);
				if(val < 0) { val = 0; }
				if(val > 1) { val = 1; }
				pw.setColor(subImgOffsetX + j%subImgWidth, subImgOffsetY + j/subImgWidth, Color.gray(val));
			}
		}

		return output;
	}

	public Matrix loadMNIST(final String filename) {
		int numImages = -1;
		int imgWidth = -1;
		int imgHeight = -1;
		Matrix trainingData = null;

		// Load MNIST training data.
		try(FileInputStream fin = new FileInputStream(filename); DataInputStream din = new DataInputStream(fin)) {
			din.readInt();
			assert(din.readInt() == 2051);
			numImages = din.readInt();
			imgHeight = din.readInt();
			imgWidth = din.readInt();
			System.out.println("numImages: " + numImages);
			System.out.println("height: " + imgHeight);
			System.out.println("width: " + imgWidth);
			trainingData = new Matrix(numImages, imgWidth*imgHeight);
			for(int i=0; i < numImages; i++) {
				for(int y=0; y < imgHeight; y++) {
					for(int x=0; x < imgWidth; x++) {
						int grey = ((int)din.readByte()) & 0xFF; // Java is always signed.  Need to and with 0xFF to undo it.
						trainingData.set(i, x+y*imgWidth, ((double)(grey-128.0))/128.0);
					}
				}
				if(i%1000 == 0) {
					System.out.println("Loaded " + i + " out of " + numImages);
				}
			}
			fin.close();
		} catch(FileNotFoundException fnfe) {
			System.out.println("Unable to find and load file.");
			System.exit(-1);
		} catch(IOException ioe) {
			System.out.println("IO Exception while reading data.");
			System.exit(-1);
		}
		return trainingData;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
