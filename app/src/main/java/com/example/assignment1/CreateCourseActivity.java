package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.assignment1.databinding.ActivityCreateCourseBinding;

public class CreateCourseActivity extends AppCompatActivity {

    public static final String COURSE_NAME = "course name";
    ActivityCreateCourseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.submitCourseButton.setOnClickListener(view -> {
            String name = binding.courseNameEdittext.getEditText().getText().toString();
            Intent intent = new Intent();
            intent.putExtra(COURSE_NAME, name);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}