package QuickActionsPack;

public class RecentlyAddedInfo {
	String name, brand;
	int calories, productid;
	double protein;

	public RecentlyAddedInfo(String name, String brand, int calories, int productid, double protein) {
		super();
		this.name = name;
		this.brand = brand;
		this.calories = calories;
		this.productid = productid;
		this.protein = protein;
	}

	public int getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public double getProtein() {
		return protein;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}
}
