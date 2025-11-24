package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Application Configuration class implementing Singleton Pattern
 * Manages all configuration settings for the CCRM application
 */
public class AppConfig {
    // Static instance - volatile for thread safety
    private static volatile AppConfig instance;

    // Configuration properties
    private String dataFolderPath;
    private String backupFolderPath;
    private String importFolderPath;
    private String exportFolderPath;
    private boolean debugMode;
    private int maxStudentsPerCourse;
    private int maxCoursesPerStudent;
    private String applicationName;
    private String version;
    private Properties customProperties;

    // Private constructor prevents external instantiation
    private AppConfig() {
        initializeDefaultConfig();
        System.out.println("🔧 AppConfig initialized (Singleton Pattern)");
    }

    /**
     * Double-checked locking for thread-safe Singleton
     * Returns the single instance of AppConfig
     */
    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    /**
     * Initialize default configuration values
     */
    private void initializeDefaultConfig() {
        this.applicationName = "Campus Course & Records Manager";
        this.version = "1.0.0";
        this.dataFolderPath = "data";
        this.backupFolderPath = "backups";
        this.importFolderPath = "imports";
        this.exportFolderPath = "exports";
        this.debugMode = false;
        this.maxStudentsPerCourse = 50;
        this.maxCoursesPerStudent = 8;
        this.customProperties = new Properties();

        createDirectoriesIfNotExist();
    }

    /**
     * Create necessary directories if they don't exist
     */
    private void createDirectoriesIfNotExist() {
        try {
            java.nio.file.Files.createDirectories(Paths.get(dataFolderPath));
            java.nio.file.Files.createDirectories(Paths.get(backupFolderPath));
            java.nio.file.Files.createDirectories(Paths.get(importFolderPath));
            java.nio.file.Files.createDirectories(Paths.get(exportFolderPath));
        } catch (Exception e) {
            System.err.println("Warning: Could not create directories: " + e.getMessage());
        }
    }

    /**
     * Get full path for data files
     */
    public Path getDataPath(String filename) {
        return Paths.get(dataFolderPath, filename);
    }

    /**
     * Get full path for backup files
     */
    public Path getBackupPath(String filename) {
        return Paths.get(backupFolderPath, filename);
    }

    /**
     * Get full path for import files
     */
    public Path getImportPath(String filename) {
        return Paths.get(importFolderPath, filename);
    }

    /**
     * Get full path for export files
     */
    public Path getExportPath(String filename) {
        return Paths.get(exportFolderPath, filename);
    }

    /**
     * Load configuration from properties file (placeholder)
     */
    public void loadConfigFromFile(String configFile) {
        // Implementation would load from properties file
        System.out.println("Loading config from: " + configFile + " (placeholder)");
    }

    /**
     * Save current configuration to file (placeholder)
     */
    public void saveConfigToFile(String configFile) {
        // Implementation would save to properties file
        System.out.println("Saving config to: " + configFile + " (placeholder)");
    }

    /**
     * Display current configuration
     */
    public void displayConfig() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         APPLICATION CONFIGURATION");
        System.out.println("=".repeat(50));
        System.out.println("Application Name     : " + applicationName);
        System.out.println("Version              : " + version);
        System.out.println("Data Folder          : " + dataFolderPath);
        System.out.println("Backup Folder        : " + backupFolderPath);
        System.out.println("Import Folder        : " + importFolderPath);
        System.out.println("Export Folder        : " + exportFolderPath);
        System.out.println("Debug Mode           : " + (debugMode ? "ON" : "OFF"));
        System.out.println("Max Students/Course  : " + maxStudentsPerCourse);
        System.out.println("Max Courses/Student  : " + maxCoursesPerStudent);
        System.out.println("=".repeat(50));
    }

    // Getters and Setters
    public String getDataFolderPath() { return dataFolderPath; }
    public void setDataFolderPath(String dataFolderPath) {
        this.dataFolderPath = dataFolderPath;
        createDirectoriesIfNotExist();
    }

    public String getBackupFolderPath() { return backupFolderPath; }
    public void setBackupFolderPath(String backupFolderPath) {
        this.backupFolderPath = backupFolderPath;
        createDirectoriesIfNotExist();
    }

    public String getImportFolderPath() { return importFolderPath; }
    public void setImportFolderPath(String importFolderPath) {
        this.importFolderPath = importFolderPath;
        createDirectoriesIfNotExist();
    }

    public String getExportFolderPath() { return exportFolderPath; }
    public void setExportFolderPath(String exportFolderPath) {
        this.exportFolderPath = exportFolderPath;
        createDirectoriesIfNotExist();
    }

    public boolean isDebugMode() { return debugMode; }
    public void setDebugMode(boolean debugMode) { this.debugMode = debugMode; }

    public int getMaxStudentsPerCourse() { return maxStudentsPerCourse; }
    public void setMaxStudentsPerCourse(int maxStudentsPerCourse) {
        this.maxStudentsPerCourse = maxStudentsPerCourse;
    }

    public int getMaxCoursesPerStudent() { return maxCoursesPerStudent; }
    public void setMaxCoursesPerStudent(int maxCoursesPerStudent) {
        this.maxCoursesPerStudent = maxCoursesPerStudent;
    }

    public String getApplicationName() { return applicationName; }
    public String getVersion() { return version; }

    public Properties getCustomProperties() { return new Properties(customProperties); }
    public void setCustomProperty(String key, String value) {
        customProperties.setProperty(key, value);
    }

    public String getCustomProperty(String key, String defaultValue) {
        return customProperties.getProperty(key, defaultValue);
    }

    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed for Singleton");
    }
}
