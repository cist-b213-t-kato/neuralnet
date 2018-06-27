package cr;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import myai.MultiNeuralNetwork;
import myai.Service;

public class CRApp3 extends Application {

	private Service service;

	private MultiNeuralNetwork neuralNet;

	// 辺の長さ
	private static final int sideSize = 10;
	private static final int xMax = 10;
	private static final int yMax = 10;
	private int[] array;
	// 0が白 1が黒
	private int color = 0;

	private Button resetButton;

	private Button learnButton;

	private Button registerButton;

	private Button computeButton;

	private TextField teacherSignalTextField;

	private Label answerLabel;

	private AnchorPane canvasPane;

	List<double[]> inputs;
	List<double[]> ress;

	public CRApp3() {
		service = new Service();
		inputs = service.getImageList(); //new ArrayList<>();
		ress = service.getTeachList(); //new ArrayList<>();
		array = new int[yMax * xMax];
		neuralNet = new MultiNeuralNetwork(100, 2000, 10, 0.1);
		double[][] i = inputs.toArray(new double[0][0]);
		double[][] r = ress.toArray(new double[0][0]);
		neuralNet.learn(i, r);
	}

	public void draw(Pane canvasPane, int x, int y) {
    	array[xMax*y+x] = color;
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
			array = new int[yMax*xMax];
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
					doubleArray[xMax*i+j] = array[xMax*i+j];
				}
			}

			try {
				int teacherSignal = Integer.parseInt(teacherSignalTextField.getText());
				if ( teacherSignal <= 9 && teacherSignal >= 0) {
					double[] teacherSignalArray = new double[10];
					teacherSignalArray[teacherSignal] = 1;
					inputs.add(doubleArray);
					ress.add(teacherSignalArray);

					service.register(doubleArray, teacherSignalArray);

					//reset
					canvasPane.getChildren().clear();
					array = new int[yMax*xMax];
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
			}

			System.out.println("登録できませんでした");

		});
		root.getChildren().add(registerButton);

		learnButton = new Button("learn");
		learnButton.setLayoutX(200);
		learnButton.setLayoutY(200);
		learnButton.setPrefWidth(100);
		learnButton.setPrefHeight(50);
		learnButton.setOnAction(event->{
			double[][] i = inputs.toArray(new double[0][0]);
			double[][] r = ress.toArray(new double[0][0]);
			neuralNet.learn(i, r);
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
        	color = ( array[xMax*y+x] + 1 ) % 2;
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
			double[] doubleArray = new double[yMax*xMax];
			for ( int i=0; i<yMax; i++ ) {
				for ( int j=0; j<xMax; j++ ) {
					doubleArray[xMax*i+j] = array[xMax*i+j];
				}
			}
			double[] result = neuralNet.compute(doubleArray);
			int max = 0;
			for ( int i=1; i<result.length; i++ ) {
				if ( result[i] > result[max] ) {
					max = i;
				}
			}
//			if ( result[max] >= 0.8 ) {
				answerLabel.setFont(new Font("Arial", 20));
				answerLabel.setText(String.format("%.1f%%の確率で%d", result[max]*100, max));
//			} else {
//				answerLabel.setFont(new Font("Arial", 10));
//				answerLabel.setText("おそらく");
//			}
			neuralNet.printResult();
		});
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
