package ProductOverview;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import DBConnection.DBHandler;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditProductInfoController implements Initializable {
	private Connection connectDB;
	private DBHandler handler;
	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button close;

	@FXML
	private TableView<DataEdit> editTable;

	@FXML
	private TableColumn<DataEdit, String> nameColumn;

	@FXML
	private TableColumn<DataEdit, String> valueColumn;

	@FXML
	void closeClick(ActionEvent event) {
		try {
			Stage stage = (Stage) close.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();

		handler = new DBHandler();
		connectDB = handler.getConnection();
		String selectInformation = "SELECT product_name, product_brand, description, priced_by, created_by_user FROM groceryproducts WHERE product_id = ?";

		String selectMacros = "SELECT * FROM macros WHERE product_id = ?";
		// PieChart and macro table inputs
		try {
		    PreparedStatement selectCombinedStatement = connectDB.prepareStatement(selectMacros);
		    selectCombinedStatement.setInt(1, productid);
		    ResultSet queryOutputCombined = selectCombinedStatement.executeQuery();
		    ArrayList<DataEdit> dataEditList = new ArrayList<>();
		    while (queryOutputCombined.next()) {
		        String calories = String.valueOf(queryOutputCombined.getInt("calories_per_100g"));
		        String protein = String.valueOf(queryOutputCombined.getDouble("protein"));
		        String carbs = String.valueOf(queryOutputCombined.getDouble("carbohydrates"));
		        String sugar = String.valueOf(queryOutputCombined.getDouble("sugar"));
		        String fiber = String.valueOf(queryOutputCombined.getDouble("fiber"));
		        String fat = String.valueOf(queryOutputCombined.getDouble("fat"));
		        String sat_fat = String.valueOf(queryOutputCombined.getDouble("saturated_fat"));
		        String salt = String.valueOf(queryOutputCombined.getDouble("salt"));

		        dataEditList.add(new DataEdit("Calories", calories));
		        dataEditList.add(new DataEdit("Protein", protein));
		        dataEditList.add(new DataEdit("Carbs", carbs));
		        dataEditList.add(new DataEdit("Sugar", sugar));
		        dataEditList.add(new DataEdit("Fiber", fiber));
		        dataEditList.add(new DataEdit("Fat", fat));
		        dataEditList.add(new DataEdit("Saturated Fat", sat_fat));
		        dataEditList.add(new DataEdit("Salt", salt));
		    }

		    try {
		        PreparedStatement selectInfoStatement = connectDB.prepareStatement(selectInformation);
		        selectInfoStatement.setInt(1, productid);
		        ResultSet queryOutputInfo = selectInfoStatement.executeQuery();
		        while (queryOutputInfo.next()) {
		            String pr_name = queryOutputInfo.getString("product_name");
		            String pr_brand = queryOutputInfo.getString("product_brand");
		            String pr_descr = queryOutputInfo.getString("description");
		            String pr_pricedby = queryOutputInfo.getString("priced_by");

		            dataEditList.add(new DataEdit("Name", pr_name));
		            dataEditList.add(new DataEdit("Brand", pr_brand));
		            dataEditList.add(new DataEdit("Description", pr_descr));
		            dataEditList.add(new DataEdit("Priced By", pr_pricedby));
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    // Set the cell value factories only once
		    nameColumn.setCellValueFactory(new PropertyValueFactory<>("macroName"));
		    valueColumn.setCellValueFactory(new PropertyValueFactory<>("macroValue"));

		    editTable.setFixedCellSize(35);
		    int maxRows = 8;
		    int numRows = Math.min(dataEditList.size(), maxRows);

		    double preferredHeight = 12 * 35 + 25; // Assuming each row has a fixed height of 35 and adding some
		                                            // extra space

		    // Set the preferred height of the TableView
		    editTable.setPrefHeight(preferredHeight);
		    ObservableList<DataEdit> list = FXCollections.observableArrayList(dataEditList.subList(0, numRows));
		    System.out.println(list.size());
		    editTable.setItems(list);

		} catch (SQLException e) {
		    e.printStackTrace();
		}
	}

}
