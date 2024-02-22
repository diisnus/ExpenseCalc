package Favourites;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import Controllers.Container;
import Controllers.LoaderClass;
import DBConnection.DBHandler;
import HboxViewsForItems.InformationContainer;
import HboxViewsForItems.ViewForItemsController;
import ProductOverview.IdContainer;
import ProductOverview.MacroData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FavouritesController implements Initializable {

	@FXML
	private BorderPane borderPane;

	@FXML
	private GridPane gridPane;

	@FXML
	private ScrollPane scrollPaneFavourites;

	@FXML
	private VBox FavouritesVbox;

	@FXML
	private ScrollPane scrollPaneRandomThatYouMightLIke;

	@FXML
	private VBox RandomThatYouMightLIkeVbox;

	private Connection connectDB;
	private DBHandler handler;

	private List<Integer> allwhitelistedProductIds = new ArrayList<>();

	private List<Integer> starredProductIds = new ArrayList<>();
	private List<Integer> suggestedProductIds = new ArrayList<>();

	ArrayList<InformationContainer> informationContainerListFavourites = new ArrayList<>();
	ArrayList<InformationContainer> informationContainerListSuggestions = new ArrayList<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		
		handler = new DBHandler();
		connectDB = handler.getConnection();

		FavouritesVbox.prefWidthProperty().bind(scrollPaneFavourites.widthProperty());
		RandomThatYouMightLIkeVbox.prefWidthProperty().bind(scrollPaneRandomThatYouMightLIke.widthProperty());

		Container container = Container.getInstance();
		int currentUserID = container.getId();

		// accessing ids for favourites

		String selectStarredItemsIds = "SELECT product_id FROM starred WHERE user_id = ? AND is_starred = 1";
		try {
			PreparedStatement getStarredItemsIdsStatement = connectDB.prepareStatement(selectStarredItemsIds);
			getStarredItemsIdsStatement.setInt(1, currentUserID);
			ResultSet queryOutputStarredItems = getStarredItemsIdsStatement.executeQuery();
			while (queryOutputStarredItems.next()) {
				int productId = queryOutputStarredItems.getInt("product_id");
				starredProductIds.add(productId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    // access all whitelisted ids
		String selectTheNumberOfAllRows = "SELECT product_id FROM groceryproducts WHERE is_whitelisted = 1";
		try {
			PreparedStatement statementselectTheNumberOfAllRows = connectDB.prepareStatement(selectTheNumberOfAllRows);
			ResultSet resultSetstatementselectTheNumberOfAllRows = statementselectTheNumberOfAllRows.executeQuery();

			while (resultSetstatementselectTheNumberOfAllRows.next()) {
				int productId = resultSetstatementselectTheNumberOfAllRows.getInt("product_id");
				allwhitelistedProductIds.add(productId);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		Collections.shuffle(allwhitelistedProductIds);

	    if (allwhitelistedProductIds.size() < 10) {
	        suggestedProductIds.addAll(allwhitelistedProductIds);
	        return;
	    }
	    for (int i = 0; i < 10; i++) {
	        suggestedProductIds.add(allwhitelistedProductIds.get(i));
	    }
		
	    getProductInformationAndPopulateVBoxes(starredProductIds, informationContainerListFavourites, FavouritesVbox);
		getProductInformationAndPopulateVBoxes(suggestedProductIds, informationContainerListSuggestions, RandomThatYouMightLIkeVbox);
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

						} catch (SQLException e) {
							e.printStackTrace();
						}

						LoaderClass load = LoaderClass.getInstance();
						load.loadFXML("/ProductOverview/ProductOverview.fxml");
						System.out.print(container1.getProduct_id());
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
		String selectName = "SELECT product_name, product_brand FROM groceryproducts WHERE product_id = ?";
		PreparedStatement selectNameStmt = connectDB.prepareStatement(selectName);
		selectNameStmt.setInt(1, productId);
		ResultSet nameResult = selectNameStmt.executeQuery();

		if (nameResult.next()) {
			String name = nameResult.getString("product_name");
			String brand = nameResult.getString("product_brand");
			container = new InformationContainer(name, brand, calories, protein, carbs, sugar, fiber, fat, sat_fat,
					salt, productId, 0);
		}

		return container;
	}
}