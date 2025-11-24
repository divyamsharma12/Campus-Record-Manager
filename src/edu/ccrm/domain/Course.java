package edu.ccrm.domain;

import java.time.LocalTime;
import java.util.*;

/**
 * Course class with Builder Pattern implementation
 * Demonstrates Builder Design Pattern and composition
 */
public class Course {
    private final String code;
    private final String title;
    private final int credits;
    private final String instructor;
    private final Semester semester;
    private final String department;
    private final String description;
    private final int maxCapacity;
    private final List<String> prerequisites;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Set<String> enrolledStudents;

    // Private constructor - only accessible through Builder
    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.semester = builder.semester;
        this.department = builder.department;
        this.description = builder.description;
        this.maxCapacity = builder.maxCapacity;
        this.prerequisites = new ArrayList<>(builder.prerequisites);
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.enrolledStudents = new HashSet<>();
    }

    // Builder Pattern Implementation
    public static class Builder {
        // Required parameters
        private final String code;
        private final String title;
        private final int credits;

        // Optional parameters with default values
        private String instructor = "TBA";
        private Semester semester = Semester.SPRING;
        private String department = "General";
        private String description = "";
        private int maxCapacity = 50;
        private List<String> prerequisites = new ArrayList<>();
        private LocalTime startTime = LocalTime.of(9, 0);
        private LocalTime endTime = LocalTime.of(10, 0);

        public Builder(String code, String title, int credits) {
            this.code = code;
            this.title = title;
            this.credits = credits;
        }

        public Builder setInstructor(String instructor) {
            this.instructor = instructor;
            return this;
        }

        public Builder setSemester(Semester semester) {
            this.semester = semester;
            return this;
        }

        public Builder setDepartment(String department) {
            this.department = department;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setMaxCapacity(int maxCapacity) {
            this.maxCapacity = maxCapacity;
            return this;
        }

        public Builder addPrerequisite(String prerequisite) {
            this.prerequisites.add(prerequisite);
            return this;
        }

        public Builder setPrerequisites(List<String> prerequisites) {
            this.prerequisites = new ArrayList<>(prerequisites);
            return this;
        }

        public Builder setSchedule(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }

    // Business methods
    public boolean addStudent(String studentId) {
        if (enrolledStudents.size() >= maxCapacity) {
            System.out.println("Course is at full capacity");
            return false;
        }

        if (enrolledStudents.add(studentId)) {
            System.out.println("Student " + studentId + " enrolled in " + code);
            return true;
        }
        return false;
    }

    public boolean removeStudent(String studentId) {
        if (enrolledStudents.remove(studentId)) {
            System.out.println("Student " + studentId + " removed from " + code);
            return true;
        }
        return false;
    }

    public boolean isFull() {
        return enrolledStudents.size() >= maxCapacity;
    }

    public int getAvailableSpots() {
        return maxCapacity - enrolledStudents.size();
    }

    public void displayInfo() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                COURSE INFORMATION");
        System.out.println("=".repeat(60));
        System.out.println("Course Code     : " + code);
        System.out.println("Title          : " + title);
        System.out.println("Credits        : " + credits);
        System.out.println("Instructor     : " + instructor);
        System.out.println("Department     : " + department);
        System.out.println("Semester       : " + semester.getName());
        System.out.println("Schedule       : " + startTime + " - " + endTime);
        System.out.println("Capacity       : " + enrolledStudents.size() + "/" + maxCapacity);
        System.out.println("Available Spots: " + getAvailableSpots());
        if (!description.isEmpty()) {
            System.out.println("Description    : " + description);
        }
        if (!prerequisites.isEmpty()) {
            System.out.println("Prerequisites  : " + String.join(", ", prerequisites));
        }
        System.out.println("=".repeat(60));
    }

    // Getters (no setters - immutable object)
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getInstructor() { return instructor; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }
    public String getDescription() { return description; }
    public int getMaxCapacity() { return maxCapacity; }
    public List<String> getPrerequisites() { return new ArrayList<>(prerequisites); }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public Set<String> getEnrolledStudents() { return new HashSet<>(enrolledStudents); }

    @Override
    public String toString() {
        return code + ": " + title + " (" + credits + " credits) - " + instructor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return code.equals(course.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}