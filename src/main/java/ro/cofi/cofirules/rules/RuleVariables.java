package ro.cofi.cofirules.rules;

public enum RuleVariables {
    TITLE("%title%"),
    INDEX("%index%"),
    DESCRIPTION("%description%");

    private final String format;

    RuleVariables(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

}
