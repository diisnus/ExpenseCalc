package Controllers;

import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ProductOverview.IdContainer;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MainPageController  implements Initializable  {

	@FXML
	public GridPane gridPaneHome;

	@FXML
	public BorderPane borderPaneMain;

	@FXML
	public Button homeButton;
	
	@FXML
	private Button searchButton;
	
	@FXML
	private Button settingsButton;
	
	@FXML
	public Button quickActions;

	@FXML
	public Button favouritesButton;

	@FXML
	public Button mostPopularButton;

	@FXML
	public Button priceComparisonsButton;

	@FXML
	public Button yourItemsButton;

	@FXML
	private AreaChart<?, ?> compareButtonChart;

	@FXML
	public AreaChart<?, ?> favouritesButtonChart;

	@FXML
	private LineChart<Number, Number> mostPopularButtonChart;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bindButtonSizeToScene(favouritesButton);
		bindButtonSizeToScene(mostPopularButton);
		bindButtonSizeToScene(yourItemsButton);
		bindButtonSizeToScene(priceComparisonsButton);
		loadFavChartData();
		loadCompareChartData();
		accessUserId();
		LoaderClass load = LoaderClass.getInstance();
		load.setBorderPaneMain(borderPaneMain);
	}


	public int accessUserId() {
		Container container = Container.getInstance();
		System.out.println(container.getId());
		return container.getId();
	}
	
	@FXML
	void favouritesButtonClick() {
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/Favourites/Favourites.fxml");
	}

	@FXML
	void mostPopularButtonClick() {

	}

	@FXML
	void priceComparisonsButtonClick() {

	}

	@FXML
	void yourItemsButtonClick() {
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/YourItems/YourItems.fxml");
	}

	@FXML
	void homeButtonClicked() {
		borderPaneMain.setCenter(gridPaneHome);
	}

	@FXML
	void quickActionsClicked() {
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/QuickActionsPack/QuickActions.fxml");

	}
	
	@FXML
	void searchButtonClicked() {
		LoaderClass load = LoaderClass.getInstance();
		load.loadFXML("/Search/Search.fxml");
	}
	
	@FXML
	void settingsButtonClicked() {
		System.out.println("not set");
	}


	private void loadCompareChartData() {
		XYChart.Series first = new XYChart.Series<>();
		first.getData().add(new XYChart.Data<>(1, 1));
		first.getData().add(new XYChart.Data<>(2, 1));
		first.getData().add(new XYChart.Data<>(3, 6));
		first.getData().add(new XYChart.Data<>(4, 12));
		first.getData().add(new XYChart.Data<>(5, 10));
		first.getData().add(new XYChart.Data<>(6, 7));
		first.getData().add(new XYChart.Data<>(7, 9));
		first.getData().add(new XYChart.Data<>(8, 8));
		first.getData().add(new XYChart.Data<>(9, 9));
		first.getData().add(new XYChart.Data<>(10, 8));

		XYChart.Series second = new XYChart.Series<>();
		second.getData().add(new XYChart.Data<>(1, 3));
		second.getData().add(new XYChart.Data<>(2, 8));
		second.getData().add(new XYChart.Data<>(3, 15));
		second.getData().add(new XYChart.Data<>(4, 15));
		second.getData().add(new XYChart.Data<>(5, 13));
		second.getData().add(new XYChart.Data<>(6, 11));
		second.getData().add(new XYChart.Data<>(7, 8));
		second.getData().add(new XYChart.Data<>(8, 4));
		second.getData().add(new XYChart.Data<>(9, 5));
		second.getData().add(new XYChart.Data<>(10, 5));

		compareButtonChart.getData().addAll(first, second);
	}

	private void loadFavChartData() {
		XYChart.Series first = new XYChart.Series<>();
		first.getData().add(new XYChart.Data<>(2, 1));
		first.getData().add(new XYChart.Data<>(5, 2));
		first.getData().add(new XYChart.Data<>(8, 1));

		XYChart.Series second = new XYChart.Series<>();
		second.getData().add(new XYChart.Data<>(2, 1));
		second.getData().add(new XYChart.Data<>(3, 3));

		XYChart.Series second2 = new XYChart.Series<>();
		second2.getData().add(new XYChart.Data<>(1, 4));
		second2.getData().add(new XYChart.Data<>(3, 3));

		XYChart.Series second3 = new XYChart.Series<>();
		second3.getData().add(new XYChart.Data<>(1, 4));
		second3.getData().add(new XYChart.Data<>(4, 4));

		XYChart.Series second4 = new XYChart.Series<>();
		second4.getData().add(new XYChart.Data<>(4, 4));
		second4.getData().add(new XYChart.Data<>(5, 6));
		second4.getData().add(new XYChart.Data<>(6, 4));
		second4.getData().add(new XYChart.Data<>(9, 4));

		XYChart.Series second5 = new XYChart.Series<>();
		second5.getData().add(new XYChart.Data<>(7, 3));
		second5.getData().add(new XYChart.Data<>(9, 4));

		XYChart.Series second6 = new XYChart.Series<>();
		second6.getData().add(new XYChart.Data<>(7, 3));
		second6.getData().add(new XYChart.Data<>(8, 1));

		favouritesButtonChart.getData().addAll(first, second, second2, second3, second4, second5, second6);
	}

	private void bindButtonSizeToScene(Button button) {
		button.sceneProperty().addListener((observable, oldScene, newScene) -> {
			if (newScene != null) {
				button.prefWidthProperty().bind(newScene.widthProperty().divide(2));
				button.prefHeightProperty().bind(newScene.heightProperty().divide(2));
			}
		});

	}


}
