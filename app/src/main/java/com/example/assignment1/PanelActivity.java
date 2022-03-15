package com.example.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.assignment1.Models.Course;
import com.example.assignment1.Models.StudentCourse;
import com.example.assignment1.databinding.ActivityPanelBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PanelActivity extends AppCompatActivity {

    ActivityPanelBinding binding;
    private SharedPreferences mPreferences;
    private String sharedPrefFile =
            "com.example.android.assignment1";

    public final static String CLASS_ROOMS = "class rooms";
    public final static String STUDENT_CLASSES = "student classes";

    public static final String EXTRA_JOIN_CLASSES = "extra join classes";
    public static final String EXTRA_COURSE = "extra course page";
    public static final String EXTRA_USER_TYPE = "extra user type";
    public final Integer CREATE_COURSE_REQUEST = 100;
    public final Integer Join_COURSE_REQUEST = 200;

    public enum UserType {
        STUDENT("STUDENT"),
        PROFESSOR("PROFESSOR");

        public final String label;

        UserType(String label) {
            this.label = label;
        }
    }

    private ArrayList<Course> personCourses;
    private CourseAdapter courseAdapter;
    private String id = "akbar";
    private UserType type = UserType.STUDENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        if (type == UserType.PROFESSOR) {
            getCoursesProfessor();
        } else if (type == UserType.STUDENT) {
            getCourseStudent();
        }

        courseAdapter = new CourseAdapter(PanelActivity.this, personCourses, type);
        binding.courseRecyclerview.setAdapter(courseAdapter);
        binding.courseRecyclerview.setLayoutManager(new LinearLayoutManager(PanelActivity.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_COURSE_REQUEST) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra(CreateCourseActivity.COURSE_NAME);

                personCourses.add(new Course(name, id));
                binding.courseRecyclerview.getAdapter().notifyItemInserted(personCourses.size() - 1);
                binding.courseRecyclerview.smoothScrollToPosition(personCourses.size() - 1);

                Gson gson = new Gson();
                String json = gson.toJson(personCourses);
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putString(CLASS_ROOMS, json);
                preferencesEditor.apply();
            }
        }
        if (requestCode == Join_COURSE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Course course = data.getParcelableExtra(JoinCourseActivity.JOINED_COURSE);
                personCourses.add(course);
                binding.courseRecyclerview.getAdapter().notifyItemInserted(personCourses.size() - 1);

                Gson gson = new Gson();
                Type type = new TypeToken<List<StudentCourse>>() {
                }.getType();
                List<StudentCourse> studentCourses = gson.fromJson(mPreferences.getString(STUDENT_CLASSES, null), type);
                if (studentCourses == null) {
                    studentCourses = new ArrayList<>();
                }
                studentCourses.add(new StudentCourse(id, course));

                Gson gson2 = new Gson();
                String json2 = gson2.toJson(studentCourses);
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putString(STUDENT_CLASSES, json2);
                preferencesEditor.apply();
            }
        }
    }

    private void getCoursesProfessor() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Course>>() {
        }.getType();
        List<Course> courses = gson.fromJson(mPreferences.getString(CLASS_ROOMS, null), type);
        personCourses = new ArrayList<>();
        if (courses != null) {
            for (Course item : courses) {
                if (item.ProfessorUsername.equals(id)) {
                    personCourses.add(item);
                }
            }
        }

        binding.newCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelActivity.this, CreateCourseActivity.class);
                startActivityForResult(intent, CREATE_COURSE_REQUEST);
            }
        });

    }

    private void getCourseStudent() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<StudentCourse>>() {
        }.getType();
        List<StudentCourse> studentCourses = gson.fromJson(mPreferences.getString(STUDENT_CLASSES, null), type);
        personCourses = new ArrayList<>();
        if (studentCourses != null) {
            for (StudentCourse item : studentCourses) {
                if (item.studentId.equals(id)) {
                    personCourses.add(item.course);
                }
            }
        }

        binding.newCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelActivity.this, JoinCourseActivity.class);
                intent.putParcelableArrayListExtra(EXTRA_JOIN_CLASSES, personCourses);
                startActivityForResult(intent, Join_COURSE_REQUEST);
            }
        });
    }
}