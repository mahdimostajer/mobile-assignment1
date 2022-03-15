package com.example.assignment1.Models;

import java.util.UUID;

public class Assignment {
    public String courseName;
    public String title;
    public String id;
    public String question;

    public Assignment(String courseName, String title, String question) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.courseName = courseName;
        this.question = question;
    }
}
