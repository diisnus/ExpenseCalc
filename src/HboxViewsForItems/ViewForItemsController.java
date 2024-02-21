package HboxViewsForItems;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import DBConnection.DBHandler;
import ProductOverview.DataEdit;
import ProductOverview.EditButtonCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class ViewForItemsController extends HBox implements Initializable {

    @FXML
    private HBox HboxMain;

    @FXML
    private PieChart macroPieChart;

    @FXML
    private TableView<InformationForViewTable> tableView;

    @FXML
    private TableColumn<InformationForViewTable, String> nameTableColumn;

    @FXML
    private TableColumn<InformationForViewTable, String> valueTableColumn;

    private Connection connectDB;
    private DBHandler handler;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        handler = new DBHandler();
        connectDB = handler.getConnection();
        
        tableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double tableWidth = newWidth.doubleValue();
			double columnWidth = tableWidth / 2.0;

			nameTableColumn.setPrefWidth(columnWidth);
			valueTableColumn.setPrefWidth(columnWidth);

		});
    }

    public void setData(InformationContainer container1) {
        // Populate PieChart
        macroPieChart.setData(FXCollections.observableArrayList(new PieChart.Data("Protein", container1.getProtein()),
                new PieChart.Data("Carbs", container1.getCarbs()), new PieChart.Data("Sugar", container1.getSugar()),
                new PieChart.Data("Fiber", container1.getFiber()), new PieChart.Data("Fat", container1.getFat()),
                new PieChart.Data("Saturated Fat", container1.getSaturated_fat()),
                new PieChart.Data("Salt", container1.getSalt())));

        String selectInformation = "SELECT product_name, product_brand FROM groceryproducts WHERE product_id = ?";
        String proteinString = String.valueOf(container1.getProtein()); 
        String sugarString = String.valueOf(container1.getSugar()); 
        String caloriesString = String.valueOf(container1.getCalories()); 


        try {
            PreparedStatement selectInfoStatement = connectDB.prepareStatement(selectInformation);
            selectInfoStatement.setInt(1, container1.product_id);
            ResultSet queryOutputInfo = selectInfoStatement.executeQuery();
            ArrayList<InformationForViewTable> infroList = new ArrayList<>();

            while (queryOutputInfo.next()) {
                String pr_name = queryOutputInfo.getString("product_name");
                String pr_brand = queryOutputInfo.getString("product_brand");

                infroList.add(new InformationForViewTable("Name", pr_name));
                infroList.add(new InformationForViewTable("Brand", pr_brand));
                infroList.add(new InformationForViewTable("Protein", proteinString));
                infroList.add(new InformationForViewTable("Sugar", sugarString));
                infroList.add(new InformationForViewTable("Calories", caloriesString));


            }
            nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            valueTableColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

            tableView.setFixedCellSize(33);
            int maxRows = 5;

            int numRows = Math.min(infroList.size(), maxRows);

            double preferredHeight = numRows * 33;

            tableView.setPrefHeight(preferredHeight);
            ObservableList<InformationForViewTable> list = FXCollections
                    .observableArrayList(infroList.subList(0, numRows));
            System.out.println(list.size());
            tableView.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
