package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.assignment1.Models.Course;
import com.example.assignment1.databinding.ActivityJoinCourseBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JoinCourseActivity extends AppCompatActivity {

    ActivityJoinCourseBinding binding;
    private SharedPreferences mPreferences;
    private String sharedPrefFile =
            "com.example.android.assignment1";

    private JoinCourseAdapter adapter;
    public static final String JOINED_COURSE = "joined course";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_course);
        binding = ActivityJoinCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        ArrayList<Course> joinedCourses = intent.getParcelableArrayListExtra(PanelActivity.EXTRA_JOIN_CLASSES);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Course>>() {
        }.getType();
        List<Course> courses = gson.fromJson(mPreferences.getString(PanelActivity.CLASS_ROOMS, null), type);
        courses.removeIf(classroom -> {
            for (Course joinedCourse : joinedCourses) {
                if (joinedCourse.name.equals(classroom.name) && joinedCourse.ProfessorUsername.equals(classroom.ProfessorUsername)) {
                    return true;
                }
            }
            return false;
        });

        adapter = new JoinCourseAdapter(JoinCourseActivity.this, courses);
        binding.joinCourseRecyclerview.setAdapter(adapter);
        binding.joinCourseRecyclerview.setLayoutManager(new LinearLayoutManager(JoinCourseActivity.this));


    }
}