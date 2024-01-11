package Search;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import DBConnection.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;




public class SearchController implements Initializable {

	private Connection connection;
	private DBHandler handler;
	private PreparedStatement pst;

    @FXML
    private HBox hboxTop;
    
    @FXML
    private VBox vboxTop;
	
	@FXML
	private TableColumn<?, ?> productBrandTableColumn;

	@FXML
	private TableColumn<?, ?> productDescriptionTableColumn;

	@FXML
	private TableColumn<?, ?> productKcalTableColumn;

	@FXML
	private TableColumn<?, ?> productNameTableColumn;

	@FXML
	private TableColumn<?, ?> productProteinTableColum;

	@FXML
	private TableView<?> productTableView;	
    
    @FXML
    private TextField searchKeyword;
    
    @FXML
    private Button button;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		handler = new DBHandler();

	}

}
