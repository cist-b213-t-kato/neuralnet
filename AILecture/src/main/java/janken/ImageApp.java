package janken;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ImageApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
//        primaryStage.setResizable(false);
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.setScene(new Scene(root, 400,100));

        GridPane handsPane = new GridPane();
//        gridPane.setMaxWidth(900);
//        gridPane.setMaxHeight(300);

//        ColumnConstraints column1 = new ColumnConstraints(100,100,Double.MAX_VALUE);
//        column1.setHgrow(Priority.ALWAYS);
//        ColumnConstraints column2 = new ColumnConstraints(100);
//        gridPane.getColumnConstraints().addAll(column1, column2); // first column gets any extra width

        ImageView gImageView = new ImageView("g.png");
        gImageView.setFitWidth(100);
        gImageView.setFitHeight(100);
        Button gButton = new Button("gButton", gImageView);
        gButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//        gButton.setPrefWidth(100);
//        gButton.setPrefHeight(100);

        ImageView cImageView = new ImageView("c.png");
        cImageView.setFitWidth(100);
        cImageView.setFitHeight(100);
        Button cButton = new Button("cButton", cImageView);
        cButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//        cButton.setPrefWidth(100);
//        cButton.setPrefHeight(100);

        ImageView pImageView = new ImageView("p.png");
        pImageView.setFitWidth(100);
        pImageView.setFitHeight(100);
        Button pButton = new Button("pButton", pImageView);
        pButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//        pButton.setPrefWidth(100);
//        pButton.setPrefHeight(100);

		HBox vbButtons = new HBox();
		vbButtons.setSpacing(10);
		vbButtons.setPadding(new Insets(0, 20, 10, 20));
		vbButtons.getChildren().addAll(gButton, cButton, pButton);

//        handsPane.add(gButton, 0, 0);
//        handsPane.add(cButton, 1, 0);
//        handsPane.add(pButton, 2, 0);

//        root.getChildren().add(vbButtons);

        root.getChildren().add(new ImageView("sazae.jpg"));
        root.getChildren().add(vbButtons);

        primaryStage.show();

	}

	public static void main(String[] args) {
		try {
			launch(args);
		} catch ( Exception e ) {
			e.printStackTrace();
			Platform.exit();
		}
	}

}
