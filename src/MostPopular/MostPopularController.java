package MostPopular;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Controllers.LoaderClass;
import DBConnection.DBHandler;
import HboxViewsForItems.InformationContainer;
import HboxViewsForItems.ViewForItemsController;
import ProductOverview.IdContainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MostPopularController implements Initializable {

	@FXML
	private BorderPane borderPane;

	@FXML
	private GridPane gridPane;

	@FXML
	private ScrollPane scrollPaneLeastPopular;

	@FXML
	private ScrollPane scrollPaneMostPopular;

	@FXML
	private VBox vboxLeastPopular;

	@FXML
	private VBox vboxMostPopular;

	private List<Integer> mostPopularIds = new ArrayList<>();
	private List<Integer> leastPopularIds = new ArrayList<>();

	ArrayList<InformationContainer> informationContainerListMostPopular = new ArrayList<>();
	ArrayList<InformationContainer> informationContainerListLeastPopular = new ArrayList<>();

	private Connection connectDB;
	private DBHandler handler;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		handler = new DBHandler();
		connectDB = handler.getConnection();

		vboxMostPopular.prefWidthProperty().bind(scrollPaneMostPopular.widthProperty());
		vboxLeastPopular.prefWidthProperty().bind(scrollPaneLeastPopular.widthProperty());

		//accessing most popular ids
		String selectMostPopular = "SELECT product_id FROM groceryproducts ORDER BY popularity DESC LIMIT 15;";

		try {
			PreparedStatement mostPopularItemsIdsStatement = connectDB.prepareStatement(selectMostPopular);
			ResultSet queryOutputMostPopular = mostPopularItemsIdsStatement.executeQuery();
			while (queryOutputMostPopular.next()) {
				int productId = queryOutputMostPopular.getInt("product_id");
				mostPopularIds.add(productId);
			}
			mostPopularItemsIdsStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// accessing least popular ids
		String selectLeastPopular = "SELECT product_id FROM groceryproducts ORDER BY popularity LIMIT 15;";

		try {
			PreparedStatement leastPopularItemsIdsStatement = connectDB.prepareStatement(selectLeastPopular);
			ResultSet queryOutputLeastPopular = leastPopularItemsIdsStatement.executeQuery();
			while (queryOutputLeastPopular.next()) {
				int productId = queryOutputLeastPopular.getInt("product_id");
				leastPopularIds.add(productId);
			}
			leastPopularItemsIdsStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	    getProductInformationAndPopulateVBoxes(mostPopularIds, informationContainerListMostPopular, vboxMostPopular);
	    getProductInformationAndPopulateVBoxes(leastPopularIds, informationContainerListLeastPopular, vboxLeastPopular);

		
	}

	public void getProductInformationAndPopulateVBoxes(List<Integer> productIds,
			ArrayList<InformationContainer> infoContainerList, VBox vbox) {
		for (int productId : productIds) {
			try {
				InformationContainer infoContainer = getProductInformation(productId);
				infoContainerList.add(infoContainer);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		for (InformationContainer container1 : infoContainerList) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/HboxViewsForItems/ViewForItems.fxml"));
			try {
				HBox productHBox = loader.load();
				ViewForItemsController controller = loader.getController();

				controller.setData(container1);
				productHBox.setAlignment(Pos.CENTER);
				productHBox.setOnMouseClicked(event -> {
					//&& event.getClickCount() == 2 - za double click
					if (event.getButton().equals(MouseButton.PRIMARY) ) {
						int productId = container1.getProduct_id();
						IdContainer idcontainer = IdContainer.getInstance();
						idcontainer.setId(productId);

						String updatePopularity = "UPDATE groceryproducts SET popularity = popularity + 1 WHERE product_id = ?";
						try {
							PreparedStatement updatePopularityStatement = connectDB.prepareStatement(updatePopularity);
							updatePopularityStatement.setInt(1, productId);
							updatePopularityStatement.executeUpdate();
							updatePopularityStatement.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}

						LoaderClass load = LoaderClass.getInstance();
						load.loadFXML("/ProductOverview/ProductOverview.fxml");
						System.out.print(container1.getProduct_id());
						try {
							connectDB.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
				vbox.getChildren().add(productHBox);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public InformationContainer getProductInformation(int productId) throws SQLException {
		InformationContainer container = null;

		// get macros
		String selectMacros = "SELECT calories_per_100g, protein, carbohydrates, sugar, fiber, fat, saturated_fat, salt FROM macros WHERE product_id = ?";
		PreparedStatement selectMacrosStmt = connectDB.prepareStatement(selectMacros);
		selectMacrosStmt.setInt(1, productId);
		ResultSet macrosResult = selectMacrosStmt.executeQuery();
		int calories = 0;
		double protein = 0, carbs = 0, sugar = 0, fiber = 0, fat = 0, sat_fat = 0, salt = 0;
		while (macrosResult.next()) {
			calories = macrosResult.getInt("calories_per_100g");
			protein = macrosResult.getDouble("protein");
			carbs = macrosResult.getDouble("carbohydrates");
			sugar = macrosResult.getDouble("sugar");
			fiber = macrosResult.getDouble("fiber");
			fat = macrosResult.getDouble("fat");
			sat_fat = macrosResult.getDouble("saturated_fat");
			salt = macrosResult.getDouble("salt");

		}

		// get name and brand
		String selectName = "SELECT product_name, product_brand, popularity FROM groceryproducts WHERE product_id = ?";
		PreparedStatement selectNameStmt = connectDB.prepareStatement(selectName);
		selectNameStmt.setInt(1, productId);
		ResultSet nameResult = selectNameStmt.executeQuery();

		if (nameResult.next()) {
			int popularity = nameResult.getInt("popularity");
			String name = nameResult.getString("product_name");
			String brand = nameResult.getString("product_brand");
			container = new InformationContainer(name, brand, calories, protein, carbs, sugar, fiber, fat, sat_fat,
					salt, productId, popularity, 1);
		}
		selectMacrosStmt.close();
		selectNameStmt.close();
		return container;

	}
}