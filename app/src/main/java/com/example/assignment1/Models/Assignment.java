package com.example.assignment1.Models;

import java.util.UUID;

public class Assignment {
    String ClassName;
    String id;

    public Assignment(String className) {
        this.id = UUID.randomUUID().toString();
        this.ClassName = className;
    }
}
