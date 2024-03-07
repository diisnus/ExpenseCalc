package Compare;

public class TableInformationContainer {
	int product_id, calories;
	double protein, sugar;
	String name, brand;

	public TableInformationContainer(int product_id, int calories, double protein, double sugar, String name,
			String brand) {
		super();
		this.product_id = product_id;
		this.calories = calories;
		this.protein = protein;
		this.sugar = sugar;
		this.name = name;
		this.brand = brand;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
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

	public double getSugar() {
		return sugar;
	}

	public void setSugar(double sugar) {
		this.sugar = sugar;
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

}
