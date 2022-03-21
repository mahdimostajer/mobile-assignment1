package com.example.assignment1.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Course implements Parcelable {
    public String id;
    public String name;
    public String ProfessorUsername;

    public Course(String name, String professorUsername) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.ProfessorUsername = professorUsername;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.ProfessorUsername);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.ProfessorUsername = source.readString();
    }

    protected Course(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.ProfessorUsername = in.readString();
    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
}
