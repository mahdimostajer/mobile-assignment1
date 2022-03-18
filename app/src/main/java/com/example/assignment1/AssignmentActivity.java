package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.assignment1.Models.Assignment;
import com.example.assignment1.databinding.ActivityAssignmentBinding;

public class AssignmentActivity extends AppCompatActivity {

    ActivityAssignmentBinding binding;
    Assignment assignment;
    public final static String EXTRA_RESPONSE = "extra response";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        assignment = intent.getParcelableExtra(CourseActivity.EXTRA_ASSIGNMENT);
        binding.assignmentTitleTextview.setText(assignment.title);
        binding.assignmentQuestionTextview.setText(assignment.question);


    }
}