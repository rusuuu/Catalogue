package org.example;
import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;


public class Main {
    public static void main(String[] args) {
        JDBC db = new JDBC();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter role: ");
        String role = scanner.nextLine();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Read password
        System.out.print("Enter password: ");
        String password = scanner.nextLine();


        if(Objects.equals(role, "admin") && Objects.equals(username, "BOSS") && Objects.equals(password, "BOSS")) {
            System.out.println("Welcome dear master!");

            boolean trueChoice = true;
            do
            {
                System.out.println("Choose an option:");
                System.out.println("1. Add a student section");
                System.out.println("2. Remove a student section");
                System.out.println("3. Add a student to a class section");
                System.out.println("4. Remove a student from section");
                System.out.println("5. Print all students section");
                System.out.println("6. Add a teacher section");
                System.out.println("7. Remove a teacher section");
                System.out.println("8. Print all teachers section");
                System.out.println("9. Add a class section");
                System.out.println("10. Remove a class section");
                System.out.println("11. Print all classes section");
                System.out.println("12. Add a grade section");
                System.out.println("13. Remove a grade section");
                System.out.println("14. Print all grades section");
                System.out.println("15. Print average grade for a student section");
                System.out.println("16. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Please enter the user name of the student: ");
                        String studentUserName = scanner.nextLine();

                        System.out.println("Please enter the user password of the student: ");
                        String studentUserPassword = scanner.nextLine();

                        System.out.println("Please enter the full name of the student: ");
                        String studentFullName = scanner.nextLine();

                        System.out.println("Please enter the email of the student: ");
                        String studentEmail = scanner.nextLine();

                        db.addStudent(studentUserName, studentUserPassword, studentFullName, studentEmail);
                        break;
                    case 2:
                        System.out.println("Please enter the user name of the student: ");
                        studentUserName = scanner.nextLine();

                        db.removeStudent(studentUserName);
                        break;
                    case 3:
                        System.out.println("Please enter the student id: ");
                        int studentId = scanner.nextInt();

                        System.out.println("Please enter the class id: ");
                        int classId = scanner.nextInt();

                        db.addEnrollment(studentId, classId);
                        break;
                    case 4:
                        System.out.println("Please enter the student id: ");
                        studentId = scanner.nextInt();

                        System.out.println("Please enter the class id: ");
                        classId = scanner.nextInt();

                        db.removeEnrollment(studentId, classId);
                        break;
                    case 5:
                        db.outputAllStudents();
                        break;
                    case 6:
                        System.out.println("Please enter the user name of the teacher: ");
                        String teacherUserName = scanner.nextLine();

                        System.out.println("Please enter the user password of the teacher: ");
                        String teacherUserPassword = scanner.nextLine();

                        System.out.println("Please enter the full name of the teacher: ");
                        String teacherFullName = scanner.nextLine();

                        System.out.println("Please enter the email of the teacher: ");
                        String teacherEmail = scanner.nextLine();

                        db.addTeacher(teacherUserName, teacherUserPassword, teacherFullName, teacherEmail);
                        break;
                    case 7:
                        System.out.println("Please enter the user name of the teacher: ");
                        teacherUserName = scanner.nextLine();

                        db.removeTeacher(teacherUserName);
                        break;
                    case 8:
                        db.outputAllTeachers();
                        break;
                    case 9:
                        System.out.println("Please enter the subject name: ");
                        String Subject = scanner.nextLine();

                        System.out.println("Please enter the user name of the teacher: ");
                        teacherUserName = scanner.nextLine();

                        db.addClass(Subject, teacherUserName);
                        break;
                    case 10:
                        System.out.println("Please enter the subject name: ");
                        Subject = scanner.nextLine();

                        db.removeClass(Subject);
                        break;
                    case 11:
                        db.outputAllClasses();
                        break;
                    case 12:
                        System.out.println("Please enter the user name of the student: ");
                        studentUserName = scanner.nextLine();

                        System.out.println("Please enter the subject: ");
                        String subject = scanner.nextLine();

                        System.out.println("Please enter the grade: ");
                        int grade = scanner.nextInt();

                        // Consume the newline character
                        scanner.nextLine();

                        System.out.println("Please enter the date of the grade (dd.MM.yyyy): ");
                        String dateStr = scanner.nextLine();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        java.util.Date parsedDate;
                        try {
                            parsedDate = dateFormat.parse(dateStr);
                        } catch (ParseException e) {
                            System.out.println("Error parsing date. Please enter a valid date format (dd.MM.yyyy).");
                            break;
                        }

                        String strDate = dateFormat.format(parsedDate);
                        db.addGrade(studentUserName, subject, grade, strDate);
                        break;
                    case 13:
                        System.out.println("Please enter the user name of the student: ");
                        studentUserName = scanner.nextLine();

                        System.out.println("Please enter the subject: ");
                        subject = scanner.nextLine();

                        // Consume the newline character
                        scanner.nextLine();

                        System.out.println("Please enter the date of the grade (dd.MM.yyyy): ");
                        dateStr = scanner.nextLine();

                        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        try {
                            parsedDate = dateFormat.parse(dateStr);
                        } catch (ParseException e) {
                            System.out.println("Error parsing date. Please enter a valid date format (dd.MM.yyyy).");
                            break;
                        }

                        strDate = dateFormat.format(parsedDate);
                        db.removeGrade(studentUserName, subject, strDate);
                        break;
                    case 14:
                        db.outputAllGrades();
                        break;
                    case 15:
                        System.out.println("Please enter the full name of the student: ");
                        studentFullName = scanner.nextLine();

                        studentId = db.fetchStudentId(studentFullName);

                        if(studentId != -1){
                            db.printAverageGradeForStudent(studentId);
                        } else {
                            System.out.println("Student not found.");
                        }

                        break;
                    case 16:
                        System.out.println("Exiting the menu");
                        trueChoice = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                        trueChoice = false;
                }
            }while(trueChoice == true);
        }
        else {
            if(role.equalsIgnoreCase( "student")) {
                Student currenStudent = db.loginStudent(username,password);
                if(currenStudent != null){
                    System.out.println("Welcome " + role + " " + currenStudent.getFullName());

                    boolean trueChoice = true;
                    do
                    {
                        System.out.println("Choose an option:");
                        System.out.println("1. About section");
                        System.out.println("2. Your classes and teachers");
                        System.out.println("3. Grades");
                        System.out.println("4. Exit");
                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                            case 1:
                                System.out.println("You've selected the about section");
                                currenStudent.displayStudentInfo();
                                break;
                            case 2:
                                System.out.println("You've selected the classes and teachers section");
                                currenStudent.displayClassesWithTeachers();
                                break;
                            case 3:
                                System.out.println("You've selected the grades section");

                                System.out.println("Please enter the class: ");
                                String subject = scanner.nextLine();

                                System.out.println("Please enter the teacher username: ");
                                String teacherUsername = scanner.nextLine();

                                int teacherId = db.fetchTeacherId(teacherUsername);

                                currenStudent.displayGradesForClass(subject,teacherId);
                                break;
                            case 4:
                                System.out.println("Exiting the menu");
                                trueChoice = false;
                                break;
                            default:
                                System.out.println("Invalid choice. Please select a valid option.");
                                trueChoice = false;
                        }
                    }while(trueChoice == true);
                }
            }
            else{
                if(role.equalsIgnoreCase( "teacher")) {
                    Teacher currentTeacher = db.loginTeacher(username,password);
                    if(currentTeacher != null){
                        System.out.println("Welcome " + role + " " + currentTeacher.getFullName());

                        boolean trueChoice = true;
                        do
                        {
                            System.out.println("Choose an option:");
                            System.out.println("1. Your classes section");
                            System.out.println("2. Your students secction");
                            System.out.println("3. Your students ordered alphabetically section");
                            System.out.println("4. Add a grade to a student section");
                            System.out.println("5. Remove a grade from a student section");
                            System.out.println("6. Output the average grade for a student section");
                            System.out.println("7. About section for a student section");
                            System.out.println("8. Exit");
                            int choice = scanner.nextInt();
                            scanner.nextLine();

                            switch (choice) {
                                case 1:
                                    System.out.println("You've selected the classes section");
                                    currentTeacher.displayTeacherClasses();
                                    break;
                                case 2:
                                    System.out.println("You've selected the students section");
                                    currentTeacher.displayEnrolledStudents();
                                    break;
                                case 3:
                                    System.out.println("You've selected the alphabetically ordered students section");
                                    currentTeacher.sortEnrolledStudentsAlphabetically();
                                    currentTeacher.displayEnrolledStudents();
                                    break;
                                case 4:
                                    System.out.println("You've selected the add a grade section");

                                    System.out.println("Please enter the user name of the student: ");
                                    String studentUserName = scanner.nextLine();

                                    System.out.println("Please enter the subject: ");
                                    String subject = scanner.nextLine();

                                    System.out.println("Please enter the grade: ");
                                    int grade = scanner.nextInt();

                                    // Consume the newline character
                                    scanner.nextLine();

                                    System.out.println("Please enter the date of the grade (dd.MM.yyyy): ");
                                    String dateStr = scanner.nextLine();

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                                    java.util.Date parsedDate;
                                    try {
                                        parsedDate = dateFormat.parse(dateStr);
                                    } catch (ParseException e) {
                                        System.out.println("Error parsing date. Please enter a valid date format (dd.MM.yyyy).");
                                        break;
                                    }

                                    String strDate = dateFormat.format(parsedDate);
                                    db.addGrade(studentUserName, subject, grade, strDate);
                                    break;
                                case 5:
                                    System.out.println("You've selected the remove a grade section");

                                    System.out.println("Please enter the user name of the student: ");
                                    studentUserName = scanner.nextLine();

                                    System.out.println("Please enter the subject: ");
                                    subject = scanner.nextLine();

                                    // Consume the newline character
                                    scanner.nextLine();

                                    System.out.println("Please enter the date of the grade (dd.MM.yyyy): ");
                                    dateStr = scanner.nextLine();

                                    dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                                    try {
                                        parsedDate = dateFormat.parse(dateStr);
                                    } catch (ParseException e) {
                                        System.out.println("Error parsing date. Please enter a valid date format (dd.MM.yyyy).");
                                        break;
                                    }

                                    strDate = dateFormat.format(parsedDate);
                                    db.removeGrade(studentUserName, subject, strDate);
                                    break;
                                case 6:
                                    System.out.println("Please enter the full name of the student: ");
                                    String studentFullName = scanner.nextLine();

                                    int studentId = db.fetchStudentId(studentFullName);

                                    if(studentId != -1){
                                        db.printAverageGradeForStudent(studentId);
                                    } else {
                                        System.out.println("Student not found.");
                                    }

                                    break;
                                case 7:
                                    System.out.println("Please enter the full name of the student: ");
                                    studentFullName = scanner.nextLine();

                                    studentId = db.fetchStudentId(studentFullName);

                                    if(studentId != -1){
                                        db.displayStudentInfoById(studentId);
                                    } else {
                                        System.out.println("Student not found.");
                                    }

                                    break;
                                case 8:
                                    System.out.println("Exiting the menu");
                                    trueChoice = false;
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please select a valid option.");
                                    trueChoice = false;
                            }
                        }while(trueChoice == true);
                    }
                }

            }
        }
        scanner.close();
    }
}