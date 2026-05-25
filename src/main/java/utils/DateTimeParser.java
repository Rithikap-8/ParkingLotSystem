package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeParser {

    public static LocalDateTime parse(String dateTime){

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd MMM yyyy h:mm a", Locale.ENGLISH);

        return LocalDateTime.parse(dateTime, formatter);
    }
}