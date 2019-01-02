package com.dohman.captionsscanner;

import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dohman.captionsscanner.controller.DatabaseHelper;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    private final DatabaseHelper db = new DatabaseHelper(this);
    private static final Handler handler = new Handler();
    private Cursor currentWord;

    private TextView answerTv;
    private TextView titleTv;

    private EditText editText;

    int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        answerTv = findViewById(R.id.answer);
        titleTv = findViewById(R.id.tv_title);
        editText = findViewById(R.id.editText);

        defaultColor = titleTv.getCurrentTextColor();

        if (SettingsActivity.getDefaults("databaseNotEmpty", this) == null) {
            Toast.makeText(this, getString(R.string.toastnowords), Toast.LENGTH_LONG).show();
        } else {
            reset();

            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (editText.getText().toString().toLowerCase().contains(currentWord.getString(2))) {
                            // CORRECT ANSWER
                            editText.setTextColor(getResources().getColor(R.color.correct_color));
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    reset();
                                }
                            }, 1000);
                        } else {
                            // INCORRECT ANSWER
                            editText.setTextColor(getResources().getColor(R.color.incorrect_color));
                            answerTv.setText(currentWord.getString(2));
                            answerTv.setVisibility(View.VISIBLE);
                        }
                    }

                    return true;
                }
            });
        }

    }

    private Cursor getRandomWord() {
        return db.randomData();
    }

    private void reset() {
        currentWord = getRandomWord();
        titleTv.setText(currentWord.getString(1));
        editText.setTextColor(defaultColor);
        answerTv.setVisibility(View.GONE);
        editText.setText("");
    }
}
