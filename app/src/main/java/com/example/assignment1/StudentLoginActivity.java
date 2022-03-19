package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment1.databinding.ActivityStudentLoginBinding;

public class StudentLoginActivity extends AppCompatActivity {
    ActivityStudentLoginBinding binding;


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
                Intent intent = new Intent(StudentLoginActivity.this, PanelActivity.class);
                startActivity(intent);
            }
        });
    }
}
