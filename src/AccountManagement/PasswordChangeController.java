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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PasswordChangeController implements Initializable{

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button changePassword;

    @FXML
    private Button close;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField oldPassword;

	private Connection connectDB;
	private DBHandler handler;
    
    @FXML
    void changePasswordClick(ActionEvent event) {
    	
    	String oldpassword = oldPassword.getText();
    	String newpassword = newPassword.getText();
    	String selectPassword = "SELECT password FROM users WHERE user_id = ?";
    	String changePassword = "UPDATE users SET password = ? WHERE user_id = ?";
    	Container container = Container.getInstance();
    	int userid = container.getId();

    	try {
    	    PreparedStatement selectedPassword = connectDB.prepareStatement(selectPassword);
    	    selectedPassword.setInt(1, userid);
    	    ResultSet passwordCheck = selectedPassword.executeQuery();

    	    if (passwordCheck.next()) {
    	        String storedPassword = passwordCheck.getString("password");
    	        if (!oldpassword.equals(storedPassword)) {
    				PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorMessageLoginSignUp.fxml");
    	            return;
    	        }
    	        PreparedStatement updatePassword = connectDB.prepareStatement(changePassword);
    	        updatePassword.setString(1, newpassword);
    	        updatePassword.setInt(2, userid);
    	        updatePassword.executeUpdate();
    	    } else {
    	    }
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	}
    	try {
			Stage stage = (Stage) newPassword.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    handler = new DBHandler();
	    connectDB = handler.getConnection();
		
	}

}
