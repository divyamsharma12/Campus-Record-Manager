package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enrollment class representing the association between Student and Course
 * Demonstrates composition and association relationships
 */
public class Enrollment {
    private String enrollmentId;
    private String studentId;
    private String courseCode;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;
    private Grade assignedGrade;
    private String remarks;

    public Enrollment(String enrollmentId, String studentId, String courseCode) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.enrollmentDate = LocalDateTime.now();
        this.status = EnrollmentStatus.ENROLLED;
        this.assignedGrade = null;
        this.remarks = "";
    }

    public void assignGrade(Grade grade) {
        this.assignedGrade = grade;
        if (grade != null) {
            this.status = EnrollmentStatus.COMPLETED;
        }
    }

    public void dropEnrollment(String reason) {
        this.status = EnrollmentStatus.DROPPED;
        this.remarks = reason;
    }

    public boolean isActive() {
        return status == EnrollmentStatus.ENROLLED;
    }

    public boolean isCompleted() {
        return status == EnrollmentStatus.COMPLETED && assignedGrade != null;
    }

    public void displayInfo() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("        ENROLLMENT DETAILS");
        System.out.println("-".repeat(40));
        System.out.println("Enrollment ID : " + enrollmentId);
        System.out.println("Student ID    : " + studentId);
        System.out.println("Course Code   : " + courseCode);
        System.out.println("Enrolled Date : " + enrollmentDate.format(
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        System.out.println("Status        : " + status.getDescription());
        if (assignedGrade != null) {
            System.out.println("Grade         : " + assignedGrade.name() + 
                             " (" + assignedGrade.getDescription() + ")");
        }
        if (!remarks.isEmpty()) {
            System.out.println("Remarks       : " + remarks);
        }
        System.out.println("-".repeat(40));
    }

    // Getters and Setters
    public String getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(String enrollmentId) { this.enrollmentId = enrollmentId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }

    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }

    public Grade getAssignedGrade() { return assignedGrade; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId='" + enrollmentId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", status=" + status +
                ", grade=" + (assignedGrade != null ? assignedGrade.name() : "Not assigned") +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Enrollment that = (Enrollment) obj;
        return enrollmentId.equals(that.enrollmentId);
    }

    @Override
    public int hashCode() {
        return enrollmentId.hashCode();
    }

    // Nested enum for enrollment status
    public enum EnrollmentStatus {
        ENROLLED("Enrolled", "Currently enrolled in course"),
        COMPLETED("Completed", "Course completed with grade"),
        DROPPED("Dropped", "Student dropped the course"),
        FAILED("Failed", "Failed to complete course requirements"),
        WITHDRAWN("Withdrawn", "Officially withdrawn from course");

        private final String description;
        private final String details;

        private EnrollmentStatus(String description, String details) {
            this.description = description;
            this.details = details;
        }

        public String getDescription() { return description; }
        public String getDetails() { return details; }
    }
}