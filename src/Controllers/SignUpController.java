package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import DBConnection.DBHandler;

public class SignUpController implements Initializable {
	@FXML
	public TextField enterusername;
	@FXML
	public PasswordField enteremail;
	@FXML
	public PasswordField enterpassword;
	@FXML
	public Button login;
	@FXML
	public Button signup;

	private Connection connection;
	private DBHandler handler;
	private PreparedStatement pst;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		handler = new DBHandler();
	}

	public void MainPageLoading() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainPage.fxml"));
			Parent root = loader.load();
			Scene signUpScene = new Scene(root,1200,800);
			Stage currentStage = (Stage) signup.getScene().getWindow();
			currentStage.setScene(signUpScene);
			currentStage.setResizable(true);
			currentStage.setMinWidth(1200);
			currentStage.setMinHeight(800);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void DataBaseSignupInput() {
	    String username = enterusername.getText();
	    String password = enterpassword.getText();
	    String email = enteremail.getText();
	    String insert = "INSERT INTO users(username, password, email) VALUES (?,?,?)";
	    String select = "SELECT * FROM users WHERE username = ?";
	    String selectid = "SELECT LAST_INSERT_ID() AS user_id"; 

	    connection = handler.getConnection();
	    try {
	        PreparedStatement selectStatement = connection.prepareStatement(select);
	        selectStatement.setString(1, username);
	        ResultSet existingUsers = selectStatement.executeQuery();

	        if (existingUsers.next()) {                
	            ErrorMessageLoginSignup.showCustomDialog("", "/FXML/UsernameTakenError.fxml");

	        } else {
	            try {
	                PreparedStatement insertStatement = connection.prepareStatement(insert);
	                insertStatement.setString(1, username);
	                insertStatement.setString(2, password);
	                insertStatement.setString(3, email);
	                insertStatement.executeUpdate();
	                PreparedStatement insertIdStatement = connection.prepareStatement(selectid);
	                ResultSet resultSet = insertIdStatement.executeQuery();

	                if (resultSet.next()) {
	                    int currentUser_id = resultSet.getInt("user_id");
	                    //System.out.println(currentUser_id);
						Container container = Container.getInstance();
						container.update(currentUser_id, username);
	                    MainPageLoading();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public void signupClick() {
		if (enterusername.getText().isEmpty() || enterpassword.getText().isEmpty() || enteremail.getText().isEmpty()) {
			ErrorMessageLoginSignup.showCustomDialog("", "/FXML/ErrorMessageLoginSignUp.fxml");
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
