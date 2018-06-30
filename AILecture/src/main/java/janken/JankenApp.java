package janken;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class JankenApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("JankenController.fxml"));
		Scene scene = new Scene(root, 700, 700);
//			primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("殺意の波動に目覚めたサザエ");
		primaryStage.setOnCloseRequest(x -> {
			System.out.println("ウフフフフ");
//	            Platform.exit();
        });
		primaryStage.show();

	}


	public static void main(String[] args){
		launch(args);
	}
}
