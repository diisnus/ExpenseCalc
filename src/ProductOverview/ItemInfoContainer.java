package ProductOverview;

public class ItemInfoContainer {

	String nameValue;
	String valValue;
	public String getNameValue() {
		return nameValue;
	}
	public void setNameValue(String nameValue) {
		this.nameValue = nameValue;
	}
	public String getValValue() {
		return valValue;
	}
	public void setValValue(String valValue) {
		this.valValue = valValue;
	}
	public ItemInfoContainer(String nameValue, String valValue) {
		super();
		this.nameValue = nameValue;
		this.valValue = valValue;
	}
	
}
