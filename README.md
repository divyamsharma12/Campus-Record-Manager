# 🎓 Campus Course & Records Manager (CCRM)

## Overview
A comprehensive Java application for managing student records, courses, and enrollments in an educational institution. Built using advanced Object-Oriented Programming concepts and modern Java features.

## Features
- **Student Management**: Add, view, search, and manage student records
- **Course Management**: Create and manage courses with Builder pattern
- **Enrollment System**: Handle student-course enrollments with validation
- **Grade Management**: Assign grades and generate transcripts
- **File I/O Operations**: Import/export data using NIO.2 APIs
- **Advanced Reporting**: Statistics and analytics with Stream API
- **Utility Functions**: Recursion demonstrations and sorting algorithms

## Technical Highlights
### Object-Oriented Programming
- **Inheritance**: Person → Student, Person → Instructor
- **Polymorphism**: Method overriding and overloading
- **Encapsulation**: Private fields with proper accessors
- **Abstraction**: Abstract classes and interfaces

### Design Patterns
- **Singleton Pattern**: Configuration management
- **Builder Pattern**: Course creation
- **Service Layer Pattern**: Business logic separation

### Advanced Java Features
- **Enums with Methods**: Grade system with GPA calculation
- **Lambda Expressions**: Functional programming in Comparators
- **Stream API**: Data processing and filtering
- **NIO.2 File Operations**: Modern file I/O
- **LocalDateTime API**: Date/time handling
- **Exception Handling**: Custom exceptions with detailed information

## Project Structure
src/
├── edu/ccrm/
│ ├── Main.java # Application entry point
│ ├── cli/ # Command Line Interface
│ │ └── CLIManager.java
│ ├── config/ # Configuration management
│ │ └── AppConfig.java
│ ├── domain/ # Domain entities
│ │ ├── Person.java # Abstract base class
│ │ ├── Student.java # Student entity
│ │ ├── Course.java # Course entity (Builder pattern)
│ │ ├── Enrollment.java # Association class
│ │ └── [other domain classes]
│ ├── service/ # Business logic layer
│ │ ├── StudentService.java
│ │ ├── CourseService.java
│ │ └── EnrollmentService.java
│ ├── exceptions/ # Custom exceptions
│ ├── io/ # File I/O operations
│ └── util/ # Utility classes


## How to Run
### Prerequisites
- Java 11 or higher
- Command line or IDE (VS Code, IntelliJ IDEA, Eclipse)

### Compilation & Execution
```bash
# Navigate to project directory
cd CampusCourseRecordsManager/src

# Compile
javac edu/ccrm/Main.java

# Run
java edu.ccrm.Main
Using VS Code
Open the project folder in VS Code

Navigate to src/edu/ccrm/Main.java

Click the ▶️ Run button

Sample Usage
The application comes with pre-loaded sample data:

3 students with different departments and GPAs

2 courses from different departments

Interactive menu system for all operations

Academic Concepts Demonstrated
Recursion: Factorial, Fibonacci, Binary Search

Collections: Lists, Sets, Maps with generic types

File Operations: CSV import/export, JSON-like format

Data Validation: Input validation with regex patterns

Sorting Algorithms: Multiple sorting criteria with Comparators

Statistical Analysis: GPA calculations, distribution analysis

Development
Language: Java 11+

Architecture: Layered architecture with separation of concerns

Testing: Manual testing through CLI interface

Documentation: Comprehensive JavaDoc comments

Author
Developed as an advanced Java programming project demonstrating enterprise-level coding practices and design patterns.
