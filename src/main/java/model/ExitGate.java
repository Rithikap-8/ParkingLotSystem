package model;

import enums.GateType;
import enums.PaymentMode;
import service.ParkingLot;

import java.time.LocalDateTime;

public class ExitGate extends Gate {
    public ExitGate(String id){
        super(id);
    }

    public GateType getType(){
        return GateType.EXIT;
    }

    public void unparkVehicle(String tickedId, LocalDateTime exitTime, PaymentMode paymentMode){
        ParkingLot.getInstance().unparkVechicle(tickedId,exitTime,paymentMode);
    }
}
