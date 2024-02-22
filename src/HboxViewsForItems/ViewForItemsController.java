package HboxViewsForItems;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

	public ArrayList<InformationForViewTable> infroList = new ArrayList<>();


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableView.setEditable(false);

		nameTableColumn.setResizable(false);
		valueTableColumn.setResizable(false);

		tableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double tableWidth = newWidth.doubleValue();
			double columnWidth = tableWidth / 2.0;

			nameTableColumn.setPrefWidth(columnWidth);
			valueTableColumn.setPrefWidth(columnWidth);

		});
	}

	public void setData(InformationContainer container1) {
		macroPieChart.setData(FXCollections.observableArrayList(new PieChart.Data("Protein", container1.getProtein()),
				new PieChart.Data("Carbs", container1.getCarbs()), new PieChart.Data("Sugar", container1.getSugar()),
				new PieChart.Data("Fiber", container1.getFiber()), new PieChart.Data("Fat", container1.getFat()),
				new PieChart.Data("Saturated Fat", container1.getSaturated_fat()),
				new PieChart.Data("Salt", container1.getSalt())));

		String proteinString = String.valueOf(container1.getProtein());
		String sugarString = String.valueOf(container1.getSugar());
		String caloriesString = String.valueOf(container1.getCalories());
		String pr_name = container1.getName();
		String pr_brand = container1.getBrand();
		String pr_popularity = String.valueOf(container1.getPopularity());
		infroList.add(new InformationForViewTable("Name", pr_name));
		infroList.add(new InformationForViewTable("Brand", pr_brand));
		infroList.add(new InformationForViewTable("Calories", caloriesString));
		infroList.add(new InformationForViewTable("Sugar", sugarString));
		if (container1.getType() == 0) {
			infroList.add(new InformationForViewTable("Protein", proteinString));
		} else {
			infroList.add(new InformationForViewTable("Popularity", pr_popularity));

		}

		nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		valueTableColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

		tableView.setFixedCellSize(33);
		int maxRows = 5;

		int numRows = Math.min(infroList.size(), maxRows);

		double preferredHeight = numRows * 33;

		tableView.setPrefHeight(preferredHeight);
		ObservableList<InformationForViewTable> list = FXCollections.observableArrayList(infroList.subList(0, numRows));
		tableView.setItems(list);

	}

}
