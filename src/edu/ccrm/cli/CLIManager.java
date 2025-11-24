package edu.ccrm.cli;

import edu.ccrm.domain.*;
import edu.ccrm.service.*;
import edu.ccrm.config.AppConfig;
import edu.ccrm.util.RecursionUtils;
import java.util.*;

/**
 * FIXED Command Line Interface Manager
 * Handles all user interactions and menu operations
 */
public class CLIManager {
    private final Scanner scanner;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private boolean running;

    public CLIManager() {
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.enrollmentService = new EnrollmentService();
        this.running = true;

        // Load sample data for demonstration
        loadSampleData();
    }

    /**
     * Start the CLI application
     */
    public void start() {
        displayWelcomeMessage();

        while (running) {
            try {
                displayMainMenu();
                int choice = getUserChoice(1, 9);
                handleMainMenuChoice(choice);
            } catch (Exception e) {
                System.err.println("\n❌ An error occurred: " + e.getMessage());
                if (AppConfig.getInstance().isDebugMode()) {
                    e.printStackTrace();
                }
                pressEnterToContinue();
            }
        }

        displayGoodbyeMessage();
        scanner.close();
    }

    /**
     * Display welcome message
     */
    private void displayWelcomeMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    🎓 Welcome to Campus Course & Records Manager! 🎓");
        System.out.println("                 Version 1.0.0");
        System.out.println("=".repeat(60));
        System.out.println("This system helps you manage students, courses, and enrollments.");
        System.out.println("Navigate through menus using number choices.");
        System.out.println("=".repeat(60));
    }

    /**
     * Display main menu
     */
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("                 MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. 👥 Student Management");
        System.out.println("2. 📚 Course Management");
        System.out.println("3. 📝 Enrollment Management");
        System.out.println("4. 🎯 Grade Management");
        System.out.println("5. 📊 Reports & Statistics");
        System.out.println("6. 🔧 Utility Functions");
        System.out.println("7. ⚙️  System Configuration");
        System.out.println("8. ℹ️  Help & Information");
        System.out.println("9. 🚪 Exit Application");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice (1-9): ");
    }

    /**
     * Handle main menu choice
     */
    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1 -> handleStudentManagement();
            case 2 -> handleCourseManagement();
            case 3 -> handleEnrollmentManagement();
            case 4 -> handleGradeManagement();
            case 5 -> handleReportsAndStatistics();
            case 6 -> handleUtilityFunctions();
            case 7 -> handleSystemConfiguration();
            case 8 -> handleHelpAndInformation();
            case 9 -> {
                System.out.println("\n👋 Exiting application...");
                running = false;
            }
            default -> System.out.println("\n❌ Invalid choice. Please try again.");
        }
    }

    /**
     * Student Management Menu
     */
    private void handleStudentManagement() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("            STUDENT MANAGEMENT");
            System.out.println("=".repeat(50));
            System.out.println("1. ➕ Add New Student");
            System.out.println("2. 👀 View All Students");
            System.out.println("3. 🔍 Search Student");
            System.out.println("4. 📋 Student Statistics");
            System.out.println("5. 🔙 Back to Main Menu");
            System.out.println("=".repeat(50));

            int choice = getUserChoice(1, 5);

            try {
                switch (choice) {
                    case 1 -> addNewStudent();
                    case 2 -> viewAllStudents();
                    case 3 -> searchStudent();
                    case 4 -> showStudentStatistics();
                    case 5 -> { return; }
                }
            } catch (Exception e) {
                System.err.println("\n❌ Error: " + e.getMessage());
            }

            if (choice != 5) {
                pressEnterToContinue();
            }
        }
    }

    /**
     * Course Management Menu
     */
    private void handleCourseManagement() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("            COURSE MANAGEMENT");
            System.out.println("=".repeat(50));
            System.out.println("1. ➕ Add New Course");
            System.out.println("2. 👀 View All Courses");
            System.out.println("3. 🔍 Search Course");
            System.out.println("4. 📋 Course Statistics");
            System.out.println("5. 🔙 Back to Main Menu");
            System.out.println("=".repeat(50));

            int choice = getUserChoice(1, 5);

            try {
                switch (choice) {
                    case 1 -> addNewCourse();
                    case 2 -> viewAllCourses();
                    case 3 -> searchCourse();
                    case 4 -> showCourseStatistics();
                    case 5 -> { return; }
                }
            } catch (Exception e) {
                System.err.println("\n❌ Error: " + e.getMessage());
            }

            if (choice != 5) {
                pressEnterToContinue();
            }
        }
    }

    /**
     * Add new student
     */
    private void addNewStudent() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("         ADD NEW STUDENT");
        System.out.println("=".repeat(40));

        try {
            String id = getInputString("Enter Student ID: ");
            String name = getInputString("Enter Full Name: ");
            String email = getInputString("Enter Email: ");
            String regNo = getInputString("Enter Registration Number: ");

            System.out.println("\nOptional Information:");
            String department = getInputString("Enter Department (or press Enter to skip): ");
            if (department.isEmpty()) department = null;

            String semesterStr = getInputString("Enter Semester (1-8, or press Enter for 1): ");
            int semester = semesterStr.isEmpty() ? 1 : Integer.parseInt(semesterStr);

            Student student = new Student(id, name, email, regNo, department, semester);
            studentService.addStudent(student);

            System.out.println("\n✅ Student added successfully!");

        } catch (NumberFormatException e) {
            System.err.println("\n❌ Invalid semester number. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("\n❌ Failed to add student: " + e.getMessage());
        }
    }

    /**
     * Add new course using Builder pattern
     */
    private void addNewCourse() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("         ADD NEW COURSE");
        System.out.println("=".repeat(40));

        try {
            String code = getInputString("Enter Course Code: ");
            String title = getInputString("Enter Course Title: ");
            int credits = Integer.parseInt(getInputString("Enter Credits (1-10): "));
            String instructor = getInputString("Enter Instructor Name: ");
            String department = getInputString("Enter Department: ");

            // Select semester
            System.out.println("\nSelect Semester:");
            Semester[] semesters = Semester.values();
            for (int i = 0; i < semesters.length; i++) {
                System.out.println((i + 1) + ". " + semesters[i].getName());
            }
            int semChoice = getUserChoice(1, semesters.length);
            Semester semester = semesters[semChoice - 1];

            Course course = new Course.Builder(code, title, credits)
                    .setInstructor(instructor)
                    .setSemester(semester)
                    .setDepartment(department)
                    .build();

            courseService.addCourse(course);
            System.out.println("\n✅ Course added successfully!");

        } catch (NumberFormatException e) {
            System.err.println("\n❌ Invalid number format. Please check your inputs.");
        } catch (Exception e) {
            System.err.println("\n❌ Failed to add course: " + e.getMessage());
        }
    }

    /**
     * Enrollment Management
     */
    private void handleEnrollmentManagement() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("          ENROLLMENT MANAGEMENT");
            System.out.println("=".repeat(50));
            System.out.println("1. ➕ Enroll Student in Course");
            System.out.println("2. 👀 View Student Enrollments");
            System.out.println("3. 📚 View Course Enrollments");
            System.out.println("4. 📋 View All Enrollments");
            System.out.println("5. 📊 Enrollment Statistics");
            System.out.println("6. 🔙 Back to Main Menu");
            System.out.println("=".repeat(50));

            int choice = getUserChoice(1, 6);

            try {
                switch (choice) {
                    case 1 -> enrollStudent();
                    case 2 -> viewStudentEnrollments();
                    case 3 -> viewCourseEnrollments();
                    case 4 -> enrollmentService.displayAllEnrollments();
                    case 5 -> enrollmentService.displayEnrollmentStatistics();
                    case 6 -> { return; }
                }
            } catch (Exception e) {
                System.err.println("\n❌ Error: " + e.getMessage());
            }

            if (choice != 6) {
                pressEnterToContinue();
            }
        }
    }

    /**
     * Enroll student in course
     */
    private void enrollStudent() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("       ENROLL STUDENT");
        System.out.println("=".repeat(40));

        String studentId = getInputString("Enter Student ID: ");
        String courseCode = getInputString("Enter Course Code: ");

        try {
            enrollmentService.enrollStudent(studentId, courseCode);
            System.out.println("\n✅ Enrollment successful!");
        } catch (Exception e) {
            System.err.println("\n❌ Enrollment failed: " + e.getMessage());
        }
    }

    /**
     * Grade Management
     */
    private void handleGradeManagement() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("            GRADE MANAGEMENT");
            System.out.println("=".repeat(50));
            System.out.println("1. 📝 Assign Grade");
            System.out.println("2. 🎓 Generate Transcript");
            System.out.println("3. 🔙 Back to Main Menu");
            System.out.println("=".repeat(50));

            int choice = getUserChoice(1, 3);

            try {
                switch (choice) {
                    case 1 -> assignGrade();
                    case 2 -> generateTranscript();
                    case 3 -> { return; }
                }
            } catch (Exception e) {
                System.err.println("\n❌ Error: " + e.getMessage());
            }

            if (choice != 3) {
                pressEnterToContinue();
            }
        }
    }

    /**
     * Assign grade to student
     */
    private void assignGrade() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("         ASSIGN GRADE");
        System.out.println("=".repeat(40));

        String studentId = getInputString("Enter Student ID: ");
        String courseCode = getInputString("Enter Course Code: ");

        System.out.println("\nSelect Grade:");
        Grade[] grades = Grade.values();
        for (int i = 0; i < grades.length; i++) {
            System.out.println((i + 1) + ". " + grades[i].name() + " - " + grades[i].getDescription());
        }

        int gradeChoice = getUserChoice(1, grades.length);
        Grade selectedGrade = grades[gradeChoice - 1];

        try {
            enrollmentService.assignGrade(studentId, courseCode, selectedGrade);
            System.out.println("\n✅ Grade assigned successfully!");
        } catch (Exception e) {
            System.err.println("\n❌ Failed to assign grade: " + e.getMessage());
        }
    }

    /**
     * Generate and display transcript
     */
    private void generateTranscript() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("       GENERATE TRANSCRIPT");
        System.out.println("=".repeat(40));

        String studentId = getInputString("Enter Student ID: ");

        try {
            enrollmentService.generateTranscript(studentId);
        } catch (Exception e) {
            System.err.println("\n❌ Failed to generate transcript: " + e.getMessage());
        }
    }

    /**
     * Utility Functions Menu
     */
    private void handleUtilityFunctions() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("            UTILITY FUNCTIONS");
            System.out.println("=".repeat(50));
            System.out.println("1. 🔢 Recursion Demonstrations");
            System.out.println("2. 🎲 Generate Student IDs");
            System.out.println("3. 📊 Sorting Demonstrations");
            System.out.println("4. 🔙 Back to Main Menu");
            System.out.println("=".repeat(50));

            int choice = getUserChoice(1, 4);

            try {
                switch (choice) {
                    case 1 -> RecursionUtils.demonstrateRecursion();
                    case 2 -> demonstrateIdGeneration();
                    case 3 -> demonstrateSorting();
                    case 4 -> { return; }
                }
            } catch (Exception e) {
                System.err.println("\n❌ Error: " + e.getMessage());
            }

            if (choice != 4) {
                pressEnterToContinue();
            }
        }
    }

    /**
     * Demonstrate ID generation
     */
    private void demonstrateIdGeneration() {
        System.out.println("\n🎲 Student ID Generation:");
        String prefix = getInputString("Enter prefix (e.g., 'S'): ");
        int count = Integer.parseInt(getInputString("Enter count: "));

        List<String> ids = RecursionUtils.generateStudentIds(prefix, count);
        System.out.println("Generated IDs: " + ids);
    }

    /**
     * Demonstrate sorting
     */
    private void demonstrateSorting() {
        System.out.println("\n📊 Sorting Demonstration:");

        List<Student> students = studentService.getAllStudents();
        List<Course> courses = courseService.getAllCourses();

        if (!students.isEmpty()) {
            System.out.println("\nStudents sorted by GPA (highest first):");
            students.stream()
                    .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                    .limit(5)
                    .forEach(s -> System.out.println("  " + s.getFullName() + 
                           " (GPA: " + String.format("%.2f", s.getGpa()) + ")"));
        }

        if (!courses.isEmpty()) {
            System.out.println("\nCourses sorted by credits:");
            courses.stream()
                    .sorted(Comparator.comparingInt(Course::getCredits).reversed())
                    .forEach(c -> System.out.println("  " + c.getCode() + 
                           " - " + c.getTitle() + " (" + c.getCredits() + " credits)"));
        }
    }

    /**
     * Helper methods
     */
    private void viewAllStudents() { studentService.displayAllStudents(); }
    private void viewAllCourses() { courseService.displayAllCourses(); }

    private void searchStudent() {
        String name = getInputString("\nEnter student name to search: ");
        var results = studentService.searchStudentsByName(name);

        if (results.isEmpty()) {
            System.out.println("\n❌ No students found matching: " + name);
        } else {
            System.out.println("\n✅ Found " + results.size() + " student(s):");
            results.forEach(Student::displayInfo);
        }
    }

    private void searchCourse() {
        String title = getInputString("\nEnter course title to search: ");
        var results = courseService.searchCoursesByTitle(title);

        if (results.isEmpty()) {
            System.out.println("\n❌ No courses found matching: " + title);
        } else {
            System.out.println("\n✅ Found " + results.size() + " course(s):");
            results.forEach(Course::displayInfo);
        }
    }

    private void showStudentStatistics() {
        StudentService.StudentStatistics stats = studentService.getStudentStatistics();
        stats.displayStatistics();
    }

    private void showCourseStatistics() {
        CourseService.CourseStatistics stats = courseService.getCourseStatistics();
        stats.displayStatistics();
    }

    private void viewStudentEnrollments() {
        String studentId = getInputString("\nEnter Student ID: ");
        var enrollments = enrollmentService.getStudentEnrollments(studentId);

        if (enrollments.isEmpty()) {
            System.out.println("\n❌ No enrollments found for student: " + studentId);
        } else {
            System.out.println("\n✅ Enrollments for student " + studentId + ":");
            enrollments.forEach(Enrollment::displayInfo);
        }
    }

    private void viewCourseEnrollments() {
        String courseCode = getInputString("\nEnter Course Code: ");
        var enrollments = enrollmentService.getCourseEnrollments(courseCode);

        if (enrollments.isEmpty()) {
            System.out.println("\n❌ No enrollments found for course: " + courseCode);
        } else {
            System.out.println("\n✅ Enrollments for course " + courseCode + ":");
            enrollments.forEach(Enrollment::displayInfo);
        }
    }

    private void handleReportsAndStatistics() {
        System.out.println("\n📊 REPORTS & STATISTICS");
        System.out.println("-".repeat(30));
        showStudentStatistics();
        showCourseStatistics();
        enrollmentService.displayEnrollmentStatistics();
    }

    private void handleSystemConfiguration() { AppConfig.getInstance().displayConfig(); }

    private void handleHelpAndInformation() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                HELP & INFORMATION");
        System.out.println("=".repeat(60));
        System.out.println("🎓 Campus Course & Records Manager (CCRM) v1.0");
        System.out.println("\nFeatures:");
        System.out.println("• Student Management (Add, View, Search)");
        System.out.println("• Course Management (Add, View, Search)");
        System.out.println("• Enrollment Management (Enroll, View)");
        System.out.println("• Grade Management (Assign grades, Generate transcripts)");
        System.out.println("• Statistics and Reports");
        System.out.println("• Utility Functions (Recursion, Sorting)");
        System.out.println("\nDeveloped using Java 24 with OOP principles");
        System.out.println("Design Patterns: Singleton, Builder, Service Layer");
        System.out.println("=".repeat(60));
    }

    /**
     * Load sample data for demonstration
     */
    private void loadSampleData() {
        System.out.println("🔄 Loading sample data...");

        try {
            // Add sample students
            studentService.addStudent(new Student("S001", "Alice Johnson", "alice@university.edu", "REG001", "Computer Science", 3));
            studentService.addStudent(new Student("S002", "Bob Smith", "bob@university.edu", "REG002", "Mathematics", 2));
            studentService.addStudent(new Student("S003", "Carol Davis", "carol@university.edu", "REG003", "Physics", 4));

            // Add sample courses
            Course cs101 = new Course.Builder("CS101", "Introduction to Programming", 3)
                    .setInstructor("Dr. Wilson")
                    .setDepartment("Computer Science")
                    .setSemester(Semester.FALL)
                    .build();
            courseService.addCourse(cs101);

            Course math201 = new Course.Builder("MATH201", "Calculus II", 4)
                    .setInstructor("Prof. Anderson")
                    .setDepartment("Mathematics")
                    .setSemester(Semester.SPRING)
                    .build();
            courseService.addCourse(math201);

            System.out.println("✅ Sample data loaded successfully!");

        } catch (Exception e) {
            System.err.println("⚠️  Warning: Could not load all sample data: " + e.getMessage());
        }
    }

    /**
     * Utility methods
     */
    private int getUserChoice(int min, int max) {
        while (true) {
            try {
                System.out.print("Enter your choice (" + min + "-" + max + "): ");
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.println("❌ Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }

    private String getInputString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void pressEnterToContinue() {
        System.out.print("\n⏸️  Press Enter to continue...");
        scanner.nextLine();
    }

    private void displayGoodbyeMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    🎓 Thank you for using CCRM! 🎓");
        System.out.println("           Come back soon!");
        System.out.println("=".repeat(60));
    }
}