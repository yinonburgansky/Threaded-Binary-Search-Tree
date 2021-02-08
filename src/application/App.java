package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

// Standard JavaFX Application Start
public class App extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainLayout.fxml"));
			BorderPane root = (BorderPane)loader.load();
			MainLayoutController controller = (MainLayoutController)loader.getController();
			// pass stage to controller to handle file chooser
			controller.setStage(primaryStage);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Threaded Binary Search Tree");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
//import java.util.*;
