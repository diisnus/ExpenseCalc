package ProductOverview;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import Controllers.Container;
import Controllers.LoaderClass;
import Controllers.PopUpWindow;
import DBConnection.DBHandler;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import java.text.SimpleDateFormat;

import java.text.ParseException;

public class ProductOverviewController implements Initializable {

    @FXML
    private Button acceptWhitelistButton;
	
	@FXML
	private Button requestWhitelistButton;

	@FXML
	private Button deleteProductButotn;

	@FXML
	private Button addPrice;

	@FXML
	private ImageView imageViewStarred;

	@FXML
	private Button isStarredButton;

	@FXML
	private Button editButton;

	@FXML
	private Label name;

	@FXML
	private Label brand;

	@FXML
	private Label currencyUsed;

	@FXML
	private Label description;

	@FXML
	private Label pricedBy;

	@FXML
	private AreaChart<String, Number> areaChartPrices;

	@FXML
	private BorderPane borderPane;

	@FXML
	private PieChart pieChartMacros;

	@FXML
	private TableView<MacroData> tableView;

	@FXML
	private TableColumn<MacroData, String> macroColumn;

	@FXML
	private TableColumn<MacroData, String> valueColumn;

	@FXML
	void addPriceClick(ActionEvent event) {
		PopUpWindow.showCustomDialog("", "/ProductOverview/UpdatePrice.fxml");
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/ProductOverview/ProductOverview.fxml");

	}

	private Connection connectDB;
	private DBHandler handler;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		handler = new DBHandler();
		connectDB = handler.getConnection();

		tableView.setEditable(false);
		macroColumn.setResizable(false);
		valueColumn.setResizable(false);
		tableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double tableWidth = newWidth.doubleValue();
			double columnWidth = tableWidth / 2.0;

			macroColumn.setPrefWidth(columnWidth);
			valueColumn.setPrefWidth(columnWidth);

		});

		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();

		Container container = Container.getInstance();
		int currentUserID = container.getId();

		// if user is admin check
		int is_admin = 0;
		String adminCheck = "SELECT is_admin FROM users WHERE user_id = ?";
		try {
			PreparedStatement adminCheckStatementStatement = connectDB.prepareStatement(adminCheck);
			adminCheckStatementStatement.setInt(1, currentUserID);
			ResultSet queryOutputIsAdmin = adminCheckStatementStatement.executeQuery();
			while (queryOutputIsAdmin.next()) {
				is_admin = queryOutputIsAdmin.getInt("is_admin");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String selectMacros = "SELECT * FROM macros WHERE product_id = ?";
		// PieChart and macro table inputs
		try {
			PreparedStatement selectMacrosStatement = connectDB.prepareStatement(selectMacros);
			selectMacrosStatement.setInt(1, productid);
			ResultSet queryOutputMacros = selectMacrosStatement.executeQuery();
			ArrayList<PieChart.Data> pieChartDataList = new ArrayList<>();
			ArrayList<MacroData> macroDataList = new ArrayList<>();
			while (queryOutputMacros.next()) {
				int calories = queryOutputMacros.getInt("calories_per_100g");
				double protein = queryOutputMacros.getDouble("protein");
				double carbs = queryOutputMacros.getDouble("carbohydrates");
				double sugar = queryOutputMacros.getDouble("sugar");
				double fiber = queryOutputMacros.getDouble("fiber");
				double fat = queryOutputMacros.getDouble("fat");
				double sat_fat = queryOutputMacros.getDouble("saturated_fat");
				double salt = queryOutputMacros.getDouble("salt");

				pieChartDataList.add(new PieChart.Data("Protein", protein));
				pieChartDataList.add(new PieChart.Data("Carbs", carbs));
				pieChartDataList.add(new PieChart.Data("Sugar", sugar));
				pieChartDataList.add(new PieChart.Data("Fiber", fiber));
				pieChartDataList.add(new PieChart.Data("Fat", fat));
				pieChartDataList.add(new PieChart.Data("Saturated Fat", sat_fat));
				pieChartDataList.add(new PieChart.Data("Salt", salt));

				macroColumn.setCellValueFactory(new PropertyValueFactory<>("macroName"));
				valueColumn.setCellValueFactory(new PropertyValueFactory<>("macroValue"));
				macroDataList.add(new MacroData("Calories", calories));
				macroDataList.add(new MacroData("Protein", protein));
				macroDataList.add(new MacroData("Carbs", carbs));
				macroDataList.add(new MacroData("Sugar", sugar));
				macroDataList.add(new MacroData("Fiber", fiber));
				macroDataList.add(new MacroData("Fat", fat));
				macroDataList.add(new MacroData("Saturated Fat", sat_fat));
				macroDataList.add(new MacroData("Salt", salt));

			}
			tableView.setFixedCellSize(35);
			tableView.prefHeightProperty()
					.bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(35));

			tableView.setMaxHeight(8 * tableView.getFixedCellSize() + 30);

			int maxRows = 8;
			int numRows = Math.min(macroDataList.size(), maxRows);
			tableView.setItems(FXCollections.observableArrayList(macroDataList.subList(0, numRows)));
			pieChartMacros.setData(FXCollections.observableArrayList(pieChartDataList));

		} catch (SQLException e) {

		}

		Container userIdContainer = Container.getInstance();
		int userid = userIdContainer.getId();
		String userPrefCurrency = null;
		// Area chart input
		String selectPrefferedCurrency = "SELECT pref_currency FROM users WHERE user_id = ?";
		String selectPrices = "SELECT price_id, price, purchase_date, currency FROM prices WHERE product_id = ?";
		try {

			// user pref select
			PreparedStatement selectUserPrefStatement = connectDB.prepareStatement(selectPrefferedCurrency);
			selectUserPrefStatement.setInt(1, userid);
			ResultSet queryOutputInfoPref = selectUserPrefStatement.executeQuery();
			while (queryOutputInfoPref.next()) {
				userPrefCurrency = queryOutputInfoPref.getString("pref_currency");
			}

			// prices select
			PreparedStatement selectPriceStatement = connectDB.prepareStatement(selectPrices);
			selectPriceStatement.setInt(1, productid);
			ResultSet queryOutputInfo = selectPriceStatement.executeQuery();

			// inserts in areachart
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			TreeMap<Date, Double> chronologicalData = new TreeMap<>();

			while (queryOutputInfo.next()) {
				String purchase_date = queryOutputInfo.getString("purchase_date");
				String currency = queryOutputInfo.getString("currency");
				double price = queryOutputInfo.getDouble("price");

				Date purchaseDate = dateFormat.parse(purchase_date);
				double convertedAmount = convertCurrency(currency, userPrefCurrency, price);

				chronologicalData.put(purchaseDate, convertedAmount);
			}

			for (var entry : chronologicalData.entrySet()) {
				String formattedDate = dateFormat.format(entry.getKey());
				series.getData().add(new XYChart.Data<>(formattedDate, entry.getValue()));
			}
			areaChartPrices.getData().add(series);
		} catch (SQLException | ParseException e) {

		}

		// product info statement
		String selectInformation = "SELECT product_name, product_brand, description, priced_by, created_by_user, user_id, is_whitelisted, req_whitelist FROM groceryproducts WHERE product_id = ?";
		try {
			PreparedStatement selectInfoStatement = connectDB.prepareStatement(selectInformation);
			selectInfoStatement.setInt(1, productid);
			ResultSet queryOutputInfo = selectInfoStatement.executeQuery();
			while (queryOutputInfo.next()) {
				int user_idThatCreated = queryOutputInfo.getInt("user_id");
				if (currentUserID == user_idThatCreated || is_admin == 1) {
					deleteProductButotn.setVisible(true);
					editButton.setVisible(true);
				} else {
					deleteProductButotn.setVisible(false);
					editButton.setVisible(false);
				}
				int itemIsWhitelisted = queryOutputInfo.getInt("is_whitelisted");
				int itemIsRequestedToWhitelist = queryOutputInfo.getInt("req_whitelist");
				if (currentUserID == user_idThatCreated && itemIsWhitelisted == 0 && itemIsRequestedToWhitelist == 0) {
					requestWhitelistButton.setVisible(true);
				} else {
					requestWhitelistButton.setVisible(false);
				}
				
				if(is_admin ==1 && itemIsRequestedToWhitelist == 1 && itemIsWhitelisted == 0) {
					acceptWhitelistButton.setVisible(true);
				}
				else {
					acceptWhitelistButton.setVisible(false);
				}
				String pr_name = queryOutputInfo.getString("product_name");
				String pr_brand = queryOutputInfo.getString("product_brand");
				String pr_descr = queryOutputInfo.getString("description");
				String pr_pricedby = queryOutputInfo.getString("priced_by");
				int createdbyuser = queryOutputInfo.getInt("created_by_user");
				name.setText("The name of the item you are currently looking at is: " + pr_name + ".");
				name.setWrapText(true);

				brand.setText("It's brand is: " + pr_brand + ".");
				brand.setWrapText(true);

				description.setText("The product is described as: " + pr_descr + ".");
				description.setWrapText(true);
				
				currencyUsed.setText("The currency for the product displayed is: " + userPrefCurrency + ".");
				currencyUsed.setWrapText(true);

				pricedBy.setText("The product is priced " + pr_pricedby + ".");
				pricedBy.setWrapText(true);


			}

		} catch (SQLException e) {

		}

		imageViewStarred.setImage(new Image(getClass().getResourceAsStream("not_starred.png")));
		// check for present rows
		try {
			String selectQuery = "SELECT * FROM starred WHERE user_id = ? AND product_id = ?";
			PreparedStatement selectStatement = connectDB.prepareStatement(selectQuery);
			selectStatement.setInt(1, currentUserID);
			selectStatement.setInt(2, productid);

			ResultSet resultSet = selectStatement.executeQuery();

			if (resultSet.next()) {
				// checkbox starred default select check if row present
				String selectStarred = "SELECT is_starred FROM starred WHERE product_id = ? AND user_id = ?";
				try {

					PreparedStatement selectStarredStatement = connectDB.prepareStatement(selectStarred);
					selectStarredStatement.setInt(1, productid);
					selectStarredStatement.setInt(2, currentUserID);
					ResultSet queryOutputInfo3 = selectStarredStatement.executeQuery();
					int isStarred = 0;
					while (queryOutputInfo3.next()) {
						isStarred = queryOutputInfo3.getInt("is_starred");

					}
					if (isStarred == 1) {
						imageViewStarred.setImage(new Image(getClass().getResourceAsStream("starred.png")));
					} else {
						imageViewStarred.setImage(new Image(getClass().getResourceAsStream("not_starred.png")));
					}
				} catch (SQLException e) {

				}
			} else {
				// insert if row is empty
				String insertStarred = "INSERT INTO starred (user_id, product_id, is_starred) VALUES (?, ?, 0)";
				try {
					PreparedStatement insertStatement = connectDB.prepareStatement(insertStarred);
					insertStatement.setInt(1, currentUserID);
					insertStatement.setInt(2, productid);
					insertStatement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			resultSet.close();
			selectStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void isStarredClick(ActionEvent event) {
		String updateUnstarredToStarred = "UPDATE starred SET is_starred = 1 WHERE product_id = ? AND user_id = ?";
		String updateStarredToUnstarred = "UPDATE starred SET is_starred = 0 WHERE product_id = ? AND user_id = ?";
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();
		Container container = Container.getInstance();
		int currentUserID = container.getId();
		int isStarred = 0;
		String selectStarred = "SELECT is_starred FROM starred WHERE product_id = ? AND user_id = ?";
		try {
			PreparedStatement selectStarredStatement = connectDB.prepareStatement(selectStarred);
			selectStarredStatement.setInt(1, productid);
			selectStarredStatement.setInt(2, currentUserID);
			ResultSet queryOutputInfo3 = selectStarredStatement.executeQuery();
			while (queryOutputInfo3.next()) {
				isStarred = queryOutputInfo3.getInt("is_starred");

			}
			if (isStarred == 1) {
				imageViewStarred.setImage(new Image(getClass().getResourceAsStream("starred.png")));
			} else {
				imageViewStarred.setImage(new Image(getClass().getResourceAsStream("not_starred.png")));
			}
		} catch (SQLException e) {

		}

		if (isStarred == 1) {
			imageViewStarred.setImage(new Image(getClass().getResourceAsStream("not_starred.png")));
			try {
				PreparedStatement selectStarredToUnstarredStatement = connectDB
						.prepareStatement(updateStarredToUnstarred);
				selectStarredToUnstarredStatement.setInt(1, productid);
				selectStarredToUnstarredStatement.setInt(2, currentUserID);
				selectStarredToUnstarredStatement.executeUpdate();
				;
			} catch (SQLException e) {

			}
		} else {
			imageViewStarred.setImage(new Image(getClass().getResourceAsStream("starred.png")));
			try {
				PreparedStatement selectUnstarredToStarredStatement = connectDB
						.prepareStatement(updateUnstarredToStarred);
				selectUnstarredToStarredStatement.setInt(1, productid);
				selectUnstarredToStarredStatement.setInt(2, currentUserID);
				selectUnstarredToStarredStatement.executeUpdate();
				;
			} catch (SQLException e) {

			}
		}
	}

	@FXML
	void deleteProductButotnClick(ActionEvent event) {
		String deleteFromPrices = "DELETE FROM prices WHERE product_id = ?";
		String deleteFromMacros = "DELETE FROM macros WHERE product_id = ?";
		String deleteFromGroceryProducts = "DELETE FROM groceryproducts WHERE product_id = ?";

		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();

		try {
			PreparedStatement deleteFromPricesStatement = connectDB.prepareStatement(deleteFromPrices);
			deleteFromPricesStatement.setInt(1, productid);
			deleteFromPricesStatement.executeUpdate();

			PreparedStatement deleteFromMacrosStatement = connectDB.prepareStatement(deleteFromMacros);
			deleteFromMacrosStatement.setInt(1, productid);
			deleteFromMacrosStatement.executeUpdate();

			PreparedStatement deleteFromGroceryProductsStatement = connectDB
					.prepareStatement(deleteFromGroceryProducts);
			deleteFromGroceryProductsStatement.setInt(1, productid);
			deleteFromGroceryProductsStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		LoaderClass load = LoaderClass.getInstance();
		load.homeFXML();

		PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/DeletedSuccessfully.fxml");

	}

	@FXML
	void requestWhitelistButtonClick(ActionEvent event) {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();

		Container container = Container.getInstance();
		int currentUserID = container.getId();
		String updateReqWhitelistToOne = "UPDATE groceryproducts SET req_whitelist = 1 WHERE product_id = ? AND user_id = ?";

		try {
			PreparedStatement updateReqWhitelistToOneStatement = connectDB.prepareStatement(updateReqWhitelistToOne);
			updateReqWhitelistToOneStatement.setInt(1, productid);
			updateReqWhitelistToOneStatement.setInt(2, currentUserID);
			updateReqWhitelistToOneStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/ProductOverview/ProductOverview.fxml");

		PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/WhitelistRequestedSuccessfully.fxml");

	}
	
	
    @FXML
    void acceptWhitelistButtonClick(ActionEvent event) {
    	IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();

		String updateReqWhitelistToOne = "UPDATE groceryproducts SET is_whitelisted = 1, req_whitelist = 0 WHERE product_id = ?";

		try {
			PreparedStatement updateReqWhitelistToOneStatement = connectDB.prepareStatement(updateReqWhitelistToOne);
			updateReqWhitelistToOneStatement.setInt(1, productid);
			updateReqWhitelistToOneStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/ProductOverview/ProductOverview.fxml");

		PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/WhitelistedSuccessfully.fxml");

	}
    	
    

	@FXML
	void editButtonClick(ActionEvent event) {

		PopUpWindow.showCustomDialog("", "/ProductOverview/EditProductInfo.fxml");

	}

	private static final Map<String, Double> conversionRates;

	static {

		conversionRates = new HashMap<>();
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
		conversionRates.put("HUF", 0.006);// Hungarian Forint
		conversionRates.put("HRK", 0.27); // Croatian Kuna
		conversionRates.put("RON", 0.41); // Romanian Leu
	}

	public static double convertCurrency(String sourceCurrency, String targetCurrency, double amount) {
		if (!conversionRates.containsKey(sourceCurrency) || !conversionRates.containsKey(targetCurrency)) {
			throw new IllegalArgumentException("Invalid currency");
		}
		double sourceRate = conversionRates.get(sourceCurrency);
		double targetRate = conversionRates.get(targetCurrency);

		double convertedAmount = amount * (sourceRate / targetRate);

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(convertedAmount));
	}

}
