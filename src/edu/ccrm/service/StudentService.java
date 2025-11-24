package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Student Service class handling all student-related business logic
 * Fixed version without circular dependencies
 */
public class StudentService {
    private final Map<String, Student> students;
    private final Set<String> usedRegistrationNumbers;

    public StudentService() {
        this.students = new ConcurrentHashMap<>();
        this.usedRegistrationNumbers = new HashSet<>();
    }

    /**
     * Add a new student to the system
     */
    public void addStudent(Student student) {
        validateStudent(student);

        if (students.containsKey(student.getId())) {
            throw new RuntimeException("Student with ID " + student.getId() + " already exists");
        }

        if (usedRegistrationNumbers.contains(student.getRegNo())) {
            throw new RuntimeException("Student with Registration Number " + 
                student.getRegNo() + " already exists");
        }

        students.put(student.getId(), student);
        usedRegistrationNumbers.add(student.getRegNo());

        System.out.println("✅ Student added successfully: " + student.getFullName());

        if (isDebugMode()) {
            System.out.println("Debug: Total students now: " + students.size());
        }
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    /**
     * Find student by ID
     */
    public Optional<Student> findStudentById(String id) {
        return Optional.ofNullable(students.get(id));
    }

    /**
     * Find student by registration number
     */
    public Optional<Student> findStudentByRegNo(String regNo) {
        return students.values().stream()
                .filter(s -> s.getRegNo().equals(regNo))
                .findFirst();
    }

    /**
     * Search students by name (case-insensitive, partial match)
     */
    public List<Student> searchStudentsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return students.values().stream()
                .filter(s -> s.getFullName().toLowerCase().contains(name.toLowerCase()))
                .sorted(Comparator.comparing(Student::getFullName))
                .collect(Collectors.toList());
    }

    /**
     * Get students by department
     */
    public List<Student> getStudentsByDepartment(String department) {
        return students.values().stream()
                .filter(s -> department.equals(s.getDepartment()))
                .sorted(Comparator.comparing(Student::getFullName))
                .collect(Collectors.toList());
    }

    /**
     * Get students by status
     */
    public List<Student> getStudentsByStatus(StudentStatus status) {
        return students.values().stream()
                .filter(s -> s.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Get students by semester
     */
    public List<Student> getStudentsBySemester(int semester) {
        return students.values().stream()
                .filter(s -> s.getSemester() == semester)
                .sorted(Comparator.comparing(Student::getFullName))
                .collect(Collectors.toList());
    }

    /**
     * Update student information
     */
    public void updateStudent(Student updatedStudent) {
        if (!students.containsKey(updatedStudent.getId())) {
            throw new RuntimeException("Student with ID " + updatedStudent.getId() + " not found");
        }

        validateStudent(updatedStudent);

        // Check if registration number conflicts with another student
        Student existingWithRegNo = students.values().stream()
                .filter(s -> !s.getId().equals(updatedStudent.getId()) && 
                           s.getRegNo().equals(updatedStudent.getRegNo()))
                .findFirst()
                .orElse(null);

        if (existingWithRegNo != null) {
            throw new RuntimeException("Registration number " + updatedStudent.getRegNo() + 
                " is already used by another student");
        }

        Student oldStudent = students.get(updatedStudent.getId());
        usedRegistrationNumbers.remove(oldStudent.getRegNo());
        usedRegistrationNumbers.add(updatedStudent.getRegNo());

        students.put(updatedStudent.getId(), updatedStudent);
        System.out.println("✅ Student updated successfully: " + updatedStudent.getFullName());
    }

    /**
     * Delete student by ID
     */
    public boolean deleteStudent(String studentId) {
        Student student = students.get(studentId);
        if (student == null) {
            throw new RuntimeException("Student with ID " + studentId + " not found");
        }

        usedRegistrationNumbers.remove(student.getRegNo());
        students.remove(studentId);

        System.out.println("✅ Student deleted successfully: " + student.getFullName());
        return true;
    }

    /**
     * Get students with high GPA (above threshold)
     */
    public List<Student> getTopPerformers(double gpaThreshold) {
        return students.values().stream()
                .filter(s -> s.getGpa() >= gpaThreshold)
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Get student statistics
     */
    public StudentStatistics getStudentStatistics() {
        if (students.isEmpty()) {
            return new StudentStatistics(0, 0.0, 0.0, 0.0, new HashMap<>(), new HashMap<>());
        }

        // Calculate statistics using Stream API
        DoubleSummaryStatistics gpaStats = students.values().stream()
                .mapToDouble(Student::getGpa)
                .summaryStatistics();

        Map<StudentStatus, Long> statusDistribution = students.values().stream()
                .collect(Collectors.groupingBy(Student::getStatus, Collectors.counting()));

        Map<String, Long> departmentDistribution = students.values().stream()
                .filter(s -> s.getDepartment() != null)
                .collect(Collectors.groupingBy(Student::getDepartment, Collectors.counting()));

        return new StudentStatistics(
            students.size(),
            gpaStats.getAverage(),
            gpaStats.getMin(),
            gpaStats.getMax(),
            statusDistribution,
            departmentDistribution
        );
    }

    /**
     * Validate student data
     */
    private void validateStudent(Student student) {
        if (!student.isValid()) {
            throw new IllegalArgumentException("Invalid student data");
        }

        if (student.getRegNo().length() < 3) {
            throw new IllegalArgumentException("Registration number must be at least 3 characters");
        }

        if (!student.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    /**
     * Get total number of students
     */
    public int getTotalStudentCount() {
        return students.size();
    }

    /**
     * Check if system is in debug mode
     */
    private boolean isDebugMode() {
        try {
            return edu.ccrm.config.AppConfig.getInstance().isDebugMode();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Display all students (utility method)
     */
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("\n📝 No students found in the system.");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("                    ALL STUDENTS (" + students.size() + ")");
        System.out.println("=".repeat(70));

        students.values().stream()
                .sorted(Comparator.comparing(Student::getFullName))
                .forEach(Student::displayInfo);
    }

    /**
     * Inner class for student statistics
     */
    public static class StudentStatistics {
        private final int totalStudents;
        private final double averageGPA;
        private final double minGPA;
        private final double maxGPA;
        private final Map<StudentStatus, Long> statusDistribution;
        private final Map<String, Long> departmentDistribution;

        public StudentStatistics(int totalStudents, double averageGPA, double minGPA, double maxGPA,
                                Map<StudentStatus, Long> statusDistribution, 
                                Map<String, Long> departmentDistribution) {
            this.totalStudents = totalStudents;
            this.averageGPA = averageGPA;
            this.minGPA = minGPA;
            this.maxGPA = maxGPA;
            this.statusDistribution = new HashMap<>(statusDistribution);
            this.departmentDistribution = new HashMap<>(departmentDistribution);
        }

        public void displayStatistics() {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("            STUDENT STATISTICS");
            System.out.println("=".repeat(50));
            System.out.println("Total Students    : " + totalStudents);
            System.out.println("Average GPA       : " + String.format("%.2f", averageGPA));
            System.out.println("Minimum GPA       : " + String.format("%.2f", minGPA));
            System.out.println("Maximum GPA       : " + String.format("%.2f", maxGPA));

            System.out.println("\nStatus Distribution:");
            statusDistribution.forEach((status, count) -> 
                System.out.println("  " + status.getDescription() + ": " + count));

            if (!departmentDistribution.isEmpty()) {
                System.out.println("\nDepartment Distribution:");
                departmentDistribution.forEach((dept, count) -> 
                    System.out.println("  " + dept + ": " + count));
            }
            System.out.println("=".repeat(50));
        }

        // Getters
        public int getTotalStudents() { return totalStudents; }
        public double getAverageGPA() { return averageGPA; }
        public double getMinGPA() { return minGPA; }
        public double getMaxGPA() { return maxGPA; }
        public Map<StudentStatus, Long> getStatusDistribution() { return new HashMap<>(statusDistribution); }
        public Map<String, Long> getDepartmentDistribution() { return new HashMap<>(departmentDistribution); }
    }
}