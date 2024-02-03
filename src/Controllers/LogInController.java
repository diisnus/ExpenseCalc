package Controllers;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DBConnection.DBHandler;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;


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
				PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorMessageLoginSignUp.fxml");
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
	
	
	
	// resizable for different screens, future reference
//	Screen screen = Screen.getPrimary();
//    Rectangle2D bounds = screen.getBounds();        
//    double screenWidth = bounds.getWidth();
//    double screenHeight = bounds.getHeight();	        
//    int sceneWidth = 0;
//    int sceneHeight = 0;
//    if (screenWidth <= 800 && screenHeight <= 600) {
//        sceneWidth = 600;
//        sceneHeight = 350;
//    } else if (screenWidth <= 1280 && screenHeight <= 768) {
//        sceneWidth = 800;
//        sceneHeight = 450;
//    } else if (screenWidth <= 1920 && screenHeight <= 1080) {
//        sceneWidth = 1000;
//        sceneHeight = 900;
//    }

	public void MainPageLoading() {
		try {
		    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainPage.fxml"));
		    Parent root = loader.load();
		    Scene signUpScene = new Scene(root, 1220, 800);
		    Stage currentStage = (Stage) signup.getScene().getWindow();	

		    currentStage.setScene(signUpScene);
		    currentStage.setResizable(true);

		    
		    currentStage.setMinWidth(1220);
		    currentStage.setMinHeight(800);
		    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		    currentStage.setX((screenBounds.getWidth() - currentStage.getWidth()) / 2);
		    currentStage.setY((screenBounds.getHeight() - currentStage.getHeight()) / 2);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	public void loginClick() {
		if (username.getText().isEmpty() || password.getText().isEmpty()) {
			PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorMessageLoginSignUp.fxml");
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
