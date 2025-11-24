package edu.ccrm.util;

import edu.ccrm.domain.*;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for data validation
 * Contains static methods for validating various data types
 */
public class Validators {

    // Email pattern for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Phone pattern (supports multiple formats)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^(\\+?1-?)?\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$"
    );

    // Student ID pattern (alphanumeric, 3-10 characters)
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile(
        "^[A-Za-z0-9]{3,10}$"
    );

    // Course code pattern (letters followed by numbers)
    private static final Pattern COURSE_CODE_PATTERN = Pattern.compile(
        "^[A-Z]{2,4}[0-9]{2,4}$"
    );

    /**
     * Validate email address format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validate student ID format
     */
    public static boolean isValidStudentId(String studentId) {
        return studentId != null && STUDENT_ID_PATTERN.matcher(studentId).matches();
    }

    /**
     * Validate course code format
     */
    public static boolean isValidCourseCode(String courseCode) {
        return courseCode != null && COURSE_CODE_PATTERN.matcher(courseCode.toUpperCase()).matches();
    }

    /**
     * Validate string is not null or empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validate string length is within range
     */
    public static boolean isLengthValid(String value, int minLength, int maxLength) {
        if (value == null) return false;
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Validate integer is within range
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Validate double is within range
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * Validate GPA (0.0 to 10.0)
     */
    public static boolean isValidGPA(double gpa) {
        return isInRange(gpa, 0.0, 10.0);
    }

    /**
     * Validate semester number (1 to 8)
     */
    public static boolean isValidSemester(int semester) {
        return isInRange(semester, 1, 8);
    }

    /**
     * Validate course credits (1 to 10)
     */
    public static boolean isValidCredits(int credits) {
        return isInRange(credits, 1, 10);
    }

    /**
     * Validate course capacity (1 to 500)
     */
    public static boolean isValidCapacity(int capacity) {
        return isInRange(capacity, 1, 500);
    }

    /**
     * Validate date string format
     */
    public static boolean isValidDate(String dateStr, String pattern) {
        if (dateStr == null || pattern == null) return false;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validate student data comprehensively
     */
    public static ValidationResult validateStudent(Student student) {
        ValidationResult result = new ValidationResult();

        // Validate ID
        if (!isValidStudentId(student.getId())) {
            result.addError("Student ID must be 3-10 alphanumeric characters");
        }

        // Validate name
        if (!isLengthValid(student.getFullName(), 2, 100)) {
            result.addError("Full name must be 2-100 characters long");
        }

        // Validate email
        if (!isValidEmail(student.getEmail())) {
            result.addError("Invalid email format");
        }

        // Validate registration number
        if (!isLengthValid(student.getRegNo(), 3, 20)) {
            result.addError("Registration number must be 3-20 characters long");
        }

        // Validate phone (if provided)
        if (student.getPhone() != null && !student.getPhone().isEmpty() && 
            !isValidPhone(student.getPhone())) {
            result.addError("Invalid phone number format");
        }

        // Validate semester
        if (!isValidSemester(student.getSemester())) {
            result.addError("Semester must be between 1 and 8");
        }

        // Validate GPA
        if (!isValidGPA(student.getGpa())) {
            result.addError("GPA must be between 0.0 and 10.0");
        }

        // Validate department (if provided)
        if (student.getDepartment() != null && !isLengthValid(student.getDepartment(), 2, 50)) {
            result.addError("Department name must be 2-50 characters long");
        }

        return result;
    }

    /**
     * Validate course data comprehensively
     */
    public static ValidationResult validateCourse(Course course) {
        ValidationResult result = new ValidationResult();

        // Validate course code
        if (!isValidCourseCode(course.getCode())) {
            result.addError("Course code must follow pattern: 2-4 letters followed by 2-4 numbers (e.g., CS101)");
        }

        // Validate title
        if (!isLengthValid(course.getTitle(), 3, 100)) {
            result.addError("Course title must be 3-100 characters long");
        }

        // Validate credits
        if (!isValidCredits(course.getCredits())) {
            result.addError("Course credits must be between 1 and 10");
        }

        // Validate instructor
        if (!isLengthValid(course.getInstructor(), 2, 50)) {
            result.addError("Instructor name must be 2-50 characters long");
        }

        // Validate department
        if (!isLengthValid(course.getDepartment(), 2, 50)) {
            result.addError("Department name must be 2-50 characters long");
        }

        // Validate capacity
        if (!isValidCapacity(course.getMaxCapacity())) {
            result.addError("Course capacity must be between 1 and 500");
        }

        // Validate description (if provided)
        if (course.getDescription() != null && course.getDescription().length() > 500) {
            result.addError("Course description must not exceed 500 characters");
        }

        return result;
    }

    /**
     * Sanitize string input (remove extra spaces, trim)
     */
    public static String sanitizeString(String input) {
        if (input == null) return null;

        return input.trim()
                   .replaceAll("\\s+", " ")  // Replace multiple spaces with single space
                   .replaceAll("[\\r\\n\\t]", " ");  // Replace line breaks and tabs with spaces
    }

    /**
     * Sanitize and validate string input
     */
    public static String sanitizeAndValidate(String input, int maxLength) {
        String sanitized = sanitizeString(input);

        if (sanitized == null || sanitized.isEmpty()) {
            return sanitized;
        }

        if (sanitized.length() > maxLength) {
            sanitized = sanitized.substring(0, maxLength);
        }

        return sanitized;
    }

    /**
     * Check if string contains only alphabetic characters and spaces
     */
    public static boolean isAlphabeticWithSpaces(String value) {
        return value != null && value.matches("^[a-zA-Z\\s]+$");
    }

    /**
     * Check if string contains only alphanumeric characters
     */
    public static boolean isAlphanumeric(String value) {
        return value != null && value.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * Inner class for validation results
     */
    public static class ValidationResult {
        private final java.util.List<String> errors = new java.util.ArrayList<>();

        public void addError(String error) {
            errors.add(error);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public java.util.List<String> getErrors() {
            return new java.util.ArrayList<>(errors);
        }

        public String getErrorsAsString() {
            return String.join("; ", errors);
        }

        public void displayErrors() {
            if (!errors.isEmpty()) {
                System.out.println("❌ Validation Errors:");
                for (int i = 0; i < errors.size(); i++) {
                    System.out.println("  " + (i + 1) + ". " + errors.get(i));
                }
            }
        }
    }
}