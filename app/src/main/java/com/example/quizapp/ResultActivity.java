package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String name  = getIntent().getStringExtra("name");
        int    score = getIntent().getIntExtra("score", 0);
        int    total = getIntent().getIntExtra("total", 0);

        TextView tvCongrats = findViewById(R.id.tv_congrats);
        TextView tvScore    = findViewById(R.id.tv_score);
        Button   btnRetake  = findViewById(R.id.btn_retake);
        Button   btnFinish  = findViewById(R.id.btn_finish);

        tvCongrats.setText("Congratulations " + name + "!");
        tvScore.setText(score + "/" + total);

        btnRetake.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnFinish.setOnClickListener(v -> finishAffinity());
    }
}