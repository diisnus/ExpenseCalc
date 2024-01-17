package ProductOverview;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class UpdatePriceController implements Initializable {
	@FXML
	private Button closeButton;

	@FXML
	private DatePicker dateCalendar;

	@FXML
	private Button insertButton;

	@FXML
	private TextField priceField;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setTextFieldFormatter(priceField);

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

	@FXML
	void insertButtonClicked(ActionEvent event) {
		if (dateCalendar.getValue() != null) {
			String date = dateCalendar.getValue().toString();
		} else {
			PopUpWindow.showCustomDialog("", "/FXML/ErrorPriceAndDateInput.fxml");
		}
		
		if (!priceField.getText().isEmpty()) {
		    try {
		        double price = Double.parseDouble(priceField.getText());
		    } catch (NumberFormatException e) {
		        System.out.println("Invalid price format");
		    }
		} else {
			PopUpWindow.showCustomDialog("", "/FXML/ErrorPriceAndDateInput.fxml");
		}
		
		//INSERT INTO prices (product_id, price, purchase_date) 
		//VALUES (9, price, date);

		
		
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


}
