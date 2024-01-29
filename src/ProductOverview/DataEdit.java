package ProductOverview;

public class DataEdit {
    private String macroName;
    private String macroValue;

    public DataEdit(String macroName, String macroValue) {
        this.macroName = macroName;
        this.macroValue = macroValue;
    }

    public String getMacroName() {
        return macroName;
    }

    public String getMacroValue() {
        return macroValue;
    }
    
    
    @Override
    public String toString() {
        return "MacroName: " + macroName + ", MacroValue: " + macroValue;
    }

}