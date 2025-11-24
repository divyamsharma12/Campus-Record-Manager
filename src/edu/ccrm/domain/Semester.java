package edu.ccrm.domain;

import java.time.Month;

/**
 * Enum representing academic semesters
 */
public enum Semester {
    SPRING("Spring", 1, Month.JANUARY, Month.MAY, "January to May"),
    SUMMER("Summer", 2, Month.JUNE, Month.AUGUST, "June to August"),
    FALL("Fall", 3, Month.SEPTEMBER, Month.DECEMBER, "September to December");

    private final String name;
    private final int order;
    private final Month startMonth;
    private final Month endMonth;
    private final String duration;

    private Semester(String name, int order, Month startMonth, Month endMonth, String duration) {
        this.name = name;
        this.order = order;
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        this.duration = duration;
    }

    // Getters
    public String getName() { return name; }
    public int getOrder() { return order; }
    public Month getStartMonth() { return startMonth; }
    public Month getEndMonth() { return endMonth; }
    public String getDuration() { return duration; }

    // Utility methods
    public Semester getNext() {
        return switch (this) {
            case SPRING -> SUMMER;
            case SUMMER -> FALL;
            case FALL -> SPRING;
        };
    }

    public Semester getPrevious() {
        return switch (this) {
            case SPRING -> FALL;
            case SUMMER -> SPRING;
            case FALL -> SUMMER;
        };
    }

    @Override
    public String toString() {
        return name + " Semester (" + duration + ")";
    }
}