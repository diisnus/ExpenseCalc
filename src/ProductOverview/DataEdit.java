package ProductOverview;

public class DataEdit {
    private String macroName;
    private double macroValue;

    public DataEdit(String macroName, double macroValue) {
        this.macroName = macroName;
        this.macroValue = macroValue;
    }

    public String getMacroName() {
        return macroName;
    }

    public double getMacroValue() {
        return macroValue;
    }
    
    
    @Override
    public String toString() {
        return "MacroName: " + macroName + ", MacroValue: " + macroValue;
    }

}