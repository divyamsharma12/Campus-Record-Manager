package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class representing a Person in the campus system
 * Demonstrates Abstraction and Encapsulation principles
 */
public abstract class Person {
    protected String id;
    protected String fullName;
    protected String email;
    protected String phone;
    protected LocalDateTime createdDate;

    // Constructor
    public Person(String id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdDate = LocalDateTime.now();
    }

    // Abstract method - must be implemented by subclasses (Abstraction)
    public abstract void displayInfo();

    // Abstract method for validation
    public abstract boolean isValid();

    // Concrete method with default implementation
    public String getFormattedCreatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return createdDate.format(formatter);
    }

    // Encapsulation: Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getCreatedDate() { return createdDate; }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", createdDate=" + getFormattedCreatedDate() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return id.equals(person.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}