package tech.asari.todo.util;

import java.sql.Timestamp;

public class NotificationTagParser {

    public static boolean validate(String notificationTags) {
        // Possible values: \d+(.\d+)?[mhd] (e.g. 1h, 1.5h, 1d, 1.5d, 1m, 1.5m)
        //  and joined d, h and / or m directly without space in this order (e.g. 1h1m, 1h1.5m, 1d1h, 1.5d1h, 1d1h1m, 1d1h1.5m)
        return notificationTags.matches("^(\\d+(\\.\\d+)?d)?(\\d+(\\.\\d+)?h)?(\\d+(\\.\\d+)?m)?$");
    }

    public static Timestamp parse(String notificationTags, Timestamp dueDate) {
        return null;
    }
}
