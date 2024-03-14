package Controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class LoaderClass {
	@FXML
	private BorderPane borderPaneMain;
	
	@FXML
	private GridPane gridPaneHome;

	public void setGridPaneHome(GridPane gridPaneHome) {
		this.gridPaneHome = gridPaneHome;
	}

	public BorderPane getBorderPaneMain() {
		return borderPaneMain;
	}

	public void setBorderPaneMain(BorderPane borderPaneMain) {
		this.borderPaneMain = borderPaneMain;
	}
	
	private static LoaderClass instance;

	public static LoaderClass getInstance() {
		if (instance == null) {
			instance = new LoaderClass();
		}
		return instance;
	}
	
	public void homeFXML() {
		borderPaneMain.setCenter(gridPaneHome);
	}
	public void loadFXML(String fxmlFile) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
			Pane newPane = loader.load();

			borderPaneMain.setCenter(newPane);
			Node centerNode = borderPaneMain.getCenter();

			if (centerNode != null) {
				BorderPane.setAlignment(newPane, javafx.geometry.Pos.CENTER); 

				newPane.setMaxWidth(borderPaneMain.getWidth());
				newPane.setMaxHeight(borderPaneMain.getHeight());

				borderPaneMain.widthProperty()
						.addListener((obs, oldVal, newVal) -> newPane.setMaxWidth(newVal.doubleValue()));
				borderPaneMain.heightProperty()
						.addListener((obs, oldVal, newVal) -> newPane.setMaxHeight(newVal.doubleValue()));

				newPane.autosize();
			} else {
				System.out.println("No valid node set as the center in the BorderPane.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
