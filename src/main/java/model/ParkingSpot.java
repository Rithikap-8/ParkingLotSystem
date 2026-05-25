package model;

import enums.VechicleType;

import java.util.concurrent.atomic.AtomicBoolean;

public class ParkingSpot {
    private final String id;
    private final VechicleType allowType;
    private AtomicBoolean occupied = new AtomicBoolean(false);

    public ParkingSpot(String id, VechicleType allowType) {
        this.id = id;
        this.allowType = allowType;
    }

    public String getId() {
        return id;
    }

    public VechicleType getAllowType() {
        return allowType;
    }

    public AtomicBoolean getOccupied() {
        return occupied;
    }

    public boolean tryOccupy(){
        return occupied.compareAndSet(false,true);
    }
    public void vacate(){
        occupied.set(false);
    }
    public boolean isOcuupied(){
        return occupied.get();
    }
}
