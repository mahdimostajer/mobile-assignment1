package com.example.assignment1.Models;

public class ResponseAssignment {
    public String assignmentId;
    public String studentId;
    public String answer;
    public String grade;


    public ResponseAssignment(String assignmentId, String studentId, String answer) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.answer = answer;
    }
}
