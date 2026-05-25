package model;

import enums.PaymentStatus;

import java.time.LocalDateTime;

public class Ticket {
    private String ticketId;
    private LocalDateTime entryTime;
    private Vechicle vechicle;
    private String floorId;
    private String spotId;
    private PaymentStatus paymentStatus;

    public Ticket(String ticketId, LocalDateTime entryTime, Vechicle vechicle, String floorId, String spotId, PaymentStatus paymentStatus) {
        this.ticketId = ticketId;
        this.entryTime = entryTime;
        this.vechicle = vechicle;
        this.floorId = floorId;
        this.spotId = spotId;
        this.paymentStatus = paymentStatus;
    }

    public String getTicketId() {
        return ticketId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public Vechicle getVechicle() {
        return vechicle;
    }

    public String getFloorId() {
        return floorId;
    }

    public String getSpotId() {
        return spotId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public void setVechicle(Vechicle vechicle) {
        this.vechicle = vechicle;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
