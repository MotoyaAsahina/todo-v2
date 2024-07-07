package tech.asari.todo.util;

import java.sql.Timestamp;
import java.util.Calendar;

public class NotificationTagParser {

    public static boolean validate(String notificationTag) {
        // Possible values: \d+(.\d+)?[mhd] (e.g. 1h, 1.5h, 1d, 1.5d, 1m, 1.5m)
        //  and joined d, h and / or m directly without space in this order (e.g. 1h1m, 1h1.5m, 1d1h, 1.5d1h, 1d1h1m, 1d1h1.5m)
        return notificationTag.matches("^(\\d+(\\.\\d+)?d)?(\\d+(\\.\\d+)?h)?(\\d+(\\.\\d+)?m)?$");
    }

    public static Timestamp parse(String notificationTag, Timestamp dueDate) {
        Timestamp notificationDate = new Timestamp(dueDate.getTime());
        String[] parts = notificationTag.split("(?<=[a-zA-Z])");
        for (String part : parts) {
            double value = Double.parseDouble(part.substring(0, part.length() - 1));
            notificationDate = switch (part.charAt(part.length() - 1)) {
                case 'd' -> new Timestamp(notificationDate.getTime() - Math.round(value * 24 * 60 * 60 * 1000));
                case 'h' -> new Timestamp(notificationDate.getTime() - Math.round(value * 60 * 60 * 1000));
                case 'm' -> new Timestamp(notificationDate.getTime() - Math.round(value * 60 * 1000));
                default -> notificationDate;
            };
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(notificationDate);
        if (cal.get(Calendar.SECOND) == 59) {
            notificationDate = new Timestamp(notificationDate.getTime() + 1000);
        }

        return notificationDate;
    }
}
