package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etName   = findViewById(R.id.et_name);
        Button   btnStart = findViewById(R.id.btn_start);

        TextView tvThemeLabel = findViewById(R.id.tv_theme_label);
        Switch switchTheme    = findViewById(R.id.switch_theme);

        tvThemeLabel.setText(ThemeHelper.isDarkMode(this) ? "Light Mode" : "Dark Mode");

        switchTheme.setChecked(ThemeHelper.isDarkMode(this));
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) return;
            ThemeHelper.setDarkMode(this, isChecked);
            tvThemeLabel.setText(isChecked ? "Light Mode" : "Dark Mode");
        });

        String returnedName = getIntent().getStringExtra("name");
        if (returnedName != null) {
            etName.setText(returnedName);
        }

        btnStart.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        });
    }
}