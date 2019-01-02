package com.dohman.captionsscanner;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.dohman.captionsscanner.controller.DatabaseHelper;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    private final DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Cursor cursorRandom = db.randomData(); // Index 0: id, Index 1: word, Index 2: translated, Index 3: json
        String test = cursorRandom.getString(2);
        Log.d(TAG, "onCreate: " + test);

        final EditText editText = findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Do something
                    handled = true;
                }
                return handled;
            }
        });
    }
}
