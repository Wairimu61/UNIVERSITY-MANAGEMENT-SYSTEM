package UniversityManagementSystem; 
// Package declaration organizes classes under a named module

import java.util.Scanner; // Import Scanner class to read input from user
import java.util.regex.Pattern; // Import Pattern class to validate registration numbers with regex

/*
 * ABSTRACT CLASS: Person
 * Demonstrates Abstraction: abstract class with abstract method displayDetails()
 * Demonstrates Encapsulation: private attributes name and registrationNumber
 */
abstract class Person {

    private String name; // Encapsulated attribute for student's name
    private String registrationNumber; // Encapsulated attribute for student's registration number

    // Constructor to initialize name and registration number
    public Person(String name, String registrationNumber) {
        this.name = name;
        this.registrationNumber = registrationNumber;
    }

    // Getter for name (Encapsulation)
    public String getName() {
        return name;
    }

    // Getter for registration number (Encapsulation)
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    // Abstract method to be implemented in subclass (Polymorphism)
    public abstract void displayDetails();
}

/* CLASS: Student
 * Inherits from Person (Inheritance)
 * Implements methods for course selection, marks entry, grade calculation, and result display
 */
class Student extends Person {

    private String courseName; // Name of the selected course
    private String[] units; // Array of units in the course
    private int[] marks; // Array to store marks for each unit

    Scanner input = new Scanner(System.in); // Scanner object for reading user input

    // Constructor to initialize name and registration number via superclass
    public Student(String name, String regNo) {
        super(name, regNo);
    }

    /*
     * Set Course and Units Based on Choice
     * Also allocates marks array based on number of units
     */
    public void setCourse(int choice) {

        switch (choice) {
            case 1:
                courseName = "BSE";
                units = new String[]{
                        "Calculus", "Programming", "Data Structures",
                        "Operating Systems", "Electronics", "Linear Algebra"
                };
                break;

            case 2:
                courseName = "BCS";
                units = new String[]{
                        "Programming", "Database Systems",
                        "Networking", "Software Engineering",
                        "Computer Architecture"
                };
                break;

            case 3:
                courseName = "BSCIT";
                units = new String[]{
                        "Web Development", "Networking", "Database",
                        "Cyber Security", "Cloud Computing", "Data Analysis"
                };
                break;

            default:
                System.out.println("Invalid Course Selection!"); // Handle invalid input
                return;
        }

        marks = new int[units.length]; // Initialize marks array according to number of units
    }

    // Getter for course name
    public String getCourseName() {
        return courseName;
    }

    /*
     * Validate registration number using regex
     * Format: COURSE-XX-YYYY/ZZZZ
     * Example: BSE-01-0005/2026
     */
    public static boolean validateRegistration(String regNo, String course) {
        String pattern = course + "-\\d{2}-\\d{4}/\\d{4}";
        return Pattern.matches(pattern, regNo);
    }

    /*
     * Enter Marks for Each Unit
     * Includes input validation to ensure marks are between 0 and 100
     */
    public void enterMarks() {
        for (int i = 0; i < units.length; i++) {
            while (true) {
                System.out.print("Enter marks for " + units[i] + " (0-100): ");
                int mark = input.nextInt();

                if (mark >= 0 && mark <= 100) { // Validate mark range
                    marks[i] = mark;
                    break;
                } else {
                    System.out.println("Invalid marks! Must be between 0 and 100.");
                }
            }
        }
    }

    /*
     * Calculate grade based on marks
     * <40 => RETAKE, 40-49 => D, 60-69 => C, 80-89 => B, 90-100 => A
     */
    private String calculateGrade(int mark) {
        if (mark < 40)
            return "RETAKE";
        else if (mark < 50)
            return "D";
        else if (mark < 70)
            return "C";
        else if (mark < 90)
            return "B";
        else
            return "A";
    }

    /*
     * Display student details and result report
     * Overrides abstract method in Person (Polymorphism)
     * Includes ordered list of failed units if marks < 40
     */
    @Override
    public void displayDetails() {

        boolean hasFail = false; // Flag to track if student has any RETAKE units
        StringBuilder failedUnits = new StringBuilder(); // Stores names of units that require retake

        System.out.println("\n========== RESULT REPORT ==========");
        System.out.println("Name: " + getName());
        System.out.println("Reg No: " + getRegistrationNumber());
        System.out.println("Course: " + courseName);
        System.out.println("-----------------------------------");

        // Loop through each unit to display marks and grade
        for (int i = 0; i < units.length; i++) {

            String grade = calculateGrade(marks[i]);
            System.out.println(units[i] + " : " + marks[i] + " (" + grade + ")");

            if (marks[i] < 40) { // Check for RETAKE units
                hasFail = true;

                if (failedUnits.length() > 0) {
                    failedUnits.append(", ");
                }
                failedUnits.append(units[i]);
            }
        }

        System.out.println("-----------------------------------");

        if (hasFail) { // Handle failed units
            System.out.println("FINAL STATUS: FAIL");
            System.out.println("\nKindly register for retake unit(s) below:");

            // Split failed units into array and display as ordered list
            String[] failedArray = failedUnits.toString().split(", ");
            for (int i = 0; i < failedArray.length; i++) {
                System.out.println((i + 1) + ". " + failedArray[i]);
            }

        } else {
            System.out.println("FINAL STATUS: PASS");
            System.out.println("You can register for the next semester.");
        }

        System.out.println("==================================");
    }
}

/*
 * MAIN APPLICATION CLASS
 * Handles input from user and drives program
 */
public class UniversityManagementSystem {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("===== UNIVERSITY MANAGEMENT SYSTEM =====");

        // Input Student Name 
        System.out.print("Enter Student Name: ");
        String name = input.nextLine();

        // Course Selection
        int choice = 0;
        boolean validChoice = false;

        // Validate course input (must be 1, 2, or 3)
        while (!validChoice) {
            System.out.println("\nSelect Course:");
            System.out.println("1. BSE");
            System.out.println("2. BCS");
            System.out.println("3. BSCIT");
            System.out.print("Enter choice (1-3): ");

            String courseInput = input.nextLine();

            if (courseInput.equals("1") || courseInput.equals("2") || courseInput.equals("3")) {
                choice = Integer.parseInt(courseInput);
                validChoice = true;
            } else {
                System.out.println("❌ Course not found! Kindly select from the listed options.");
            }
        }

        // Create Student object with empty registration number initially
        Student student = new Student(name, "");
        student.setCourse(choice);

        // Registration Number Input 
        String regNo;
        while (true) {
            System.out.println("Enter Registration Number (Format: "
                    + student.getCourseName() + "-XX-YYYY/ZZZZ )");
            regNo = input.nextLine();

            if (Student.validateRegistration(regNo, student.getCourseName()))
                break;
            else
                System.out.println("❌ Invalid Registration Format! Try again.");
        }

        // Recreate Student object with validated registration number
        student = new Student(name, regNo);
        student.setCourse(choice);

        // Marks Entry 
        student.enterMarks();

        // Display Result 
        student.displayDetails();
    }
}