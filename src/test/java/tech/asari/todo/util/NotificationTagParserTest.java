package tech.asari.todo.util;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationTagParserTest {

    @Test
    void testValidate() {
        assertTrue(NotificationTagParser.validate("1m"));
        assertTrue(NotificationTagParser.validate("30m"));
        assertTrue(NotificationTagParser.validate("1h"));
        assertTrue(NotificationTagParser.validate("3h"));
        assertTrue(NotificationTagParser.validate("1d"));
        assertTrue(NotificationTagParser.validate("100d"));
        assertTrue(NotificationTagParser.validate("1h45m"));
        assertTrue(NotificationTagParser.validate("1.75m"));

        assertFalse(NotificationTagParser.validate("1m45m"));
    }

    @Test
    void testSplitNotificationTag() {
        String[] parts = "1.5d1h45m".split("(?<=[a-zA-Z])");
        Stream.of(parts).forEach(System.out::println);
    }

    @Test
    void testParse() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // TODO: 小数計算を行ったときにミリ秒まで合っているか確認

        long time1 = sdf.parse("2024-04-28 12:00:00").getTime();
        assertEquals(sdf.format(NotificationTagParser.parse("1m", new Timestamp(time1))), "2024-04-28 11:59:00");
        assertEquals(sdf.format(NotificationTagParser.parse("30m", new Timestamp(time1))), "2024-04-28 11:30:00");
        assertEquals(sdf.format(NotificationTagParser.parse("1h", new Timestamp(time1))), "2024-04-28 11:00:00");
        assertEquals(sdf.format(NotificationTagParser.parse("1d", new Timestamp(time1))), "2024-04-27 12:00:00");
        assertEquals(sdf.format(NotificationTagParser.parse("1.5d", new Timestamp(time1))), "2024-04-27 00:00:00");
        assertEquals(sdf.format(NotificationTagParser.parse("1d12h", new Timestamp(time1))), "2024-04-27 00:00:00");

        long time2 = sdf.parse("2024-04-28 23:59:59").getTime();
        assertEquals(sdf.format(NotificationTagParser.parse("1m", new Timestamp(time2))), "2024-04-28 23:59:00");
        assertEquals(sdf.format(NotificationTagParser.parse("30m", new Timestamp(time2))), "2024-04-28 23:30:00");
        assertEquals(sdf.format(NotificationTagParser.parse("1h", new Timestamp(time2))), "2024-04-28 23:00:00");
        assertEquals(sdf.format(NotificationTagParser.parse("1d", new Timestamp(time2))), "2024-04-28 00:00:00");
        assertEquals(sdf.format(NotificationTagParser.parse("1.5d", new Timestamp(time2))), "2024-04-27 12:00:00");
        assertEquals(sdf.format(NotificationTagParser.parse("1d12h", new Timestamp(time2))), "2024-04-27 12:00:00");
    }
}
