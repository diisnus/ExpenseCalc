package YourItems;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.Container;
import DBConnection.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUserItemsController implements Initializable {

	@FXML
	private ChoiceBox<String> itemsPricePer;

	private String[] choicesPer = { "Per 100g", "Per 1kg", "Per 1g" };
	@FXML
	private Button addItemButton;

	@FXML
	private GridPane gridPaneCentre;

	@FXML
	private GridPane gridPaneTop;

	@FXML
	private TextField itemNameTextField;

	@FXML
	private TextArea itemDescriptionTextField;

	@FXML
	private TextField ItemBrandTextField;

	@FXML
	private TextField itemCaloriesPer100;

	@FXML
	private TextField itemProteinPer100;

	@FXML
	private TextField itemCarbohydratesPer100;

	@FXML
	private TextField itemFatsPer100;

	@FXML
	private TextField itemFibersPer100;

	@FXML
	private TextField itemSaltPer100;

	@FXML
	private TextField itemSaturatedFatsPer100;

	@FXML
	private TextField itemSugarsPer100;


	
	private Connection connection;
	private DBHandler handler;
	private PreparedStatement pst;

	public int accessUserId() {
		Container container = Container.getInstance();
		System.out.println(container.getId());
		return container.getId();
	}


	
	@FXML
	void addItemButtonClick(ActionEvent event) {
		int userid = accessUserId();
		String itemname = itemNameTextField.getText();
		String itemdescr = itemDescriptionTextField.getText();
		String itembrand = ItemBrandTextField.getText();
		String choiceper = itemsPricePer.getValue();
		int caloriesValue = Integer.parseInt(itemCaloriesPer100.getText());
		double proteinValue = Double.parseDouble(itemProteinPer100.getText());
		double carbs = Double.parseDouble(itemCarbohydratesPer100.getText());
		double sugars = Double.parseDouble(itemSugarsPer100.getText());
		double fibers = Double.parseDouble(itemFibersPer100.getText());
		double fats = Double.parseDouble(itemFatsPer100.getText());
		double satfats = Double.parseDouble(itemSaturatedFatsPer100.getText());
		double salts = Double.parseDouble(itemSaltPer100.getText());

		System.out.println(proteinValue);

		connection = handler.getConnection();
		String insert = "INSERT INTO groceryproducts(user_id, product_name, product_brand, description, priced_by, calories_per_100g, protein, carbohydrates, sugar, fiber, fat, saturated_fat, salt, is_whitelisted, created_by_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			PreparedStatement insertStatement = connection.prepareStatement(insert);
			insertStatement.setInt(1, userid);
			insertStatement.setString(2, itemname);
			insertStatement.setString(3, itembrand);
			insertStatement.setString(4, itemdescr);
			insertStatement.setString(5, choiceper);
			insertStatement.setInt(6, caloriesValue);
			insertStatement.setDouble(7, proteinValue);
			insertStatement.setDouble(8, carbs);
			insertStatement.setDouble(9, sugars);
			insertStatement.setDouble(10, fibers);
			insertStatement.setDouble(11, fats);
			insertStatement.setDouble(12, satfats);
			insertStatement.setDouble(13, salts);
			insertStatement.setInt(14, 0);	//  whitelisted = 1
			insertStatement.setInt(15, 1);  // created by user = 1

			int rowsAffected = insertStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Data inserted successfully");
			} else {
				System.out.println("Insertion failed");
			}

			insertStatement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		itemsPricePer.getItems().addAll(choicesPer);
		handler = new DBHandler();
		accessUserId();
		setTextFieldFormatter(itemCaloriesPer100, true);
		setTextFieldFormatter(itemProteinPer100, false);
		setTextFieldFormatter(itemCarbohydratesPer100, false);
		setTextFieldFormatter(itemFatsPer100, false);
		setTextFieldFormatter(itemFibersPer100, false);
		setTextFieldFormatter(itemSaltPer100, false);
		setTextFieldFormatter(itemSaturatedFatsPer100, false);
		setTextFieldFormatter(itemSugarsPer100, false);

	}

	private void setTextFieldFormatter(TextField textField, boolean isInteger) {
		if (isInteger) {
			TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
				String newText = change.getControlNewText();
				if (newText.matches("\\d*")) { // Allow only digits
					return change;
				} else {
					return null; // Reject the change
				}
			});
			textField.setTextFormatter(textFormatter);
		} else {
			TextFormatter<Double> textFormatter = new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
				String newText = change.getControlNewText();
				if (newText.matches("\\d*(\\.\\d{0,1})?")) { // Allow up to 2 decimal places
					try {
						double newValue = Double.parseDouble(newText);
						if (newValue <= 999.99) {
							return change;
						}
					} catch (NumberFormatException ignored) {
					}
				}
				return null; // Reject the change
			});
			textField.setTextFormatter(textFormatter);
		}
	}
}
