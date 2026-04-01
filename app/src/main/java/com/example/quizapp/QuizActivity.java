package com.example.quizapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final List<Question> QUESTIONS = new ArrayList<>();

    static {
        QUESTIONS.add(new Question("Android question title:",
                "Which method is called when an Activity is first created?",
                new String[]{"onStart()", "onCreate()", "onResume()", "onAttach()"}, 1));
        QUESTIONS.add(new Question("Android question title:",
                "Which layout arranges children in a single row or column?",
                new String[]{"RelativeLayout", "ConstraintLayout", "LinearLayout", "FrameLayout"}, 2));
        QUESTIONS.add(new Question("Android question title:",
                "What file format does Android use to define UI layouts?",
                new String[]{"JSON", "YAML", "XML", "HTML"}, 2));
        QUESTIONS.add(new Question("Android question title:",
                "Which component runs long operations in the background?",
                new String[]{"Fragment", "Service", "BroadcastReceiver", "ContentProvider"}, 1));
        QUESTIONS.add(new Question("Android question title:",
                "What does ADB stand for in Android development?",
                new String[]{"App Debug Bridge", "Android Debug Bridge", "Android Device Builder", "App Device Builder"}, 1));
    }

    private TextView tvProgress, tvQuestionTitle, tvQuestionDetail;
    private ProgressBar progressBar;
    private List<RadioButton> radioButtons = new ArrayList<>();
    private Button btnSubmit;

    private String userName;
    private int currentIndex = 0;
    private int score        = 0;
    private boolean submitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        userName         = getIntent().getStringExtra("name");
        tvProgress       = findViewById(R.id.tv_progress);
        tvQuestionTitle  = findViewById(R.id.tv_question_title);
        tvQuestionDetail = findViewById(R.id.tv_question_detail);
        progressBar      = findViewById(R.id.progress_bar);
        btnSubmit        = findViewById(R.id.btn_submit);

        radioButtons.add(findViewById(R.id.rb_answer_0));
        radioButtons.add(findViewById(R.id.rb_answer_1));
        radioButtons.add(findViewById(R.id.rb_answer_2));
        radioButtons.add(findViewById(R.id.rb_answer_3));

        progressBar.setMax(100);

        for (RadioButton rb : radioButtons) {
            rb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (submitted) return;
                btnSubmit.setEnabled(anyChecked());
            });
        }

        btnSubmit.setOnClickListener(v -> onSubmitClicked());

        loadQuestion();
    }

    private boolean anyChecked() {
        for (RadioButton rb : radioButtons) {
            if (rb.isChecked()) return true;
        }
        return false;
    }

    private void updateProgress() {
        int total      = QUESTIONS.size();
        int percentage = (currentIndex * 100) / total;

        tvProgress.setText("Question " + (currentIndex + 1) + "/" + total
                + " (" + percentage + "% completed)");

        ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), percentage)
                .setDuration(300)
                .start();
    }

    private void loadQuestion() {
        submitted = false;

        Question q = QUESTIONS.get(currentIndex);
        tvQuestionTitle.setText(q.getTitle());
        tvQuestionDetail.setText(q.getDetail());

        String[] answers = q.getAnswers();
        for (int i = 0; i < radioButtons.size(); i++) {
            RadioButton rb = radioButtons.get(i);
            rb.setText(answers[i]);
            rb.setChecked(false);
            rb.setEnabled(true);
            rb.setTextColor(Color.BLACK);
        }

        btnSubmit.setText("Submit");
        btnSubmit.setEnabled(false);

        updateProgress();
    }

    private void onSubmitClicked() {
        if (!submitted) {
            if (!anyChecked()) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            submitted = true;
            Question q = QUESTIONS.get(currentIndex);

            for (RadioButton rb : radioButtons) rb.setEnabled(false);

            boolean correct = radioButtons.get(q.getCorrectIndex()).isChecked();

            for (int i = 0; i < radioButtons.size(); i++) {
                RadioButton rb = radioButtons.get(i);
                if (i == q.getCorrectIndex()) {
                    rb.setTextColor(Color.parseColor("#4CAF50"));
                } else if (rb.isChecked()) {
                    rb.setTextColor(Color.parseColor("#F44336"));
                }
            }

            if (correct) score++;

            btnSubmit.setText(currentIndex == QUESTIONS.size() - 1 ? "Finish" : "Next");

        } else {
            currentIndex++;
            if (currentIndex >= QUESTIONS.size()) {
                // Progress bar to 100% before leaving
                tvProgress.setText("Question " + QUESTIONS.size() + "/" + QUESTIONS.size() + " (100% completed)");
                progressBar.setProgress(100);

                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("name", userName);
                intent.putExtra("score", score);
                intent.putExtra("total", QUESTIONS.size());
                startActivity(intent);
                finish();
            } else {
                loadQuestion();
            }
        }
    }
}