package Controllers;

public class Container {

	private String username;
	private int id;

	public void update(int id, String username) {
		this.id = id;
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	private static Container instance;

	public static Container getInstance() {
		if (instance == null) {
			instance = new Container();
		}
		return instance;
	}

}
