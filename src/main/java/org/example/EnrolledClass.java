package org.example;

import java.util.ArrayList;
import java.util.List;

public class EnrolledClass {
    private int classId;
    private String subject;
    private List<Student> assignedStudents;

    public EnrolledClass(int classId, String subject) {
        this.classId = classId;
        this.subject = subject;
        this.assignedStudents = new ArrayList<>();
    }

    public int getClassId() {
        return classId;
    }

    public String getSubject() {
        return subject;
    }

    public List<Student> getAssignedStudents() {
        return assignedStudents;
    }

    public void addStudent(Student student) {
        assignedStudents.add(student);
    }
}
