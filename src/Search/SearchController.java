package Search;

import java.util.logging.Logger;
import java.sql.Statement;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DBConnection.DBHandler;

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
	private Button searchButton;

	ObservableList<productSearchModel> productSearchModelObservableList = FXCollections.observableArrayList();

//    double columnPercentage = 100.0 / 6; // Assuming numberOfColumns is the total number of columns in your table
//
//    productNameTableColumn.setPrefWidth(productTableView.widthProperty().multiply(columnPercentage / 100).doubleValue());
//    productBrandTableColumn.setPrefWidth(productTableView.widthProperty().multiply(columnPercentage / 100).doubleValue());
//    productDescriptionTableColumn.setPrefWidth(productTableView.widthProperty().multiply(columnPercentage / 100).doubleValue());
//    productKcalTableColumn.setPrefWidth(productTableView.widthProperty().multiply(columnPercentage / 100).doubleValue());
//    productProteinTableColum.setPrefWidth(productTableView.widthProperty().multiply(columnPercentage / 100).doubleValue());

//                towa trqbwa da e za razdelqne na kolonata proporcionalno sprqmo resize

	private Connection connectDB;
	private DBHandler handler;
	private PreparedStatement pst;

	// productNameTableColumn.maxWidthProperty().bind(productTableView.widthProperty().divide(3));
	@Override
	public void initialize(URL arg0, ResourceBundle resource) {
		handler = new DBHandler();
		connectDB = handler.getConnection();

		String productViewQuery = "SELECT product_id, product_name, product_brand, description, calories_per_100g, protein FROM groceryproducts WHERE is_whitelisted = 1";
		productTableView.setEditable(false);
		try {
			Statement statement = connectDB.createStatement();
			ResultSet queryOutput = statement.executeQuery(productViewQuery);
			while (queryOutput.next()) {
				int queryProductID = queryOutput.getInt("product_id");
				String queryProductName = queryOutput.getString("product_name");
				String queryProductBrand = queryOutput.getString("product_brand");
				String queryProductDescription = queryOutput.getString("description");
				int queryProductCalories_per_100 = queryOutput.getInt("calories_per_100g");
				double queryProductProtein = queryOutput.getDouble("protein");

				productSearchModelObservableList.add(new productSearchModel(queryProductID, queryProductBrand,
						queryProductName, queryProductDescription, queryProductCalories_per_100, queryProductProtein));
			}

			productNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			productBrandTableColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
			productDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
			productKcalTableColumn.setCellValueFactory(new PropertyValueFactory<>("calories_per_100"));
			productProteinTableColum.setCellValueFactory(new PropertyValueFactory<>("protein"));

			productTableView.setItems(productSearchModelObservableList);

			FilteredList<productSearchModel> filteredData = new FilteredList<>(productSearchModelObservableList,
					b -> true);
			searchKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(productSearchModel -> {
					if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {
						return true;
					}
					String keywordSearch = newValue.toLowerCase();
					
					if(productSearchModel.getName().toLowerCase().indexOf(keywordSearch) > -1 ) {
						return true;
					}else if(productSearchModel.getDescription().toLowerCase().indexOf(keywordSearch) > -1) {
						return true;
					}else if(productSearchModel.getBrand().toLowerCase().indexOf(keywordSearch) > -1) {
						return true;
					}else {
						return false;
					}
				});
			});
			
			
			SortedList<productSearchModel> sortedData = new SortedList <>(filteredData);
			sortedData.comparatorProperty().bind(productTableView.comparatorProperty());
			productTableView.setItems(sortedData);
			
		    productTableView.setRowFactory(tv -> {
		        TableRow<productSearchModel> row = new TableRow<>();
		        row.setOnMouseClicked(event -> {
		            if (event.getClickCount() == 2 && !row.isEmpty()) {
		                productSearchModel rowData = row.getItem();
		                int productId = rowData.getProduct_id();
		                System.out.println("Double-clicked on product with ID: " + productId);
		                //loadFXML2("/QuickActionsPack/QuickActions.fxml");
		            }
		        });
		        return row;
		    });

		} catch (SQLException e) {
			Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		}

	}
	
	
	public void loadFXML2(String fxmlFile) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
			Pane newPane = loader.load();
			if (newPane instanceof BorderPane) {
				FXMLLoader borderPaneloader = new FXMLLoader(getClass().getResource(fxmlFile));
				BorderPane newBorderPane = borderPaneloader.load();
				borderPane.getChildren().setAll(newBorderPane);
			} else if (newPane instanceof AnchorPane) {
				FXMLLoader anchorPaneLoader = new FXMLLoader(getClass().getResource(fxmlFile));
				AnchorPane newAnchorPane = anchorPaneLoader.load();

				borderPane.getChildren().setAll(newAnchorPane);
			}if (newPane instanceof GridPane) {
				FXMLLoader gridPaneLoader = new FXMLLoader(getClass().getResource(fxmlFile));
				GridPane newGridPane = gridPaneLoader.load();
				//borderPaneMain.setCenter(newGridPane);
			} else {
				borderPane.getChildren().setAll(newPane);
			}
			Node centerNode1 = borderPane.getChildren().get(0);
			if (centerNode1 != null) {
				AnchorPane.setTopAnchor(centerNode1, 0.0);
				AnchorPane.setRightAnchor(centerNode1, 0.0);
				AnchorPane.setBottomAnchor(centerNode1, 0.0);
				AnchorPane.setLeftAnchor(centerNode1, 0.0);
			} else {
				System.out.println("No valid node set for the AnchorPane.");
			}

		} catch (IOException e) {	
			e.printStackTrace();
		}
	}
}
