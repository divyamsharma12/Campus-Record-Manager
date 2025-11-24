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
 * Data Export Service using NIO.2 APIs
 * Handles CSV and JSON-like exports with modern file I/O
 */
public class DataExportService {
    private final AppConfig config;
    private final DateTimeFormatter dateFormatter;

    public DataExportService() {
        this.config = AppConfig.getInstance();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Export students to CSV file using NIO.2
     */
    public void exportStudentsToCSV(List<Student> students, String filename) throws IOException {
        Path filePath = config.getExportPath(filename);

        // Create directories if they don't exist
        Files.createDirectories(filePath.getParent());

        List<String> lines = new ArrayList<>();

        // CSV Header
        lines.add("StudentID,RegNo,FullName,Email,Phone,Department,Semester,Status,GPA,EnrollmentDate,EnrolledCourses");

        // CSV Data
        for (Student student : students) {
            StringBuilder coursesList = new StringBuilder();
            List<String> courses = student.getEnrolledCourses();
            if (!courses.isEmpty()) {
                coursesList.append("\"").append(String.join(";", courses)).append("\"");
            } else {
                coursesList.append("\"\"");
            }

            String line = String.join(",",
                escapeCSV(student.getId()),
                escapeCSV(student.getRegNo()),
                escapeCSV(student.getFullName()),
                escapeCSV(student.getEmail()),
                escapeCSV(student.getPhone() != null ? student.getPhone() : ""),
                escapeCSV(student.getDepartment() != null ? student.getDepartment() : ""),
                String.valueOf(student.getSemester()),
                escapeCSV(student.getStatus().name()),
                String.valueOf(student.getGpa()),
                escapeCSV(student.getEnrollmentDate().format(dateFormatter)),
                coursesList.toString()
            );
            lines.add(line);
        }

        // Write to file using NIO.2
        Files.write(filePath, lines, StandardCharsets.UTF_8);

        System.out.println("✅ Students exported to: " + filePath.toAbsolutePath());
        System.out.println("📊 Total students exported: " + students.size());
    }

    /**
     * Export courses to CSV file using NIO.2
     */
    public void exportCoursesToCSV(List<Course> courses, String filename) throws IOException {
        Path filePath = config.getExportPath(filename);

        Files.createDirectories(filePath.getParent());

        List<String> lines = new ArrayList<>();

        // CSV Header
        lines.add("CourseCode,Title,Credits,Instructor,Department,Semester,Description,MaxCapacity,EnrolledStudents,Prerequisites");

        // CSV Data
        for (Course course : courses) {
            String prerequisites = course.getPrerequisites().isEmpty() ? 
                "\"\"" : "\"" + String.join(";", course.getPrerequisites()) + "\"";

            String enrolledStudents = course.getEnrolledStudents().isEmpty() ?
                "\"\"" : "\"" + String.join(";", course.getEnrolledStudents()) + "\"";

            String line = String.join(",",
                escapeCSV(course.getCode()),
                escapeCSV(course.getTitle()),
                String.valueOf(course.getCredits()),
                escapeCSV(course.getInstructor()),
                escapeCSV(course.getDepartment()),
                escapeCSV(course.getSemester().name()),
                escapeCSV(course.getDescription()),
                String.valueOf(course.getMaxCapacity()),
                enrolledStudents,
                prerequisites
            );
            lines.add(line);
        }

        Files.write(filePath, lines, StandardCharsets.UTF_8);

        System.out.println("✅ Courses exported to: " + filePath.toAbsolutePath());
        System.out.println("📊 Total courses exported: " + courses.size());
    }

    /**
     * Export data in JSON-like format
     */
    public void exportStudentsToJSON(List<Student> students, String filename) throws IOException {
        Path filePath = config.getExportPath(filename);

        Files.createDirectories(filePath.getParent());

        List<String> jsonLines = new ArrayList<>();
        jsonLines.add("{");
        jsonLines.add("  \"exportInfo\": {");
        jsonLines.add("    \"timestamp\": \"" + LocalDateTime.now().format(dateFormatter) + "\",");
        jsonLines.add("    \"totalStudents\": " + students.size() + ",");
        jsonLines.add("    \"exportedBy\": \"CCRM System\"");
        jsonLines.add("  },");
        jsonLines.add("  \"students\": [");

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            jsonLines.add("    {");
            jsonLines.add("      \"id\": \"" + student.getId() + "\",");
            jsonLines.add("      \"regNo\": \"" + student.getRegNo() + "\",");
            jsonLines.add("      \"fullName\": \"" + escapeJSON(student.getFullName()) + "\",");
            jsonLines.add("      \"email\": \"" + student.getEmail() + "\",");
            jsonLines.add("      \"department\": \"" + (student.getDepartment() != null ? student.getDepartment() : "") + "\",");
            jsonLines.add("      \"semester\": " + student.getSemester() + ",");
            jsonLines.add("      \"status\": \"" + student.getStatus().name() + "\",");
            jsonLines.add("      \"gpa\": " + student.getGpa() + ",");
            jsonLines.add("      \"enrolledCourses\": [" + 
                student.getEnrolledCourses().stream()
                    .map(course -> "\"" + course + "\"")
                    .collect(Collectors.joining(", ")) + "]");

            if (i < students.size() - 1) {
                jsonLines.add("    },");
            } else {
                jsonLines.add("    }");
            }
        }

        jsonLines.add("  ]");
        jsonLines.add("}");

        Files.write(filePath, jsonLines, StandardCharsets.UTF_8);

        System.out.println("✅ Students exported to JSON: " + filePath.toAbsolutePath());
    }

    /**
     * Generate comprehensive report
     */
    public void generateFullReport(StudentService studentService, CourseService courseService) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String reportFilename = "CCRM_Report_" + timestamp + ".txt";
        Path filePath = config.getExportPath(reportFilename);

        Files.createDirectories(filePath.getParent());

        List<String> reportLines = new ArrayList<>();

        // Header
        reportLines.add("=".repeat(80));
        reportLines.add("              CAMPUS COURSE & RECORDS MANAGER");
        reportLines.add("                    COMPREHENSIVE REPORT");
        reportLines.add("=".repeat(80));
        reportLines.add("Generated on: " + LocalDateTime.now().format(dateFormatter));
        reportLines.add("System Version: " + config.getApplicationName() + " v" + config.getVersion());
        reportLines.add("");

        // Student Statistics
        StudentService.StudentStatistics studentStats = studentService.getStudentStatistics();
        reportLines.add("STUDENT SUMMARY:");
        reportLines.add("-".repeat(40));
        reportLines.add("Total Students: " + studentStats.getTotalStudents());
        reportLines.add("Average GPA: " + String.format("%.2f", studentStats.getAverageGPA()));
        reportLines.add("Minimum GPA: " + String.format("%.2f", studentStats.getMinGPA()));
        reportLines.add("Maximum GPA: " + String.format("%.2f", studentStats.getMaxGPA()));
        reportLines.add("");

        reportLines.add("Status Distribution:");
        studentStats.getStatusDistribution().forEach((status, count) -> 
            reportLines.add("  " + status.getDescription() + ": " + count));
        reportLines.add("");

        if (!studentStats.getDepartmentDistribution().isEmpty()) {
            reportLines.add("Department Distribution:");
            studentStats.getDepartmentDistribution().forEach((dept, count) -> 
                reportLines.add("  " + dept + ": " + count));
            reportLines.add("");
        }

        // Course Statistics
        CourseService.CourseStatistics courseStats = courseService.getCourseStatistics();
        reportLines.add("COURSE SUMMARY:");
        reportLines.add("-".repeat(40));
        reportLines.add("Total Courses: " + courseStats.getTotalCourses());
        reportLines.add("Average Credits: " + String.format("%.1f", courseStats.getAverageCredits()));
        reportLines.add("");

        reportLines.add("Department Distribution:");
        courseStats.getDepartmentDistribution().forEach((dept, count) -> 
            reportLines.add("  " + dept + ": " + count));
        reportLines.add("");

        reportLines.add("Semester Distribution:");
        courseStats.getSemesterDistribution().forEach((semester, count) -> 
            reportLines.add("  " + semester.getName() + ": " + count));
        reportLines.add("");

        // System Information
        reportLines.add("SYSTEM CONFIGURATION:");
        reportLines.add("-".repeat(40));
        reportLines.add("Data Folder: " + config.getDataFolderPath());
        reportLines.add("Backup Folder: " + config.getBackupFolderPath());
        reportLines.add("Debug Mode: " + (config.isDebugMode() ? "ON" : "OFF"));
        reportLines.add("Max Students per Course: " + config.getMaxStudentsPerCourse());
        reportLines.add("Max Courses per Student: " + config.getMaxCoursesPerStudent());
        reportLines.add("");

        reportLines.add("=".repeat(80));
        reportLines.add("                    END OF REPORT");
        reportLines.add("=".repeat(80));

        Files.write(filePath, reportLines, StandardCharsets.UTF_8);

        System.out.println("✅ Comprehensive report generated: " + filePath.toAbsolutePath());
    }

    /**
     * Backup all data to a timestamped folder
     */
    public void backupAllData(StudentService studentService, CourseService courseService) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFolderName = "backup_" + timestamp;
        Path backupPath = Paths.get(config.getBackupFolderPath(), backupFolderName);

        Files.createDirectories(backupPath);

        // Backup students
        exportStudentsToCSV(studentService.getAllStudents(), 
            backupFolderName + "/students_" + timestamp + ".csv");

        // Backup courses
        exportCoursesToCSV(courseService.getAllCourses(), 
            backupFolderName + "/courses_" + timestamp + ".csv");

        // Generate backup report
        generateFullReport(studentService, courseService);

        System.out.println("✅ Complete backup created in: " + backupPath.toAbsolutePath());
    }

    /**
     * Helper method to escape CSV values
     */
    private String escapeCSV(String value) {
        if (value == null) {
            return "\"\"";
        }

        // Escape quotes and wrap in quotes if necessary
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value.isEmpty() ? "\"\"" : value;
    }

    /**
     * Helper method to escape JSON values
     */
    private String escapeJSON(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    /**
     * List all export files
     */
    public void listExportFiles() throws IOException {
        Path exportDir = Paths.get(config.getExportFolderPath());

        if (!Files.exists(exportDir)) {
            System.out.println("📁 Export directory does not exist: " + exportDir.toAbsolutePath());
            return;
        }

        System.out.println("\n📁 Export Files:");
        System.out.println("-".repeat(50));

        Files.list(exportDir)
             .filter(Files::isRegularFile)
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
}