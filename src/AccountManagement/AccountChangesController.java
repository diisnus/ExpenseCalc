package AccountManagement;

import Controllers.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class AccountChangesController {

    @FXML
    private BorderPane borderPane;

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


}
