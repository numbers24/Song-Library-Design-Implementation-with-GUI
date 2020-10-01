package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.SonglibController;

public class SongLibApp extends Application {
	
	public void start(Stage primaryStage) throws Exception {
		
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/songlib.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			SonglibController Controller = loader.getController();
			Controller.start(primaryStage);
			Scene scene = new Scene(root, 600, 400);
			primaryStage.setScene(scene);
			primaryStage.show(); 

	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
