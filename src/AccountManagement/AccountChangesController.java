package AccountManagement;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AccountChangesController implements Initializable{

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane gridPane;
    
    @FXML
    private Button emailChange;

    @FXML
    private Button passwordChange;

    @FXML
    private Button usernameChange;

    @FXML
    void usernameChangeClick(ActionEvent event) {
    	PopUpWindow.showCustomDialog("" , "/AccountManagement/UsernameChange.fxml");
    }
    
    @FXML
    void passwordChangeClick(ActionEvent event) {
    	PopUpWindow.showCustomDialog("" , "/AccountManagement/PasswordChange.fxml");
    }
    
    @FXML
    void emailChangeClick(ActionEvent event) {
    	PopUpWindow.showCustomDialog("" , "/AccountManagement/EmailChange.fxml");
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gridPane.setGridLinesVisible(true);	
		
	}


}
