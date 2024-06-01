package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class Teacher {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private List<Class> enrolledClasses;
    private List<EnrolledClass> enrolledStudents;
    JDBC jdbc;

    // Constructor
    public Teacher(String username, String password, String fullName, String email, JDBC jdbc) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.enrolledClasses = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.jdbc = jdbc;
        retrieveEnrolledClassesFromDatabase();
        retrieveEnrolledStudentsFromDatabase();
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

    public void retrieveEnrolledClassesFromDatabase() {
        if (jdbc != null) {
            enrolledClasses.addAll(jdbc.getTeacherClasses(username));
        } else {
            System.out.println("JDBC instance not provided. Cannot retrieve enrolled classes.");
        }
    }

    public void retrieveEnrolledStudentsFromDatabase() {
        if (jdbc != null) {
            List<Class> teacherClasses = jdbc.getTeacherClasses(username);

            for (Class teacherClass : teacherClasses) {
                EnrolledClass enrolledClass = new EnrolledClass(teacherClass.getClassId(), teacherClass.getSubject());

                List<Integer> studentIds = jdbc.getStudentsInClass(teacherClass.getClassId());
                for (Integer studentId : studentIds) {
                    Student student = jdbc.getStudentById(studentId);
                    enrolledClass.addStudent(student);
                }

                enrolledStudents.add(enrolledClass);
            }
        } else {
            System.out.println("JDBC instance not provided. Cannot retrieve enrolled students.");
        }
    }
    public void displayTeacherClasses() {
        System.out.println("Classes assigned to " + fullName + ":");

        for (Class teacherClass : enrolledClasses) {
            System.out.println("Subject: " + teacherClass.getSubject());

            System.out.println();
        }
    }

    public void displayEnrolledStudents() {
        if (!enrolledClasses.isEmpty()) {
            System.out.println("Enrolled students for " + fullName + ":");

            for (EnrolledClass enrolledClass : enrolledStudents) {
                System.out.println("Class: " + enrolledClass.getSubject());
                for (Student student : enrolledClass.getAssignedStudents()) {
                    System.out.println("Student Username: " + student.getUsername());
                    System.out.println("Full Name: " + student.getFullName());
                    System.out.println();
                }
            }
        } else {
            System.out.println("No enrolled students found.");
        }
    }

    public void sortEnrolledStudentsAlphabetically() {
        for (EnrolledClass enrolledClass : enrolledStudents) {
            // Sort students alphabetically
            List<Student> students = enrolledClass.getAssignedStudents();
            Collections.sort(students, Comparator.comparing(Student::getFullName));
        }
    }
}

