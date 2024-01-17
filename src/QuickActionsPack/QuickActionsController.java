package QuickActionsPack;



import java.net.URL;
import java.util.ResourceBundle;


import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import Controllers.LoaderClass;
import Controllers.MainPageController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class QuickActionsController extends MainPageController implements Initializable {
	@FXML
	public Button addItem;

	@FXML
	public AnchorPane anchorPane;


	
	@FXML
	void addItemClick() {
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/YourItems/addUserItems.fxml");
	}

	public int i = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	

}
