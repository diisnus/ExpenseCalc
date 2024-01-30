package QuickActionsPack;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controllers.LoaderClass;
import DBConnection.DBHandler;
import ProductOverview.DataEdit;
import ProductOverview.IdContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RecentlyAddedController implements Initializable {
	private Connection connectDB;
	private DBHandler handler;
	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button close;

	@FXML
	private Label labelEdit;

	@FXML
	private TableView<RecentlyAddedInfo> tableView;

	@FXML
	private TableColumn<RecentlyAddedInfo, String> nameColumn;

	@FXML
	private TableColumn<RecentlyAddedInfo, String> brandColumn;

	@FXML
	private TableColumn<RecentlyAddedInfo, Double> proteinColumn;

	@FXML
	private TableColumn<RecentlyAddedInfo, Integer> caloriesColumn;

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
		tableView.setEditable(false);
		handler = new DBHandler();
		connectDB = handler.getConnection();
		int products[];
		String selectProducts = "SELECT product_id, product_name, product_brand FROM groceryproducts WHERE is_whitelisted = 1 ORDER BY product_id DESC LIMIT 10;";
		String selectMacros = "SELECT calories_per_100g, protein FROM macros WHERE product_id = ?";
		try {
			PreparedStatement selectInfoStatement = connectDB.prepareStatement(selectProducts);
			ResultSet queryOutputInfo = selectInfoStatement.executeQuery();
			ArrayList<RecentlyAddedInfo> recentlyAddedInfoList = new ArrayList<>();

			while (queryOutputInfo.next()) {
				int productid = queryOutputInfo.getInt("product_id");
				String name = queryOutputInfo.getString("product_name");
				String brand = queryOutputInfo.getString("product_brand");

				
				PreparedStatement selectMacrosStatement = connectDB.prepareStatement(selectMacros);
				selectMacrosStatement.setInt(1, productid);
				ResultSet queryOutputInfo2 = selectMacrosStatement.executeQuery(); 

				while (queryOutputInfo2.next()) {
				    int calories = queryOutputInfo2.getInt("calories_per_100g");
				    double protein = queryOutputInfo2.getDouble("protein");
				    recentlyAddedInfoList.add(new RecentlyAddedInfo(name, brand, calories, productid, protein));
				}

			}
			 nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			 brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
			 caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
			 proteinColumn.setCellValueFactory(new PropertyValueFactory<>("protein"));
			 
			    tableView.setFixedCellSize(33);
			    int maxRows = 10;
			    int numRows = Math.min(recentlyAddedInfoList.size(), maxRows);

			    double preferredHeight = 10 * 33 + 30;

			    tableView.setPrefHeight(preferredHeight);
			    ObservableList<RecentlyAddedInfo> list = FXCollections.observableArrayList(recentlyAddedInfoList.subList(0, numRows));
			    System.out.println(list.size());
			    tableView.setItems(list);
			 
		        tableView.setOnMouseClicked(event -> {
		            if (event.getClickCount() == 2) {
		                RecentlyAddedInfo selectedItem = tableView.getSelectionModel().getSelectedItem();
		                int productId = selectedItem.getProductid();
		                IdContainer idcontainer = IdContainer.getInstance();
						idcontainer.setId(productId);
						LoaderClass load = LoaderClass.getInstance();
						load.loadFXML("/ProductOverview/ProductOverview.fxml");
						try {
							Stage stage = (Stage) close.getScene().getWindow();
							stage.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
		            }
		        });
			 
		} catch (SQLException e) {

		}
	}

}
