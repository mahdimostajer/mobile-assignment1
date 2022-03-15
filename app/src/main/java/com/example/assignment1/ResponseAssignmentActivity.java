package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.assignment1.databinding.ActivityResponseAssignmentBinding;

public class ResponseAssignmentActivity extends AppCompatActivity {

    ActivityResponseAssignmentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResponseAssignmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}