package strategy.pricing;

import enums.VechicleType;

import java.time.LocalDateTime;

public interface PricingStrategy {
    double calculateFee(VechicleType vechicleType, LocalDateTime entryTime,LocalDateTime exitTime);
}
