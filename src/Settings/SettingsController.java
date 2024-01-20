package Settings;

import Controllers.LoaderClass;
import Controllers.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class SettingsController {

	@FXML
	private BorderPane borderPane;

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
}
