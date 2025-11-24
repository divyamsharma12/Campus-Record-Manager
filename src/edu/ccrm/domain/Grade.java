package edu.ccrm.domain;

/**
 * Enum representing academic grades with grade points
 * Demonstrates advanced enum features
 */
public enum Grade {
    S("Excellent", 10.0, "Outstanding performance"),
    A("Very Good", 9.0, "Very good performance"),
    B("Good", 8.0, "Good performance"),
    C("Average", 7.0, "Satisfactory performance"),
    D("Below Average", 6.0, "Below average performance"),
    F("Fail", 0.0, "Unsatisfactory performance"),
    I("Incomplete", 0.0, "Course work incomplete"),
    W("Withdrawn", 0.0, "Withdrawn from course");

    private final String description;
    private final double gradePoints;
    private final String remarks;

    // Constructor
    private Grade(String description, double gradePoints, String remarks) {
        this.description = description;
        this.gradePoints = gradePoints;
        this.remarks = remarks;
    }

    // Getters
    public String getDescription() { return description; }
    public double getGradePoints() { return gradePoints; }
    public String getRemarks() { return remarks; }

    // Utility methods
    public boolean isPassing() {
        return gradePoints >= 6.0;
    }

    public boolean isExcellent() {
        return gradePoints >= 9.0;
    }

    public String getGradeLetter() {
        return this.name();
    }

    // Static method to get grade from points
    public static Grade fromGradePoints(double points) {
        if (points >= 10.0) return S;
        if (points >= 9.0) return A;
        if (points >= 8.0) return B;
        if (points >= 7.0) return C;
        if (points >= 6.0) return D;
        return F;
    }

    @Override
    public String toString() {
        return name() + " (" + description + ") - " + gradePoints + " points";
    }
}