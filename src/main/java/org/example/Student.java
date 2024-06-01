package org.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
public class Student {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private List<Class> enrolledClasses;
    private List<Grade> grades;
    private JDBC jdbc;

    private float average;

    // Constructor
    public Student(String username, String password, String fullName, String email, JDBC jdbc) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.enrolledClasses = new ArrayList<>();
        this.grades = new ArrayList<>();
        this.jdbc = jdbc;
        retrieveEnrolledClassesFromDatabase();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Student student = (Student) obj;
        return username.equals(student.username) &&
                password.equals(student.password) &&
                fullName.equals(student.fullName) &&
                email.equals(student.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, fullName, email);
    }

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; }

    // Getter and Setter for fullName
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Methods to handle enrolled classes

    public void retrieveEnrolledClassesFromDatabase() {
        if (jdbc != null) {
            enrolledClasses.addAll(jdbc.getEnrolledClasses(username));
            grades.addAll(jdbc.getGradesForStudent(username));
        } else {
            System.out.println("JDBC instance not provided. Cannot retrieve enrolled classes.");
        }
    }

    public List<Class> getEnrolledClasses() {
        return enrolledClasses;
    }

    // Methods to handle grades
    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    public List<Grade> getGrades() {
        return grades;
    }

    // Method to output all information
    public void displayStudentInfo() {
        System.out.println("Student Information:");
        System.out.println("Username: " + username);
        System.out.println("Full Name: " + fullName);
        System.out.println("Email: " + email);

        System.out.println("\nEnrolled Classes:");
        for (Class enrolledClass : enrolledClasses) {
            System.out.println(enrolledClass);
        }

        System.out.println("\nGrades:");
        for (Grade grade : grades) {
            System.out.println(grade);
        }
    }

    // Method to output grades for a specific class
    public void displayGradesForClass(String subject, int teacherID) {
        boolean gradesFound = false;

        for (Class enrolledClass : enrolledClasses) {
            if (enrolledClass.getSubject().equals(subject) && enrolledClass.getTeacherID() == teacherID) {
                int classId = enrolledClass.getClassId();
                for (Grade grade : grades) {
                    if (grade.getClassId() == classId) {
                        System.out.println(grade);
                        gradesFound = true;
                    }
                }
            }
        }

        if (!gradesFound) {
            System.out.println("No grades found for the specified class.");
        }
    }

    public void displayClassesWithTeachers() {
        System.out.println("\nEnrolled Classes with Teachers:");
        for (Class enrolledClass : enrolledClasses) {
            int teacherId = enrolledClass.getTeacherID();
            String teacherName = jdbc.fetchTeacherName(teacherId);
            String teacherUsername = jdbc.fetchTeacherUsername(teacherId);

            System.out.println("Class: " + enrolledClass.getSubject() + " | Teacher: " + teacherName + " | Username: " + teacherUsername);
        }
    }

    public double calculateAverageGrade() {
        if (grades.isEmpty()) {
            return 0.0;
        }

        int sum = 0;
        for (Grade grade : grades) {
            sum += grade.getGrade();
        }

        return (double) sum / grades.size();
    }
}