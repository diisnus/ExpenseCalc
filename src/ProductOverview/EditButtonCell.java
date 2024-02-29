package ProductOverview;

import Controllers.PopUpWindow;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

public class EditButtonCell extends TableCell<DataEdit, Void> {
	private final Button makeAdminButton;
	private final TableView<DataEdit> tableView;

	public EditButtonCell(TableView<DataEdit> tableView) {
		this.tableView = tableView;
		this.makeAdminButton = new Button("Edit");
		this.makeAdminButton.setOnAction(event -> {
			selectedItemEdit();

		});
	}

	
	private void selectedItemEdit() {
		DataEdit selectedItem = getTableView().getItems().get(getIndex());
		String selectedRowName = selectedItem.getMacroName();

		if (selectedRowName.equals("Name")) {
			changeName();

		} else if (selectedRowName.equals("Brand")) {
			changeBrand();

		} else if (selectedRowName.equals("Description")) {
			changeDescription();

		} else if (selectedRowName.equals("Priced By")) {
			changePricedBy();

		} else if (selectedRowName.equals("Calories")) {
			changeCalories();

		} else if (selectedRowName.equals("Protein")) {
			changeProtein();

		} else if (selectedRowName.equals("Carbs")) {
			changeCarbs();

		} else if (selectedRowName.equals("Sugar")) {
			changeSugar();

		} else if (selectedRowName.equals("Fiber")) {
			changeFiber();

		} else if (selectedRowName.equals("Fat")) {
			changeFat();

		} else if (selectedRowName.equals("Saturated Fat")) {
			changeSaturatedFat();

		} else if (selectedRowName.equals("Salt")) {
			changeSalt();

		}

	}

	private void updateCellValue(String newValue) {
		int rowIndex = getIndex();
		if (rowIndex >= 0 && rowIndex < tableView.getItems().size()) {
			DataEdit item = tableView.getItems().get(rowIndex);
			item.setMacroValue(newValue);
			tableView.refresh();
		}
	}

	private void changeName() {
		String changeNameStatement = "UPDATE groceryproducts SET product_name = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("String");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");

		}

	}

	private void changeBrand() {
		String changeNameStatement = "UPDATE groceryproducts SET product_brand = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("String");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");

		}
	}

	private void changeDescription() {
		String changeNameStatement = "UPDATE groceryproducts SET description = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("String");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");

		}
	}

	private void changePricedBy() {
		String changeNameStatement = "UPDATE groceryproducts SET priced_by = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("PricedBy");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");
		}

	}

	private void changeCalories() {
		String changeNameStatement = "UPDATE macros SET calories_per_100g = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("Int");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");

		}
	}

	private void changeProtein() {
		String changeNameStatement = "UPDATE macros SET protein = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("Double");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");

		}
	}

	private void changeSugar() {
		String changeNameStatement = "UPDATE macros SET sugar = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("Double");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");

		}
	}

	private void changeFiber() {
		String changeNameStatement = "UPDATE macros SET fiber = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("Double");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");

		}
	}

	private void changeFat() {
		String changeNameStatement = "UPDATE macros SET fat = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("Double");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");

		}
	}

	private void changeCarbs() {
		String changeNameStatement = "UPDATE macros SET  carbohydrates  = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("Double");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");

		}
	}

	private void changeSaturatedFat() {
		String changeNameStatement = "UPDATE macros SET saturated_fat = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("Double");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");

		}
	}

	private void changeSalt() {
		String changeNameStatement = "UPDATE macros SET salt = ? WHERE product_id = ?";
		ChangeProductInformation changeProductInformation = ChangeProductInformation.getInstance();
		changeProductInformation.setStatement(changeNameStatement);
		changeProductInformation.setType("Double");
		PopUpWindow.showCustomDialog("", "/ProductOverview/EditDifferentStringInfo.fxml");

		String newValueForTable = changeProductInformation.getNewValue();
		if (newValueForTable != null && !newValueForTable.trim().isEmpty()) {
			updateCellValue(newValueForTable);
			changeProductInformation.setNewValue("");

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