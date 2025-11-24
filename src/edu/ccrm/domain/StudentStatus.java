package edu.ccrm.domain;

/**
 * Enum representing different student statuses
 * Demonstrates Enum with constructor and methods
 */
public enum StudentStatus {
    ACTIVE("Active", "Currently enrolled and attending classes"),
    INACTIVE("Inactive", "Temporarily not attending classes"),
    GRADUATED("Graduated", "Successfully completed all requirements"),
    SUSPENDED("Suspended", "Temporarily prohibited from attending"),
    DROPPED_OUT("Dropped Out", "Voluntarily left the institution"),
    EXPELLED("Expelled", "Permanently removed from institution");

    private final String description;
    private final String details;

    // Enum constructor (always private)
    private StudentStatus(String description, String details) {
        this.description = description;
        this.details = details;
    }

    // Methods
    public String getDescription() {
        return description;
    }

    public String getDetails() {
        return details;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canEnrollInCourses() {
        return this == ACTIVE;
    }

    @Override
    public String toString() {
        return description + " - " + details;
    }
}