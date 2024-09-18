package com.damzxyno.rasdspringapi.core.interfaces;

import com.damzxyno.rasdspringapi.models.TimeRange;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.List;

public interface SecurityMapperProxy {
    void addLocationAccepted (String [] pattern, RequestMethod method, List<String> locations);
    void addLocationRestricted (String [] pattern, RequestMethod method, List<String> restricted);
    void addTimeAccepted (String [] pattern, RequestMethod method, EnumMap<DayOfWeek, TimeRange> acceptedTime);
    void addTimeRestricted (String [] pattern, RequestMethod method, EnumMap<DayOfWeek, TimeRange> restrictedTime);

}
