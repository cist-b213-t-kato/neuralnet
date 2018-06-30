package cr2;
import java.util.ArrayList;
import java.util.List;

import ai.INeuralNetwork;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * 文字認識アプリの抽象クラス
 * @author tkato
 *
 */
public abstract class AbstractCRGUIApp extends Application {

	protected INeuralNetwork nn;

	// 辺の長さ
	private static final int sideSize = 10;
	protected static final int xMax = 10;
	protected static final int yMax = 10;
	protected int[] displayArray;
	// 0が白 1が黒
	protected int color = 0;

	protected Button resetButton;
	protected Button learnButton;
	protected Button registerButton;
	protected TextField teacherSignalTextField;
	protected Label answerLabel;
	protected AnchorPane canvasPane;

	protected List<double[]> inputs;
	protected List<double[]> teaches;

	protected void drawInput( double[] input ) {
		for ( int i=0; i<yMax; i++ ) {
			for ( int j=0; j<xMax; j++ ) {
				System.out.printf("%.0f", input[i*xMax+j]);
			}
			System.out.println();
		}
	}

	protected abstract INeuralNetwork createNeuralNetwork();

	protected void initConst() {
		inputs = new ArrayList<>();
		teaches = new ArrayList<>();
	}

	/**
	 * DB等に永久保存させるコールバックメソッド
	 * @param inputArray
	 * @param teachArray
	 */
	protected void insertRecord(double[] inputArray, double[] teachArray) {

	}

	public AbstractCRGUIApp() {
		initConst();
		displayArray = new int[yMax * xMax];
		nn = createNeuralNetwork();
		double[][] inputsArray = inputs.toArray(new double[0][0]);
		double[][] teachesArray = teaches.toArray(new double[0][0]);
		nn.learn(inputsArray, teachesArray);
	}

	private void draw(Pane canvasPane, int x, int y) {
    	displayArray[xMax*y+x] = color;
    	int drawX = x * sideSize;
    	int drawY = y * sideSize;
    	Rectangle rect = new Rectangle(sideSize, sideSize);
    	if ( color == 0 ) {
    		rect.setFill(Color.WHITE);
    	} else if ( color == 1 ) {
    		rect.setFill(Color.BLACK);
    	}
    	rect.setX(drawX);
    	rect.setY(drawY);
    	canvasPane.getChildren().add(rect);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("character recognition");
		stage.setWidth(300);
		stage.setHeight(400);

		AnchorPane root = new AnchorPane();//(BorderPane)FXMLLoader.load(getClass().getResource("CRApp.fxml"));
		Scene scene = new Scene(root);
		canvasPane = new AnchorPane();
		canvasPane.setPrefWidth(100);
		canvasPane.setPrefHeight(100);
		color = 0;
		for ( int i=0; i<yMax; i++ ) {
			for ( int j=0; j<xMax; j++ ) {
				draw(canvasPane, j, i);
			}
		}
		root.getChildren().add(canvasPane);
		resetButton = new Button("reset");
		resetButton.setLayoutX(200);
		resetButton.setLayoutY(0);
		resetButton.setPrefWidth(100);
		resetButton.setPrefHeight(50);
		resetButton.setOnAction(event->{
			//reset
			canvasPane.getChildren().clear();
			displayArray = new int[yMax*xMax];
			color = 0;
			for ( int i=0; i<yMax; i++ ) {
				for ( int j=0; j<xMax; j++ ) {
					draw(canvasPane, j, i);
				}
			}
		});
		root.getChildren().add(resetButton);

		teacherSignalTextField = new TextField();
		teacherSignalTextField.setPrefWidth(100);
		teacherSignalTextField.setPrefHeight(50);
		teacherSignalTextField.setLayoutX(0);
		teacherSignalTextField.setLayoutY(200);
		root.getChildren().add(teacherSignalTextField);

		registerButton = new Button("register");
		registerButton.setLayoutX(100);
		registerButton.setLayoutY(200);
		registerButton.setPrefWidth(100);
		registerButton.setPrefHeight(50);
		registerButton.setOnAction(event->{
			double[] doubleArray = new double[yMax*xMax];
			for ( int i=0; i<yMax; i++ ) {
				for ( int j=0; j<xMax; j++ ) {
					doubleArray[xMax*i+j] = displayArray[xMax*i+j];
				}
			}

			try {
				int teachSignal = Integer.parseInt(teacherSignalTextField.getText());
				if ( teachSignal <= 9 && teachSignal >= 0) {
					double[] teacherSignalArray = new double[10];
					teacherSignalArray[teachSignal] = 1;
					inputs.add(doubleArray);
					teaches.add(teacherSignalArray);

					insertRecord(doubleArray, teacherSignalArray);

					//reset
					canvasPane.getChildren().clear();
					displayArray = new int[yMax*xMax];
					color = 0;
					for ( int i=0; i<yMax; i++ ) {
						for ( int j=0; j<xMax; j++ ) {
							draw(canvasPane, j, i);
						}
					}
					teacherSignalTextField.clear();

//					if (inputs.size() > 30) {
//						inputs.remove(0);
//						ress.remove(0);
//					}

					return;
				}
			} catch ( NumberFormatException e ) {
				System.err.println("登録できませんでした");
			}
		});
		root.getChildren().add(registerButton);

		learnButton = new Button("learn");
		learnButton.setLayoutX(200);
		learnButton.setLayoutY(200);
		learnButton.setPrefWidth(100);
		learnButton.setPrefHeight(50);
		learnButton.setOnAction(event->{
			double[][] i = inputs.toArray(new double[0][0]);
			double[][] r = teaches.toArray(new double[0][0]);
			nn.learn(i, r);
			System.out.println("学習終了");
		});
		root.getChildren().add(learnButton);

		answerLabel = new Label("-");
		answerLabel.setLayoutX(0);
		answerLabel.setLayoutY(110);
		root.getChildren().add(answerLabel);

		scene.setOnMousePressed((event) -> {
        	int x = (int)(event.getX()/sideSize);
        	int y = (int)(event.getY()/sideSize);
        	if ( x < 0 || x >= xMax || y < 0 || y >= yMax ) {
        		return;
        	}
        	color = ( displayArray[xMax*y+x] + 1 ) % 2;
        	draw(canvasPane, x, y);
        });
		scene.setOnMouseDragged((event)->{
        	int x = (int)(event.getX()/sideSize);
        	int y = (int)(event.getY()/sideSize);
        	if ( x < 0 || x >= xMax || y < 0 || y >= yMax ) {
        		return;
        	}
        	draw(canvasPane, x, y);
        });
		scene.setOnMouseReleased((event)->{
			writingAreaOnMouseReleased();
		});
		stage.setScene(scene);
		stage.show();
	}

	protected abstract void writingAreaOnMouseReleased();

	protected int calcMaxIndex(double[] output) {
		int maxIndex = 0;
		for ( int i=1; i<output.length; i++ ) {
			if ( output[i] > output[maxIndex] ) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}

}
