package model;

import enums.VechicleType;

public class Car extends Vechicle{
    public Car(String number) {
        super(number, VechicleType.CAR);
    }
}
