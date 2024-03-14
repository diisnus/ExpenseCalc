package Compare;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import DBConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
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
	private AreaChart<?, ?> AreaChartComparePrices;

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

	private Connection connectDB;
	private DBHandler handler;

	ObservableList<AllInformationForItemsContainer> AllInformationForItemsContainerObservableList = FXCollections
			.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(75);

		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);

		//gridPane.getColumnConstraints().addAll(col1, col2);
		
		handler = new DBHandler();
		connectDB = handler.getConnection();

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
		
		int productid = 3;
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
				String queryProductBrand = queryOutput.getString("product_brand");
				String queryProductDescription = queryOutput.getString("description");
				String queryProductPricedBy = queryOutput.getString("priced_by");
				int queryProductPopularity= queryOutput.getInt("popularity");

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

						AllInformationForItemsContainerObservableList
								.add(new AllInformationForItemsContainer(queryProductName, queryProductBrand, queryProductDescription, queryProductPricedBy,
										queryProductPopularity, queryProductCalories_per_100, queryProductProtein, queryProductCarbohydrates, 
										queryProductSugar, queryProductFiber, queryProductFat, queryProductSaturated_fat, queryProductSalt));
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
		
		//da adna label koito pishe neshto ot sorta na
		// all prices have been changed to ur current preffered currency that is : primerno BGN
		//mejdu tablicata i areacharta 
		
		//sushto label otdolu, koito da pokazwa koi cwqt liniq za koi item e ili tablica w dqsno, koqto e 25% ot dolniq grid pane cell
	}

}
