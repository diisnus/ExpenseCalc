package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class ErrorMessageLoginSignup{

	public static void showCustomDialog(String title, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ErrorMessageLoginSignup.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setScene(new Scene(root));

            // Set the controller if needed
            // CustomDialogController controller = loader.getController();

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
