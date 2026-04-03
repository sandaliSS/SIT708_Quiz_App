package com.example.quizapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final List<Question> QUESTIONS = new ArrayList<>();

    static {
        QUESTIONS.add(new Question("Question 1:",
                "Which method is called when an Activity is first created?",
                new String[]{"onStart()", "onCreate()", "onResume()", "onAttach()"}, 1));
        QUESTIONS.add(new Question("Question 2:",
                "Which layout arranges children in a single row or column?",
                new String[]{"RelativeLayout", "ConstraintLayout", "LinearLayout", "FrameLayout"}, 2));
        QUESTIONS.add(new Question("Question 3:",
                "What file format does Android use to define UI layouts?",
                new String[]{"JSON", "YAML", "XML", "HTML"}, 2));
        QUESTIONS.add(new Question("Question 4:",
                "Which component runs long operations in the background?",
                new String[]{"Fragment", "Service", "BroadcastReceiver", "ContentProvider"}, 1));
        QUESTIONS.add(new Question("Question 5:",
                "What does ADB stand for in Android development?",
                new String[]{"App Debug Bridge", "Android Debug Bridge", "Android Device Builder", "App Device Builder"}, 1));
    }

    private static final String KEY_INDEX     = "current_index";
    private static final String KEY_SCORE     = "score";
    private static final String KEY_SUBMITTED = "submitted";

    private TextView tvProgress, tvQuestionTitle, tvQuestionDetail;
    private ProgressBar progressBar;
    private List<CheckBox> checkBoxes = new ArrayList<>();
    private Button btnSubmit;

    private String userName;
    private int currentIndex = 0;
    private int score        = 0;
    private boolean submitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            score        = savedInstanceState.getInt(KEY_SCORE, 0);
            submitted    = savedInstanceState.getBoolean(KEY_SUBMITTED, false);
        }

        userName         = getIntent().getStringExtra("name");
        tvProgress       = findViewById(R.id.tv_progress);
        tvQuestionTitle  = findViewById(R.id.tv_question_title);
        tvQuestionDetail = findViewById(R.id.tv_question_detail);
        progressBar      = findViewById(R.id.progress_bar);
        btnSubmit        = findViewById(R.id.btn_submit);

        checkBoxes.add(findViewById(R.id.cb_answer_0));
        checkBoxes.add(findViewById(R.id.cb_answer_1));
        checkBoxes.add(findViewById(R.id.cb_answer_2));
        checkBoxes.add(findViewById(R.id.cb_answer_3));

        TextView tvThemeLabel = findViewById(R.id.tv_theme_label);
        Switch switchTheme    = findViewById(R.id.switch_theme);

        tvThemeLabel.setText(ThemeHelper.isDarkMode(this) ? "Light Mode" : "Dark Mode");

        switchTheme.setChecked(ThemeHelper.isDarkMode(this));
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) return;
            ThemeHelper.setDarkMode(this, isChecked);
            tvThemeLabel.setText(isChecked ? "Light Mode" : "Dark Mode");
        });
        progressBar.setMax(100);
        btnSubmit.setOnClickListener(v -> onSubmitClicked());

        loadQuestion();
    }

    // Save state before activity is destroyed by theme change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, currentIndex);
        outState.putInt(KEY_SCORE, score);
        outState.putBoolean(KEY_SUBMITTED, submitted);
    }

    private boolean anyChecked() {
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) return true;
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
        for (int i = 0; i < checkBoxes.size(); i++) {
            CheckBox cb = checkBoxes.get(i);
            cb.setText(answers[i]);
            cb.setChecked(false);
            cb.setEnabled(true);
            cb.setButtonTintList(null);
            cb.setTextColor(ThemeHelper.isDarkMode(this) ? Color.WHITE : Color.BLACK);
        }

        btnSubmit.setText("Submit");
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

            for (CheckBox cb : checkBoxes) cb.setEnabled(false);

            boolean correct = checkBoxes.get(q.getCorrectAnswer()).isChecked();

            for (int i = 0; i < checkBoxes.size(); i++) {
                CheckBox cb = checkBoxes.get(i);
                if (i == q.getCorrectAnswer()) {
                    cb.setTextColor(Color.parseColor("#4CAF50"));
                } else if (cb.isChecked()) {
                    cb.setTextColor(Color.parseColor("#F44336"));
                }
            }

            if (correct) score++;

            btnSubmit.setText(currentIndex == QUESTIONS.size() - 1 ? "Finish" : "Next");

        } else {
            currentIndex++;
            if (currentIndex >= QUESTIONS.size()) {
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("name",userName);
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