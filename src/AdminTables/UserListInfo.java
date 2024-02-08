package AdminTables;

public class UserListInfo {
	String username, email;
	int is_admin;
	int userid;
	
	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}


	
	public UserListInfo(String username, String email, int is_admin, int userid) {
		super();
		this.username = username;
		this.email = email;
		this.is_admin = is_admin;
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getIs_admin() {
		return is_admin;
	}
	public void setIs_admin(int is_admin) {
		this.is_admin = is_admin;
	}

	
	
}
