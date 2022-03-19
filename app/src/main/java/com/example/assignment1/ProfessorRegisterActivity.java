package com.example.assignment1;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment1.databinding.ActivityProfessorLoginBinding;
import com.example.assignment1.databinding.ActivityProfessorRegisterBinding;

public class ProfessorRegisterActivity extends AppCompatActivity {
    ActivityProfessorRegisterBinding binding;
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

    private boolean checkUserExistence(String username, String password){
        return true;
    }
}
