package ProductOverview;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import AdminTables.MakeAdminButtonCell;
import Controllers.LoaderClass;
import DBConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EditProductInfoController implements Initializable {
	private Connection connectDB;
	private DBHandler handler;
	@FXML
	private AnchorPane anchorPane;


    @FXML
    private HBox titleBar;
	
    @FXML
    private Button closeWindow;

    
	@FXML
	private Button close;

    @FXML
    private Label labelEdit;
	
	@FXML
	private TableView<DataEdit> editTable;

	@FXML
	private TableColumn<DataEdit, String> nameColumn;

	@FXML
	private TableColumn<DataEdit, String> valueColumn;
	
    @FXML
    private TableColumn<DataEdit, Void> editColumn;

	@FXML
	void closeClick(ActionEvent event) {
		try {
			Stage stage = (Stage) close.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/ProductOverview/ProductOverview.fxml");
	}
	private double xOffset = 0;
	private double yOffset = 0;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		editTable.setEditable(false);
		nameColumn.setResizable(false);
		valueColumn.setResizable(false);
		editColumn.setResizable(false);

		
		titleBar.setOnMousePressed(event -> {
			Stage stage = (Stage) titleBar.getScene().getWindow();
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
			stage.setUserData(new double[] { stage.getX(), stage.getY() });
		});

		titleBar.setOnMouseDragged(event -> {
			Stage stage = (Stage) titleBar.getScene().getWindow();
			double newX = event.getScreenX() - xOffset;
			double newY = event.getScreenY() - yOffset;
			stage.setX(newX);
			stage.setY(newY);
		});
		
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();

		valueColumn.setCellFactory(tc -> {
		    TableCell<DataEdit, String> cell = new TableCell<>();
		    Text text = new Text();
		    cell.setGraphic(text);
		    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
		    text.setStyle("-fx-fill: rgb(215, 231, 234);"); 

		    text.wrappingWidthProperty().bind(valueColumn.widthProperty());

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
		
		handler = new DBHandler();
		connectDB = handler.getConnection();
		String selectInformation = "SELECT product_name, product_brand, description, priced_by, created_by_user FROM groceryproducts WHERE product_id = ?";

		String selectMacros = "SELECT * FROM macros WHERE product_id = ?";
		

		
		try {
	        PreparedStatement selectInfoStatement = connectDB.prepareStatement(selectInformation);
	        selectInfoStatement.setInt(1, productid);
	        ResultSet queryOutputInfo = selectInfoStatement.executeQuery();
		    ArrayList<DataEdit> dataEditList = new ArrayList<>();

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

		    try {
			    PreparedStatement selectCombinedStatement = connectDB.prepareStatement(selectMacros);
			    selectCombinedStatement.setInt(1, productid);
			    ResultSet queryOutputCombined = selectCombinedStatement.executeQuery();
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
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    nameColumn.setCellValueFactory(new PropertyValueFactory<>("macroName"));
		    valueColumn.setCellValueFactory(new PropertyValueFactory<>("macroValue"));
	        editColumn.setCellFactory(column -> new EditButtonCell(editTable));

		    
		    editTable.setFixedCellSize(33);
		    int maxRows = 12;
		    int numRows = Math.min(dataEditList.size(), maxRows);

		    double preferredHeight = 12 * 33 + 30;

		    editTable.setPrefHeight(preferredHeight);
		    ObservableList<DataEdit> list = FXCollections.observableArrayList(dataEditList.subList(0, numRows));
		    System.out.println(list.size());
		    editTable.setItems(list);

		    
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	}

}
