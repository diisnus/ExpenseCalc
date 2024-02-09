package Search;

import java.util.logging.Logger;

import Controllers.Container;
import Controllers.LoaderClass;

import java.sql.Statement;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DBConnection.DBHandler;
import ProductOverview.IdContainer;
import ProductOverview.ProductOverviewController;

import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public class SearchController implements Initializable {

	@FXML
	private BorderPane borderPane;

	@FXML
	private HBox hboxTop;

	@FXML
	private VBox vboxTop;

	@FXML
	private TableView<productSearchModel> productTableView;

	@FXML
	private TableColumn<productSearchModel, String> productNameTableColumn;

	@FXML
	private TableColumn<productSearchModel, String> productBrandTableColumn;

	@FXML
	private TableColumn<productSearchModel, String> productDescriptionTableColumn;

	@FXML
	private TableColumn<productSearchModel, Integer> productKcalTableColumn;

	@FXML
	private TableColumn<productSearchModel, Double> productProteinTableColum;

	@FXML
	private TextField searchKeyword;

	@FXML
	private Label labelSearch;

	@FXML
	private ChoiceBox<String> searchByChoiceBox;

	private String[] typesOfSearch = { "By Information", "By Protein", "By Calories" };

	ObservableList<productSearchModel> productSearchModelObservableList = FXCollections.observableArrayList();

	private Connection connectDB;
	private DBHandler handler;

	@Override
	public void initialize(URL arg0, ResourceBundle resource) {
		handler = new DBHandler();
		connectDB = handler.getConnection();
		productTableView.setEditable(false);
		searchByChoiceBox.getItems().addAll(typesOfSearch);

		productTableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double tableWidth = newWidth.doubleValue();
			double columnWidth = tableWidth / 6.0;

			productNameTableColumn.setPrefWidth(columnWidth);
			productBrandTableColumn.setPrefWidth(columnWidth);
			productDescriptionTableColumn.setPrefWidth(columnWidth * 2);
			productKcalTableColumn.setPrefWidth(columnWidth);
			productProteinTableColum.setPrefWidth(columnWidth);
		});

		productDescriptionTableColumn.setCellFactory(tc -> {
			TableCell<productSearchModel, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.setStyle("-fx-fill: rgb(215, 231, 234);");

			text.wrappingWidthProperty().bind(productDescriptionTableColumn.widthProperty());

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
		String productViewQuery = "";
		if (is_admin == 1) {
			productViewQuery = "SELECT product_id, product_name, product_brand, description FROM groceryproducts";
		} else {
			productViewQuery = "SELECT product_id, product_name, product_brand, description FROM groceryproducts WHERE is_whitelisted = 1 OR user_id = ?";
		}
		try {
			ResultSet queryOutput = null;

			if (is_admin == 1) {
				PreparedStatement statement = connectDB.prepareStatement(productViewQuery);
				queryOutput = statement.executeQuery();
			} else {
				PreparedStatement statement = connectDB.prepareStatement(productViewQuery);
				statement.setInt(1, currentUserID); // Set the value for the parameter user_id
				queryOutput = statement.executeQuery();
			}

			while (queryOutput.next()) {
				int queryProductID = queryOutput.getInt("product_id");
				String queryProductName = queryOutput.getString("product_name");
				String queryProductBrand = queryOutput.getString("product_brand");
				String queryProductDescription = queryOutput.getString("description");

				String productViewQueryMacros = "SELECT calories_per_100g, protein FROM macros WHERE product_id=?";
				try (PreparedStatement pstMacros = connectDB.prepareStatement(productViewQueryMacros)) {
					pstMacros.setInt(1, queryProductID);
					ResultSet queryOutputMacros = pstMacros.executeQuery();
					if (queryOutputMacros.next()) {
						int queryProductCalories_per_100 = queryOutputMacros.getInt("calories_per_100g");
						double queryProductProtein = queryOutputMacros.getDouble("protein");
						productSearchModelObservableList
								.add(new productSearchModel(queryProductID, queryProductBrand, queryProductName,
										queryProductDescription, queryProductCalories_per_100, queryProductProtein));
					}
				}
			}

			productNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			productBrandTableColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
			productDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
			productKcalTableColumn.setCellValueFactory(new PropertyValueFactory<>("calories_per_100"));
			productProteinTableColum.setCellValueFactory(new PropertyValueFactory<>("protein"));
		    System.out.println(productSearchModelObservableList.size());

			productTableView.setItems(productSearchModelObservableList);

			FilteredList<productSearchModel> filteredData = new FilteredList<>(productSearchModelObservableList,
					b -> true);
			searchKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(productSearchModel -> {
					if (newValue == null || newValue.isBlank()) {
						return true;
					}
					String keywordSearch = newValue.toLowerCase().trim();

					String searchBy = searchByChoiceBox.getValue();
					if (searchBy == null || searchBy.isEmpty() || searchBy.isBlank()) {

						return productSearchModel.getName().toLowerCase().contains(keywordSearch)
								|| productSearchModel.getDescription().toLowerCase().contains(keywordSearch)
								|| String.valueOf(productSearchModel.getBrand()).toLowerCase().contains(keywordSearch);
					} else {
						if (searchBy.equals("By Information")) {

							return productSearchModel.getName().toLowerCase().contains(keywordSearch)
									|| productSearchModel.getDescription().toLowerCase().contains(keywordSearch)
									|| String.valueOf(productSearchModel.getBrand()).toLowerCase()
											.contains(keywordSearch);

						} else if (searchBy.equals("By Protein")) {

							return Double.toString(productSearchModel.getProtein()).toLowerCase()
									.contains(keywordSearch);

						} else if (searchBy.equals("By Calories")) {

							return Integer.toString(productSearchModel.getCalories_per_100()).toLowerCase()
									.contains(keywordSearch);
						}
					}
					return false;
				});
			});

			SortedList<productSearchModel> sortedData = new SortedList<>(filteredData);
			sortedData.comparatorProperty().bind(productTableView.comparatorProperty());
			productTableView.setItems(sortedData);

			productTableView.setRowFactory(tv -> {
				TableRow<productSearchModel> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && !row.isEmpty()) {
						productSearchModel rowData = row.getItem();
						int productId = rowData.getProduct_id();
						IdContainer idcontainer = IdContainer.getInstance();
						idcontainer.setId(productId);
						System.out.println("Double-clicked on product with ID from container: " + idcontainer.getId());
						String updatePopularity = "UPDATE groceryproducts SET popularity = popularity + 1 WHERE product_id = ?";
						try {
							PreparedStatement updatePopularityStatement = connectDB.prepareStatement(updatePopularity);
							updatePopularityStatement.setInt(1, productId);
							updatePopularityStatement.executeUpdate();

						} catch (SQLException e) {
							e.printStackTrace();
						}
						LoaderClass load = LoaderClass.getInstance();
						load.loadFXML("/ProductOverview/ProductOverview.fxml");
					}
				});
				return row;
			});

		} catch (SQLException e) {
			Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		}

	}

}
