package AdminTables;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import DBConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UsersListController implements Initializable {

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button close;

	@FXML
	private Label labelEdit;

	@FXML
	private TableView<UserListInfo> tableView;

	@FXML
	private TableColumn<UserListInfo, String> usernameColumn;

	@FXML
	private TableColumn<UserListInfo, String> emailColumn;

	@FXML
	private TableColumn<UserListInfo, Integer> adminColumn;

	@FXML
	private TableColumn<UserListInfo, Void> deleteColumn;

	@FXML
	private TableColumn<UserListInfo, Void> makeAdminColumn;

	private Connection connectDB;
	private DBHandler handler;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    tableView.setEditable(false);
	    handler = new DBHandler();
	    connectDB = handler.getConnection();
	    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
	    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
	    adminColumn.setCellValueFactory(new PropertyValueFactory<>("is_admin"));
	    
	    deleteColumn.setCellFactory(column -> new DeleteButtonCell(connectDB));
	    makeAdminColumn.setCellFactory(column -> new MakeAdminButtonCell(connectDB));
	    
	    loadData();
	}

	private void loadData() {
		String query = "SELECT user_id,username, email, is_admin FROM users";
		try (PreparedStatement statement = connectDB.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {

			ObservableList<UserListInfo> userList = FXCollections.observableArrayList();
			while (resultSet.next()) {
				int userid = resultSet.getInt("user_id");
				String username = resultSet.getString("username");
				String email = resultSet.getString("email");
				int isAdmin = resultSet.getInt("is_admin");
				userList.add(new UserListInfo(username, email, isAdmin, userid));
			}

			tableView.setFixedCellSize(33);
			double preferredHeight = 10 * 33 + 30;

			tableView.setPrefHeight(preferredHeight);
			System.out.println(userList.size());

			tableView.setItems(userList);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void closeClick(ActionEvent event) {
		try {
			Stage stage = (Stage) close.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
