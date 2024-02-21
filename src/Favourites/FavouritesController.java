package Favourites;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Controllers.Container;
import DBConnection.DBHandler;
import HboxViewsForItems.InformationContainer;
import HboxViewsForItems.ViewForItemsController;
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

	private List<Integer> starredProductIds = new ArrayList<>();
	ArrayList<InformationContainer> informationContainerList = new ArrayList<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		handler = new DBHandler();
		connectDB = handler.getConnection();

		FavouritesVbox.setFillWidth(true);
		//FavouritesVbox.prefWidthProperty().bind(scrollPaneFavourites.prefWidthProperty());
//		scrollPaneFavourites.prefWidthProperty().addListener((obs, oldValue, newValue) -> {
//			FavouritesVbox.setMinWidth(newValue.doubleValue());
//		    FavouritesVbox.setMaxWidth(newValue.doubleValue());
//		});
		
		Container container = Container.getInstance();
		int currentUserID = container.getId();

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

		for (int i = 0; i < starredProductIds.size(); i++) {
			int productId = starredProductIds.get(i);
			String selectMacrosForStarredItems = "SELECT calories_per_100g, protein, carbohydrates, sugar, fiber, fat, saturated_fat, salt FROM "
					+ "macros WHERE product_id = ?";
			// accessing macros
			try {
				PreparedStatement selectMacrosForStarredItemsStatement = connectDB
						.prepareStatement(selectMacrosForStarredItems);
				selectMacrosForStarredItemsStatement.setInt(1, productId);
				ResultSet queryOutputMacrosForStarredItems = selectMacrosForStarredItemsStatement.executeQuery();
				int calories = 0;
				double protein = 0, carbs = 0, sugar = 0, fiber = 0, fat = 0, sat_fat = 0, salt = 0;
				while (queryOutputMacrosForStarredItems.next()) {
					calories = queryOutputMacrosForStarredItems.getInt("calories_per_100g");
					protein = queryOutputMacrosForStarredItems.getDouble("protein");
					carbs = queryOutputMacrosForStarredItems.getDouble("carbohydrates");
					sugar = queryOutputMacrosForStarredItems.getDouble("sugar");
					fiber = queryOutputMacrosForStarredItems.getDouble("fiber");
					fat = queryOutputMacrosForStarredItems.getDouble("fat");
					sat_fat = queryOutputMacrosForStarredItems.getDouble("saturated_fat");
					salt = queryOutputMacrosForStarredItems.getDouble("salt");

				}
				String selectNameForStarredItems = "SELECT product_name, product_brand FROM groceryproducts WHERE product_id = ?";

				PreparedStatement selectNameForStarredItemsStatement = connectDB
						.prepareStatement(selectNameForStarredItems);
				selectNameForStarredItemsStatement.setInt(1, productId);
				ResultSet queryOutputNameForStarredItems = selectNameForStarredItemsStatement.executeQuery();
				while (queryOutputNameForStarredItems.next()) {
					String name = queryOutputNameForStarredItems.getString("product_name");
					String brand = queryOutputNameForStarredItems.getString("product_brand");

					informationContainerList.add(new InformationContainer(name, brand, calories, protein, carbs, sugar,
							fiber, fat, sat_fat, salt, productId));

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        FavouritesVbox.setAlignment(Pos.CENTER);
		for (InformationContainer container1 : informationContainerList) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/HboxViewsForItems/ViewForItems.fxml"));
			try {
				HBox productHBox = loader.load();
				ViewForItemsController controller = loader.getController();

				controller.setData(container1);
				productHBox.setAlignment(Pos.CENTER);
				productHBox.setOnMouseClicked(event -> {
					if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
						System.out.print(container1.getProduct_id());
					}
				});
				FavouritesVbox.getChildren().add(productHBox);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
