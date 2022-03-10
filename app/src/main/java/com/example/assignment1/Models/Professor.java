package com.example.assignment1.Models;

public class Professor extends User{
    String university;


    public Professor(String username, String password, String firstname, String lastname, String university) {
        super(username, password, firstname, lastname);
        this.university = university;
    }
}
