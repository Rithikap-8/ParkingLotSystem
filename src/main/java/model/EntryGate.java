package model;

import enums.GateType;
import enums.VechicleType;
import service.ParkingLot;

import java.time.LocalDateTime;

public class EntryGate extends  Gate {
    public EntryGate(String id){
        super(id);
    }
    @Override
    public GateType getType(){
        return GateType.ENRTY;
    }

    public Ticket parkVehicle(Vechicle vechicle, LocalDateTime entryTime){
        return ParkingLot.getInstance().parkVechicle(vechicle,entryTime);
    }
}
