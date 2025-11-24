package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.service.*;
import edu.ccrm.config.AppConfig;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;

/**
 * Data Import Service using NIO.2 APIs
 * Handles CSV imports with validation and error handling
 */
public class DataImportService {
    private final AppConfig config;
    private final DateTimeFormatter dateFormatter;

    public DataImportService() {
        this.config = AppConfig.getInstance();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Import students from CSV file
     */
    public ImportResult importStudentsFromCSV(String filename, StudentService studentService) throws IOException {
        Path filePath = config.getImportPath(filename);

        if (!Files.exists(filePath)) {
            throw new IOException("Import file not found: " + filePath.toAbsolutePath());
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        ImportResult result = new ImportResult();

        if (lines.isEmpty()) {
            throw new IOException("Import file is empty");
        }

        // Skip header line
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            try {
                Student student = parseStudentFromCSV(line);
                studentService.addStudent(student);
                result.incrementSuccessful();
            } catch (Exception e) {
                result.addError(i + 1, line, e.getMessage());
            }
        }

        result.setTotalProcessed(lines.size() - 1); // Exclude header
        return result;
    }

    /**
     * Import courses from CSV file
     */
    public ImportResult importCoursesFromCSV(String filename, CourseService courseService) throws IOException {
        Path filePath = config.getImportPath(filename);

        if (!Files.exists(filePath)) {
            throw new IOException("Import file not found: " + filePath.toAbsolutePath());
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        ImportResult result = new ImportResult();

        if (lines.isEmpty()) {
            throw new IOException("Import file is empty");
        }

        // Skip header line
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            try {
                Course course = parseCourseFromCSV(line);
                courseService.addCourse(course);
                result.incrementSuccessful();
            } catch (Exception e) {
                result.addError(i + 1, line, e.getMessage());
            }
        }

        result.setTotalProcessed(lines.size() - 1);
        return result;
    }

    /**
     * Parse student from CSV line
     */
    private Student parseStudentFromCSV(String csvLine) {
        String[] fields = parseCSVLine(csvLine);

        if (fields.length < 4) {
            throw new IllegalArgumentException("Insufficient fields in CSV line. Expected at least 4, got " + fields.length);
        }

        String studentId = fields[0].trim();
        String regNo = fields[1].trim();
        String fullName = fields[2].trim();
        String email = fields[3].trim();

        if (studentId.isEmpty() || regNo.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("Required fields (ID, RegNo, Name, Email) cannot be empty");
        }

        Student student = new Student(studentId, fullName, email, regNo);

        // Optional fields
        if (fields.length > 4 && !fields[4].trim().isEmpty()) {
            student.setPhone(fields[4].trim());
        }

        if (fields.length > 5 && !fields[5].trim().isEmpty()) {
            student.setDepartment(fields[5].trim());
        }

        if (fields.length > 6 && !fields[6].trim().isEmpty()) {
            try {
                int semester = Integer.parseInt(fields[6].trim());
                student.setSemester(semester);
            } catch (NumberFormatException e) {
                // Keep default semester
            }
        }

        if (fields.length > 7 && !fields[7].trim().isEmpty()) {
            try {
                StudentStatus status = StudentStatus.valueOf(fields[7].trim().toUpperCase());
                student.setStatus(status);
            } catch (IllegalArgumentException e) {
                // Keep default status
            }
        }

        return student;
    }

    /**
     * Parse course from CSV line
     */
    private Course parseCourseFromCSV(String csvLine) {
        String[] fields = parseCSVLine(csvLine);

        if (fields.length < 4) {
            throw new IllegalArgumentException("Insufficient fields in CSV line. Expected at least 4, got " + fields.length);
        }

        String courseCode = fields[0].trim();
        String title = fields[1].trim();
        String creditsStr = fields[2].trim();
        String instructor = fields[3].trim();

        if (courseCode.isEmpty() || title.isEmpty() || creditsStr.isEmpty() || instructor.isEmpty()) {
            throw new IllegalArgumentException("Required fields (Code, Title, Credits, Instructor) cannot be empty");
        }

        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid credits value: " + creditsStr);
        }

        Course.Builder builder = new Course.Builder(courseCode, title, credits)
                .setInstructor(instructor);

        // Optional fields
        if (fields.length > 4 && !fields[4].trim().isEmpty()) {
            builder.setDepartment(fields[4].trim());
        }

        if (fields.length > 5 && !fields[5].trim().isEmpty()) {
            try {
                Semester semester = Semester.valueOf(fields[5].trim().toUpperCase());
                builder.setSemester(semester);
            } catch (IllegalArgumentException e) {
                // Keep default semester
            }
        }

        if (fields.length > 6 && !fields[6].trim().isEmpty()) {
            builder.setDescription(fields[6].trim());
        }

        if (fields.length > 7 && !fields[7].trim().isEmpty()) {
            try {
                int maxCapacity = Integer.parseInt(fields[7].trim());
                builder.setMaxCapacity(maxCapacity);
            } catch (NumberFormatException e) {
                // Keep default capacity
            }
        }

        return builder.build();
    }

    /**
     * Parse CSV line handling quotes and commas
     */
    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    // Escaped quote
                    currentField.append('\"');
                    i++; // Skip next quote
                } else {
                    // Toggle quote state
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Field separator
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        // Add the last field
        fields.add(currentField.toString());

        return fields.toArray(new String[0]);
    }

    /**
     * Create sample import files for testing
     */
    public void createSampleImportFiles() throws IOException {
        Path importDir = Paths.get(config.getImportFolderPath());
        Files.createDirectories(importDir);

        // Create sample students CSV
        createSampleStudentsCSV();

        // Create sample courses CSV
        createSampleCoursesCSV();

        System.out.println("✅ Sample import files created in: " + importDir.toAbsolutePath());
    }

    /**
     * Create sample students CSV file
     */
    private void createSampleStudentsCSV() throws IOException {
        Path filePath = config.getImportPath("sample_students.csv");

        List<String> lines = Arrays.asList(
            "StudentID,RegNo,FullName,Email,Phone,Department,Semester,Status",
            "S004,REG2024004,David Wilson,david.wilson@university.edu,123-456-7890,Computer Science,2,ACTIVE",
            "S005,REG2024005,Emma Thompson,emma.thompson@university.edu,123-456-7891,Mathematics,3,ACTIVE",
            "S006,REG2024006,James Brown,james.brown@university.edu,123-456-7892,Physics,1,ACTIVE",
            "S007,REG2024007,Sarah Davis,sarah.davis@university.edu,123-456-7893,Chemistry,4,ACTIVE",
            "S008,REG2024008,Michael Johnson,michael.johnson@university.edu,123-456-7894,Biology,2,ACTIVE"
        );

        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    /**
     * Create sample courses CSV file
     */
    private void createSampleCoursesCSV() throws IOException {
        Path filePath = config.getImportPath("sample_courses.csv");

        List<String> lines = Arrays.asList(
            "CourseCode,Title,Credits,Instructor,Department,Semester,Description,MaxCapacity",
            "CS102,Data Structures,3,Dr. Smith,Computer Science,SPRING,Introduction to data structures and algorithms,40",
            "MATH301,Linear Algebra,4,Prof. Johnson,Mathematics,FALL,Vector spaces and linear transformations,35",
            "PHYS201,Physics II,3,Dr. Williams,Physics,SPRING,Electricity and magnetism,45",
            "CHEM101,General Chemistry,4,Prof. Davis,Chemistry,FALL,Basic principles of chemistry,50",
            "BIO101,Introduction to Biology,3,Dr. Miller,Biology,SPRING,Fundamentals of biological sciences,60"
        );

        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    /**
     * List all import files
     */
    public void listImportFiles() throws IOException {
        Path importDir = Paths.get(config.getImportFolderPath());

        if (!Files.exists(importDir)) {
            System.out.println("📁 Import directory does not exist: " + importDir.toAbsolutePath());
            return;
        }

        System.out.println("\n📁 Available Import Files:");
        System.out.println("-".repeat(50));

        Files.list(importDir)
             .filter(Files::isRegularFile)
             .filter(path -> path.toString().toLowerCase().endsWith(".csv"))
             .sorted()
             .forEach(file -> {
                 try {
                     long size = Files.size(file);
                     System.out.printf("📄 %-30s (%d bytes)%n", 
                         file.getFileName().toString(), size);
                 } catch (IOException e) {
                     System.out.println("📄 " + file.getFileName().toString() + " (size unknown)");
                 }
             });
    }

    /**
     * Inner class for import results
     */
    public static class ImportResult {
        private int totalProcessed = 0;
        private int successful = 0;
        private final List<ImportError> errors = new ArrayList<>();

        public void setTotalProcessed(int total) { this.totalProcessed = total; }
        public void incrementSuccessful() { this.successful++; }

        public void addError(int lineNumber, String line, String errorMessage) {
            errors.add(new ImportError(lineNumber, line, errorMessage));
        }

        public int getTotalProcessed() { return totalProcessed; }
        public int getSuccessful() { return successful; }
        public int getFailed() { return totalProcessed - successful; }
        public List<ImportError> getErrors() { return new ArrayList<>(errors); }

        public void displayResults() {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("            IMPORT RESULTS");
            System.out.println("=".repeat(50));
            System.out.println("Total Processed : " + totalProcessed);
            System.out.println("Successful      : " + successful);
            System.out.println("Failed          : " + getFailed());

            if (!errors.isEmpty()) {
                System.out.println("\nErrors:");
                System.out.println("-".repeat(30));
                for (ImportError error : errors) {
                    System.out.println("Line " + error.lineNumber + ": " + error.errorMessage);
                    if (error.line.length() > 50) {
                        System.out.println("  Data: " + error.line.substring(0, 50) + "...");
                    } else {
                        System.out.println("  Data: " + error.line);
                    }
                }
            }
            System.out.println("=".repeat(50));
        }
    }

    /**
     * Inner class for import errors
     */
    public static class ImportError {
        private final int lineNumber;
        private final String line;
        private final String errorMessage;

        public ImportError(int lineNumber, String line, String errorMessage) {
            this.lineNumber = lineNumber;
            this.line = line;
            this.errorMessage = errorMessage;
        }

        public int getLineNumber() { return lineNumber; }
        public String getLine() { return line; }
        public String getErrorMessage() { return errorMessage; }
    }
}