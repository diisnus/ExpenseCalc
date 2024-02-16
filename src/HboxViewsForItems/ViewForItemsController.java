package HboxViewsForItems;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class ViewForItemsController  extends HBox{


	@FXML
    private HBox HboxMain;

    @FXML
    private PieChart macroPieChart;

    @FXML
    private TableView<?> tableView;
    
    @FXML
    private TableColumn<?, ?> nameTableColumn;

    @FXML
    private TableColumn<?, ?> valueTableColumn;



	public void setData(InformationContainer container1) {
		ArrayList<PieChart.Data> pieChartDataList = new ArrayList<>();
		pieChartDataList.add(new PieChart.Data("Protein", container1.protein));
		pieChartDataList.add(new PieChart.Data("Carbs", container1.carbs));
		pieChartDataList.add(new PieChart.Data("Sugar", container1.sugar));
		pieChartDataList.add(new PieChart.Data("Fiber", container1.fiber));
		pieChartDataList.add(new PieChart.Data("Fat", container1.fat));
		pieChartDataList.add(new PieChart.Data("Saturated Fat", container1.saturated_fat));
		pieChartDataList.add(new PieChart.Data("Salt", container1.salt));	
		macroPieChart.setData(FXCollections.observableArrayList(pieChartDataList));

	}

}
