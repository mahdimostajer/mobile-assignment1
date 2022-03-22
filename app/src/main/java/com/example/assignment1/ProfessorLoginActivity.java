package com.example.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment1.Models.Professor;
import com.example.assignment1.databinding.ActivityProfessorLoginBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ProfessorLoginActivity extends AppCompatActivity {
    public final static String USERNAME = "username";
    private SharedPreferences preferences;
    ActivityProfessorLoginBinding binding;
    String username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfessorLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView textView = findViewById(R.id.registerTextView);
        String text = "Don't have an account? Register";
        SpannableString ss = new SpannableString(text);
        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ProfessorLoginActivity.this, ProfessorRegisterActivity.class);
                startActivity(intent);
            }
        };

        ss.setSpan(cs,23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        binding.loginButton.setOnClickListener(view -> {
            username = binding.editTextUsername.getText().toString();
            password = binding.editTextPassword.getText().toString();
            boolean isValid = validateUser(username, password);
            if (isValid){
                Intent intent = new Intent(ProfessorLoginActivity.this, PanelActivity.class);
                intent.putExtra(StudentLoginActivity.USERNAME, username);
                intent.putExtra(MainActivity.USERTYPE, PanelActivity.UserType.PROFESSOR);
                startActivity(intent);
            }
        });
    }

    private boolean validateUser(String username, String password){
        String sharedPrefFile = "com.example.android.assignment1";
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Professor>>() {}.getType();
        List<Professor> professors = gson.fromJson(preferences.getString(ProfessorRegisterActivity.PROFESSORS, null), type);
        if(professors != null){
            for (Professor professor : professors){
                if(professor.username.equals(username)){
                    if(professor.password.equals(password))
                        return true;
                    Toast.makeText(ProfessorLoginActivity.this, "Wrong password is entered!", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        Toast.makeText(ProfessorLoginActivity.this, "Professor doesn't exists with this username!", Toast.LENGTH_LONG).show();
        return false;
    }
}
