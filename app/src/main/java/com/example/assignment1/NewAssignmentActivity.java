package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.assignment1.databinding.ActivityCourseBinding;
import com.example.assignment1.databinding.ActivityNewAssignmentBinding;

public class NewAssignmentActivity extends AppCompatActivity {

    ActivityNewAssignmentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewAssignmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.submitAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.assignmentTitleEdittext.getText().toString();
                String question = binding.questionEdittext.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(CourseActivity.EXTRA_ASSIGNMENT_TITLE, title);
                intent.putExtra(CourseActivity.EXTRA_ASSIGNMENT_QUESTION, question);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}