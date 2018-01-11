package app;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class TextFieldApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("TextField App");
		stage.setWidth(100);
		stage.setHeight(100+20);

		Group root = new Group();

		Path path = new Path();
		root.getChildren().add(path);

		TextField textField = new TextField();
		root.getChildren().add(textField);

		Button submitButton = new Button("Submit");
		root.getChildren().add(submitButton);

		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
