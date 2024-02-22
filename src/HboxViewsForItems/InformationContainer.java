package HboxViewsForItems;

public class InformationContainer {
	// type is only relevant for changing one row, depending on the class that calls
	// this cclass
	int type;
	String name, brand;
	int calories, product_id;

	double protein, carbs, sugar, fiber, fat, saturated_fat, salt;

	public InformationContainer(String name, String brand, int calories, double protein, double carbs, double sugar,
			double fiber, double fat, double saturated_fat, double salt, int product_id, int type) {
		super();
		this.name = name;
		this.brand = brand;
		this.calories = calories;
		this.protein = protein;
		this.carbs = carbs;
		this.sugar = sugar;
		this.fiber = fiber;
		this.fat = fat;
		this.saturated_fat = saturated_fat;
		this.salt = salt;
		this.product_id = product_id;
		this.type = type;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public double getCarbs() {
		return carbs;
	}

	public void setCarbs(double carbs) {
		this.carbs = carbs;
	}

	public double getSugar() {
		return sugar;
	}

	public void setSugar(double sugar) {
		this.sugar = sugar;
	}

	public double getFiber() {
		return fiber;
	}

	public void setFiber(double fiber) {
		this.fiber = fiber;
	}

	public double getFat() {
		return fat;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public double getSaturated_fat() {
		return saturated_fat;
	}

	public void setSaturated_fat(double saturated_fat) {
		this.saturated_fat = saturated_fat;
	}

	public double getSalt() {
		return salt;
	}

	public void setSalt(double salt) {
		this.salt = salt;
	}

}
