package QuickActionsPack;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import Controllers.MainPageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public class QuickActionsController extends MainPageController implements Initializable {
	@FXML
	public Button addItem;

	@FXML
	public AnchorPane anchorPane;


	
	@FXML
	void addItemClick() {
		loadFXML2("/YourItems/addUserItems.fxml");
	}

	public int i = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public void loadFXML2(String fxmlFile) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
			Pane newPane = loader.load();
			if (newPane instanceof BorderPane) {
				FXMLLoader borderPaneloader = new FXMLLoader(getClass().getResource(fxmlFile));
				BorderPane newBorderPane = borderPaneloader.load();
				anchorPane.getChildren().setAll(newBorderPane);
			} else if (newPane instanceof AnchorPane) {
				FXMLLoader anchorPaneLoader = new FXMLLoader(getClass().getResource(fxmlFile));
				AnchorPane newAnchorPane = anchorPaneLoader.load();

				anchorPane.getChildren().setAll(newAnchorPane);
			}if (newPane instanceof GridPane) {
				FXMLLoader gridPaneLoader = new FXMLLoader(getClass().getResource(fxmlFile));
				GridPane newGridPane = gridPaneLoader.load();
				//borderPaneMain.setCenter(newGridPane);
			} else {
				anchorPane.getChildren().setAll(newPane);
			}
			Node centerNode1 = anchorPane.getChildren().get(0);
			if (centerNode1 != null) {
				AnchorPane.setTopAnchor(centerNode1, 0.0);
				AnchorPane.setRightAnchor(centerNode1, 0.0);
				AnchorPane.setBottomAnchor(centerNode1, 0.0);
				AnchorPane.setLeftAnchor(centerNode1, 0.0);
			} else {
				System.out.println("No valid node set for the AnchorPane.");
			}

		} catch (IOException e) {	
			e.printStackTrace();
		}
	}

}
