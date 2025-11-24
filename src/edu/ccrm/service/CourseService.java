package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Course Service class handling all course-related business logic
 * Fixed version without circular dependencies
 */
public class CourseService {
    private final Map<String, Course> courses;
    private final Map<String, Set<String>> departmentCourses;

    public CourseService() {
        this.courses = new HashMap<>();
        this.departmentCourses = new HashMap<>();
    }

    /**
     * Add a new course using Builder pattern
     */
    public void addCourse(Course course) {
        if (courses.containsKey(course.getCode())) {
            throw new RuntimeException("Course with code " + course.getCode() + " already exists");
        }

        validateCourse(course);
        courses.put(course.getCode(), course);

        // Add to department mapping
        departmentCourses.computeIfAbsent(course.getDepartment(), k -> new HashSet<>())
                         .add(course.getCode());

        System.out.println("✅ Course added successfully: " + course.getCode() + " - " + course.getTitle());
    }

    /**
     * Create and add course using Builder pattern
     */
    public void createCourse(String code, String title, int credits, String instructor,
                           Semester semester, String department) {
        Course course = new Course.Builder(code, title, credits)
                .setInstructor(instructor)
                .setSemester(semester)
                .setDepartment(department)
                .build();

        addCourse(course);
    }

    /**
     * Get all courses
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    /**
     * Find course by code
     */
    public Optional<Course> findCourseByCode(String code) {
        return Optional.ofNullable(courses.get(code));
    }

    /**
     * Search courses by title (partial match)
     */
    public List<Course> searchCoursesByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return courses.values().stream()
                .filter(c -> c.getTitle().toLowerCase().contains(title.toLowerCase()))
                .sorted(Comparator.comparing(Course::getTitle))
                .collect(Collectors.toList());
    }

    /**
     * Get courses by department
     */
    public List<Course> getCoursesByDepartment(String department) {
        return courses.values().stream()
                .filter(c -> department.equals(c.getDepartment()))
                .sorted(Comparator.comparing(Course::getCode))
                .collect(Collectors.toList());
    }

    /**
     * Get courses by semester
     */
    public List<Course> getCoursesBySemester(Semester semester) {
        return courses.values().stream()
                .filter(c -> c.getSemester() == semester)
                .collect(Collectors.toList());
    }

    /**
     * Get courses by instructor
     */
    public List<Course> getCoursesByInstructor(String instructor) {
        return courses.values().stream()
                .filter(c -> instructor.equals(c.getInstructor()))
                .collect(Collectors.toList());
    }

    /**
     * Get courses by credit hours
     */
    public List<Course> getCoursesByCredits(int credits) {
        return courses.values().stream()
                .filter(c -> c.getCredits() == credits)
                .collect(Collectors.toList());
    }

    /**
     * Get available courses (not full)
     */
    public List<Course> getAvailableCourses() {
        return courses.values().stream()
                .filter(c -> !c.isFull())
                .sorted(Comparator.comparing(Course::getCode))
                .collect(Collectors.toList());
    }

    /**
     * Update course information
     */
    public void updateCourse(Course updatedCourse) {
        if (!courses.containsKey(updatedCourse.getCode())) {
            throw new RuntimeException("Course with code " + updatedCourse.getCode() + " not found");
        }

        validateCourse(updatedCourse);

        Course oldCourse = courses.get(updatedCourse.getCode());

        // Update department mapping if department changed
        if (!oldCourse.getDepartment().equals(updatedCourse.getDepartment())) {
            departmentCourses.get(oldCourse.getDepartment()).remove(oldCourse.getCode());
            departmentCourses.computeIfAbsent(updatedCourse.getDepartment(), k -> new HashSet<>())
                             .add(updatedCourse.getCode());
        }

        courses.put(updatedCourse.getCode(), updatedCourse);
        System.out.println("✅ Course updated successfully: " + updatedCourse.getCode());
    }

    /**
     * Delete course by code
     */
    public boolean deleteCourse(String courseCode) {
        Course course = courses.get(courseCode);
        if (course == null) {
            throw new RuntimeException("Course with code " + courseCode + " not found");
        }

        // Check if course has enrolled students
        if (!course.getEnrolledStudents().isEmpty()) {
            throw new IllegalStateException("Cannot delete course with enrolled students. " +
                "Current enrollment: " + course.getEnrolledStudents().size());
        }

        courses.remove(courseCode);
        departmentCourses.get(course.getDepartment()).remove(courseCode);

        System.out.println("✅ Course deleted successfully: " + course.getTitle());
        return true;
    }

    /**
     * Get course statistics
     */
    public CourseStatistics getCourseStatistics() {
        if (courses.isEmpty()) {
            return new CourseStatistics(0, 0.0, new HashMap<>(), new HashMap<>(), new HashMap<>());
        }

        double averageCredits = courses.values().stream()
                .mapToInt(Course::getCredits)
                .average()
                .orElse(0.0);

        Map<String, Long> departmentCounts = courses.values().stream()
                .collect(Collectors.groupingBy(Course::getDepartment, Collectors.counting()));

        Map<Semester, Long> semesterCounts = courses.values().stream()
                .collect(Collectors.groupingBy(Course::getSemester, Collectors.counting()));

        Map<Integer, Long> creditDistribution = courses.values().stream()
                .collect(Collectors.groupingBy(Course::getCredits, Collectors.counting()));

        return new CourseStatistics(courses.size(), averageCredits, departmentCounts, 
                                  semesterCounts, creditDistribution);
    }

    /**
     * Validate course data
     */
    private void validateCourse(Course course) {
        if (course.getCode() == null || course.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty");
        }

        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty");
        }

        if (course.getCredits() <= 0 || course.getCredits() > 10) {
            throw new IllegalArgumentException("Course credits must be between 1 and 10");
        }

        if (course.getMaxCapacity() <= 0) {
            throw new IllegalArgumentException("Course capacity must be positive");
        }
    }

    /**
     * Get total number of courses
     */
    public int getTotalCourseCount() {
        return courses.size();
    }

    /**
     * Display all courses
     */
    public void displayAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("\n📚 No courses found in the system.");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("                    ALL COURSES (" + courses.size() + ")");
        System.out.println("=".repeat(70));

        courses.values().stream()
                .sorted(Comparator.comparing(Course::getCode))
                .forEach(Course::displayInfo);
    }

    /**
     * Inner class for course statistics
     */
    public static class CourseStatistics {
        private final int totalCourses;
        private final double averageCredits;
        private final Map<String, Long> departmentDistribution;
        private final Map<Semester, Long> semesterDistribution;
        private final Map<Integer, Long> creditDistribution;

        public CourseStatistics(int totalCourses, double averageCredits,
                               Map<String, Long> departmentDistribution,
                               Map<Semester, Long> semesterDistribution,
                               Map<Integer, Long> creditDistribution) {
            this.totalCourses = totalCourses;
            this.averageCredits = averageCredits;
            this.departmentDistribution = new HashMap<>(departmentDistribution);
            this.semesterDistribution = new HashMap<>(semesterDistribution);
            this.creditDistribution = new HashMap<>(creditDistribution);
        }

        public void displayStatistics() {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("            COURSE STATISTICS");
            System.out.println("=".repeat(50));
            System.out.println("Total Courses     : " + totalCourses);
            System.out.println("Average Credits   : " + String.format("%.1f", averageCredits));

            System.out.println("\nDepartment Distribution:");
            departmentDistribution.forEach((dept, count) -> 
                System.out.println("  " + dept + ": " + count));

            System.out.println("\nSemester Distribution:");
            semesterDistribution.forEach((semester, count) -> 
                System.out.println("  " + semester.getName() + ": " + count));

            System.out.println("\nCredit Hours Distribution:");
            creditDistribution.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println("  " + entry.getKey() + " credits: " + entry.getValue()));

            System.out.println("=".repeat(50));
        }

        // Getters
        public int getTotalCourses() { return totalCourses; }
        public double getAverageCredits() { return averageCredits; }
        public Map<String, Long> getDepartmentDistribution() { return new HashMap<>(departmentDistribution); }
        public Map<Semester, Long> getSemesterDistribution() { return new HashMap<>(semesterDistribution); }
        public Map<Integer, Long> getCreditDistribution() { return new HashMap<>(creditDistribution); }
    }
}