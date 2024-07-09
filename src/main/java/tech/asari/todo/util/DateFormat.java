package tech.asari.todo.util;

import java.text.SimpleDateFormat;

public class DateFormat {

    public static SimpleDateFormat DEFAULT() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
