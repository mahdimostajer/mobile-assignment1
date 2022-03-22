package com.example.assignment1;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assignment1.Models.Student;
import com.example.assignment1.databinding.ActivityStudentRegisterBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StudentRegisterActivity extends AppCompatActivity {
    ActivityStudentRegisterBinding binding;
    public final static String STUDENTS = "students";
    private SharedPreferences preferences;
    String firstName, lastName, studentId, username, password, passwordConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerButton.setOnClickListener(view -> {
            firstName = binding.firstNameEditText.getText().toString();
            lastName = binding.lastNameEditText.getText().toString();
            studentId = binding.studentIdEditText.getText().toString();
            username = binding.usernameEditText.getText().toString();
            password = binding.passwordEditText.getText().toString();
            passwordConfirm = binding.passwordConfirmEditText.getText().toString();
            boolean hasEmptyField = checkEmptyFields(firstName, lastName, studentId, username, password, passwordConfirm);
            boolean hasEqualPassword = checkPasswordConfirmEquality(password, passwordConfirm);
            boolean professorExistsBefore = checkUserExistence(username,studentId);
            if (!hasEmptyField && hasEqualPassword && !professorExistsBefore){
                createStudent(firstName, lastName, studentId, username, password);
                Intent intent = new Intent(StudentRegisterActivity.this, PanelActivity.class);
                intent.putExtra(StudentLoginActivity.USERNAME, username);
                intent.putExtra(MainActivity.USERTYPE, PanelActivity.UserType.STUDENT);
                intent.putExtra(StudentLoginActivity.USERID, studentId);
                startActivity(intent);
            }

        });

    }

    private boolean checkEmptyFields(String firstName, String lastName, String studentId, String username, String password, String passwordConfirm){
        String emptyField = "";
        if (firstName.isEmpty())
            emptyField = "firstNameEditText";
        else if (lastName.isEmpty())
            emptyField = "lastNameEditText";
        else if (studentId.isEmpty())
            emptyField = "studentIdEditText";
        else if (username.isEmpty())
            emptyField = "usernameEditText";
        else if (password.isEmpty())
            emptyField = "passwordEditText";
        else if (passwordConfirm.isEmpty())
            emptyField = "passwordConfirmEditText";

        if (!emptyField.isEmpty()){
            int resourceId = getResources().getIdentifier(emptyField, "id", "com.example.assignment1");
            EditText view = findViewById(resourceId);
            Toast.makeText(StudentRegisterActivity.this, String.format("Field %s is empty!", view.getHint().toString()), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean checkPasswordConfirmEquality(String password, String passwordConfirm){
        if (!password.equals(passwordConfirm)){
            Toast.makeText(StudentRegisterActivity.this, "Password and its confirm doesn't matches!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkUserExistence(String username,String id){
        String sharedPrefFile = "com.example.android.assignment1";
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Student>>() {}.getType();
        List<Student> students = gson.fromJson(preferences.getString(StudentRegisterActivity.STUDENTS, null), type);
        if(students != null){
            for (Student student : students){
                if(student.username.equals(username)){
                    Toast.makeText(StudentRegisterActivity.this, "Student Exists with this username!", Toast.LENGTH_LONG).show();
                    return true;
                }
                if (student.studentId.equals(id)) {
                    Toast.makeText(StudentRegisterActivity.this, "Student Exists with this id!", Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        }
        return false;
    }

    private void createStudent(String firstName, String lastName, String studentId, String username, String password){
        String sharedPrefFile = "com.example.android.assignment1";
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Student>>() {}.getType();
        List<Student> students = gson.fromJson(preferences.getString(StudentRegisterActivity.STUDENTS, null), type);
        if(students == null)
            students = new ArrayList<>();
        Student newStudent = new Student(username, password, firstName, lastName, studentId);
        students.add(newStudent);
        String studentsJson = gson.toJson(students);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(StudentRegisterActivity.STUDENTS, studentsJson);
        editor.apply();
        Toast.makeText(StudentRegisterActivity.this, String.format("Student %s registration completed successfully!", username), Toast.LENGTH_LONG).show();
    }
}
