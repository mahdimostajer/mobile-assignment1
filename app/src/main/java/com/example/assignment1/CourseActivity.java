package com.example.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.assignment1.Models.Assignment;
import com.example.assignment1.Models.Course;
import com.example.assignment1.databinding.ActivityCourseBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    private Course course;
    ActivityCourseBinding binding;
    private SharedPreferences mPreferences;
    private String sharedPrefFile =
            "com.example.android.assignment1";
    public final static String ASSIGNMENTS = "assignments";
    public final Integer CREATE_ASSIGNMENT_REQUEST = 100;
    public final static String EXTRA_ASSIGNMENT_TITLE = "extra assignment title";
    public final static String EXTRA_ASSIGNMENT_QUESTION = "extra assignment question";
    public final static String EXTRA_ASSIGNMENT = "extra assignment";
    public static final String EXTRA_USER_TYPE = "extra user type";



    private ArrayList<Assignment> assignments = new ArrayList<>();
    private AssignmentAdapter adapter;
    private PanelActivity.UserType userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        Intent intent = getIntent();
        course = intent.getParcelableExtra(PanelActivity.EXTRA_COURSE);
        userType = (PanelActivity.UserType) intent.getSerializableExtra(PanelActivity.EXTRA_USER_TYPE);
        binding.courseNameTextview.setText(course.name);
        binding.professorNameTextview.setText(course.ProfessorUsername);

        if (userType == PanelActivity.UserType.PROFESSOR) {
            binding.newAssignmentButton.setVisibility(View.VISIBLE);
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Assignment>>() {
        }.getType();
        List<Assignment> allAssignments = gson.fromJson(mPreferences.getString(ASSIGNMENTS, null), type);

        if (allAssignments != null) {
            for (Assignment assignment : allAssignments) {
                if (assignment.courseId.equals(course.id)) {
                    assignments.add(assignment);
                }
            }
        }

        adapter = new AssignmentAdapter(CourseActivity.this, assignments, userType);
        binding.assignmentRecyclerView.setAdapter(adapter);
        binding.assignmentRecyclerView.setLayoutManager(new LinearLayoutManager(CourseActivity.this));

        binding.newAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, NewAssignmentActivity.class);
                startActivityForResult(intent, CREATE_ASSIGNMENT_REQUEST);
//                assignments.add(new Assignment(course.name, "hw1", "how are you?"));
//                binding.assignmentRecyclerView.getAdapter().notifyItemInserted(assignments.size() - 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_ASSIGNMENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra(EXTRA_ASSIGNMENT_TITLE);
                String question = data.getStringExtra(EXTRA_ASSIGNMENT_QUESTION);

                Assignment newAssignment = new Assignment(course.id, title, question);
                assignments.add(newAssignment);
                binding.assignmentRecyclerView.getAdapter().notifyItemInserted(assignments.size() - 1);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Assignment>>() {
                }.getType();
                List<Assignment> allAssignments = gson.fromJson(mPreferences.getString(ASSIGNMENTS, null), type);
                if (allAssignments == null) {
                    allAssignments = new ArrayList<>();
                }
                allAssignments.add(newAssignment);

                Gson gson2 = new Gson();
                String json2 = gson2.toJson(allAssignments);
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putString(ASSIGNMENTS, json2);
                preferencesEditor.apply();

            }
        }
    }
}