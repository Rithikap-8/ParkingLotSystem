package factory;

import enums.VechicleType;
import model.Bike;
import model.Car;
import model.Truck;
import model.Vechicle;

public class VechicleFactory {
    public static Vechicle create(String number, VechicleType type){
      return switch (type){
         case CAR -> new Car(number);
         case TRUCK -> new Truck(number);
         case BIKE -> new Bike(number);
      };
    }
}
