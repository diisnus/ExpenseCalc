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

	    String selectMacros = "SELECT * FROM macros WHERE product_id = ?";
	    // PieChart and macro table inputs
	    try {
	        PreparedStatement selectMacrosStatement = connectDB.prepareStatement(selectMacros);
	        selectMacrosStatement.setInt(1, productid);
	        ResultSet queryOutputMacros = selectMacrosStatement.executeQuery();
	        ArrayList<DataEdit> dataEditList = new ArrayList<>();
	        while (queryOutputMacros.next()) {
	            int calories = queryOutputMacros.getInt("calories_per_100g");
	            double protein = queryOutputMacros.getDouble("protein");
	            double carbs = queryOutputMacros.getDouble("carbohydrates");
	            double sugar = queryOutputMacros.getDouble("sugar");
	            double fiber = queryOutputMacros.getDouble("fiber");
	            double fat = queryOutputMacros.getDouble("fat");
	            double sat_fat = queryOutputMacros.getDouble("saturated_fat");
	            double salt = queryOutputMacros.getDouble("salt");
	            
	            nameColumn.setCellValueFactory(new PropertyValueFactory<>("macroName"));
				valueColumn.setCellValueFactory(new PropertyValueFactory<>("macroValue"));
	            dataEditList.add(new DataEdit("Calories", calories));
	            dataEditList.add(new DataEdit("Protein", protein));
	            dataEditList.add(new DataEdit("Carbs", carbs));
	            dataEditList.add(new DataEdit("Sugar", sugar));
	            dataEditList.add(new DataEdit("Fiber", fiber));
	            dataEditList.add(new DataEdit("Fat", fat));
	            dataEditList.add(new DataEdit("Saturated Fat", sat_fat));
	            dataEditList.add(new DataEdit("Salt", salt));
	        }
	        editTable.setFixedCellSize(35);
			editTable.prefHeightProperty()
					.bind(Bindings.size(editTable.getItems()).multiply(editTable.getFixedCellSize()).add(35));

			editTable.setMaxHeight(8 * editTable.getFixedCellSize() + 30);	        

	        int maxRows = 8;
	        int numRows = Math.min(dataEditList.size(), maxRows);
	        ObservableList<DataEdit> list = FXCollections.observableArrayList(dataEditList.subList(0, numRows));
	        System.out.println(list.size());
	        editTable.setItems(list);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


}
