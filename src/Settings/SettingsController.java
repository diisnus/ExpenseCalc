package Settings;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.LoaderClass;
import Controllers.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class SettingsController implements Initializable{

	@FXML
	private BorderPane borderPane;

    @FXML
    private GridPane gridPane;
	
	@FXML
	private Button currencyCalculator;

	@FXML
	private Button feedback;

	@FXML
	private Button profileCurrencyChange;

	@FXML
	private Button themeChange;

	@FXML
	private Button userAccountEdit;

	@FXML
	void profileCurrencyChangeClick(ActionEvent event) {

	}

	@FXML
	void currencyCalculatorClick(ActionEvent event) {
		PopUpWindow.showCustomDialog("", "/Settings/CurrencyConverter.fxml");
	}

	@FXML
	void userAccountEditClick(ActionEvent event) {
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/AccountManagement/AccountChanges.fxml");
	}

	@FXML
	void themeChangeClick(ActionEvent event) {

	}

	@FXML
	void feedbackClick(ActionEvent event) {

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    gridPane.setGridLinesVisible(true);		
	}
}
