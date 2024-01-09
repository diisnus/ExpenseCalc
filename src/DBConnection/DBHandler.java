package DBConnection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class DBHandler extends Configs {
	
	Connection dbconnection;
	
	public Connection getConnection() {
		String connectionString = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?user=" + this.user + "&password=" + this.password;
		try {
			dbconnection = DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dbconnection;
	}
	
}
//public class DBHandler extends Configs {
//
//	Connection dbconnection;
//	
//	public Connection getConnection() {
//		String connectionString = this.url + this.user + this.password; 
//		System.out.print("aaaaaaa");
//		try {
//			dbconnection = DriverManager.getConnection(connectionString);
//		} catch (SQLException e) {
//			System.out.print("aaa");
//		}
//		
//		return dbconnection;
//	}
//	
//}
