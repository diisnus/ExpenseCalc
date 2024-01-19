package ProductOverview;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Controllers.PopUpWindow;
import DBConnection.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

public class UpdatePriceController implements Initializable {
	@FXML
	private Button closeButton;

	@FXML
	private DatePicker dateCalendar;

    @FXML
    private ChoiceBox<String> currency;
	
    private String[] currencies = {"BGN", "EUR", "USD", "GBP", "CHF", "SEK", "NOK", "DKK", "PLN", "CZK", "HUF", "HRK", "RON"};
    
	@FXML
	private Button insertButton;

	@FXML
	private TextField priceField;

	private Connection connection;
	private DBHandler handler;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		currency.getItems().addAll(currencies);
		setTextFieldFormatter(priceField);
		handler = new DBHandler();

	}

	@FXML
	void insertButtonClicked(ActionEvent event) {
		if (dateCalendar.getValue() != null) {
		} else {
			PopUpWindow.showCustomDialog("", "/FXML/ErrorPriceAndDateInput.fxml");
		}
		if (!priceField.getText().isEmpty()) {
			try {
			} catch (NumberFormatException e) {
				System.out.println("Invalid price format");
			}
		} else {
			PopUpWindow.showCustomDialog("", "/FXML/ErrorPriceAndDateInput.fxml");
		}
		IdContainer idcontainer = IdContainer.getInstance();
		String insert = "INSERT INTO prices (product_id, price, purchase_date, currency) VALUES (?, ?, ?, ?);";
		connection = handler.getConnection();
		try {
			PreparedStatement insertStatement = connection.prepareStatement(insert);
			insertStatement.setInt(1, idcontainer.getId());
			insertStatement.setDouble(2, Double.parseDouble(priceField.getText()));
			insertStatement.setString(3, dateCalendar.getValue().toString());
			insertStatement.setString(4, currency.getValue().toString());
			
			int rowsAffected = insertStatement.executeUpdate();
			if (rowsAffected > 0) {

				System.out.println("Data inserted successfully");
				try {
					Stage stage = (Stage) closeButton.getScene().getWindow();
					stage.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Insertion failed");
			}

			insertStatement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void setTextFieldFormatter(TextField textField) {
		TextFormatter<Double> textFormatter = new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*(\\.\\d{0,2})?")) {
				try {
					double newValue = Double.parseDouble(newText);
					if (newValue <= 999.99) {
						return change;
					}
				} catch (NumberFormatException ignored) {
				}
			}
			return null;
		});
		textField.setTextFormatter(textFormatter);
	}

	@FXML
	void closeButtonClicked(ActionEvent event) {

		try {
			Stage stage = (Stage) closeButton.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
