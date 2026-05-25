package strategy.pricing;

import enums.VechicleType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeBasedPricing implements PricingStrategy {
    //peak and non-peak hour value

    private static final LocalTime PEAK_TIME_START = LocalTime.of(8, 0);
    private static final LocalTime PEAK_TIME_END = LocalTime.of(17, 0);

    private boolean isPeak(LocalTime time) {
        return !time.isBefore(PEAK_TIME_START) && !time.isAfter(PEAK_TIME_END);
    }

    @Override
    public double calculateFee(VechicleType vechicleType, LocalDateTime entryTime, LocalDateTime exitTime) {
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time is before entry time");
        }
        long durationMinutes = Duration.between(entryTime, exitTime).toMinutes();
        long totalHours = (long) Math.ceil(durationMinutes / 60.0);
        int peakHours = 0;
        int nonPeakHours = 0;
        //year month date 08:55,cursor will extract hour only
        LocalDateTime cursor = entryTime.truncatedTo(ChronoUnit.HOURS);
        for (int i = 0; i < totalHours; i++) {
            LocalTime hoursStart = cursor.toLocalTime();
            if (isPeak(hoursStart)) {
                peakHours++;
            } else {
                nonPeakHours++;
            }
            cursor = cursor.plusHours(1);
        }
        //enhanced switch statement
        double peakRate = switch (vechicleType) {
            case CAR -> 30.0;
            case BIKE -> 15.0;
            case TRUCK -> 50.0;
        };

        double nonpeakRate = switch (vechicleType) {
            case CAR -> 20.0;
            case BIKE -> 10.0;
            case TRUCK -> 30.0;
        };
        return  peakRate*peakHours + nonpeakRate*nonPeakHours;

    }
}
