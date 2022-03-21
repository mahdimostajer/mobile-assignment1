package com.example.assignment1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment1.Models.Assignment;
import com.example.assignment1.Models.ResponseAssignment;
import com.example.assignment1.databinding.ActivityAssignmentBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AssignmentActivity extends AppCompatActivity {

    ActivityAssignmentBinding binding;
    Assignment assignment;
    PanelActivity.UserType userType;
    TextView answer;
    public final static String RESPONSE_ASSIGNMENTS = "responseAssignments";
    private SharedPreferences preferences;
    private String sharedPrefFile =
            "com.example.android.assignment1";
    Button assignmentAnswerButton;


    public final static String EXTRA_RESPONSE = "extra response";
    private String userId;
    private String grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Intent intent = getIntent();
        assignment = intent.getParcelableExtra(CourseActivity.EXTRA_ASSIGNMENT);
        userType = (PanelActivity.UserType) intent.getSerializableExtra(CourseActivity.EXTRA_USER_TYPE);

        if (userType == PanelActivity.UserType.PROFESSOR) {
            professorContent();
        } else {
            userId = intent.getStringExtra(StudentLoginActivity.USERID);
            studentContent();
        }

        binding.assignmentTitleTextview.setText(assignment.title);
        binding.assignmentQuestionTextview.setText(assignment.question);
        ResponseAssignment responseAssignment = getAssignment();
        if (responseAssignment != null) {
            binding.uploadButton.setText("change answer");
            binding.assignmentAnswerTextview.setText(responseAssignment.answer);
            grade = responseAssignment.grade;
        } else {
            binding.assignmentAnswerTextview.setText("no answer yet");
            binding.assignmentAnswerTextview.setTextColor(Color.RED);
        }



    }

    private void professorContent() {

    }

    private void studentContent() {
        binding.uploadButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_assignment_answer_dialog, null);
            TextView question = dialogView.findViewById(R.id.answer_dialog_question_textview);
            question.setText(assignment.question);
            answer = dialogView.findViewById(R.id.answer_dialog_plain_text);
            ResponseAssignment responseAssignment = getAssignment();
            if (responseAssignment != null) {
                answer.setText(responseAssignment.answer);
            }
            builder.setTitle(assignment.title);
            builder.setView(dialogView);
            builder.setPositiveButton("submit", ((dialogInterface, i) -> handleAnswerSubmission()));
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.show();
        });
    }

    private ResponseAssignment getAssignment() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ResponseAssignment>>() {}.getType();
        List<ResponseAssignment> responseAssignments = gson.fromJson(preferences.getString(AssignmentActivity.RESPONSE_ASSIGNMENTS, null), type);
        if(responseAssignments == null)
            responseAssignments = new ArrayList<>();
        for (ResponseAssignment responseAssignment : responseAssignments) {
            if (responseAssignment.assignmentId.equals(assignment.id) && userId.equals(responseAssignment.studentId)) {
                return responseAssignment;
            }
        }
        return null;
    }

    private void handleAnswerSubmission() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ResponseAssignment>>() {}.getType();
        List<ResponseAssignment> responseAssignments = gson.fromJson(preferences.getString(AssignmentActivity.RESPONSE_ASSIGNMENTS, null), type);
        if(responseAssignments == null)
            responseAssignments = new ArrayList<>();
        ArrayList<ResponseAssignment> toBeRemoved = new ArrayList<>();
        for (ResponseAssignment responseAssignment : responseAssignments) {
            if (responseAssignment.assignmentId.equals(assignment.id) && userId.equals(responseAssignment.studentId)) {
                toBeRemoved.add(responseAssignment);
            }
        }
        responseAssignments.removeAll(toBeRemoved);
        ResponseAssignment newResponseAssignment = new ResponseAssignment(assignment.id, userId, answer.getText().toString());
        responseAssignments.add(newResponseAssignment);
        String responsesJson = gson.toJson(responseAssignments);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AssignmentActivity.RESPONSE_ASSIGNMENTS, responsesJson);
        editor.apply();
        binding.assignmentAnswerTextview.setText(answer.getText().toString());
        binding.assignmentAnswerTextview.setTextColor(Color.GREEN);
        binding.uploadButton.setText("change answer");
        Toast.makeText(AssignmentActivity.this, "response submitted successfully", Toast.LENGTH_LONG).show();
        Toast.makeText(this, answer.getText().toString(), Toast.LENGTH_LONG).show();
    }

}