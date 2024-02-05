package QuickActionsPack;



import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Controllers.LoaderClass;
import Controllers.MainPageController;
import Controllers.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;

public class QuickActionsController extends MainPageController implements Initializable {
	@FXML
	public Button addItem;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button recentlyAdded;
    
    @FXML
    private Button logOut;
    
    @FXML
    private GridPane gridPane;
	
	@FXML
	void addItemClick() {
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/YourItems/addUserItems.fxml");
	}

    @FXML
    void logOutClick(ActionEvent event) {
    	
    	try {
    	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LogIn.fxml"));
    	    Parent root = loader.load();
    	    Scene signUpScene = new Scene(root, 700, 400);
    	    Stage newStage = new Stage(); 
    	    newStage.initStyle(StageStyle.UNDECORATED);  
    	    newStage.setScene(signUpScene);
    	    newStage.setResizable(false);
    	    newStage.show(); 
    	    Stage currentStage = (Stage) logOut.getScene().getWindow();
    	    currentStage.close();
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    }
	

    @FXML
    void recentlyAddedClick(ActionEvent event) {
    	PopUpWindow.showCustomDialog("", "/QuickActionsPack/RecentlyAdded.fxml");
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    gridPane.setGridLinesVisible(true);		

	}

	

}
