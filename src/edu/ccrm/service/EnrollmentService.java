package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple Enrollment Service without complex dependencies
 * Manages student-course enrollments with basic functionality
 */
public class EnrollmentService {
    private final Map<String, Enrollment> enrollments;
    private int enrollmentCounter;

    public EnrollmentService() {
        this.enrollments = new HashMap<>();
        this.enrollmentCounter = 1000;
    }

    /**
     * Enroll student in a course (simplified version)
     */
    public void enrollStudent(String studentId, String courseCode) {
        // Check for duplicate enrollment
        String enrollmentKey = studentId + "-" + courseCode;
        if (enrollments.containsKey(enrollmentKey)) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        // Create enrollment
        String enrollmentId = "ENR" + (enrollmentCounter++);
        Enrollment enrollment = new Enrollment(enrollmentId, studentId, courseCode);
        enrollments.put(enrollmentKey, enrollment);

        System.out.println("✅ Student " + studentId + " enrolled in course " + courseCode);
    }

    /**
     * Unenroll student from course
     */
    public void unenrollStudent(String studentId, String courseCode, String reason) {
        String enrollmentKey = studentId + "-" + courseCode;
        Enrollment enrollment = enrollments.get(enrollmentKey);

        if (enrollment == null) {
            throw new RuntimeException("Enrollment not found for student " + 
                studentId + " in course " + courseCode);
        }

        enrollment.dropEnrollment(reason);
        System.out.println("✅ Student unenrolled from " + courseCode + ". Reason: " + reason);
    }

    /**
     * Assign grade to student for a course
     */
    public void assignGrade(String studentId, String courseCode, Grade grade) {
        String enrollmentKey = studentId + "-" + courseCode;
        Enrollment enrollment = enrollments.get(enrollmentKey);

        if (enrollment == null) {
            throw new RuntimeException("Enrollment not found");
        }

        if (!enrollment.isActive()) {
            throw new RuntimeException("Cannot assign grade to inactive enrollment");
        }

        enrollment.assignGrade(grade);
        System.out.println("✅ Grade " + grade.name() + " assigned to student " + 
            studentId + " for course " + courseCode);
    }

    /**
     * Get all enrollments for a student
     */
    public List<Enrollment> getStudentEnrollments(String studentId) {
        return enrollments.values().stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .sorted(Comparator.comparing(Enrollment::getEnrollmentDate))
                .collect(Collectors.toList());
    }

    /**
     * Get all enrollments for a course
     */
    public List<Enrollment> getCourseEnrollments(String courseCode) {
        return enrollments.values().stream()
                .filter(e -> e.getCourseCode().equals(courseCode))
                .sorted(Comparator.comparing(Enrollment::getEnrollmentDate))
                .collect(Collectors.toList());
    }

    /**
     * Get active enrollments only
     */
    public List<Enrollment> getActiveEnrollments() {
        return enrollments.values().stream()
                .filter(Enrollment::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Generate simple transcript for a student
     */
    public void generateTranscript(String studentId) {
        List<Enrollment> studentEnrollments = getStudentEnrollments(studentId);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("              STUDENT TRANSCRIPT");
        System.out.println("=".repeat(60));
        System.out.println("Student ID: " + studentId);
        System.out.println("\nEnrollment History:");
        System.out.println("-".repeat(40));

        if (studentEnrollments.isEmpty()) {
            System.out.println("No enrollments found.");
        } else {
            for (Enrollment enrollment : studentEnrollments) {
                System.out.printf("%-10s %-15s %-10s%n",
                    enrollment.getCourseCode(),
                    enrollment.getStatus().getDescription(),
                    enrollment.getAssignedGrade() != null ? 
                        enrollment.getAssignedGrade().name() : "Not Graded"
                );
            }
        }
        System.out.println("=".repeat(60));
    }

    /**
     * Display all enrollments
     */
    public void displayAllEnrollments() {
        if (enrollments.isEmpty()) {
            System.out.println("\n📋 No enrollments found in the system.");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("                ALL ENROLLMENTS (" + enrollments.size() + ")");
        System.out.println("=".repeat(70));

        enrollments.values().stream()
                .sorted(Comparator.comparing(Enrollment::getEnrollmentDate))
                .forEach(Enrollment::displayInfo);
    }

    /**
     * Get enrollment statistics
     */
    public void displayEnrollmentStatistics() {
        int totalEnrollments = enrollments.size();
        int activeEnrollments = (int) enrollments.values().stream()
                .filter(Enrollment::isActive)
                .count();
        int completedEnrollments = (int) enrollments.values().stream()
                .filter(Enrollment::isCompleted)
                .count();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("          ENROLLMENT STATISTICS");
        System.out.println("=".repeat(50));
        System.out.println("Total Enrollments     : " + totalEnrollments);
        System.out.println("Active Enrollments    : " + activeEnrollments);
        System.out.println("Completed Enrollments : " + completedEnrollments);
        System.out.println("Dropped Enrollments   : " + (totalEnrollments - activeEnrollments - completedEnrollments));
        System.out.println("=".repeat(50));
    }
}