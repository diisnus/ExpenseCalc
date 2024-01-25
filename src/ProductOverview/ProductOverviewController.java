package ProductOverview;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controllers.LoaderClass;
import Controllers.PopUpWindow;
import DBConnection.DBHandler;
import Search.productSearchModel;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class ProductOverviewController implements Initializable {

	@FXML
	private Button addPrice;

	@FXML
	private AreaChart<?, ?> areaChartPrices;

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
		// ako se chupi e tuk pri reload
	}

	private Connection connectDB;
	private DBHandler handler;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableView.setEditable(false);
        macroColumn.setResizable(false);
        valueColumn.setResizable(false);
	    tableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
	        double tableWidth = newWidth.doubleValue();
	        double columnWidth = tableWidth / 2.0;

	        macroColumn.setPrefWidth(columnWidth);
	        valueColumn.setPrefWidth(columnWidth);

	    });
		
		
		IdContainer idcontainer = IdContainer.getInstance();
		int productid = idcontainer.getId();
		handler = new DBHandler();
		connectDB = handler.getConnection();
		String selectMacros = "SELECT * FROM macros WHERE product_id = ?";
		String selectInformation = "SELECT product_name, product_brand, description, priced_by, created_by_user FROM groceryproducts WHERE product_id = ?";
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
			tableView.setFixedCellSize(40); 
			tableView.prefHeightProperty()
					.bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(30)); 


			tableView.setMaxHeight(8 * tableView.getFixedCellSize() + 30); 

			int maxRows = 8;
			int numRows = Math.min(macroDataList.size(), maxRows);
			tableView.setItems(FXCollections.observableArrayList(macroDataList.subList(0, numRows)));
			pieChartMacros.setData(FXCollections.observableArrayList(pieChartDataList));

		} catch (SQLException e) {

		}

		try {
			PreparedStatement selectInfoStatement = connectDB.prepareStatement(selectInformation);
			selectInfoStatement.setInt(1, productid);
			ResultSet queryOutputInfo = selectInfoStatement.executeQuery();
			ArrayList<PieChart.Data> pieChartDataList = new ArrayList<>();
			while (queryOutputInfo.next()) {
				String name = queryOutputInfo.getString("product_name");
				String brand = queryOutputInfo.getString("product_brand");
				String descr = queryOutputInfo.getString("description");
				String pricedby = queryOutputInfo.getString("priced_by");
				int createdbyuser = queryOutputInfo.getInt("created_by_user");

			}

		} catch (SQLException e) {

		}

	}

}
