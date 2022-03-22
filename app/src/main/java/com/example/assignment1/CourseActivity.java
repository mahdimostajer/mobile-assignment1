package com.example.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private String userId;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateAssignments();
            binding.assignmentRecyclerView.getAdapter().notifyDataSetChanged();
        }
    };
    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("refreshAssignments"));
        super.onCreate(savedInstanceState);
        binding = ActivityCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        Intent intent = getIntent();
        course = intent.getParcelableExtra(PanelActivity.EXTRA_COURSE);
        userType = (PanelActivity.UserType) intent.getSerializableExtra(PanelActivity.EXTRA_USER_TYPE);
        if (userType.label.equals("STUDENT")) {
            userId = intent.getStringExtra(StudentLoginActivity.USERID);
        }
        binding.courseNameTextview.setText(course.name);
        binding.professorNameTextview.setText(course.ProfessorUsername);

        if (userType == PanelActivity.UserType.PROFESSOR) {
            binding.newAssignmentButton.setVisibility(View.VISIBLE);
        }
        updateAssignments();

        adapter = new AssignmentAdapter(CourseActivity.this, assignments, userType,userId);
        binding.assignmentRecyclerView.setAdapter(adapter);
        binding.assignmentRecyclerView.setLayoutManager(new LinearLayoutManager(CourseActivity.this));
        binding.swiperefreshlayout.setOnRefreshListener(() -> {
            updateAssignments();
            binding.assignmentRecyclerView.getAdapter().notifyDataSetChanged();
            binding.swiperefreshlayout.setRefreshing(false);

        });
        binding.newAssignmentButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(CourseActivity.this, NewAssignmentActivity.class);
            startActivityForResult(intent1, CREATE_ASSIGNMENT_REQUEST);
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
    private void updateAssignments() {
        assignments.clear();
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
    }
}
