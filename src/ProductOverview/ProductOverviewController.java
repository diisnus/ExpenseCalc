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
import Search.productSearchModel;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;

import java.text.ParseException;

public class ProductOverviewController implements Initializable {

	
	
    @FXML
    private Button acceptWhitelistButton;
	
	@FXML
    private Button removeWhitelistButton;
	
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
    private TableView<ItemInfoContainer> tableView2;

	@FXML
	private TableColumn<ItemInfoContainer, String> informationColumn;

	@FXML
	private TableColumn<ItemInfoContainer, String> nameOfFieldColumn;
	
	
	
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
		
		tableView2.setEditable(false);
		tableView.setEditable(false);
		
		macroColumn.setResizable(false);
		valueColumn.setResizable(false);
		
		informationColumn.setResizable(false);
		nameOfFieldColumn.setResizable(false);
		
		tableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double tableWidth = newWidth.doubleValue();
			double columnWidth = tableWidth / 2.0;

			macroColumn.setPrefWidth(columnWidth);
			valueColumn.setPrefWidth(columnWidth);

		});
		
		tableView2.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double tableWidth = newWidth.doubleValue();
			double columnWidth = tableWidth / 4.0;

			informationColumn.setPrefWidth(columnWidth*3);
			nameOfFieldColumn.setPrefWidth(columnWidth);

		});

		
		informationColumn.setCellFactory(tc -> {
		    TableCell<ItemInfoContainer, String> cell = new TableCell<>();
		    Text text = new Text();
		    cell.setGraphic(text);
		    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
		    text.setStyle("-fx-fill: rgb(215, 231, 234);"); 

		    text.wrappingWidthProperty().bind(informationColumn.widthProperty());

		    cell.setOnMouseEntered(event -> {
		        String cellText = cell.getItem();
		        if (cellText != null && !cellText.isEmpty()) {
		            Tooltip tooltip = new Tooltip(cellText);
		            Tooltip.install(text, tooltip);
		        }
		    });

		    cell.setOnMouseExited(event -> Tooltip.uninstall(text, null));

		    text.textProperty().bind(cell.itemProperty());

		    return cell;
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
			ArrayList<ItemInfoContainer> infoDataList = new ArrayList<>();

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
				if(is_admin ==1 && itemIsWhitelisted == 1) {
					removeWhitelistButton.setVisible(true);
				}else {
					removeWhitelistButton.setVisible(false);
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
				

				nameOfFieldColumn.setCellValueFactory(new PropertyValueFactory<>("nameValue"));
				informationColumn.setCellValueFactory(new PropertyValueFactory<>("valValue"));
				infoDataList.add(new ItemInfoContainer("Name:", pr_name));
				infoDataList.add(new ItemInfoContainer("Brand:", pr_brand));
				infoDataList.add(new ItemInfoContainer("Description:", pr_descr));
				infoDataList.add(new ItemInfoContainer("Priced By:", pr_pricedby));
				infoDataList.add(new ItemInfoContainer("Currency:", userPrefCurrency));
				

			}
			tableView2.setFixedCellSize(55);
			tableView2.prefHeightProperty()
					.bind(Bindings.size(tableView2.getItems()).multiply(tableView2.getFixedCellSize()).add(55));

			tableView2.setMaxHeight(5 * tableView2.getFixedCellSize() + 30);

			int maxRows = 5;
			int numRows = Math.min(infoDataList.size(), maxRows);
			tableView2.setItems(FXCollections.observableArrayList(infoDataList.subList(0, numRows)));
			
			
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
    void removeWhitelistButtonClick() {
    	IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();

		String updateReqWhitelistToOne = "UPDATE groceryproducts SET is_whitelisted = 0, req_whitelist = 0 WHERE product_id = ?";

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
