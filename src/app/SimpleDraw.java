package app;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class SimpleDraw extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("simple draw");
		Group root = new Group();
		Path path = new Path();
		root.getChildren().add(path);
		Scene scene = new Scene(root);
        scene.setOnMousePressed((event) -> {
        	path.getElements().add(new MoveTo(event.getX(),event.getY()));
        });
        scene.setOnMouseDragged((event)->{
        	path.getElements().add(new LineTo(event.getX(),event.getY()));
        });
		stage.setScene(scene);
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
