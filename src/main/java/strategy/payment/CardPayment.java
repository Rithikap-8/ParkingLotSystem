package strategy.payment;

import model.Ticket;

public class CardPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(Ticket ticket, double amount) {
        System.out.println("Paid Rs. "+amount +" for ticket "+ticket.getTicketId()+" via card payment.");
        return true;
    }
}
