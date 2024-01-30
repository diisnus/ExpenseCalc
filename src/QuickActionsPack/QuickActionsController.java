package QuickActionsPack;



import java.net.URL;
import java.util.ResourceBundle;


import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import Controllers.LoaderClass;
import Controllers.MainPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class QuickActionsController extends MainPageController implements Initializable {
	@FXML
	public Button addItem;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button logOut;
	
	@FXML
	void addItemClick() {
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/YourItems/addUserItems.fxml");
	}

    @FXML
    void logOutClick(ActionEvent event) {

    }
	
	public int i = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	

}
