package edu.ccrm.domain;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Instructor class extending Person
 * Demonstrates inheritance and polymorphism
 */
public class Instructor extends Person {
    private String employeeId;
    private String department;
    private String designation;
    private List<String> taughtCourses;
    private double salary;
    private InstructorStatus status;

    public Instructor(String id, String fullName, String email, String employeeId) {
        super(id, fullName, email);
        this.employeeId = employeeId;
        this.taughtCourses = new ArrayList<>();
        this.status = InstructorStatus.ACTIVE;
        this.salary = 0.0;
    }

    public Instructor(String id, String fullName, String email, String employeeId,
                     String department, String designation) {
        this(id, fullName, email, employeeId);
        this.department = department;
        this.designation = designation;
    }

    @Override
    public void displayInfo() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            INSTRUCTOR INFORMATION");
        System.out.println("=".repeat(50));
        System.out.println("Instructor ID   : " + getId());
        System.out.println("Employee ID     : " + employeeId);
        System.out.println("Full Name       : " + getFullName());
        System.out.println("Email          : " + getEmail());
        System.out.println("Phone          : " + (getPhone() != null ? getPhone() : "Not provided"));
        System.out.println("Department     : " + (department != null ? department : "Not assigned"));
        System.out.println("Designation    : " + (designation != null ? designation : "Not assigned"));
        System.out.println("Status         : " + status.getDescription());
        System.out.println("Courses Taught : " + taughtCourses.size());
        if (!taughtCourses.isEmpty()) {
            System.out.println("Course List    : " + String.join(", ", taughtCourses));
        }
        System.out.println("=".repeat(50));
    }

    @Override
    public boolean isValid() {
        return employeeId != null && !employeeId.trim().isEmpty() &&
               getFullName() != null && !getFullName().trim().isEmpty() &&
               getEmail() != null && getEmail().contains("@");
    }

    // Method overloading
    public void assignCourse(String courseCode) {
        if (!taughtCourses.contains(courseCode)) {
            taughtCourses.add(courseCode);
            System.out.println("Course " + courseCode + " assigned to instructor " + getFullName());
        }
    }

    public void assignCourse(Course course) {
        assignCourse(course.getCode());
    }

    public boolean removeCourse(String courseCode) {
        if (taughtCourses.remove(courseCode)) {
            System.out.println("Course " + courseCode + " removed from instructor " + getFullName());
            return true;
        }
        return false;
    }

    public List<String> getCoursesByDepartment(String dept) {
        // This would typically filter by actual course objects
        // For now, return all courses (placeholder implementation)
        return new ArrayList<>(taughtCourses);
    }

    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public List<String> getTaughtCourses() { return new ArrayList<>(taughtCourses); }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public InstructorStatus getStatus() { return status; }
    public void setStatus(InstructorStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Instructor{" +
                "employeeId='" + employeeId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", department='" + department + '\'' +
                ", designation='" + designation + '\'' +
                ", coursesCount=" + taughtCourses.size() +
                '}';
    }

    // Nested enum for instructor status
    public enum InstructorStatus {
        ACTIVE("Active", "Currently teaching"),
        ON_LEAVE("On Leave", "Temporarily away"),
        RETIRED("Retired", "No longer teaching"),
        TERMINATED("Terminated", "Employment ended");

        private final String description;
        private final String details;

        private InstructorStatus(String description, String details) {
            this.description = description;
            this.details = details;
        }

        public String getDescription() { return description; }
        public String getDetails() { return details; }
    }
}