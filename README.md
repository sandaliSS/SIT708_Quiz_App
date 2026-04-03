# SIT305 Quiz App

An Android quiz application built in Java for **SIT305 Mobile Application Development**. The app tests Android development knowledge through a multi-screen quiz interface with dark and light mode support.

---

## Features

- Name entry with session persistence — pre-filled on return
- Dark / light mode toggle persisted across the session via `SharedPreferences`
- Animated horizontal progress bar across quiz questions
- Multiple-choice answers using `CheckBox` widgets
- Score summary on completion with replay and exit options

---

## Screens

### Start Screen
Displays the app title, a text input for the user's name, a **START** button, and a dark/light mode switch in the top bar.

### Quiz Screen
Shows a progress label, an animated horizontal progress bar, the question title and detail, four checkbox answers, and a **Submit** button that becomes **Next** or **Finish** after submission.

### Result Screen
Shows a congratulations message with the user's name, the final score, a **Take New Quiz** button that returns to the start screen with the name pre-filled, and a **Finish** button that closes the app.

---

## Project Structure

```
app/src/main/
├── java/com/example/quizapp/
│   ├── MainActivity.java        # Start screen
│   ├── QuizActivity.java        # Quiz logic and UI
│   ├── ResultActivity.java      # Score screen
│   ├── Question.java            # Data model
│   └── ThemeHelper.java         # Dark and light mode helper
│
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   ├── activity_quiz.xml
    │   └── activity_result.xml
    ├── values/
    │   ├── colors.xml
    │   ├── strings.xml
    │   └── themes.xml
    └── values-night/
        └── themes.xml           # Dark mode theme overrides
```

---

## Requirements

| Property | Value |
|---|---|
| Minimum SDK | API 21 — Android 5.0 |
| Target SDK | API 34 — Android 14 |
| Language | Java |
| Dependencies | AndroidX AppCompat only |

---

## Getting Started

1. Clone or download the repository
2. Open the project in **Android Studio**
3. Let Gradle sync and resolve dependencies
4. Run on an emulator or physical device (API 21+)

---
