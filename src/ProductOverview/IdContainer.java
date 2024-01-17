package ProductOverview;



import javafx.fxml.FXML;


public class IdContainer {
	private int productId;


	public void update(int id) {
		this.productId = id;
	}

	public int getId() {
		return productId;
	}


	public void setId(int id) {
		this.productId = id;
	}

	private static IdContainer instance;

	public static IdContainer getInstance() {
		if (instance == null) {
			instance = new IdContainer();
		}
		return instance;
	}
	
	
	
	
}
