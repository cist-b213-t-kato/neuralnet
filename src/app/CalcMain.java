package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class CalcMain extends Application {


	@Override
	public void start(Stage primaryStage) throws Exception {

			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root,500,500);
			primaryStage.setScene(scene);
			primaryStage.setTitle("いいちこじゃんけん");
			primaryStage.setOnCloseRequest(x -> {
	            Platform.exit();
	        });
			primaryStage.show();

	}


	public static void main(String[] args){
		launch(args);
	}
}
