package ProductOverview;


import java.net.URL;
import java.util.ResourceBundle;
import Controllers.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class ProductOverviewController implements Initializable {

	@FXML
	private BorderPane borderPane;
	
	@FXML
	private Button addPrice;

	@FXML
	void addPriceClick(ActionEvent event) {
		PopUpWindow.showCustomDialog("", "/ProductOverview/UpdatePrice.fxml");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		IdContainer idcontainer = IdContainer.getInstance();

	}

}
