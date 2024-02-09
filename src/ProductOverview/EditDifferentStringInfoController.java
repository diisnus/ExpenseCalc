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
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class EditDifferentStringInfoController implements Initializable {

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button changeValueButton;

	@FXML
	private Button close;

	@FXML
	private HBox titleBar;

	@FXML
	private TextField newValue;

	@FXML
	private ChoiceBox<String> itemsPricePer;

	private String[] choicesPer = { "Per 100g", "Per 1kg", "Per 1g" };

	private Connection connectDB;
	private DBHandler handler;

	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		handler = new DBHandler();
		connectDB = handler.getConnection();

		itemsPricePer.getItems().addAll(choicesPer);

		titleBar.setOnMousePressed(event -> {
			Stage stage = (Stage) titleBar.getScene().getWindow();
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
			stage.setUserData(new double[] { stage.getX(), stage.getY() });
		});

		titleBar.setOnMouseDragged(event -> {
			Stage stage = (Stage) titleBar.getScene().getWindow();
			double newX = event.getScreenX() - xOffset;
			double newY = event.getScreenY() - yOffset;
			stage.setX(newX);
			stage.setY(newY);
		});

		newValue.setVisible(true);
		itemsPricePer.setVisible(false);
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		String type = changeProductInformation.getType();

		if (type.equals("Double")) {
			setTextFieldFormatter(newValue, false);
		} else if (type.equals("Int")) {
			setTextFieldFormatter(newValue, true);
		}
		if (type.equals("PricedBy")) {
			newValue.setVisible(false);
			itemsPricePer.setVisible(true);
		}

	}

	@FXML
	void changeValueButtonClick(ActionEvent event) {
		String updateValue;
		
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		updateValue = changeProductInformation.getStatement();
		
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();
		
		String newVAl = newValue.getText();
		String newPricedBy = itemsPricePer.getValue();
		String type = changeProductInformation.getType();
		
		if (type.equals("PricedBy")) {
			if (newPricedBy.isBlank() || newPricedBy.isEmpty() || newPricedBy == null) {
				PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorMessageLoginSignUp.fxml");
				return;
			}
		} else {
			if (newVAl.isBlank() || newVAl.isEmpty()) {
				PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorMessageLoginSignUp.fxml");
				return;
			}
		}
		
		try {

			PreparedStatement updateValueStatement = connectDB.prepareStatement(updateValue);
			updateValueStatement.setString(1, newVAl);
			if (type.equals("PricedBy")) {
				updateValueStatement.setString(1, newPricedBy);
				changeProductInformation.setNewValue(newPricedBy);
			} else {
				updateValueStatement.setString(1, newVAl);
				changeProductInformation.setNewValue(newVAl);
			}
			updateValueStatement.setInt(2, productid);

			int rowsAffected = updateValueStatement.executeUpdate();
			if (rowsAffected > 0) {
				Stage stage = (Stage) close.getScene().getWindow();
				stage.close();
			}

		} catch (SQLException e) {

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

	private void setTextFieldFormatter(TextField textField, boolean isInteger) {
		if (isInteger) {
			TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
				String newText = change.getControlNewText();
				if (newText.matches("\\d*")) {
					return change;
				} else {
					return null;
				}
			});
			textField.setTextFormatter(textFormatter);
		} else {
			TextFormatter<Double> textFormatter = new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
				String newText = change.getControlNewText();
				if (newText.isEmpty() || newText.matches("\\d*(\\.\\d{0,2})?")) {
					if (textField.getPromptText() != null && !textField.getPromptText().isEmpty()) {
						textField.setPromptText("");
					}
					try {
						double newValue = newText.isEmpty() ? 0.0 : Double.parseDouble(newText);
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
}
