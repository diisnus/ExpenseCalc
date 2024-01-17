module ExpenseCalc {
	
	requires javafx.controls;
	opens application to javafx.graphics;
	requires javafx.fxml;
	requires java.desktop;
	requires java.sql;
	requires jdk.httpserver;
	requires javafx.graphics;
	requires javafx.base;
	opens Controllers to javafx.fxml;
	exports Controllers;
	opens Favourites to javafx.fxml;
	exports Favourites; 
	opens YourItems to javafx.fxml;
	exports YourItems;
	opens QuickActionsPack to javafx.fxml;
	exports QuickActionsPack; 
	opens Search to javafx.fxml;
	exports Search; 
	opens ProductOverview to javafx.fxml;
	exports ProductOverview; 
	
}
