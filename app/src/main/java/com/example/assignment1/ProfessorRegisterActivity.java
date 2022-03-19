package com.example.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment1.Models.Course;
import com.example.assignment1.Models.Professor;
import com.example.assignment1.databinding.ActivityProfessorLoginBinding;
import com.example.assignment1.databinding.ActivityProfessorRegisterBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ProfessorRegisterActivity extends AppCompatActivity {
    ActivityProfessorRegisterBinding binding;
    public final static String PROFESSORS = "professors";
    private SharedPreferences preferences;
    String firstName, lastName, university, username, password, passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfessorRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = binding.firstNameEditText.getText().toString();
                lastName = binding.lastNameEditText.getText().toString();
                university = binding.universityEditText.getText().toString();
                username = binding.usernameEditText.getText().toString();
                password = binding.passwordEditText.getText().toString();
                passwordConfirm = binding.passwordConfirmEditText.getText().toString();
                boolean hasEmptyField = checkEmptyFields(firstName, lastName, university, username, password, passwordConfirm);
                boolean hasEqualPassword = checkPasswordConfirmEquality(password, passwordConfirm);
                boolean professorExistsBefore = checkUserExistence(username);
                if (!hasEmptyField && hasEqualPassword && !professorExistsBefore){
                    createProfessor(firstName, lastName, university, username, password);
                    Intent intent = new Intent(ProfessorRegisterActivity.this, PanelActivity.class);
                    intent.putExtra(ProfessorLoginActivity.USERNAME, username);
                    intent.putExtra(MainActivity.USERTYPE, PanelActivity.UserType.PROFESSOR);
                    startActivity(intent);
                }

            }
        });

    }

    private boolean checkEmptyFields(String firstName, String lastName, String university, String username, String password, String passwordConfirm){
        String emptyField = "";
        if (firstName.isEmpty())
            emptyField = "firstNameEditText";
        else if (lastName.isEmpty())
            emptyField = "lastNameEditText";
        else if (university.isEmpty())
            emptyField = "universityEditText";
        else if (username.isEmpty())
            emptyField = "usernameEditText";
        else if (password.isEmpty())
            emptyField = "passwordEditText";
        else if (passwordConfirm.isEmpty())
            emptyField = "passwordConfirmEditText";

        if (!emptyField.isEmpty()){
            int resourceId = getResources().getIdentifier(emptyField, "id", "com.example.assignment1");
            EditText view = (EditText) findViewById(resourceId);
            Toast.makeText(ProfessorRegisterActivity.this, String.format("Field %s is empty!", view.getHint().toString()), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean checkPasswordConfirmEquality(String password, String passwordConfirm){
        if (!password.equals(passwordConfirm)){
            Toast.makeText(ProfessorRegisterActivity.this, "Password and its confirm doesn't matches!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkUserExistence(String username){
        String sharedPrefFile = "com.example.android.assignment1";
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Professor>>() {}.getType();
        List<Professor> professors = gson.fromJson(preferences.getString(ProfessorRegisterActivity.PROFESSORS, null), type);
        for (Professor professor : professors){
            if(professor.username.equals(username)){
                Toast.makeText(ProfessorRegisterActivity.this, "Professor Exists with this username!", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }

    private void createProfessor(String firstName, String lastName, String university, String username, String password){
        String sharedPrefFile = "com.example.android.assignment1";
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Professor>>() {}.getType();
        List<Professor> professors = gson.fromJson(preferences.getString(ProfessorRegisterActivity.PROFESSORS, null), type);
        Professor newProf = new Professor(firstName, lastName, university, username, password);
        professors.add(newProf);
        String profsJson = gson.toJson(professors);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ProfessorRegisterActivity.PROFESSORS, profsJson);
        editor.apply();
    }
}
