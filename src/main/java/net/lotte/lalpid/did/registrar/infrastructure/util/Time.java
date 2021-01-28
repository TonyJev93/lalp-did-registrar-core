package net.lotte.lalpid.did.registrar.infrastructure.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Time {
    // 현제 시간을 W3C 표준에 맞는 포맷으로 return
    public static String getCurrentDateTimeForW3CFormat() {

        ZonedDateTime ldtZoned = LocalDateTime.now().atZone(ZoneId.systemDefault());
        ZonedDateTime utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZoned.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    }
}
