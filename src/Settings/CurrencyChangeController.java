package Settings;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Controllers.Container;
import DBConnection.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CurrencyChangeController implements Initializable {

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button close;

	@FXML
	private Label currentCurrency;

	@FXML
	private Button insertNewCurrency;

    @FXML
    private ChoiceBox<String> newCurrency;
    private String[] currencies = {"BGN", "EUR", "USD", "GBP", "CHF", "SEK", "NOK", "DKK", "PLN", "CZK", "HUF", "HRK", "RON"};

	private Connection connectDB;
	private DBHandler handler;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newCurrency.getItems().addAll(currencies);
		
		
	    handler = new DBHandler();
	    connectDB = handler.getConnection();
	    Container container = Container.getInstance();
	    int userid = container.getId();
	    String selectCurrency = "SELECT pref_currency FROM users WHERE user_id = ?";
	    try {
	        PreparedStatement selectedCurrency = connectDB.prepareStatement(selectCurrency);
	        selectedCurrency.setInt(1, userid);
	        ResultSet rs = selectedCurrency.executeQuery();
	        if (rs.next()) {
	            String currency = rs.getString("pref_currency");
	            currentCurrency.setText("Your current currency is: " + currency + ".");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	}

	@FXML
	void closeClick(ActionEvent event) {
		try {
			Stage stage = (Stage) close.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void insertNewCurrencyClick(ActionEvent event) {

		String updateCurrency = "UPDATE users SET pref_currency = ? WHERE user_id = ?;";
		connectDB = handler.getConnection();
        String newcurrency = newCurrency.getValue();
        Container container = Container.getInstance();
		int userid = container.getId();
		try {
			PreparedStatement updateCurrentCurrency = connectDB.prepareStatement(updateCurrency);
			updateCurrentCurrency.setString(1, newcurrency);
			updateCurrentCurrency.setInt(2, userid);
			updateCurrentCurrency.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Stage stage = (Stage) newCurrency.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
