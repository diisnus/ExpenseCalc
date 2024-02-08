package AdminTables;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AdminTablesController implements Initializable{

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button checkUsersButton;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button requestedWhitelistButton;

    @FXML
    void checkUsersButtonClick(ActionEvent event) {
    	PopUpWindow.showCustomDialog("", "/AdminTables/UsersList.fxml");

    }

    @FXML
    void requestedWhitelistButtonClick(ActionEvent event) {
    	PopUpWindow.showCustomDialog("", "/AdminTables/RequestedWhitelistItems.fxml");
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    gridPane.setGridLinesVisible(true);		
		
	}

}
