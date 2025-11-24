package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Student class extending Person - demonstrates Inheritance
 */
public class Student extends Person {
    private String regNo;
    private StudentStatus status;
    private String department;
    private int semester;
    private List<String> enrolledCourses;
    private Map<String, Grade> courseGrades;
    private LocalDateTime enrollmentDate;
    private double gpa;

    // Constructors
    public Student(String id, String fullName, String email, String regNo) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.status = StudentStatus.ACTIVE;
        this.enrolledCourses = new ArrayList<>();
        this.courseGrades = new HashMap<>();
        this.enrollmentDate = LocalDateTime.now();
        this.gpa = 0.0;
        this.semester = 1;
    }

    public Student(String id, String fullName, String email, String regNo, 
                  String department, int semester) {
        this(id, fullName, email, regNo);
        this.department = department;
        this.semester = semester;
    }

    // Polymorphism: Override abstract method from Person
    @Override
    public void displayInfo() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("              STUDENT INFORMATION");
        System.out.println("=".repeat(50));
        System.out.println("Student ID       : " + getId());
        System.out.println("Registration No  : " + regNo);
        System.out.println("Full Name        : " + getFullName());
        System.out.println("Email           : " + getEmail());
        System.out.println("Phone           : " + (getPhone() != null ? getPhone() : "Not provided"));
        System.out.println("Department      : " + (department != null ? department : "Not assigned"));
        System.out.println("Semester        : " + semester);
        System.out.println("Status          : " + status.getDescription());
        System.out.println("GPA             : " + String.format("%.2f", gpa));
        System.out.println("Enrollment Date : " + getFormattedCreatedDate());
        System.out.println("Enrolled Courses: " + enrolledCourses.size());
        if (!enrolledCourses.isEmpty()) {
            System.out.println("Course List     : " + String.join(", ", enrolledCourses));
        }
        System.out.println("=".repeat(50));
    }

    @Override
    public boolean isValid() {
        return regNo != null && !regNo.trim().isEmpty() && 
               getFullName() != null && !getFullName().trim().isEmpty() &&
               getEmail() != null && getEmail().contains("@");
    }

    // Method overloading - Polymorphism
    public void enroll(String courseCode) {
        if (!enrolledCourses.contains(courseCode)) {
            enrolledCourses.add(courseCode);
            System.out.println("Student enrolled in course: " + courseCode);
        } else {
            System.out.println("Student already enrolled in course: " + courseCode);
        }
    }

    public void enroll(Course course) {
        enroll(course.getCode());
    }

    public boolean unenroll(String courseCode) {
        if (enrolledCourses.remove(courseCode)) {
            courseGrades.remove(courseCode);
            recalculateGPA();
            System.out.println("Student unenrolled from course: " + courseCode);
            return true;
        }
        return false;
    }

    public void assignGrade(String courseCode, Grade grade) {
        if (enrolledCourses.contains(courseCode)) {
            courseGrades.put(courseCode, grade);
            recalculateGPA();
            System.out.println("Grade " + grade.name() + " assigned for course: " + courseCode);
        } else {
            System.out.println("Student not enrolled in course: " + courseCode);
        }
    }

    private void recalculateGPA() {
        if (courseGrades.isEmpty()) {
            gpa = 0.0;
            return;
        }

        double totalPoints = courseGrades.values().stream()
                .mapToDouble(Grade::getGradePoints)
                .sum();

        gpa = totalPoints / courseGrades.size();
    }

    public List<String> getCoursesWithGrades() {
        return courseGrades.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue().name() + 
                     " (" + entry.getValue().getDescription() + ")")
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }

    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public List<String> getEnrolledCourses() { return new ArrayList<>(enrolledCourses); }

    public Map<String, Grade> getCourseGrades() { return new HashMap<>(courseGrades); }

    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }

    public double getGpa() { return gpa; }

    @Override
    public String toString() {
        return "Student{" +
                "regNo='" + regNo + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", status=" + status +
                ", gpa=" + String.format("%.2f", gpa) +
                ", enrolledCourses=" + enrolledCourses.size() +
                '}';
    }
}