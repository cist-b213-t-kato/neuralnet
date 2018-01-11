package app;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DotDrawApp extends Application {

	// 辺の長さ
	private static final int sideSize = 20;
	private static final int xMax = 5;
	private static final int yMax = 5;
	private int[][] array = new int[yMax][xMax];
	// 0が白 1が黒
	private int color = 0;

	public void draw(Group group, int x, int y) {
    	if ( x < 0 || x >= xMax || y < 0 || y >= yMax ) {
    		return;
    	}
    	array[y][x] = color;
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
    	group.getChildren().add(rect);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("dot draw");
		stage.setWidth(100);
		stage.setHeight(100+20);
		Group root = new Group();
		Path path = new Path();
		root.getChildren().add(path);
		Scene scene = new Scene(root);
        scene.setOnMousePressed((event) -> {
        	int x = (int)(event.getX()/sideSize);
        	int y = (int)(event.getY()/sideSize);
        	color = ( array[y][x] + 1 ) % 2;
        	draw(root, x, y);
        });
        scene.setOnMouseDragged((event)->{
        	int x = (int)(event.getX()/sideSize);
        	int y = (int)(event.getY()/sideSize);
        	draw(root, x, y);
        });
		stage.setScene(scene);
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
