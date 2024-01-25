package ProductOverview;

public class MacroData {
    private String macroName;
    private double macroValue;

    public MacroData(String macroName, double macroValue) {
        this.macroName = macroName;
        this.macroValue = macroValue;
    }

    // Getter methods
    public String getMacroName() {
        return macroName;
    }

    public double getMacroValue() {
        return macroValue;
    }

}