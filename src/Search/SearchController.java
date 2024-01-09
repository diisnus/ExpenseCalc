package Search;

import java.awt.TextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import DBConnection.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SearchController implements Initializable {

	private Connection connection;
	private DBHandler handler;
	private PreparedStatement pst;
	
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
    private TextField searchedKeyword;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		handler = new DBHandler();
		
	}
}
