package com.example.assignment1.Models;

public class Student extends User {
    public String studentId;

    public Student(String username, String password, String firstname, String lastname, String studentId) {
        super(username, password, firstname, lastname);
        this.studentId = studentId;
    }
}
