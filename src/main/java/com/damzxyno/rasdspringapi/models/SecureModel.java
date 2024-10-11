package com.damzxyno.rasdspringapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SecureModel {
    private Set<String> roles = new HashSet<>();
    private Set<String> permissions = new HashSet<>();
    private EnumMap<DayOfWeek, TimeRange> timeRulesAccept;
    private EnumMap<DayOfWeek, TimeRange> timeRulesRestrict;
    private Set<String> acceptedLocation;
    private Set<String> restrictedLocation;



    @JsonIgnore
    public boolean isEmptyStringAndAuthorisation(){
        return roles.isEmpty() && permissions.isEmpty();
    }
    private void initializeAcceptedLocation() {
        if (acceptedLocation == null) {
            acceptedLocation = new HashSet<>();
        }
    }

    private void initializeRestrictedLocation() {
        if (restrictedLocation == null) {
            restrictedLocation = new HashSet<>();
        }
    }

    public void addAcceptedLocation(String location) {
        initializeAcceptedLocation();  // Ensure the set is initialized
        acceptedLocation.add(location);  // Add the location
    }

    public void addAcceptedLocations(Set<String> locations) {
        initializeAcceptedLocation();  // Ensure the set is initialized
        acceptedLocation.addAll(locations);  // Add the locations
    }
    public void addAcceptedLocations(List<String> locations) {
        initializeAcceptedLocation();  // Ensure the set is initialized
        acceptedLocation.addAll(locations);  // Add the locations
    }

    public void addRestrictedLocation(String location) {
        initializeRestrictedLocation();  // Ensure the set is initialized
        restrictedLocation.add(location);  // Add the location
    }

    public void addRestrictedLocations(Set<String> locations) {
        initializeRestrictedLocation();  // Ensure the set is initialized
        restrictedLocation.addAll(locations);  // Add the locations
    }

    public void addRestrictedLocations(List<String> locations) {
        initializeRestrictedLocation();  // Ensure the set is initialized
        restrictedLocation.addAll(locations);  // Add the locations
    }

    public Set<String> getAcceptedLocation() {
        return acceptedLocation;
    }

    public Set<String> getRestrictedLocation() {
        return restrictedLocation;
    }


    public void addAcceptTimeRange(DayOfWeek day, TimeRange timeRange){
        if (timeRulesAccept == null) {
            timeRulesAccept = new EnumMap<>(DayOfWeek.class);
        }
        timeRulesAccept.put(day, timeRange);
    }

    public void addAcceptTimeRanges(EnumMap<DayOfWeek, TimeRange> timeRules){
        if (timeRulesAccept == null) {
            timeRulesAccept = new EnumMap<>(DayOfWeek.class);
        }
        timeRulesAccept.putAll(timeRules);
    }

    public void addRestrictAcceptTimeRange(DayOfWeek day, TimeRange timeRange){
        if (timeRulesRestrict == null) {
            timeRulesRestrict = new EnumMap<>(DayOfWeek.class);
        }
        timeRulesRestrict.put(day, timeRange);
    }

    public void addAcceptedTimeRanges(EnumMap<DayOfWeek, TimeRange> timeRules){
        if (timeRulesAccept == null) {
            timeRulesAccept = new EnumMap<>(DayOfWeek.class);
        }
        timeRulesAccept.putAll(timeRules);
    }
    public void addRestrictedTimeRanges(EnumMap<DayOfWeek, TimeRange> timeRules){
        if (timeRulesRestrict == null) {
            timeRulesRestrict = new EnumMap<>(DayOfWeek.class);
        }
        timeRulesRestrict.putAll(timeRules);
    }

    public EnumMap<DayOfWeek, TimeRange> getTimeRulesRestrict(){
        return this.timeRulesRestrict;
    }

    public EnumMap<DayOfWeek, TimeRange> getTimeRulesAccept(){
        return this.timeRulesAccept;
    }
    public void addRole (String role){
        roles.add(role);
    }

    public void addPermission (String permission){
        permissions.add(permission);
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public boolean equalsThisSecureModel (SecureModel secureModelA){
        return secureModelA.getPermissions().equals(this.permissions) &&
                secureModelA.getRoles().equals(this.roles);

    }


}
