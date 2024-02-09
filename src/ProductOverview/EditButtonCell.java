package ProductOverview;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import AdminTables.UserListInfo;
import DBConnection.DBHandler;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

public class EditButtonCell extends TableCell<DataEdit, Void> {
	private final Button makeAdminButton;
    private final Connection connection;

    public EditButtonCell(Connection connection, ArrayList dataEditList) {
    	this.connection = connection;
        this.makeAdminButton = new Button("Edit");
        this.makeAdminButton.setOnAction(event -> {
        	selectedItemEdit();
           
        });
    }

    private void selectedItemEdit() {
    	DataEdit selectedItem = getTableView().getItems().get(getIndex());
        String selectedRowName = selectedItem.getMacroName();
        String selectedRowvalue = selectedItem.getMacroValue();
        

    	if(selectedRowName.equals("Name")) {
            System.out.println("Name");
            changeName();

    	} else if(selectedRowName.equals("Brand")) {
            System.out.println("Brand");
            changeBrand();

    	} else if(selectedRowName.equals("Description")) {
            System.out.println("Description");
            changeDescription();

       	} else if(selectedRowName.equals("Priced By")) {
            System.out.println("Priced By");
            changePricedBy();

       	} else if(selectedRowName.equals("Calories")) {
            System.out.println("Calories");
            changeCalories();

       	} else if(selectedRowName.equals("Protein")) {
            System.out.println("Name");
            changeProtein();

       	} else if(selectedRowName.equals("Carbs")) {
            System.out.println("Carbs");
            changeCarbs();

       	} else if(selectedRowName.equals("Sugar")) {
            System.out.println("Sugar");
            changeSugar();

       	} else if(selectedRowName.equals("Fiber")) {
            System.out.println("Fiber");
            changeFiber();

       	} else if(selectedRowName.equals("Fat")) {
            System.out.println("Fat");
            changeFat();

       	} else if(selectedRowName.equals("Saturated Fat")) {
            System.out.println("Saturated");
            changeSaturatedFat();

       	} else if(selectedRowName.equals("Salt")) {
            System.out.println("Salt");
            changeSalt();

       	} 
    	
    }

	private void changeName() {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();	
		
	}
	
	private void changeBrand() {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();		
	}
	
	private void changeDescription() {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();		
	}
	
	private void changePricedBy() {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();		
	}
	
	private void changeCalories() {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();		
	}
	
	private void changeProtein() {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();
		
	}
	
	private void changeSugar() {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();		
	}
	
	private void changeFiber() {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();		
	}
	
	private void changeFat() {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();		
	}
	
	private void changeCarbs() {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();		
	}

	private void changeSaturatedFat() {
		IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();		
	}
	
    private void changeSalt() {
    	IdContainer productIdContainer = IdContainer.getInstance();
		int productid = productIdContainer.getId();
    	
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