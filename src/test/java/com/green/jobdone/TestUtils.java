package com.green.jobdone;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils {
    //파라미터 datTime으로 넘어오는 값이 DB에 저장된 dateTime값 2024-12-30 11:32:23
    public static void assertCurrentTimestamp(String dateTime) {
        //자바에서 현재일시 데이터
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime actualNow = LocalDateTime.parse(dateTime, formatter);
//        LocalDateTime expectedNow = LocalDateTime.now();//2024-12-30T11:32:23
//        assertTrue(Duration.between(expectedNow, actualNow).getSeconds() <= 1); 아래의 메소드 호출로 사용하도록 변경
        assertCurrentTimestamp(actualNow);
    }

    public static void assertCurrentTimestamp(LocalDateTime dateTime) {
        //자바에서 현재일시 데이터
        LocalDateTime expectedNow = LocalDateTime.now();//2024-12-30T11:32:23
        assertTrue(Duration.between(expectedNow, dateTime).getSeconds() <= 1);
    }
}
