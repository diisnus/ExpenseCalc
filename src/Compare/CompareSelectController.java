package Compare;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Controllers.Container;
import DBConnection.DBHandler;
import Search.productSearchModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class CompareSelectController implements Initializable{

	@FXML
	private BorderPane borderPane;

	@FXML
	private GridPane gridPaneMain;

	@FXML
	private GridPane gridPaneSide;

	@FXML
	private TableView<TableInformationContainer> tableViewSelectFrom;

	@FXML
	private TableColumn<TableInformationContainer, String> toSelectBrandColumn;

	@FXML
	private TableColumn<TableInformationContainer, Integer> toSelectCaloriesColumn;

	@FXML
	private TableColumn<TableInformationContainer, String> toSelectNameColumn;

	@FXML
	private TableColumn<TableInformationContainer, Double> toSelectProteinColumn;

	@FXML
	private TableColumn<TableInformationContainer, Double> toSelectSugarColumn;
	// ------------------------------------------------------------//
	@FXML
	private TableView<?> tableViewSelected;

	@FXML
	private TableColumn<?, ?> selectedBrandColumn;

	@FXML
	private TableColumn<?, ?> selectedCaloriesColumn;

	@FXML
	private TableColumn<?, ?> selectedNameColumn;

	@FXML
	private TableColumn<?, ?> selectedProteinColumn;

	@FXML
	private TableColumn<?, ?> selectedSugarsColumn;


	private Connection connectDB;
	private DBHandler handler;
	
	ObservableList<TableInformationContainer> tableInformationContainerObservableList = FXCollections.observableArrayList();

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		handler = new DBHandler();
		connectDB = handler.getConnection();
		
		tableViewSelectFrom.setEditable(false);
		
		toSelectBrandColumn.setResizable(false);
		toSelectCaloriesColumn.setResizable(false);
		toSelectNameColumn.setResizable(false);
		toSelectProteinColumn.setResizable(false);
		toSelectSugarColumn.setResizable(false);
		
		tableViewSelected.setEditable(false);
		
		selectedBrandColumn.setResizable(false);
		selectedCaloriesColumn.setResizable(false);
		selectedNameColumn.setResizable(false);
		selectedProteinColumn.setResizable(false);
		selectedSugarsColumn.setResizable(false);
		
		
		tableViewSelectFrom.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double tableWidth = newWidth.doubleValue();
			double columnWidth = tableWidth / 5.0;

			toSelectBrandColumn.setPrefWidth(columnWidth);
			toSelectCaloriesColumn.setPrefWidth(columnWidth);
			toSelectNameColumn.setPrefWidth(columnWidth);
			toSelectProteinColumn.setPrefWidth(columnWidth);
			toSelectSugarColumn.setPrefWidth(columnWidth);
		});
		
		tableViewSelected.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double tableWidth = newWidth.doubleValue();
			double columnWidth = tableWidth / 5.0;

			selectedBrandColumn.setPrefWidth(columnWidth);
			selectedCaloriesColumn.setPrefWidth(columnWidth);
			selectedNameColumn.setPrefWidth(columnWidth);
			selectedProteinColumn.setPrefWidth(columnWidth);
			selectedSugarsColumn.setPrefWidth(columnWidth);
		});
		
		
		Container container = Container.getInstance();
		int currentUserID = container.getId();
		
		int is_admin = 0;
		String adminCheck = "SELECT is_admin FROM users WHERE user_id = ?";
		try {
			PreparedStatement adminCheckStatementStatement = connectDB.prepareStatement(adminCheck);
			adminCheckStatementStatement.setInt(1, currentUserID);
			ResultSet queryOutputIsAdmin = adminCheckStatementStatement.executeQuery();
			while (queryOutputIsAdmin.next()) {
				is_admin = queryOutputIsAdmin.getInt("is_admin");
			}
			adminCheckStatementStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//input data into to select from table
		String productViewQuery = "";
		if (is_admin == 1) {
			productViewQuery = "SELECT product_id, product_name, product_brand FROM groceryproducts";
		} else {
			productViewQuery = "SELECT product_id, product_name, product_brand FROM groceryproducts WHERE is_whitelisted = 1 OR user_id = ?";
		}
		try {
			
			ResultSet queryOutput = null;

			if (is_admin == 1) {
				PreparedStatement statement = connectDB.prepareStatement(productViewQuery);
				queryOutput = statement.executeQuery();
			} else {
				PreparedStatement statement = connectDB.prepareStatement(productViewQuery);
				statement.setInt(1, currentUserID); 
				queryOutput = statement.executeQuery();
			}
			while (queryOutput.next()) {
				int queryProductID = queryOutput.getInt("product_id");
				String queryProductName = queryOutput.getString("product_name");
				String queryProductBrand = queryOutput.getString("product_brand");

				String productViewQueryMacros = "SELECT calories_per_100g, protein, sugar FROM macros WHERE product_id=?";
				try (PreparedStatement pstMacros = connectDB.prepareStatement(productViewQueryMacros)) {
					pstMacros.setInt(1, queryProductID);
					ResultSet queryOutputMacros = pstMacros.executeQuery();
					if (queryOutputMacros.next()) {
						int queryProductCalories_per_100 = queryOutputMacros.getInt("calories_per_100g");
						double queryProductProtein = queryOutputMacros.getDouble("protein");
						double queryProductSugar = queryOutputMacros.getDouble("sugar");

						tableInformationContainerObservableList
								.add(new TableInformationContainer(queryProductID, queryProductCalories_per_100, queryProductProtein,
										queryProductSugar, queryProductName, queryProductBrand));
					}
				}
			}

			toSelectNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			toSelectBrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
			toSelectSugarColumn.setCellValueFactory(new PropertyValueFactory<>("sugar"));
			toSelectCaloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
			toSelectProteinColumn.setCellValueFactory(new PropertyValueFactory<>("protein"));
		    System.out.println(tableInformationContainerObservableList.size());

		    tableViewSelectFrom.setItems(tableInformationContainerObservableList);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

}
