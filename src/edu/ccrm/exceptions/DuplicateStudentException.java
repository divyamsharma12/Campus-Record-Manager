package edu.ccrm.exceptions;

/**
 * Custom exception for duplicate student scenarios
 */
public class DuplicateStudentException extends RuntimeException {
    private String studentId;
    private String regNo;

    public DuplicateStudentException(String message) {
        super(message);
    }

    public DuplicateStudentException(String message, String studentId) {
        super(message);
        this.studentId = studentId;
    }

    public DuplicateStudentException(String message, String studentId, String regNo) {
        super(message);
        this.studentId = studentId;
        this.regNo = regNo;
    }

    public String getStudentId() { return studentId; }
    public String getRegNo() { return regNo; }
}

/**
 * Custom exception for student not found scenarios
 */
class StudentNotFoundException extends RuntimeException {
    private String studentId;

    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(String message, String studentId) {
        super(message);
        this.studentId = studentId;
    }

    public String getStudentId() { return studentId; }
}

/**
 * Custom exception for duplicate course scenarios
 */
class DuplicateCourseException extends RuntimeException {
    private String courseCode;

    public DuplicateCourseException(String message) {
        super(message);
    }

    public DuplicateCourseException(String message, String courseCode) {
        super(message);
        this.courseCode = courseCode;
    }

    public String getCourseCode() { return courseCode; }
}

/**
 * Custom exception for course not found scenarios
 */
class CourseNotFoundException extends RuntimeException {
    private String courseCode;

    public CourseNotFoundException(String message) {
        super(message);
    }

    public CourseNotFoundException(String message, String courseCode) {
        super(message);
        this.courseCode = courseCode;
    }

    public String getCourseCode() { return courseCode; }
}

/**
 * Custom exception for duplicate enrollment scenarios
 */
class DuplicateEnrollmentException extends RuntimeException {
    private String studentId;
    private String courseCode;

    public DuplicateEnrollmentException(String message, String studentId, String courseCode) {
        super(message);
        this.studentId = studentId;
        this.courseCode = courseCode;
    }

    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
}

/**
 * Custom exception for enrollment not found scenarios
 */
class EnrollmentNotFoundException extends RuntimeException {
    private String enrollmentId;

    public EnrollmentNotFoundException(String message) {
        super(message);
    }

    public EnrollmentNotFoundException(String message, String enrollmentId) {
        super(message);
        this.enrollmentId = enrollmentId;
    }

    public String getEnrollmentId() { return enrollmentId; }
}

/**
 * Custom exception for credit limit exceeded scenarios
 */
class MaxCreditLimitExceededException extends RuntimeException {
    private int currentCredits;
    private int maxCredits;
    private int attemptedCredits;

    public MaxCreditLimitExceededException(String message, int attemptedCredits, int maxCredits) {
        super(message);
        this.attemptedCredits = attemptedCredits;
        this.maxCredits = maxCredits;
    }

    public MaxCreditLimitExceededException(String message, int currentCredits, 
                                          int attemptedCredits, int maxCredits) {
        super(message);
        this.currentCredits = currentCredits;
        this.attemptedCredits = attemptedCredits;
        this.maxCredits = maxCredits;
    }

    public int getCurrentCredits() { return currentCredits; }
    public int getMaxCredits() { return maxCredits; }
    public int getAttemptedCredits() { return attemptedCredits; }
}

/**
 * Custom exception for file operations
 */
class FileOperationException extends RuntimeException {
    private String fileName;
    private String operation;

    public FileOperationException(String message, String fileName, String operation) {
        super(message);
        this.fileName = fileName;
        this.operation = operation;
    }

    public FileOperationException(String message, String fileName, String operation, Throwable cause) {
        super(message, cause);
        this.fileName = fileName;
        this.operation = operation;
    }

    public String getFileName() { return fileName; }
    public String getOperation() { return operation; }
}

/**
 * Custom exception for data validation errors
 */
class DataValidationException extends RuntimeException {
    private String fieldName;
    private String fieldValue;

    public DataValidationException(String message) {
        super(message);
    }

    public DataValidationException(String message, String fieldName, String fieldValue) {
        super(message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() { return fieldName; }
    public String getFieldValue() { return fieldValue; }
}