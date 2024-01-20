package Settings;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

public class CurrencyConverterController implements Initializable {

    @FXML
    private TextField amountToConvert;

    @FXML
    private ChoiceBox<String> convertFrom;

    @FXML
    private ChoiceBox<String> convertTo;

    @FXML
    private Button convert;
    
    @FXML
    private Button close;

    @FXML
    private Label resultAfterConvert;

    private String[] currencies = {"BGN", "EUR", "USD", "GBP", "CHF", "SEK", "NOK", "DKK", "PLN", "CZK", "HUF", "HRK", "RON"};

  
    private Map<String, Double> conversionRates = new HashMap<>();

    @FXML
    void convertClicked(ActionEvent event) {
        try {
            double amount = Double.parseDouble(amountToConvert.getText());
            String fromCurrency = convertFrom.getValue();
            String toCurrency = convertTo.getValue();

            double convertedAmount = performCurrencyConversion(amount, fromCurrency, toCurrency);

            resultAfterConvert.setText(String.format("%.2f %s = %.2f %s", amount, fromCurrency, convertedAmount, toCurrency));
        } catch (NumberFormatException e) {
            resultAfterConvert.setText("Invalid input. Please enter a valid number.");
        }
    }

    private double performCurrencyConversion(double amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount; 
        }

        double fromRate = conversionRates.get(fromCurrency);
        double toRate = conversionRates.get(toCurrency);


        return amount * (fromRate / toRate) ;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	setTextFieldFormatter(amountToConvert);

        convertFrom.getItems().addAll(currencies);
        convertTo.getItems().addAll(currencies);

        convertFrom.setValue(currencies[0]);
        convertTo.setValue(currencies[1]);

        conversionRates.put("BGN", 1.0);
        conversionRates.put("EUR", 1.96);
        conversionRates.put("USD", 1.80);
        conversionRates.put("GBP", 2.20); // British Pound
        conversionRates.put("CHF", 1.22); // Swiss Franc
        conversionRates.put("SEK", 0.19); // Swedish Krona
        conversionRates.put("NOK", 0.18); // Norwegian Krone
        conversionRates.put("DKK", 0.26); // Danish Krone
        conversionRates.put("PLN", 0.43); // Polish Złoty
        conversionRates.put("CZK", 0.08); // Czech Koruna
        conversionRates.put("HUF", 0.006); // Hungarian Forint
        conversionRates.put("HRK", 0.27); // Croatian Kuna
        conversionRates.put("RON", 0.41); // Romanian Leu
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
