package AccountManagement;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class EmailChangeController implements Initializable{

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button close;

    @FXML
    private Button emailChange;

    @FXML
    private TextField newEmail;

    @FXML
    private Label yourEmail;

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
    void emailChangeClick(ActionEvent event) {

    	    String selectEmail = "SELECT * FROM users WHERE email = ?";
    	    String updateEmail = "UPDATE users SET email = ? WHERE user_id = ?";
    	    String newemail = newEmail.getText();


    	    if (!isValidEmail(newemail)) {

    	        PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorInvalidEmail.fxml");
    	        return;
    	    }

    	    connectDB = handler.getConnection();

    	    try {

    	        PreparedStatement selectEmailStatement = connectDB.prepareStatement(selectEmail);
    	        selectEmailStatement.setString(1, newemail);
    	        ResultSet existingUsersByEmail = selectEmailStatement.executeQuery();

    	        if (existingUsersByEmail.next()) {
    	            PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorInvalidEmail.fxml");
    	            return;
    	        }

    	        Container container = Container.getInstance();
    	        int userId = container.getId();
    	        PreparedStatement updateEmailStatement = connectDB.prepareStatement(updateEmail);
    	        updateEmailStatement.setString(1, newemail);
    	        updateEmailStatement.setInt(2, userId);
    	        updateEmailStatement.executeUpdate();

    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }

    	    try {
    	        Stage stage = (Stage) newEmail.getScene().getWindow();
    	        stage.close();
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	}

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }




	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		handler = new DBHandler();
	    connectDB = handler.getConnection();
	    Container container = Container.getInstance();
	    int userid = container.getId();
	    String selectEmail = "SELECT email FROM users WHERE user_id = ?";
	    try {
	        PreparedStatement selectedEmail = connectDB.prepareStatement(selectEmail);
	        selectedEmail.setInt(1, userid);
	        ResultSet rs = selectedEmail.executeQuery();
	        if (rs.next()) {
	            String email = rs.getString("email");
	            yourEmail.setText("Your current email is: " + email + ".");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		
	}

}
