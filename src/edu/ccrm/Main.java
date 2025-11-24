package edu.ccrm;

import edu.ccrm.cli.CLIManager;
import edu.ccrm.config.AppConfig;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("    🎓 Campus Course & Records Manager (CCRM) v1.0");
        System.out.println("==========================================================");

        try {
            // Initialize application configuration (Singleton pattern)
            AppConfig config = AppConfig.getInstance();

            // Start the CLI interface
            CLIManager cli = new CLIManager();
            cli.start();

        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}