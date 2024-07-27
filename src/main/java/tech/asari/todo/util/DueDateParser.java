package tech.asari.todo.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;

public class DueDateParser {

    private static final String[] WEEKDAYS = {
            "日", "月", "火", "水", "木", "金", "土",
            "日曜", "月曜", "火曜", "水曜", "木曜", "金曜", "土曜",
            "日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日",
    };

    public static Timestamp parseDueDate(String strDueDate) {
        if (strDueDate == null || strDueDate.isEmpty()) {
            return null;
        }

        try {
            return Timestamp.valueOf(strDueDate);
        } catch (IllegalArgumentException ignored) {
        }

        String[] dueDateParts = strDueDate.split(" ");
        if (dueDateParts.length < 1 || dueDateParts.length > 2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid due date format");

        Calendar dueDate = parseDate(dueDateParts[0]);
        dueDate.set(Calendar.MILLISECOND, 0);

        if (dueDateParts.length == 2) {
            if (!dueDateParts[1].matches("\\d{1,2}:\\d{1,2}"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid due date format");
            String[] time = dueDateParts[1].split(":");
            dueDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            dueDate.set(Calendar.MINUTE, Integer.parseInt(time[1]));
            dueDate.set(Calendar.SECOND, 0);
        } else {
            dueDate.set(Calendar.HOUR_OF_DAY, 23);
            dueDate.set(Calendar.MINUTE, 59);
            dueDate.set(Calendar.SECOND, 59);
        }

        // NOTE: `mm/dd` の場合のみ、時間によっては来年の日付とする
        if (dueDateParts[0].matches("\\d{1,2}/\\d{1,2}") && dueDate.before(Calendar.getInstance())) {
            dueDate.add(Calendar.YEAR, 1);
        }

        // NOTE: 曜日の場合のみ、時間によっては来週の日付とする
        if (Arrays.asList(WEEKDAYS).contains(dueDateParts[0]) && dueDate.before(Calendar.getInstance())) {
            dueDate.add(Calendar.DAY_OF_MONTH, 7);
        }

        return new Timestamp(dueDate.getTimeInMillis());
    }

    private static Calendar parseDate(String date) {
        // possible formats: mm/dd, yyyy/mm/dd, yyyy-mm-dd,
        //  今日, today, tdy, 明日, tomorrow, tmr,
        //  (\d+)日後, (\d+)d, +(\d+), +(\d+)d,
        //  月, 火, 水, 木, 金, 土, 日,
        //  月曜, 火曜, 水曜, 木曜, 金曜, 土曜, 日曜,
        //  月曜日, 火曜日, 水曜日, 木曜日, 金曜日, 土曜日, 日曜日

        Calendar calendar = Calendar.getInstance();

        if (date.matches("\\d{1,2}/\\d{1,2}")) {
            String[] dateParts = date.split("/");
            calendar.set(calendar.get(Calendar.YEAR), Integer.parseInt(dateParts[0]) - 1, Integer.parseInt(dateParts[1]));
        } else if (date.matches("\\d{4}/\\d{1,2}/\\d{1,2}")) {
            String[] dateParts = date.split("/");
            calendar.set(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]) - 1, Integer.parseInt(dateParts[2]));
        } else if (date.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            String[] dateParts = date.split("-");
            calendar.set(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]) - 1, Integer.parseInt(dateParts[2]));
        } else if (date.matches("\\d+日後") || date.matches("\\d+d") || date.matches("\\+\\d+") || date.matches("\\+\\d+d")) {
            int days = Integer.parseInt(date.replaceAll("[^0-9]", ""));
            calendar.add(Calendar.DAY_OF_MONTH, days);
        } else if (date.matches("今日") || date.matches("today") || date.matches("tdy")) {
            // do nothing
        } else if (date.matches("明日") || date.matches("tomorrow") || date.matches("tmr")) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } else if (Arrays.asList(WEEKDAYS).contains(date)) {
            int dayOfWeek = Arrays.asList(WEEKDAYS).indexOf(date) % 7 + 1;
            int diff = dayOfWeek - calendar.get(Calendar.DAY_OF_WEEK);
            if (diff < 0) diff += 7;
            calendar.add(Calendar.DAY_OF_MONTH, diff);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid due date format");
        }

        return calendar;
    }
}
