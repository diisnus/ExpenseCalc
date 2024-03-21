package Compare;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import Controllers.Container;
import Controllers.LoaderClass;
import Controllers.PopUpWindow;
import DBConnection.DBHandler;
import ProductOverview.IdContainer;
import Search.productSearchModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class CompareSelectController implements Initializable {

	@FXML
	private BorderPane borderPane;

	@FXML
	private GridPane gridPaneMain;

	@FXML
	private GridPane gridPaneSide;

	@FXML
	private Button CompareButton;

	@FXML
	private Label labelTop1;

	@FXML
	private Label labelTop2;

	@FXML
	private Label labelMid2;

    @FXML
    private Label labelMid3;
	
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
	private TableView<TableInformationContainer> tableViewSelected;

	@FXML
	private TableColumn<TableInformationContainer, String> selectedBrandColumn;

	@FXML
	private TableColumn<TableInformationContainer, Integer> selectedCaloriesColumn;

	@FXML
	private TableColumn<TableInformationContainer, String> selectedNameColumn;

	@FXML
	private TableColumn<TableInformationContainer, Double> selectedProteinColumn;

	@FXML
	private TableColumn<TableInformationContainer, Double> selectedSugarsColumn;

	// ------------------------------------------------------------//

	private Connection connectDB;
	private DBHandler handler;

	// to select
	ObservableList<TableInformationContainer> tableInformationContainerToSelectObservableList = FXCollections
			.observableArrayList();
	// selected
	ObservableList<TableInformationContainer> tableInformationContainerSelectEDObservableList = FXCollections
			.observableArrayList();

	private List<Integer> selectedProductIds = new ArrayList<>();

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

		toSelectNameColumn.setCellFactory(tc -> {
			TableCell<TableInformationContainer, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.setStyle("-fx-fill: rgb(215, 231, 234);");

			text.wrappingWidthProperty().bind(toSelectNameColumn.widthProperty());

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

		// input data into to select from table
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

						tableInformationContainerToSelectObservableList
								.add(new TableInformationContainer(queryProductID, queryProductCalories_per_100,
										queryProductProtein, queryProductSugar, queryProductName, queryProductBrand));
					}
				}
			}

			toSelectNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			toSelectBrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
			toSelectSugarColumn.setCellValueFactory(new PropertyValueFactory<>("sugar"));
			toSelectCaloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
			toSelectProteinColumn.setCellValueFactory(new PropertyValueFactory<>("protein"));

			tableViewSelectFrom.setItems(tableInformationContainerToSelectObservableList);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		selectedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		selectedBrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
		selectedSugarsColumn.setCellValueFactory(new PropertyValueFactory<>("sugar"));
		selectedCaloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
		selectedProteinColumn.setCellValueFactory(new PropertyValueFactory<>("protein"));

		// Sort both tables by name
		Comparator<TableInformationContainer> compareByName = (o1, o2) -> o1.getName()
				.compareToIgnoreCase(o2.getName());
		tableInformationContainerToSelectObservableList.sort(compareByName);

		tableViewSelectFrom.setRowFactory(tv -> {
			TableRow<TableInformationContainer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && !row.isEmpty()) {

					TableInformationContainer rowdata = row.getItem();

					// maximum itemi selectnati
					if (tableInformationContainerSelectEDObservableList.size() < 8) {
						// transfer item to selected table
						tableInformationContainerSelectEDObservableList.add(rowdata);
						tableInformationContainerToSelectObservableList.remove(rowdata);

						// sort
						Comparator<TableInformationContainer> compareBYName = (o1, o2) -> o1.getName()
								.compareToIgnoreCase(o2.getName());
						tableInformationContainerToSelectObservableList.sort(compareBYName);
						tableInformationContainerSelectEDObservableList.sort(compareBYName);

						tableViewSelectFrom.setItems(tableInformationContainerToSelectObservableList);
						tableViewSelected.setItems(tableInformationContainerSelectEDObservableList);

						int productId = rowdata.getProduct_id();
						selectedProductIds.add(productId);

					} else {
						PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorMaximumItemsSelectedReached.fxml");

					}

				}
			});
			return row;
		});

		// ------------------------------------------------------------//

		tableViewSelected.setRowFactory(tv -> {
			TableRow<TableInformationContainer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && !row.isEmpty()) {

					TableInformationContainer rowdata = row.getItem();

					// transfger item to toselect table
					tableInformationContainerToSelectObservableList.add(rowdata);
					tableInformationContainerSelectEDObservableList.remove(rowdata);

					// sort
					Comparator<TableInformationContainer> compareBYName = (o1, o2) -> o1.getName()
							.compareToIgnoreCase(o2.getName());
					tableInformationContainerToSelectObservableList.sort(compareBYName);
					tableInformationContainerSelectEDObservableList.sort(compareBYName);

					tableViewSelectFrom.setItems(tableInformationContainerToSelectObservableList);
					tableViewSelected.setItems(tableInformationContainerSelectEDObservableList);
					// System.out.println(selectedProductIds);

					try {
						int productId = rowdata.getProduct_id();
						if (selectedProductIds.contains(productId)) {
							int index = selectedProductIds.indexOf(productId);
							selectedProductIds.remove(index);
						}
					} catch (Exception e) {

					}
					// System.out.println(selectedProductIds);

				}
			});
			return row;
		});
	}

	@FXML
	void CompareButtonClick() {
		if (selectedProductIds.isEmpty()) {
			PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorNoSelectedItems.fxml");

		} else {
			AllInformationForItemsContainer informationSet = AllInformationForItemsContainer.getInstance();

			informationSet.setSelectedToCompareIDs(selectedProductIds);
			try {
				connectDB.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			LoaderClass load = LoaderClass.getInstance();
			load.loadFXML("/Compare/CompareMain.fxml");

		}
	}
}
