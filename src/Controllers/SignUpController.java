package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import DBConnection.DBHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController implements Initializable {
	@FXML
	public TextField enterusername;
	@FXML
	public TextField enteremail;
	@FXML
	public PasswordField enterpassword;
	@FXML
	public Button login;
	@FXML
	public Button signup;

	private Connection connection;
	private DBHandler handler;
	private PreparedStatement pst;
	
	private static final String EMAIL_PATTERN =
	        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		handler = new DBHandler();
	}

	public void MainPageLoading() {
		try {
		    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainPage.fxml"));
		    Parent root = loader.load();
		    Scene signUpScene = new Scene(root, 1200, 800);
		    Stage currentStage = (Stage) signup.getScene().getWindow();

		    currentStage.setScene(signUpScene);
		    currentStage.setResizable(true);
		    currentStage.setMinWidth(1200);
		    currentStage.setMinHeight(800);
		    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		    currentStage.setX((screenBounds.getWidth() - currentStage.getWidth()) / 2);
		    currentStage.setY((screenBounds.getHeight() - currentStage.getHeight()) / 2);
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}

	public void DataBaseSignupInput() {
	    String username = enterusername.getText();
	    String password = enterpassword.getText();
	    String email = enteremail.getText();

	    
	    if (!isValidEmail(email)) {
	        PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorInvalidEmail.fxml");
	        return; 
	    }

	    String insert = "INSERT INTO users(username, password, email) VALUES (?,?,?)";
	    String selectUsername = "SELECT * FROM users WHERE username = ?";
	    String selectEmail = "SELECT * FROM users WHERE email = ?";
	    String selectId = "SELECT LAST_INSERT_ID() AS user_id"; 

	    connection = handler.getConnection();
	    try {
	       
	        PreparedStatement selectUsernameStatement = connection.prepareStatement(selectUsername);
	        selectUsernameStatement.setString(1, username);
	        ResultSet existingUsersByUsername = selectUsernameStatement.executeQuery();

	        if (existingUsersByUsername.next()) {                
	            PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorUsernameTaken.fxml");
	            return; 
	        }

	
	        PreparedStatement selectEmailStatement = connection.prepareStatement(selectEmail);
	        selectEmailStatement.setString(1, email);
	        ResultSet existingUsersByEmail = selectEmailStatement.executeQuery();

	        if (existingUsersByEmail.next()) {                
	            PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorEmailTaken.fxml");
	            return; 
	        }

	 
	        try {
	            PreparedStatement insertStatement = connection.prepareStatement(insert);
	            insertStatement.setString(1, username);
	            insertStatement.setString(2, password);
	            insertStatement.setString(3, email);
	            insertStatement.executeUpdate();

	            // Retrieve user ID
	            PreparedStatement insertIdStatement = connection.prepareStatement(selectId);
	            ResultSet resultSet = insertIdStatement.executeQuery();

	            if (resultSet.next()) {
	                int currentUser_id = resultSet.getInt("user_id");
	                Container container = Container.getInstance();
	                container.update(currentUser_id, username);
	                MainPageLoading();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	private boolean isValidEmail(String email) {
	    Matcher matcher = pattern.matcher(email);
	    return matcher.matches();
	}

	public void signupClick() {
		if (enterusername.getText().isEmpty() || enterpassword.getText().isEmpty() || enteremail.getText().isEmpty()) {
			PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorMessageLoginSignUp.fxml");
		} else {
			DataBaseSignupInput();
		}
	}

	public void loginClick() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LogIn.fxml"));
			Parent signUpRoot = loader.load();
			Scene signUpScene = new Scene(signUpRoot);
			Stage currentStage = (Stage) signup.getScene().getWindow();
			currentStage.setScene(signUpScene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
