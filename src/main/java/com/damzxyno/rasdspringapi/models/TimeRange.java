package com.damzxyno.rasdspringapi.models;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeRange {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private String startTime;
    private String endTime;

    public TimeRange(){}

    public TimeRange(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime.format(formatter);
        this.endTime = endTime.format(formatter);
    }


    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}