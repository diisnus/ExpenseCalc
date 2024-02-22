package AdminTables;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteButtonCell extends TableCell<UserListInfo, Void> {
	private final Button deleteButton;
	private final Connection connectDB;

	public DeleteButtonCell(Connection connection) {
		this.connectDB = connection;
		this.deleteButton = new Button("Delete");
		this.deleteButton.setOnAction(event -> {
			UserListInfo item = getTableView().getItems().get(getIndex());
			deleteUser(item.getUserid());
			getTableView().getItems().remove(item);
		});
	}

	private void deleteUser(int userId) {
		String deleteUserQuery = "DELETE FROM users WHERE user_id = ?";
		String updateProductsQuery = "UPDATE groceryproducts SET user_id = 1 WHERE user_id = ?";

		try {

			PreparedStatement deleteStatement = connectDB.prepareStatement(deleteUserQuery);
			deleteStatement.setInt(1, userId);
			deleteStatement.executeUpdate();

			PreparedStatement updateStatement = connectDB.prepareStatement(updateProductsQuery);
			updateStatement.setInt(1, userId);
			updateStatement.executeUpdate();
			
			deleteStatement.close();
			updateStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void updateItem(Void item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
		} else {
			setGraphic(deleteButton);
		}
	}
}
