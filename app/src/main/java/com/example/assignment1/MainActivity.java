package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.assignment1.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity{
    ActivityMainBinding binding;
    public final static String USERTYPE = "type";
    private SharedPreferences mPreferences;
    private String sharedPrefFile =
            "com.example.android.assignment1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        reset();
        binding.studentButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StudentLoginActivity.class);
            startActivity(intent);
        });
        binding.professorButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfessorLoginActivity.class);
            startActivity(intent);
        });
    }

    private void reset(){
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }
}