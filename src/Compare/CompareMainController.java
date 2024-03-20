package Compare;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.TreeSet;

import Controllers.Container;
import DBConnection.DBHandler;
import ProductOverview.MacroData;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class CompareMainController implements Initializable {

	@FXML
	private BorderPane borderPane;

	@FXML
	private GridPane gridPane;

	@FXML
	private GridPane gridPaneBottom;

	@FXML
	private AreaChart<String, Number> AreaChartComparePrices;

	@FXML
	private TableView<AllInformationForItemsContainer> tableView;

	@FXML
	private TableColumn<AllInformationForItemsContainer, String> NameColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, String> BrandColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, String> DescriptionColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, Integer> PopularityColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, String> PricedByColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, Integer> CaloriesColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, Double> ProteinColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, Double> CarbsColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, Double> SugarColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, Double> FiberColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, Double> FatColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, Double> satFatColumn;

	@FXML
	private TableColumn<AllInformationForItemsContainer, Double> saltColumn;

	// -----------------------------------------------//

	@FXML
	private TableView<HolderContainer> tableViewType;

	@FXML
	private TableColumn<HolderContainer, String> nameColumn;

	@FXML
	private TableColumn<HolderContainer, String> valueColumn;

	private Connection connectDB;
	private DBHandler handler;

	ObservableList<AllInformationForItemsContainer> AllInformationForItemsContainerObservableList = FXCollections
			.observableArrayList();

	private List<Integer> selectedProductIds = new ArrayList<>();
	private List<String> selectedProductNames = new ArrayList<>();

	ArrayList<HolderContainer> holderContainerList = new ArrayList<>();

	ArrayList<String> colors = new ArrayList<String>() {
		{
			add("Yellow");
			add("Green");
			add("Light Blue");
			add("Blue");
			add("Purple");
			add("Pink");
			add("Gray");
			add("Colour");
		}
	};

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		handler = new DBHandler();
		connectDB = handler.getConnection();

		Container container = Container.getInstance();
		int currentUserID = container.getId();

		tableViewType.setEditable(false);

		nameColumn.setResizable(false);
		valueColumn.setResizable(false);

		tableViewType.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double tableWidth = newWidth.doubleValue();
			double columnWidth = tableWidth / 3.0;

			nameColumn.setPrefWidth(columnWidth * 2);
			valueColumn.setPrefWidth(columnWidth);

		});

		tableView.setEditable(false);

		NameColumn.setResizable(false);
		BrandColumn.setResizable(false);
		DescriptionColumn.setResizable(false);
		PopularityColumn.setResizable(false);
		PricedByColumn.setResizable(false);
		CaloriesColumn.setResizable(false);
		ProteinColumn.setResizable(false);
		CarbsColumn.setResizable(false);
		SugarColumn.setResizable(false);
		FiberColumn.setResizable(false);
		FatColumn.setResizable(false);
		satFatColumn.setResizable(false);
		saltColumn.setResizable(false);

		tableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double tableWidth = newWidth.doubleValue();
			double columnWidth = tableWidth / 14.0;

			NameColumn.setPrefWidth(columnWidth);
			BrandColumn.setPrefWidth(columnWidth);
			DescriptionColumn.setPrefWidth(columnWidth * 2);
			PopularityColumn.setPrefWidth(columnWidth);
			PricedByColumn.setPrefWidth(columnWidth);
			CaloriesColumn.setPrefWidth(columnWidth);
			ProteinColumn.setPrefWidth(columnWidth);
			CarbsColumn.setPrefWidth(columnWidth);
			SugarColumn.setPrefWidth(columnWidth);
			FiberColumn.setPrefWidth(columnWidth);
			FatColumn.setPrefWidth(columnWidth);
			satFatColumn.setPrefWidth(columnWidth);
			saltColumn.setPrefWidth(columnWidth);
		});

		DescriptionColumn.setCellFactory(tc -> {
			TableCell<AllInformationForItemsContainer, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.setStyle("-fx-fill: rgb(215, 231, 234);");

			text.wrappingWidthProperty().bind(DescriptionColumn.widthProperty());

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

		NameColumn.setCellFactory(tc -> {
			TableCell<AllInformationForItemsContainer, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.setStyle("-fx-fill: rgb(215, 231, 234);");

			text.wrappingWidthProperty().bind(NameColumn.widthProperty());

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

		PricedByColumn.setCellFactory(tc -> {
			TableCell<AllInformationForItemsContainer, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.setStyle("-fx-fill: rgb(215, 231, 234);");

			text.wrappingWidthProperty().bind(PricedByColumn.widthProperty());

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

		AllInformationForItemsContainer informationSet = AllInformationForItemsContainer.getInstance();
		selectedProductIds.addAll(informationSet.getSelectedToCompareIDs());
		System.out.println(selectedProductIds);

		// big table up input
		int productid;
		for (int i = 0; i < selectedProductIds.size(); i++) {

			productid = selectedProductIds.get(i);
			// input data into to select from table
			String productViewQuery = "SELECT product_name, product_brand, description, priced_by, popularity  FROM groceryproducts "
					+ "WHERE product_id = ?";

			try {
				ResultSet queryOutput = null;

				PreparedStatement statement = connectDB.prepareStatement(productViewQuery);
				statement.setInt(1, productid);
				queryOutput = statement.executeQuery();

				while (queryOutput.next()) {
					String queryProductName = queryOutput.getString("product_name");
					selectedProductNames.add(queryProductName);
					String queryProductBrand = queryOutput.getString("product_brand");
					String queryProductDescription = queryOutput.getString("description");
					String queryProductPricedBy = queryOutput.getString("priced_by");
					int queryProductPopularity = queryOutput.getInt("popularity");

					String productViewQueryMacros = "SELECT calories_per_100g, protein, carbohydrates, sugar, fiber, fat, saturated_fat,"
							+ " salt FROM macros WHERE product_id=?";
					try (PreparedStatement pstMacros = connectDB.prepareStatement(productViewQueryMacros)) {
						pstMacros.setInt(1, productid);
						ResultSet queryOutputMacros = pstMacros.executeQuery();
						if (queryOutputMacros.next()) {
							int queryProductCalories_per_100 = queryOutputMacros.getInt("calories_per_100g");
							double queryProductProtein = queryOutputMacros.getDouble("protein");
							double queryProductCarbohydrates = queryOutputMacros.getDouble("carbohydrates");
							double queryProductSugar = queryOutputMacros.getDouble("sugar");
							double queryProductFiber = queryOutputMacros.getDouble("fiber");
							double queryProductFat = queryOutputMacros.getDouble("fat");
							double queryProductSaturated_fat = queryOutputMacros.getDouble("saturated_fat");
							double queryProductSalt = queryOutputMacros.getDouble("salt");

							AllInformationForItemsContainerObservableList.add(new AllInformationForItemsContainer(
									queryProductName, queryProductBrand, queryProductDescription, queryProductPricedBy,
									queryProductPopularity, queryProductCalories_per_100, queryProductProtein,
									queryProductCarbohydrates, queryProductSugar, queryProductFiber, queryProductFat,
									queryProductSaturated_fat, queryProductSalt));
							pstMacros.close();
						}
					}
				}

				NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
				BrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
				DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
				PopularityColumn.setCellValueFactory(new PropertyValueFactory<>("popularity"));
				PricedByColumn.setCellValueFactory(new PropertyValueFactory<>("pricedBy"));
				CaloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
				ProteinColumn.setCellValueFactory(new PropertyValueFactory<>("protein"));
				CarbsColumn.setCellValueFactory(new PropertyValueFactory<>("carbs"));
				SugarColumn.setCellValueFactory(new PropertyValueFactory<>("sugar"));
				FiberColumn.setCellValueFactory(new PropertyValueFactory<>("fiber"));
				FatColumn.setCellValueFactory(new PropertyValueFactory<>("fat"));
				satFatColumn.setCellValueFactory(new PropertyValueFactory<>("satfat"));
				saltColumn.setCellValueFactory(new PropertyValueFactory<>("salt"));

				tableView.setFixedCellSize(55);
				double preferredHeight = 6 * 55 + 40;

				tableView.setPrefHeight(preferredHeight);		
				
				
				tableView.setItems(AllInformationForItemsContainerObservableList);
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		String userPrefCurrency = "BGN";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		TreeMap<Date, Map<String, Double>> aggregatedData = new TreeMap<>();

		for (int i = 0; i < selectedProductIds.size(); i++) {
		    int productID = selectedProductIds.get(i);

		    // Area chart input
		    String selectPrices = "SELECT  purchase_date FROM prices WHERE product_id = ?";
		    try {
		        // prices select
		        PreparedStatement selectPriceStatement = connectDB.prepareStatement(selectPrices);
		        selectPriceStatement.setInt(1, productID);
		        ResultSet queryOutputInfo = selectPriceStatement.executeQuery();

		        while (queryOutputInfo.next()) {
		            String purchase_date = queryOutputInfo.getString("purchase_date");

		            Date purchaseDate = dateFormat.parse(purchase_date);
		            // Aggregate data for each date
		            if (!aggregatedData.containsKey(purchaseDate)) {
		                aggregatedData.put(purchaseDate, new HashMap<>());
		            }
		            aggregatedData.get(purchaseDate).put(selectedProductNames.get(i), (double) 0);
		        }

		        selectPriceStatement.close();

		    } catch (SQLException | ParseException e) {
		        // Handle exceptions
		    }
		}

		// Plot aggregated data
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		for (var entry : aggregatedData.entrySet()) {
		    String formattedDate = dateFormat.format(entry.getKey());
		    double totalAmount = entry.getValue().values().stream().mapToDouble(Double::doubleValue).sum();
		    series1.getData().add(new XYChart.Data<>(formattedDate, totalAmount));
		}
		AreaChartComparePrices.getData().add(series1);

		
	
		int productID;
		for (int i = 0; i < selectedProductIds.size(); i++) {

			productID = selectedProductIds.get(i);
			// Area chart input
			String selectPrefferedCurrency = "SELECT pref_currency FROM users WHERE user_id = ?";
			String selectPrices = "SELECT price_id, price, purchase_date, currency FROM prices WHERE product_id = ?";
			try {

				// user pref select
				PreparedStatement selectUserPrefStatement = connectDB.prepareStatement(selectPrefferedCurrency);
				selectUserPrefStatement.setInt(1, currentUserID);
				ResultSet queryOutputInfoPref = selectUserPrefStatement.executeQuery();
				while (queryOutputInfoPref.next()) {
					userPrefCurrency = queryOutputInfoPref.getString("pref_currency");

				}

				// prices select
				PreparedStatement selectPriceStatement = connectDB.prepareStatement(selectPrices);
				selectPriceStatement.setInt(1, productID);
				ResultSet queryOutputInfo = selectPriceStatement.executeQuery();

				// inserts in areachart
				XYChart.Series<String, Number> series = new XYChart.Series<>();
				series.setName(selectedProductNames.get(i));
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
				AreaChartComparePrices.getData().add(series);

				selectUserPrefStatement.close();
				selectPriceStatement.close();

			} catch (SQLException | ParseException e) {

			}
		}

		// input for the side colour table and current currency
		holderContainerList.add(new HolderContainer(userPrefCurrency.toString(), "Current viewing currency"));
		for (int i = 0; i < selectedProductIds.size(); i++) {
			holderContainerList.add(new HolderContainer(colors.get(i), selectedProductNames.get(i)));
		}
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("macroValue"));
		valueColumn.setCellValueFactory(new PropertyValueFactory<>("macroName"));
		tableViewType.setFixedCellSize(35);
		tableViewType.prefHeightProperty()
				.bind(Bindings.size(tableViewType.getItems()).multiply(tableViewType.getFixedCellSize()).add(50));

		tableViewType.setMaxHeight(9 * tableViewType.getFixedCellSize() + 30);

		int maxRows = 9;
		tableViewType.setItems(FXCollections.observableArrayList(holderContainerList));
		int numRows = Math.min(holderContainerList.size(), maxRows);
		tableViewType.setItems(FXCollections.observableArrayList(holderContainerList.subList(0, numRows)));
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
