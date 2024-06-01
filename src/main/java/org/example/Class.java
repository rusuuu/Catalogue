package org.example;

public class Class {
    private int classId;
    private String subject;
    private int teacher_id;
    private JDBC jdbc;

    // Constructor
    public Class(int classId, String subject, int teacherID, JDBC jdbc) {
        this.classId = classId;
        this.subject = subject;
        this.teacher_id = teacherID;
        this.jdbc = jdbc;
    }

    @Override
    public String toString() {
        String teacherName = jdbc.fetchTeacherName(teacher_id);
        return subject + " taught by " + teacherName;
    }

    // Getters
    public int getClassId() {
        return classId;
    }

    public String getSubject() {
        return subject;
    }

    public int getTeacherID() {
        return teacher_id;
    }

    // Setters
    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTeacherID(int teacherID) {
        this.teacher_id = teacherID;
    }

}

