package com.damzxyno.rasdspringapi.models;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeRange)) return false;
        TimeRange timeRange = (TimeRange) o;
        return Objects.equals(startTime, timeRange.startTime) &&
                Objects.equals(endTime, timeRange.endTime);
    }
}