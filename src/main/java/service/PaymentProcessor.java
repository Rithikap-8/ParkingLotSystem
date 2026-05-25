package service;

import enums.PaymentStatus;
import model.Ticket;
import strategy.payment.PaymentStrategy;

public class PaymentProcessor {
    private final PaymentStrategy strategy;

    public PaymentProcessor(PaymentStrategy strategy) {
        this.strategy = strategy;
    }
  public boolean pay(Ticket ticket, double amoubt){
        boolean success = strategy.processPayment(ticket,amoubt);
        if(success){
            ticket.setPaymentStatus(PaymentStatus.SUCCESS);
        }else{
            ticket.setPaymentStatus(PaymentStatus.FAILURE);
            System.out.print("Payment falied for the ticket : "+ticket.getTicketId());
        }
        return success;
  }
}
