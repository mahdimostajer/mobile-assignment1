package com.example.assignment1.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Assignment implements Parcelable {
    public String courseId;
    public String title;
    public String id;
    public String question;

    public Assignment(String courseId, String title, String question) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.courseId = courseId;
        this.question = question;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.courseId);
        dest.writeString(this.title);
        dest.writeString(this.id);
        dest.writeString(this.question);
    }

    public void readFromParcel(Parcel source) {
        this.courseId = source.readString();
        this.title = source.readString();
        this.id = source.readString();
        this.question = source.readString();
    }

    protected Assignment(Parcel in) {
        this.courseId = in.readString();
        this.title = in.readString();
        this.id = in.readString();
        this.question = in.readString();
    }

    public static final Parcelable.Creator<Assignment> CREATOR = new Parcelable.Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel source) {
            return new Assignment(source);
        }

        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };
}
