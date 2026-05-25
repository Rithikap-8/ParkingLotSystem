import enums.PaymentMode;
import enums.PricingStrategyType;
import enums.VechicleType;
import factory.PricingStrategyFactory;
import factory.VechicleFactory;
import model.*;
import service.ParkingLot;
import utils.DateTimeParser;

import java.time.LocalDateTime;

public class Main {
    public  static void main(String[]args){
        ParkingLot lot = ParkingLot.getInstance();
        EntryGate entryGate = new EntryGate("EG1");
        ExitGate exitGate = new ExitGate("XG1");
        lot.setPricingStrategy(PricingStrategyFactory.get(PricingStrategyType.valueOf("EVENT_BASED")));

        ParkingFloor floor1 = new ParkingFloor("Floor1");
        floor1.addSpot(new ParkingSpot("F1S1", VechicleType.BIKE));
        floor1.addSpot(new ParkingSpot("F1S2", VechicleType.CAR));
        floor1.addSpot(new ParkingSpot("F1S3", VechicleType.TRUCK));
        floor1.addSpot(new ParkingSpot("F1S4", VechicleType.CAR));
        lot.addFloor(floor1);


        System.out.println("---------------------------");


        Vechicle car = VechicleFactory.create("KA01AB1234",VechicleType.CAR);
        LocalDateTime entryTime = DateTimeParser.parse("25 May 2026 7:30 AM");
        Ticket ticket = entryGate.parkVehicle(car,entryTime);


        System.out.println("---------------------------");

        lot.printStatus();


        System.out.println("---------------------------");

        LocalDateTime exitTime = DateTimeParser.parse("25 May 2026 4:15 PM");
        exitGate.unparkVehicle(ticket.getTicketId(),exitTime, PaymentMode.UPI);

        System.out.println("---------------------------");

        lot.printStatus();

    }
}
