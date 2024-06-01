package org.example;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Grade {
    private int gradeId;
    private int studentId;
    private int classId;
    private int grade;
    private Date gradeDate;
    private JDBC jdbc;

    // Constructor
    public Grade(int gradeId, int studentId, int classId, int grade, Date gradeDate, JDBC jdbc) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.classId = classId;
        this.grade = grade;
        this.gradeDate = gradeDate;
        this.jdbc = jdbc;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Fetch student name
        String studentName = jdbc.fetchStudentName(studentId);

        // Fetch class details (subject and teacher)
        Class classDetails = jdbc.fetchClassDetails(classId);
        String classSubject = classDetails.getSubject();
        String teacherName = jdbc.fetchTeacherName(classDetails.getTeacherID());

        return "Grade[" +
                "studentName='" + studentName + '\'' +
                ", classSubject='" + classSubject + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", grade=" + grade +
                ", gradeDate=" + dateFormat.format(gradeDate) +
                ']';
    }

    // Getters
    public int getGradeId() {
        return gradeId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getClassId() {
        return classId;
    }

    public int getGrade() {
        return grade;
    }

    public Date getGradeDate() {
        return gradeDate;
    }

    // Setters
    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setGradeDate(Date gradeDate) {
        this.gradeDate = gradeDate;
    }
}
