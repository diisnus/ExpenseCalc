package ProductOverview;

public class DataEdit {
    private String macroName;
    private String macroValue;

    public DataEdit(String macroName, String macroValue) {
        this.macroName = macroName;
        this.macroValue = macroValue;
    }

    public void setMacroName(String macroName) {
		this.macroName = macroName;
	}

	public void setMacroValue(String macroValue) {
		this.macroValue = macroValue;
	}

	public String getMacroName() {
        return macroName;
    }

    public String getMacroValue() {
        return macroValue;
    }
    
    

}