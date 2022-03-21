package com.example.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment1.Models.Assignment;
import com.example.assignment1.Models.ResponseAssignment;
import com.example.assignment1.databinding.ActivityProfessorAssignmentBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfessorAssignmentActivity extends AppCompatActivity {

    ActivityProfessorAssignmentBinding binding;
    Assignment assignment;
    private ArrayList<ResponseAssignment> responseAssignments = new ArrayList<>();
    public final static String RESPONSE_ASSIGNMENTS = "responseAssignments";
    private SharedPreferences preferences;
    private String sharedPrefFile =
            "com.example.android.assignment1";
    Button assignmentAnswerButton;
    private AssignmentAnswerAdapter adapter;

    public final static String EXTRA_RESPONSE = "extra response";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfessorAssignmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Intent intent = getIntent();
        assignment = intent.getParcelableExtra(CourseActivity.EXTRA_ASSIGNMENT);
        binding.assignmentTitleEditText.setText(assignment.title);
        binding.questionTextview.setText(assignment.question);
        binding.assignmentTitleEditText.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Assignment>>() {
                }.getType();
                List<Assignment> allAssignments = gson.fromJson(preferences.getString(CourseActivity.ASSIGNMENTS, null), type);
                if (allAssignments == null) {
                    allAssignments = new ArrayList<>();
                }
                for (Assignment changedAssignment : allAssignments) {
                    if (changedAssignment.id.equals(assignment.id)) {
                        changedAssignment.title = binding.assignmentTitleEditText.getText().toString();
                    }
                }
                Gson gson2 = new Gson();
                String json2 = gson2.toJson(allAssignments);
                SharedPreferences.Editor preferencesEditor = preferences.edit();
                preferencesEditor.putString(CourseActivity.ASSIGNMENTS, json2);
                preferencesEditor.apply();

            }
        });

        Gson gson = new Gson();
        Type type = new TypeToken<List<ResponseAssignment>>() {
        }.getType();
        List<ResponseAssignment> allResponseAssignments = gson.fromJson(preferences.getString(StudentAssignmentActivity.RESPONSE_ASSIGNMENTS, null), type);
        if (allResponseAssignments != null) {
            for (ResponseAssignment responseAssignment : allResponseAssignments) {
                if (responseAssignment.assignmentId.equals(assignment.id)) {
                    responseAssignments.add(responseAssignment);
                }
            }
        }

        adapter = new AssignmentAnswerAdapter(this, responseAssignments);
        binding.answerRecyclerView.setAdapter(adapter);
        binding.answerRecyclerView.setLayoutManager(new LinearLayoutManager(this));




    }




}