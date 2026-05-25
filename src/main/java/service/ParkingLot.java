package service;

import enums.PaymentMode;
import enums.PaymentStatus;
import factory.PaymentStrategyFactory;
import factory.PricingStrategyFactory;
import model.ParkingFloor;
import model.ParkingSpot;
import model.Ticket;
import model.Vechicle;
import strategy.payment.PaymentStrategy;
import strategy.pricing.PricingStrategy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static enums.PricingStrategyType.TIME_BASED;

public class ParkingLot {

    //create singleton object
    private static final ParkingLot INSTANCE = new ParkingLot();
    //to know which floor and intiate ticket
    //to add floors
    private final Map<String, ParkingFloor> floors = new HashMap<>();
    //to add active tickets
    private final Map<String, Ticket> activeTickets = new HashMap<>();
    private PricingStrategy pricingStrategy;
  //singleton , so constructor should be private
    private ParkingLot() {
        this.pricingStrategy = PricingStrategyFactory.get(TIME_BASED);
    }
    public static ParkingLot getInstance(){
        return  INSTANCE;
    }
    public void addFloor(ParkingFloor floor){
        floors.put(floor.getId(), floor);
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public Map<String, ParkingFloor> getFloors() {
        return floors;
    }

    public Map<String, Ticket> getActiveTickets() {
        return activeTickets;
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    public Ticket parkVechicle(Vechicle vechicle, LocalDateTime entryTime){
        for(ParkingFloor floor : floors.values()){
            ParkingSpot spot = floor.findAvailability(vechicle.getVechicleType());
            if(spot!=null){
                String ticketId = UUID.randomUUID().toString();
                Ticket ticket = new Ticket(ticketId,entryTime,vechicle, floor.getId(), spot.getId(), PaymentStatus.PENDING);
                activeTickets.put(ticketId,ticket);
                System.out.println("vechicle parked successfully.. Ticket ID: "+ticketId);
                return  ticket;
            }
        }
        //inf no spot
        System.out.println("No spot available for the vechicle type "+vechicle.getVechicleType());
        return  null;
    }
    public void unparkVechicle(String ticketId, LocalDateTime exitTime, PaymentMode paymentMode){
        Ticket ticket = activeTickets.get(ticketId);
        if(ticket == null){
            System.out.println("No ticket available for the vechicle type ..Invalid ticketID"+ticketId);
            return;
        }
        double fees = pricingStrategy.calculateFee(ticket.getVechicle().getVechicleType(), ticket.getEntryTime(),exitTime);
        PaymentStrategy paymentStrategy = PaymentStrategyFactory.getPaymentStrategy(paymentMode);
        PaymentProcessor paymentProcessor= new PaymentProcessor(paymentStrategy);
        boolean paid = paymentProcessor.pay(ticket,fees);
        if(!paid){
            System.out.println("Vechicle cannot exit..payment unsuccessfull");
            return;
        }
       ParkingSpot parkingSpot = floors.get(ticket.getFloorId()).getSpots().get(ticket.getSpotId());
        parkingSpot.vacate();
        activeTickets.remove((ticketId));
        System.out.println("Vehicle exited...Fee charged : Rs. "+fees);
    }
    public void printStatus(){
        floors.forEach((floorId,floor)->{
            System.out.println("Floor : "+floorId);
            floor.getSpots().values().forEach(spot ->{
                System.out.println("Spot : "+ spot.getId() + spot.getAllowType() + (spot.isOcuupied()?"Occupied":"free"));
            });
        });
    }
}
