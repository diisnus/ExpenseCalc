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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AccountChangesController implements Initializable{

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane gridPane;
    
    @FXML
    private Button emailChange;

    @FXML
    private Button passwordChange;

    @FXML
    private Button usernameChange;
    
    @FXML
    private Label currentEmail;

    @FXML
    private Label currentUsername;

    @FXML
    void usernameChangeClick(ActionEvent event) {
    	PopUpWindow.showCustomDialog("" , "/AccountManagement/UsernameChange.fxml");
    }
    
    @FXML
    void passwordChangeClick(ActionEvent event) {
    	PopUpWindow.showCustomDialog("" , "/AccountManagement/PasswordChange.fxml");
    }
    
    @FXML
    void emailChangeClick(ActionEvent event) {
    	PopUpWindow.showCustomDialog("" , "/AccountManagement/EmailChange.fxml");
    }
	private Connection connectDB;
	private DBHandler handler;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gridPane.setGridLinesVisible(true);	
	    handler = new DBHandler();
	    connectDB = handler.getConnection();
	    Container container = Container.getInstance();
	    int userid = container.getId();	    
	    String selectUsername = "SELECT username, email FROM users WHERE user_id = ?";
	    try {
	        PreparedStatement selectedUsername = connectDB.prepareStatement(selectUsername);
	        selectedUsername.setInt(1, userid);
	        ResultSet rs = selectedUsername.executeQuery();
	        if (rs.next()) {
	            String username = rs.getString("username");
	            currentUsername.setText("Your current username is: " + username + ".");
	            String email = rs.getString("email");
	            currentEmail.setText("Your current email is: " + email + ".");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


}
