package application;

import Controllers.MainPageController;
import QuickActionsPack.QuickActionsController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/LogIn.fxml"));
			Scene scene = new Scene(root,700,400);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

//		try {
//			Parent root = FXMLLoader.load(getClass().getResource("/FXML/MainPage.fxml"));
//			Scene scene = new Scene(root);
//			root.setStyle("-fx-background-color: transparent;"); 
//			primaryStage.setScene(scene);
//			primaryStage.show();
//			primaryStage.setResizable(true);
//			primaryStage.setMinWidth(1200);
//			primaryStage.setMinHeight(900);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

}
