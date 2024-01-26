package AccountManagement;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Controllers.Container;
import Controllers.PopUpWindow;
import DBConnection.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UsernameChangeController implements Initializable {

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button close;

	@FXML
	private Label currentUsername;

	@FXML
	private TextField newUsername;

	@FXML
	private Button updateUsername;

	private Connection connectDB;
	private DBHandler handler;

	@FXML
	void closeClick(ActionEvent event) {
		try {
			Stage stage = (Stage) close.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void updateUsernameClick(ActionEvent event) {
		String selectUsername = "SELECT * FROM users WHERE username = ?";
		String updateUsername = "UPDATE users SET username = ? WHERE user_id = ?;";
		String newusername = newUsername.getText();

		connectDB = handler.getConnection();

		try {
			PreparedStatement selectUsernameStatement = connectDB.prepareStatement(selectUsername);
			selectUsernameStatement.setString(1, newusername);
			ResultSet existingUsersByUsername = selectUsernameStatement.executeQuery();

			if (existingUsersByUsername.next()) {
				PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorUsernameTaken.fxml");
				return;
			}
			if(newusername.isBlank() || newusername.isEmpty()) {
				PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorMessageLoginSignUp.fxml");
				return;
			}
			Container container = Container.getInstance();
			int userid = container.getId();
			PreparedStatement updateUsernameStatement = connectDB.prepareStatement(updateUsername);
			updateUsernameStatement.setString(1, newusername);
			updateUsernameStatement.setInt(2, userid);

			int rowsAffected = updateUsernameStatement.executeUpdate();

			if (rowsAffected > 0) {
				Stage stage = (Stage) close.getScene().getWindow();
				stage.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    handler = new DBHandler();
	    connectDB = handler.getConnection();
	    Container container = Container.getInstance();
	    int userid = container.getId();
	    String selectUsername = "SELECT username FROM users WHERE user_id = ?";
	    try {
	        PreparedStatement selectedUsername = connectDB.prepareStatement(selectUsername);
	        selectedUsername.setInt(1, userid);
	        ResultSet rs = selectedUsername.executeQuery();
	        if (rs.next()) {
	            String username = rs.getString("username");
	            System.out.println(username);
	            currentUsername.setText("Your current username is: " + username + ".");
	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


}
