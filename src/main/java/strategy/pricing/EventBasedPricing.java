package strategy.pricing;

import enums.VechicleType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class EventBasedPricing implements PricingStrategy {
    //whatever the time, the price is same
    private static  final Map<VechicleType,Double> EVENT_HOURLY_RATES = Map.of(
            VechicleType.CAR, 50.0,
            VechicleType.BIKE,30.0,
            VechicleType.TRUCK,70.0
    );
    @Override
    public double calculateFee(VechicleType vechicleType, LocalDateTime entryTime, LocalDateTime exitTime) {
        long durationMinutes = Duration.between(entryTime, exitTime).toMinutes();
        long totalHours = (long) Math.ceil(durationMinutes / 60.0);
        double ratePerHour = EVENT_HOURLY_RATES.getOrDefault(vechicleType,0.0);
        return ratePerHour*totalHours;
    }
}
