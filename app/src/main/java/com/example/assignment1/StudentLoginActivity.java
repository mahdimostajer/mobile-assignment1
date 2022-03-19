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

import com.example.assignment1.Models.Student;
import com.example.assignment1.databinding.ActivityStudentLoginBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StudentLoginActivity extends AppCompatActivity {
    public final static String USERNAME = "username";
    private SharedPreferences preferences;
    ActivityStudentLoginBinding binding;
    String username, password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView textView = (TextView) findViewById(R.id.registerTextView);
        String text = "Don't have an account? Register";
        SpannableString ss = new SpannableString(text);
        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(StudentLoginActivity.this, StudentRegisterActivity.class);
                startActivity(intent);
            }
        };

        ss.setSpan(cs,23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = binding.editTextUsername.getText().toString();
                password = binding.editTextPassword.getText().toString();
                boolean isValid = validateUser(username, password);
                if (isValid){
                    Intent intent = new Intent(StudentLoginActivity.this, PanelActivity.class);
                    intent.putExtra(StudentLoginActivity.USERNAME, username);
                    intent.putExtra(MainActivity.USERTYPE, PanelActivity.UserType.STUDENT);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateUser(String username, String password){
        String sharedPrefFile = "com.example.android.assignment1";
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Student>>() {}.getType();
        List<Student> students = gson.fromJson(preferences.getString(StudentRegisterActivity.STUDENTS, null), type);
        if(students != null){
            for (Student student : students){
                if(student.username.equals(username)){
                    if(student.firstname.equals(password))
                        return true;
                    Toast.makeText(StudentLoginActivity.this, "Wrong password is entered!", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        Toast.makeText(StudentLoginActivity.this, "Student doesn't exists with this username!", Toast.LENGTH_LONG).show();
        return false;
    }
}
