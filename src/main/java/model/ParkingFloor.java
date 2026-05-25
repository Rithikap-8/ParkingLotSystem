package model;

import enums.VechicleType;

import java.util.HashMap;
import java.util.Map;

public class ParkingFloor {
    private final String id;
    //Many parking spot, so creating Map
    private final Map<String, ParkingSpot> spots= new HashMap<>();

    public Map<String, ParkingSpot> getSpots() {
        return spots;
    }

    public String getId() {
        return id;
    }

    public ParkingFloor(String id) {
        this.id = id;
    }
    public void addSpot(ParkingSpot spot){
        spots.put(spot.getId(),spot);
    }
    public ParkingSpot findAvailability(VechicleType vechicleType){
        for(ParkingSpot spot : spots.values()){
            if(spot.getAllowType()==vechicleType && spot.tryOccupy()){
                return spot;
            }
        }
        return null;
    }

}
