package YourItems;

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

public class YourItemsController implements Initializable {

	@FXML
	private BorderPane borderPane;

	@FXML
	private GridPane gridPane;

	@FXML
	private ScrollPane scrollPaneLeastPopular;

	@FXML
	private ScrollPane scrollPaneYourItems;

	@FXML
	private VBox vboxLeastPopular;

	@FXML
	private VBox vboxYourItems;

	@FXML
	private Label randomUserLabel;

	private Connection connectDB;
	private DBHandler handler;

	private List<Integer> yourProductIds = new ArrayList<>();

	private List<Integer> allUserIdS = new ArrayList<>();
	private List<Integer> suggestedProductIds = new ArrayList<>();

	ArrayList<InformationContainer> informationContainerListYourItems = new ArrayList<>();
	ArrayList<InformationContainer> informationContainerListSuggestions = new ArrayList<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		handler = new DBHandler();
		connectDB = handler.getConnection();

		vboxYourItems.prefWidthProperty().bind(scrollPaneYourItems.widthProperty());
		vboxLeastPopular.prefWidthProperty().bind(scrollPaneLeastPopular.widthProperty());

		Container container = Container.getInstance();
		int currentUserID = container.getId();

		// accessing ids for your items

		String selectYourItemsIds = "SELECT product_id FROM groceryproducts WHERE user_id = ?";
		try {
			PreparedStatement getYourItemsIdsStatement = connectDB.prepareStatement(selectYourItemsIds);
			getYourItemsIdsStatement.setInt(1, currentUserID);
			ResultSet queryOutputYourItems = getYourItemsIdsStatement.executeQuery();
			while (queryOutputYourItems.next()) {
				int productId = queryOutputYourItems.getInt("product_id");
				yourProductIds.add(productId);
			}
			getYourItemsIdsStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// access all users ids
		String selectAllUserIds = "SELECT user_id FROM users";
		try {
			PreparedStatement statementselectAllUserIds = connectDB.prepareStatement(selectAllUserIds);
			ResultSet resultSetstatementselectAllUserIds = statementselectAllUserIds.executeQuery();

			while (resultSetstatementselectAllUserIds.next()) {
				int userids = resultSetstatementselectAllUserIds.getInt("user_id");
				allUserIdS.add(userids);
			}
			resultSetstatementselectAllUserIds.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// random id
		Collections.shuffle(allUserIdS);
		int selectedUserId = allUserIdS.get(1);
		if (selectedUserId == currentUserID) {
			selectedUserId = allUserIdS.get(2);
		}

		// select product ids
		String selectProductsIdsByUser = "SELECT product_id FROM groceryproducts WHERE user_id = ?";
		try {
			PreparedStatement statementselectProductsIdsByUser = connectDB.prepareStatement(selectProductsIdsByUser);
			statementselectProductsIdsByUser.setInt(1, selectedUserId);
			ResultSet resultSetstatementselectProductsIdsByUser = statementselectProductsIdsByUser.executeQuery();

			while (resultSetstatementselectProductsIdsByUser.next()) {
				int product_ids = resultSetstatementselectProductsIdsByUser.getInt("product_id");
				suggestedProductIds.add(product_ids);
			}
			resultSetstatementselectProductsIdsByUser.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// select info for the user
		String selectRandomUserDetails = "SELECT username FROM users WHERE user_id = ?";
		try {
			PreparedStatement statementselectRandomUserDetails = connectDB.prepareStatement(selectRandomUserDetails);
			statementselectRandomUserDetails.setInt(1, selectedUserId);
			ResultSet resultSetstatementselectRandomUserDetails = statementselectRandomUserDetails.executeQuery();

			while (resultSetstatementselectRandomUserDetails.next()) {
				String username = resultSetstatementselectRandomUserDetails.getString("username");
				randomUserLabel.setText(username + "'s items:");
			}
			resultSetstatementselectRandomUserDetails.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		getProductInformationAndPopulateVBoxes(yourProductIds, informationContainerListYourItems, vboxYourItems);
		getProductInformationAndPopulateVBoxes(suggestedProductIds, informationContainerListSuggestions,
				vboxLeastPopular);

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
					// && event.getClickCount() == 2 - za double click
					if (event.getButton().equals(MouseButton.PRIMARY)) {
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
		String selectName = "SELECT product_name, product_brand FROM groceryproducts WHERE product_id = ?";
		PreparedStatement selectNameStmt = connectDB.prepareStatement(selectName);
		selectNameStmt.setInt(1, productId);
		ResultSet nameResult = selectNameStmt.executeQuery();

		if (nameResult.next()) {
			String name = nameResult.getString("product_name");
			String brand = nameResult.getString("product_brand");
			container = new InformationContainer(name, brand, calories, protein, carbs, sugar, fiber, fat, sat_fat,
					salt, productId, 0, 0);
		}
		selectMacrosStmt.close();
		selectNameStmt.close();
		return container;
	}
}