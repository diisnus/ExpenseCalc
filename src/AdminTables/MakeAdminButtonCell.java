package AdminTables;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class MakeAdminButtonCell extends TableCell<UserListInfo, Void> {
    private final Button makeAdminButton;
    private final Connection connection;

    public MakeAdminButtonCell(Connection connection) {
    	this.connection = connection;
        this.makeAdminButton = new Button("Change Admin");
        this.makeAdminButton.setOnAction(event -> {
            UserListInfo item = getTableView().getItems().get(getIndex());
            int newAdminStatus = item.getIs_admin() == 0 ? 1 : 0;
            toggleAdminStatus(item.getUserid(), newAdminStatus);
            item.setIs_admin(newAdminStatus);
            getTableView().refresh();
        });
    }

    private void toggleAdminStatus(int userId, int newAdminStatus) {
        String updateQuery = "UPDATE users SET is_admin = ? WHERE user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setInt(1, newAdminStatus);
            statement.setInt(2, userId);
            statement.executeUpdate();
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
            setGraphic(makeAdminButton);
        }
    }
}
