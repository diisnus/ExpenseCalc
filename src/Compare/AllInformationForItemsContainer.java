package Compare;

import java.util.ArrayList;
import java.util.List;

import Controllers.LoaderClass;

public class AllInformationForItemsContainer {

	private String name, brand, description, pricedBy;
	private int popularity, calories;
	private double protein, carbs, sugar, fiber, fat, satfat, salt;
	
	
	public AllInformationForItemsContainer(String name, String brand, String description, String pricedBy,
			int popularity, int calories, double protein, double carbs, double sugar, double fiber, double fat,
			double satfat, double salt) {
		super();
		this.name = name;
		this.brand = brand;
		this.description = description;
		this.pricedBy = pricedBy;
		this.popularity = popularity;
		this.calories = calories;
		this.protein = protein;
		this.carbs = carbs;
		this.sugar = sugar;
		this.fiber = fiber;
		this.fat = fat;
		this.satfat = satfat;
		this.salt = salt;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPricedBy() {
		return pricedBy;
	}
	public void setPricedBy(String pricedBy) {
		this.pricedBy = pricedBy;
	}
	public int getPopularity() {
		return popularity;
	}
	public void setPopularity(int popularity) {
		this.popularity = popularity;
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
	public double getSatfat() {
		return satfat;
	}
	public void setSatfat(double satfat) {
		this.satfat = satfat;
	}
	public double getSalt() {
		return salt;
	}

	public void setSalt(double salt) {
		this.salt = salt;
	}

	private List<Integer> selectedToCompareIDs = new ArrayList<>();

	
	public List<Integer> getSelectedToCompareIDs() {
		return selectedToCompareIDs;
	}
	
	public void setSelectedToCompareIDs(List<Integer> selectedToCompareIDs) {
		this.selectedToCompareIDs = selectedToCompareIDs;
	}
	
	
	public AllInformationForItemsContainer() {
	    // Initialize fields with default values here if needed
	}

	private static AllInformationForItemsContainer instance;

	public static AllInformationForItemsContainer getInstance() {
		if (instance == null) {
			instance = new AllInformationForItemsContainer();
		}
		return instance;
	}
}
