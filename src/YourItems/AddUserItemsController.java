package YourItems;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.Container;
import Controllers.LoaderClass;
import Controllers.PopUpWindow;
import DBConnection.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddUserItemsController implements Initializable {

	@FXML
	private Label brandLabel;

	@FXML
	private Label calloriesLabel;

	@FXML
	private Label carbsLabel;

	@FXML
	private Label descriptionLabel;

	@FXML
	private Label fatsLabel;

	@FXML
	private Label fibersLabel;

	@FXML
	private Label nameLabel;

	@FXML
	private Label pricedByLabel;

	@FXML
	private Label proteinLabel;

	@FXML
	private Label saltLabel;

	@FXML
	private Label saturatedFatLabel;

	@FXML
	private Label sugarsLabel;

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
		// System.out.println(container.getId());
		return container.getId();
	}

	@FXML
	void addItemButtonClick(ActionEvent event) {
		int userid = accessUserId();
		String itemname = itemNameTextField.getText();

		String itemdescr = itemDescriptionTextField.getText();
		if (itemdescr.isBlank() || itemname.isEmpty()) {
			itemdescr.equals("No description mentioned");
		}
		String itembrand = ItemBrandTextField.getText();
		if (itembrand.isBlank() || itemname.isEmpty()) {
			itembrand.equals("No brand specified");
		}
		String choiceper = itemsPricePer.getValue();
		int caloriesValue = Integer.parseInt(itemCaloriesPer100.getText());
		double proteinValue = Double.parseDouble(itemProteinPer100.getText());
		double carbs = Double.parseDouble(itemCarbohydratesPer100.getText());
		double sugars = Double.parseDouble(itemSugarsPer100.getText());
		double fibers = Double.parseDouble(itemFibersPer100.getText());
		double fats = Double.parseDouble(itemFatsPer100.getText());
		double satfats = Double.parseDouble(itemSaturatedFatsPer100.getText());
		double salts = Double.parseDouble(itemSaltPer100.getText());

		if (itemname.isBlank() || itemname.isEmpty() || choiceper == null) {
			PopUpWindow.showCustomDialog("", "/FXML/ErrorAddItems.fxml");

		} else {

			connection = handler.getConnection();
			String insert = "INSERT INTO groceryproducts(user_id, product_name, product_brand, description, priced_by, is_whitelisted, created_by_user) VALUES (?, ?, ?, ?, ?, ?, ?)";
			String insertMacros = "INSERT INTO macros(product_id, calories_per_100g, protein, carbohydrates, sugar, fiber, fat, saturated_fat, salt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try {

				PreparedStatement insertStatement = connection.prepareStatement(insert,
						PreparedStatement.RETURN_GENERATED_KEYS);
				PreparedStatement insertStatementMacros = connection.prepareStatement(insertMacros);
				insertStatement.setInt(1, userid);
				insertStatement.setString(2, itemname);
				insertStatement.setString(3, itembrand);
				insertStatement.setString(4, itemdescr);
				insertStatement.setString(5, choiceper);
				insertStatement.setInt(6, 0); // whitelisted = 1
				insertStatement.setInt(7, 1); // created by user = 1

				int rowsAffected = insertStatement.executeUpdate();
				if (rowsAffected > 0) {
					ResultSet generatedKeys = insertStatement.getGeneratedKeys();
					if (generatedKeys.next()) {
						int currentProduct_id = generatedKeys.getInt(1);
						System.out.println(currentProduct_id);

						insertStatementMacros.setInt(1, currentProduct_id);
						insertStatementMacros.setInt(2, caloriesValue);
						insertStatementMacros.setDouble(3, proteinValue);
						insertStatementMacros.setDouble(4, carbs);
						insertStatementMacros.setDouble(5, sugars);
						insertStatementMacros.setDouble(6, fibers);
						insertStatementMacros.setDouble(7, fats);
						insertStatementMacros.setDouble(8, satfats);
						insertStatementMacros.setDouble(9, salts);
						int macrosRowsAffected = insertStatementMacros.executeUpdate();
						if (macrosRowsAffected > 0) {
							System.out.println("Data inserted successfully");
						} else {
							System.out.println("Insertion into macros failed");
						}
					}
					generatedKeys.close();
					System.out.println("Data inserted successfully");
					PopUpWindow.showCustomDialog("", "/FXML/InsertedSuccessfully.fxml");
				} else {
					System.out.println("Insertion failed");
				}

				insertStatement.close();
				connection.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
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
				if (newText.matches("\\d*(\\.\\d{0,1})?")) {
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
}