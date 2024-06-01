package org.example;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

public class JDBC {
    private Connection connection;

    public JDBC()
    {
        try{
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/catalogue_schema",
                    "root",
                    "1q2w3e"
            );
        }
        catch(SQLException e) {
            e.printStackTrace();
            this.connection = null;
        }
    }

    //Students table related functions

    public Student loginStudent(String userName, String userPassword) {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return null;
        }

        try {
            String query = "SELECT * FROM students WHERE user_name = ? AND user_password = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPassword);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Student found, retrieve attributes and create a Student object
                String fullName = resultSet.getString("full_name");
                String studentEmail = resultSet.getString("student_email");

                return new Student(userName, userPassword, fullName, studentEmail, this);
            } else {
                // Student not found
                System.out.println("Login failed. Username or password incorrect.");
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void registerStudent(String userName, String userPassword, String fullName, String studentEmail) {
        addStudent(userName, userPassword, fullName, studentEmail);
    }
    public void addStudent(String userName, String userPassword, String fullName, String studentEmail) {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            String query = "INSERT INTO students (user_name, user_password, full_name, student_email) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPassword);
            preparedStatement.setString(3, fullName);
            preparedStatement.setString(4, studentEmail);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("A new student was inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeStudent(String username) {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            String query = "DELETE FROM students WHERE user_name = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("A student was removed successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String fetchStudentName(int studentId) {
        String studentName = null;

        if (connection == null) {
            System.out.println("Failed to establish database connection.");
            return studentName;
        }

        try {
            String query = "SELECT full_name FROM students WHERE student_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, studentId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                studentName = resultSet.getString("full_name");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching student name: " + e.getMessage());
            e.printStackTrace();
        }

        return studentName;
    }

    public int fetchStudentId(String fullName) {
        int studentId = -1;

        if (connection == null) {
            System.out.println("Failed to establish database connection.");
            return studentId;
        }

        try {
            String query = "SELECT student_id FROM students WHERE full_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, fullName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                studentId = resultSet.getInt("student_id");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching student ID: " + e.getMessage());
            e.printStackTrace();
        }

        return studentId;
    }

    public void addEnrollment(int student_id, int class_id) {
        if (connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            String query = "INSERT INTO student_classes (student_id, class_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, student_id);
            preparedStatement.setInt(2, class_id);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("Enrollment added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeEnrollment(int studentId, int classId) {
        if (connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            String query = "DELETE FROM StudentClasses WHERE StudentID = ? AND ClassID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, classId);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("Enrollment removed successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student getStudentById(int studentId) {
        String query = "SELECT * FROM students WHERE student_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("user_name");
                    String password = resultSet.getString("user_password");
                    String fullName = resultSet.getString("full_name");
                    String email = resultSet.getString("student_email");

                    // Assuming you have a constructor for the Student class
                    return new Student(username, password, fullName, email, this);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception based on your application's needs
        }

        return null; // Return null if student not found or an error occurred
    }

    public List<Class> getEnrolledClasses(String username) {
        List<Class> enrolledClasses = new ArrayList<>();

        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return enrolledClasses;
        }

        try {
            String query = "SELECT c.* FROM classes c " +
                    "JOIN student_classes sc ON c.class_id = sc.class_id " +
                    "JOIN students s ON sc.student_id = s.student_id " +
                    "WHERE s.user_name = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int classId = resultSet.getInt("class_id");
                String subject = resultSet.getString("subject");
                int teacherID = resultSet.getInt("teacher_id");

                enrolledClasses.add(new Class(classId, subject, teacherID, this));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving enrolled classes: " + e.getMessage());
            e.printStackTrace();
        }

        return enrolledClasses;
    }

    public List<Grade> getGradesForStudent(String username) {
        List<Grade> studentGrades = new ArrayList<>();

        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return studentGrades;
        }

        try {
            // Assuming there is a students table with student_id and user_name
            String studentIdQuery = "SELECT student_id FROM students WHERE user_name = ?";
            PreparedStatement studentIdStatement = this.connection.prepareStatement(studentIdQuery);
            studentIdStatement.setString(1, username);
            ResultSet studentIdResultSet = studentIdStatement.executeQuery();

            if (studentIdResultSet.next()) {
                int studentId = studentIdResultSet.getInt("student_id");

                String query = "SELECT * FROM grades WHERE student_id = ?";
                PreparedStatement preparedStatement = this.connection.prepareStatement(query);
                preparedStatement.setInt(1, studentId);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int gradeId = resultSet.getInt("grade_id");
                    int classId = resultSet.getInt("class_id");
                    int grade = resultSet.getInt("grade");
                    Date gradeDate = resultSet.getDate("grade_date");
                    // Additional attributes as needed

                    studentGrades.add(new Grade(gradeId, classId, studentId, grade, gradeDate,this));
                }
            } else {
                System.out.println("Student not found with username: " + username);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching grades for student: " + e.getMessage());
            e.printStackTrace();
        }

        return studentGrades;
    }

    public void outputAllStudents() {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("student_id") + " " + resultSet.getString("user_name") + " " + resultSet.getString("user_password") + " " + resultSet.getString("full_name") + " " + resultSet.getString("student_email"));
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayStudentInfoById(int studentId) {
        Student student = getStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found");
        } else {
            student.displayStudentInfo();
        }
    }

    //Teachers table related functions

    public Teacher loginTeacher(String userName, String userPassword) {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return null;
        }

        try {
            String query = "SELECT * FROM teachers WHERE user_name = ? AND user_password = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPassword);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Student found, retrieve attributes and create a Student object
                String fullName = resultSet.getString("full_name");
                String teacherEmail = resultSet.getString("teacher_email");

                return new Teacher(userName, userPassword, fullName, teacherEmail, this);
            } else {
                // Student not found
                System.out.println("Login failed. Username or password incorrect.");
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void registerTeacher(String userName, String userPassword, String fullName, String teacherEmail) {
        addStudent(userName, userPassword, fullName, teacherEmail);
    }
    public void addTeacher(String userName, String userPassword, String fullName, String teacherEmail) {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            String query = "INSERT INTO teachers (user_name, user_password, full_name, teacher_email) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPassword);
            preparedStatement.setString(3, fullName);
            preparedStatement.setString(4, teacherEmail);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("A new teacher was inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeTeacher(String username) {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            String query = "DELETE FROM teachers WHERE user_name = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("A teacher was removed successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String fetchTeacherName(int teacherID) {
        String teacherName = null;

        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return teacherName;
        }

        try {
            String query = "SELECT full_name FROM teachers WHERE teacher_id = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, teacherID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                teacherName = resultSet.getString("full_name");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching teacher name: " + e.getMessage());
            e.printStackTrace();
        }

        return teacherName;
    }

    public String fetchTeacherUsername(int teacherId) {
        String teacherUsername = null;

        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return teacherUsername;
        }

        try {
            String query = "SELECT user_name FROM teachers WHERE teacher_id = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, teacherId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                teacherUsername = resultSet.getString("user_name");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching teacher username: " + e.getMessage());
            e.printStackTrace();
        }

        return teacherUsername;
    }

    public int fetchTeacherId(String teacherUsername) {
        int teacherId = -1;

        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return teacherId;
        }

        try {
            String query = "SELECT teacher_id FROM teachers WHERE user_name = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, teacherUsername);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                teacherId = resultSet.getInt("teacher_id");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching teacher ID: " + e.getMessage());
            e.printStackTrace();
        }

        return teacherId;
    }

    public List<Class> getTeacherClasses(String teacherUsername) {
        List<Class> teacherClasses = new ArrayList<>();

        int teacherId = fetchTeacherId(teacherUsername);

        if (teacherId != -1) {
            String query = "SELECT * FROM classes WHERE teacher_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, teacherId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int classId = resultSet.getInt("class_id");
                        String subject = resultSet.getString("subject");

                        Class teacherClass = new Class(classId, subject, teacherId, this);

                        teacherClasses.add(teacherClass);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Teacher not found with username: " + teacherUsername);
        }

        return teacherClasses;
    }

    public void outputAllTeachers() {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM teachers");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("teacher_id") + " " + resultSet.getString("user_name") + " " + resultSet.getString("user_password") + " " + resultSet.getString("full_name") + " " + resultSet.getString("teacher_email"));
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Classes table related functions
    public void addClass(String subject, String teacherUserName) {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            String query = "SELECT teacher_id FROM teachers WHERE user_name = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, teacherUserName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int teacherId = resultSet.getInt("teacher_id");

                query = "INSERT INTO classes (subject, teacher_id) VALUES (?, ?)";
                preparedStatement = this.connection.prepareStatement(query);
                preparedStatement.setString(1, subject);
                preparedStatement.setInt(2, teacherId);

                int result = preparedStatement.executeUpdate();

                if (result > 0) {
                    System.out.println("A new class was inserted successfully!");
                }
            } else {
                System.out.println("Teacher not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeClass(String subject) {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            String query = "DELETE FROM classes WHERE subject = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, subject);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("Classes with subject '" + subject + "' were removed successfully!");
            } else {
                System.out.println("No classes found with subject '" + subject + "'.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Class fetchClassDetails(int classId) {
        Class classDetails = null;

        if (connection == null) {
            System.out.println("Failed to establish database connection.");
            return classDetails;
        }

        try {
            String query = "SELECT * FROM classes WHERE class_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, classId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int fetchedClassId = resultSet.getInt("class_id");
                String subject = resultSet.getString("subject");
                int teacherId = resultSet.getInt("teacher_id");

                classDetails = new Class(fetchedClassId, subject, teacherId,this);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching class details: " + e.getMessage());
            e.printStackTrace();
        }

        return classDetails;
    }

    public List<Integer> getStudentsInClass(int classId) {
        List<Integer> studentIds = new ArrayList<>();

        if (connection == null) {
            System.out.println("Failed to establish database connection.");
            return studentIds;
        }

        try {
            String query = "SELECT student_id FROM student_classes WHERE class_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, classId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                studentIds.add(resultSet.getInt("student_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentIds;
    }

    public void outputAllClasses() {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            Statement statement = this.connection.createStatement();
            String query = "SELECT * FROM classes " + "JOIN teachers ON classes.teacher_id = teachers.teacher_id ";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println(resultSet.getString("class_id") + " " + resultSet.getString("subject") + " " + resultSet.getString("teacher_id") + " " + resultSet.getString("full_name"));
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Grades table related functions
    public void addGrade(String studentUserName, String subject, int grade, String dateStr) {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            Date gradeDate = null;
            if (dateStr != null && !dateStr.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date parsedDate = dateFormat.parse(dateStr);
                gradeDate = new Date(parsedDate.getTime());
            } else {
                gradeDate = new Date(System.currentTimeMillis());
            }

            String studentQuery = "SELECT student_id FROM students WHERE user_name = ?";
            PreparedStatement studentPreparedStatement = this.connection.prepareStatement(studentQuery);
            studentPreparedStatement.setString(1, studentUserName);
            ResultSet studentResultSet = studentPreparedStatement.executeQuery();

            if (studentResultSet.next()) {
                int studentId = studentResultSet.getInt("student_id");

                String classQuery = "SELECT class_id FROM classes WHERE subject = ?";
                PreparedStatement classPreparedStatement = this.connection.prepareStatement(classQuery);
                classPreparedStatement.setString(1, subject);
                ResultSet classResultSet = classPreparedStatement.executeQuery();

                if (classResultSet.next()) {
                    int classId = classResultSet.getInt("class_id");

                    String gradeQuery = "INSERT INTO grades (student_id, class_id, grade, grade_date) VALUES (?, ?, ?, ?)";
                    PreparedStatement gradePreparedStatement = this.connection.prepareStatement(gradeQuery);
                    gradePreparedStatement.setInt(1, studentId);
                    gradePreparedStatement.setInt(2, classId);
                    gradePreparedStatement.setInt(3, grade);
                    gradePreparedStatement.setDate(4, gradeDate);

                    int result = gradePreparedStatement.executeUpdate();

                    if (result > 0) {
                        System.out.println("A new grade was inserted successfully!");
                    }
                } else {
                    System.out.println("Class not found.");
                }
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void removeGrade(String studentUserName, String subject, String dateStr) {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date parsedDate = dateFormat.parse(dateStr);
            Date gradeDate = new Date(parsedDate.getTime());

            String studentQuery = "SELECT student_id FROM students WHERE user_name = ?";
            PreparedStatement studentPreparedStatement = this.connection.prepareStatement(studentQuery);
            studentPreparedStatement.setString(1, studentUserName);
            ResultSet studentResultSet = studentPreparedStatement.executeQuery();

            if (studentResultSet.next()) {
                int studentId = studentResultSet.getInt("student_id");

                String classQuery = "SELECT class_id FROM classes WHERE subject = ?";
                PreparedStatement classPreparedStatement = this.connection.prepareStatement(classQuery);
                classPreparedStatement.setString(1, subject);
                ResultSet classResultSet = classPreparedStatement.executeQuery();

                if (classResultSet.next()) {
                    int classId = classResultSet.getInt("class_id");

                    String gradeQuery = "DELETE FROM grades WHERE student_id = ? AND class_id = ? AND grade_date = ?";
                    PreparedStatement gradePreparedStatement = this.connection.prepareStatement(gradeQuery);
                    gradePreparedStatement.setInt(1, studentId);
                    gradePreparedStatement.setInt(2, classId);
                    gradePreparedStatement.setDate(3, gradeDate);

                    int result = gradePreparedStatement.executeUpdate();

                    if (result > 0) {
                        System.out.println("Grade removed successfully!");
                    } else {
                        System.out.println("No grade found for the specified student, class, and date.");
                    }
                } else {
                    System.out.println("Class not found.");
                }
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }
    public void outputAllGrades() {
        if (this.connection == null) {
            System.out.println("Failed to establish database connection.");
            return;
        }

        try {
            Statement statement = this.connection.createStatement();
            String query = "SELECT * FROM grades " + "JOIN students ON grades.student_id = students.student_id " + "JOIN classes ON grades.class_id = classes.class_id";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                java.sql.Date gradeDate = resultSet.getDate("grade_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String formattedDate = dateFormat.format(gradeDate);
                System.out.println(resultSet.getString("student_id") + " " + resultSet.getString("full_name") + " " + resultSet.getString("subject") + " " + resultSet.getString("grade") + " " + formattedDate);
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printAverageGradeForStudent(int studentId) {
        Student student = getStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found");
        } else {
            double average = student.calculateAverageGrade();
            System.out.println("The average grade for student with ID " + studentId + " is: " + average);
        }
    }
}
