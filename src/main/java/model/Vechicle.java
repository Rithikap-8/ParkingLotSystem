package model;

import enums.VechicleType;

public abstract class Vechicle {
    private final String number;
    private final VechicleType vechicleType;//enum datatype

    protected Vechicle(String number, VechicleType vechicleType) {
        this.number = number;
        this.vechicleType = vechicleType;
    }
//variables are declared as final, so getter is only possible to use
    public String getNumber() {
        return number;
    }

    public VechicleType getVechicleType() {
        return vechicleType;
    }
}
