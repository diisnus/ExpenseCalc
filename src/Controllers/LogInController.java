package Controllers;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import DBConnection.DBHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LogInController implements Initializable {
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Button login;
	@FXML
	private Button signup;
	@FXML
	private Button forgotpassword;
	@FXML
	private CheckBox remember;

	private Connection connection;
	private DBHandler handler;
	private PreparedStatement pst;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		handler = new DBHandler();
	}

	
	
	public void DataBaseLoginGetInfo() {
		String usernames = username.getText();
		String passwords = password.getText();
		connection = handler.getConnection();
		String selectcheck = "SELECT * FROM users WHERE username=? AND password=?";
		String selectid = "SELECT user_id FROM users WHERE username=? AND password=?";
		try {
			pst = connection.prepareStatement(selectcheck);
			pst.setString(1, usernames);
			pst.setString(2, passwords);
			ResultSet rs = pst.executeQuery();

			int count = 0;

			while (rs.next()) {
				count = count + 1;
			}

			if (count == 1) {
				pst = connection.prepareStatement(selectid); 
				pst.setString(1, usernames);
				pst.setString(2, passwords);
				ResultSet rs2 = pst.executeQuery();

				if (rs2.next()) {
					int currentUser_id = rs2.getInt("user_id"); 
					//System.out.println(currentUser_id);				
					Container container = Container.getInstance();
					container.update(currentUser_id, usernames);
					MainPageLoading();
				}
			} else {
				ErrorMessageLoginSignup.showCustomDialog("", "/FXML/ErrorMessageLoginSignUp.fxml");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	


	public void MainPageLoading() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainPage.fxml"));
			Parent root = loader.load();
			Scene signUpScene = new Scene(root);
			Stage currentStage = (Stage) signup.getScene().getWindow();
			currentStage.setScene(signUpScene);
			currentStage.setTitle("Main Page");
			currentStage.setResizable(true);
			currentStage.setMinWidth(1200);
			currentStage.setMinHeight(900);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loginClick() {
		if (username.getText().isEmpty() || password.getText().isEmpty()) {
			ErrorMessageLoginSignup.showCustomDialog("", "/FXML/ErrorMessageLoginSignUp.fxml");
		} else {
			DataBaseLoginGetInfo();
		}
	}

	public void signupClick() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SignUp.fxml"));
			Parent root = loader.load();
			Scene signUpScene = new Scene(root);
			Stage currentStage = (Stage) signup.getScene().getWindow();
			currentStage.setScene(signUpScene);
			currentStage.setTitle("Sign Up");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
