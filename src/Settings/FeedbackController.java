package Settings;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Controllers.Container;
import Controllers.PopUpWindow;
import DBConnection.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FeedbackController implements Initializable {

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button close;

	@FXML
	private TextArea descriptionOfFeedback;

	@FXML
	private Button sendFeedback;

	@FXML
	private TextField specificReason;

	@FXML
	private ChoiceBox<String> typeOfFeedBack;
	private String[] feedbackTypes = { "Visuals are bugged.", "Malfunction", "Idea suggestions.", "I want...",
			"I have a question.", "Something else" };
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
	void sendFeedbackClick(ActionEvent event) {
		String insertFeedback = "INSERT INTO feedback (user_id, type, reason, feedback_dscr) VALUES (?, ?, ?, ?);";
		connectDB = handler.getConnection();
		String type = typeOfFeedBack.getValue();
		String reason = specificReason.getText();
		String description = descriptionOfFeedback.getText();
		try {
			if (reason.isBlank() || reason.isEmpty()) {
				PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorMessageLoginSignUp.fxml");
				return;
			}
			if (type.isBlank() || type.isEmpty()) {
				PopUpWindow.showCustomDialog("", "/ErrorsAndPopups/ErrorMessageLoginSignUp.fxml");
				return;
			}
			Container container = Container.getInstance();
			int userid = container.getId();
			PreparedStatement insertFeedbackStatement = connectDB.prepareStatement(insertFeedback);
			insertFeedbackStatement.setInt(1, userid);
			insertFeedbackStatement.setString(2, type);
			insertFeedbackStatement.setString(3, reason);
			insertFeedbackStatement.setString(4, description);
			int rowsAffected = insertFeedbackStatement.executeUpdate();

			if (rowsAffected > 0) {
				Stage stage = (Stage) typeOfFeedBack.getScene().getWindow();
				stage.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		handler = new DBHandler();

		typeOfFeedBack.getItems().addAll(feedbackTypes);

	}

}
