package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class ErrorMessageLoginSignUpController {

    @FXML
    private Button back;

	
	public void backClicked() {
		try {
			Stage stage = (Stage) back.getScene().getWindow(); 
			stage.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
