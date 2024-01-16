package Search;

public class productSearchModel {
	int product_id,calories_per_100;
	String brand, name, description;
	double  protein;
	
	public productSearchModel(int product_id, String brand, String name, String description, int calories_per_100,
			double protein) {
		super();
		this.product_id = product_id;
		this.brand = brand;
		this.name = name;
		this.description = description;
		this.calories_per_100 = calories_per_100;
		this.protein = protein;
	}
	
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int productID) {
		this.product_id = productID;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCalories_per_100() {
		return calories_per_100;
	}
	public void setCalories_per_100(int calories_per_100) {
		this.calories_per_100 = calories_per_100;
	}
	public double getProtein() {
		return protein;
	}
	public void setProtein(double protein_per_100) {
		this.protein = protein_per_100;
	}
	
}
