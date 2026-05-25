package model;

import enums.VechicleType;

public class Truck extends Vechicle{

    public Truck(String number) {
        super(number, VechicleType.TRUCK);
    }
}
