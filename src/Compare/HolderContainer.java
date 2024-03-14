package Compare;

public class HolderContainer {
	private String macroName;
    private String macroValue;

    public HolderContainer(String macroName, String macroValue) {
        this.macroName = macroName;
        this.macroValue = macroValue;
    }

    // Getter methods
    public String getMacroName() {
        return macroName;
    }

    public String getMacroValue() {
        return macroValue;
    }

}