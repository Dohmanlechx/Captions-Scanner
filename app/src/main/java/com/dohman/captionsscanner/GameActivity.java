package com.dohman.captionsscanner;

import android.database.Cursor;
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

    private TextView result;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        result = findViewById(R.id.result);
        textView = findViewById(R.id.tv_title);
        final Cursor currentWord;

        if (SettingsActivity.getDefaults("databaseNotEmpty", this) == null) {
            Toast.makeText(this, getString(R.string.toastnowords), Toast.LENGTH_LONG).show();
        } else {
            currentWord = getRandomWord();
            textView.setText(currentWord.getString(1));

            final EditText editText = findViewById(R.id.editText);
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (editText.getText().toString().toLowerCase().contains(currentWord.getString(2))) {
                            Log.d(TAG, "onEditorAction: INPUT =" + editText.getText().toString());
                            Log.d(TAG, "onEditorAction: SVAR =" + currentWord.getString(2));
                            result.setVisibility(View.VISIBLE);
                            result.setText(getString(R.string.correct));
                            handled = true;
                        } else {
                            Log.d(TAG, "onEditorAction: INPUT =" + editText.getText().toString());
                            Log.d(TAG, "onEditorAction: SVAR =" + currentWord.getString(2));
                            result.setVisibility(View.VISIBLE);
                            result.setText(getString(R.string.incorrecct));
                            handled = true;
                        }
                    }

                    return handled;
                }
            });
        }
    }

    private Cursor getRandomWord() {
        return db.randomData();
    }
}
