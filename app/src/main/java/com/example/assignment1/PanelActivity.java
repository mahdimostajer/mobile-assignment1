package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.assignment1.databinding.ActivityMainBinding;
import com.example.assignment1.databinding.ActivityPanelBinding;

public class PanelActivity extends AppCompatActivity {

    ActivityPanelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}