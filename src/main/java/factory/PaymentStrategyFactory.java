package factory;

import enums.PaymentMode;
import strategy.payment.CardPayment;
import strategy.payment.CashPayment;
import strategy.payment.PaymentStrategy;
import strategy.payment.UpiPayment;

public class PaymentStrategyFactory {
    public static PaymentStrategy getPaymentStrategy(PaymentMode paymentMode){
        return switch(paymentMode){
            case UPI -> new UpiPayment();
            case CARD -> new CardPayment();
            case CASH -> new CashPayment();
        };
    }
}
