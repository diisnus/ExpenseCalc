package ProductOverview;

import Controllers.Container;

public class ChangeProductInformation {

	String statement;
	String type;
	String newValue;

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void update(String username) {
		this.statement = statement;
	}
	
	public ChangeProductInformation() {
		super();
		this.statement = statement;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
	private static ChangeProductInformation instance;

	public static ChangeProductInformation getInstance() {
		if (instance == null) {
			instance = new ChangeProductInformation();
		}
		return instance;
	}
}
