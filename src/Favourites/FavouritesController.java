package Favourites;

import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class FavouritesController implements Initializable {
    @FXML
    public Button nnnn;

    @FXML
    public AnchorPane anchorPaneFavs;
    

    @FXML
    void buttonClick() {

    }

    private void bindButtonSizeToScene(Button button, double minWidth, double minHeight, double maxWidth, double maxHeight) {
//        button.setMinWidth(minWidth);
//        button.setMinHeight(minHeight);
//        button.setMaxWidth(maxWidth);
//        button.setMaxHeight(maxHeight);
		button.sceneProperty().addListener((observable, oldScene, newScene) -> {
			if (newScene != null) {
				button.prefWidthProperty().bind(newScene.widthProperty().divide(2));
				button.prefHeightProperty().bind(newScene.heightProperty().divide(2));
			}
		});

	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		bindButtonSizeToScene(nnnn,60,60,600,600);


	}
}

