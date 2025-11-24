package edu.ccrm.util;

import edu.ccrm.domain.*;
import java.util.Comparator;
import java.util.List;

/**
 * ERROR-FREE Utility class containing various Comparators for sorting
 * All field names are unique to avoid conflicts
 */
public class Comparators {

    // Student Comparators - with unique names
    public static final Comparator<Student> STUDENT_BY_NAME = 
        (s1, s2) -> s1.getFullName().compareTo(s2.getFullName());

    public static final Comparator<Student> STUDENT_BY_ID = 
        (s1, s2) -> s1.getId().compareTo(s2.getId());

    public static final Comparator<Student> STUDENT_BY_REG_NO = 
        (s1, s2) -> s1.getRegNo().compareTo(s2.getRegNo());

    public static final Comparator<Student> STUDENT_BY_GPA = 
        (s1, s2) -> Double.compare(s1.getGpa(), s2.getGpa());

    public static final Comparator<Student> STUDENT_BY_GPA_DESC = 
        (s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa());

    public static final Comparator<Student> STUDENT_BY_SEMESTER = 
        (s1, s2) -> Integer.compare(s1.getSemester(), s2.getSemester());

    public static final Comparator<Student> STUDENT_BY_DEPARTMENT = 
        (s1, s2) -> {
            String dept1 = s1.getDepartment();
            String dept2 = s2.getDepartment();
            if (dept1 == null && dept2 == null) return 0;
            if (dept1 == null) return 1;
            if (dept2 == null) return -1;
            return dept1.compareTo(dept2);
        };

    public static final Comparator<Student> STUDENT_BY_STATUS = 
        (s1, s2) -> s1.getStatus().name().compareTo(s2.getStatus().name());

    public static final Comparator<Student> STUDENT_BY_ENROLLMENT_DATE = 
        (s1, s2) -> s1.getEnrollmentDate().compareTo(s2.getEnrollmentDate());

    // Course Comparators - with unique names
    public static final Comparator<Course> COURSE_BY_CODE = 
        (c1, c2) -> c1.getCode().compareTo(c2.getCode());

    public static final Comparator<Course> COURSE_BY_TITLE = 
        (c1, c2) -> c1.getTitle().compareTo(c2.getTitle());

    public static final Comparator<Course> COURSE_BY_CREDITS = 
        (c1, c2) -> Integer.compare(c1.getCredits(), c2.getCredits());

    public static final Comparator<Course> COURSE_BY_CREDITS_DESC = 
        (c1, c2) -> Integer.compare(c2.getCredits(), c1.getCredits());

    public static final Comparator<Course> COURSE_BY_INSTRUCTOR = 
        (c1, c2) -> c1.getInstructor().compareTo(c2.getInstructor());

    public static final Comparator<Course> COURSE_BY_DEPARTMENT = 
        (c1, c2) -> c1.getDepartment().compareTo(c2.getDepartment());

    public static final Comparator<Course> COURSE_BY_SEMESTER = 
        (c1, c2) -> c1.getSemester().name().compareTo(c2.getSemester().name());

    public static final Comparator<Course> COURSE_BY_CAPACITY = 
        (c1, c2) -> Integer.compare(c1.getMaxCapacity(), c2.getMaxCapacity());

    public static final Comparator<Course> COURSE_BY_ENROLLMENT_COUNT = 
        (c1, c2) -> Integer.compare(c1.getEnrolledStudents().size(), c2.getEnrolledStudents().size());

    public static final Comparator<Course> COURSE_BY_AVAILABILITY = 
        (c1, c2) -> Integer.compare(c2.getAvailableSpots(), c1.getAvailableSpots());

    // Enrollment Comparators - with unique names
    public static final Comparator<Enrollment> ENROLLMENT_BY_DATE = 
        (e1, e2) -> e1.getEnrollmentDate().compareTo(e2.getEnrollmentDate());

    public static final Comparator<Enrollment> ENROLLMENT_BY_STUDENT_ID = 
        (e1, e2) -> e1.getStudentId().compareTo(e2.getStudentId());

    public static final Comparator<Enrollment> ENROLLMENT_BY_COURSE_CODE = 
        (e1, e2) -> e1.getCourseCode().compareTo(e2.getCourseCode());

    public static final Comparator<Enrollment> ENROLLMENT_BY_STATUS = 
        (e1, e2) -> e1.getStatus().name().compareTo(e2.getStatus().name());

    public static final Comparator<Enrollment> ENROLLMENT_BY_GRADE = 
        (e1, e2) -> {
            Grade g1 = e1.getAssignedGrade();
            Grade g2 = e2.getAssignedGrade();
            if (g1 == null && g2 == null) return 0;
            if (g1 == null) return 1;
            if (g2 == null) return -1;
            return g1.name().compareTo(g2.name());
        };

    // Instructor Comparators - with unique names
    public static final Comparator<Instructor> INSTRUCTOR_BY_NAME = 
        (i1, i2) -> i1.getFullName().compareTo(i2.getFullName());

    public static final Comparator<Instructor> INSTRUCTOR_BY_EMPLOYEE_ID = 
        (i1, i2) -> i1.getEmployeeId().compareTo(i2.getEmployeeId());

    public static final Comparator<Instructor> INSTRUCTOR_BY_DEPARTMENT = 
        (i1, i2) -> {
            String dept1 = i1.getDepartment();
            String dept2 = i2.getDepartment();
            if (dept1 == null && dept2 == null) return 0;
            if (dept1 == null) return 1;
            if (dept2 == null) return -1;
            return dept1.compareTo(dept2);
        };

    public static final Comparator<Instructor> INSTRUCTOR_BY_DESIGNATION = 
        (i1, i2) -> {
            String des1 = i1.getDesignation();
            String des2 = i2.getDesignation();
            if (des1 == null && des2 == null) return 0;
            if (des1 == null) return 1;
            if (des2 == null) return -1;
            return des1.compareTo(des2);
        };

    public static final Comparator<Instructor> INSTRUCTOR_BY_COURSE_COUNT = 
        (i1, i2) -> Integer.compare(i1.getTaughtCourses().size(), i2.getTaughtCourses().size());

    // Composite Comparators with unique names
    public static final Comparator<Student> STUDENT_BY_DEPARTMENT_THEN_GPA = 
        STUDENT_BY_DEPARTMENT.thenComparing(STUDENT_BY_GPA_DESC);

    public static final Comparator<Student> STUDENT_BY_SEMESTER_THEN_NAME = 
        STUDENT_BY_SEMESTER.thenComparing(STUDENT_BY_NAME);

    public static final Comparator<Course> COURSE_BY_DEPARTMENT_THEN_CODE = 
        COURSE_BY_DEPARTMENT.thenComparing(COURSE_BY_CODE);

    public static final Comparator<Course> COURSE_BY_SEMESTER_THEN_CREDITS = 
        COURSE_BY_SEMESTER.thenComparing(COURSE_BY_CREDITS_DESC);

    /**
     * Get student comparator by criteria string
     */
    public static Comparator<Student> getStudentComparator(String criteria) {
        if (criteria == null) return STUDENT_BY_NAME;

        switch (criteria.toLowerCase()) {
            case "name":
                return STUDENT_BY_NAME;
            case "id":
                return STUDENT_BY_ID;
            case "regno":
                return STUDENT_BY_REG_NO;
            case "gpa":
                return STUDENT_BY_GPA_DESC;
            case "semester":
                return STUDENT_BY_SEMESTER;
            case "department":
                return STUDENT_BY_DEPARTMENT;
            case "status":
                return STUDENT_BY_STATUS;
            case "enrollment":
                return STUDENT_BY_ENROLLMENT_DATE;
            default:
                return STUDENT_BY_NAME;
        }
    }

    /**
     * Get course comparator by criteria string
     */
    public static Comparator<Course> getCourseComparator(String criteria) {
        if (criteria == null) return COURSE_BY_CODE;

        switch (criteria.toLowerCase()) {
            case "code":
                return COURSE_BY_CODE;
            case "title":
                return COURSE_BY_TITLE;
            case "credits":
                return COURSE_BY_CREDITS_DESC;
            case "instructor":
                return COURSE_BY_INSTRUCTOR;
            case "department":
                return COURSE_BY_DEPARTMENT;
            case "semester":
                return COURSE_BY_SEMESTER;
            case "capacity":
                return COURSE_BY_CAPACITY;
            case "enrollment":
                return COURSE_BY_ENROLLMENT_COUNT;
            case "availability":
                return COURSE_BY_AVAILABILITY;
            default:
                return COURSE_BY_CODE;
        }
    }

    /**
     * Create reversed comparator
     */
    public static <T> Comparator<T> reverse(Comparator<T> comparator) {
        return (a, b) -> comparator.compare(b, a);
    }

    /**
     * Sort students demonstration
     */
    public static void demonstrateStudentSorting(List<Student> students) {
        if (students == null || students.isEmpty()) {
            System.out.println("No students to sort.");
            return;
        }

        System.out.println("\n🔄 Student Sorting Demonstrations:");
        System.out.println("-".repeat(40));

        System.out.println("\n📊 Students by GPA (highest first):");
        students.stream()
                .sorted(STUDENT_BY_GPA_DESC)
                .limit(5)
                .forEach(student -> System.out.println("  " + student.getFullName() + 
                       " - GPA: " + String.format("%.2f", student.getGpa())));

        System.out.println("\n📊 Students by Name:");
        students.stream()
                .sorted(STUDENT_BY_NAME)
                .limit(5)
                .forEach(student -> System.out.println("  " + student.getFullName()));
    }

    /**
     * Sort courses demonstration
     */
    public static void demonstrateCourseSorting(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            System.out.println("No courses to sort.");
            return;
        }

        System.out.println("\n🔄 Course Sorting Demonstrations:");
        System.out.println("-".repeat(40));

        System.out.println("\n📚 Courses by Credits (highest first):");
        courses.stream()
                .sorted(COURSE_BY_CREDITS_DESC)
                .forEach(course -> System.out.println("  " + course.getCode() + 
                       " - " + course.getTitle() + " (" + course.getCredits() + " credits)"));
    }

    /**
     * General sorting demonstration
     */
    public static void demonstrateSorting(List<Student> students, List<Course> courses) {
        System.out.println("\n🔄 Sorting Demonstrations:");
        System.out.println("=".repeat(50));

        if (students != null && !students.isEmpty()) {
            demonstrateStudentSorting(students);
        }

        if (courses != null && !courses.isEmpty()) {
            demonstrateCourseSorting(courses);
        }

        System.out.println("=".repeat(50));
    }
}