package com.dohman.captionsscanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dohman.captionsscanner.controller.DatabaseHelper;
import com.dohman.captionsscanner.controller.LockableViewPager;
import com.dohman.captionsscanner.controller.PagerAdapter;
import com.dohman.captionsscanner.gitignore.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("SetJavaScriptEnabled")
public class WordActivity extends AppCompatActivity {
    private static final String TAG = "WordActivity";
    private final Context context = this;

    private static Word word;

    private DatabaseHelper db;
    private TextView wordTV;
    private EditText editText;
    private WebView browser;

    public static String wordStr = "null"; // Declared "null" to prevent NullException
    public static String jsonForDatabase;
    public String translatedStr;

    public LockableViewPager viewPager;
    public TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        // Database
        db = new DatabaseHelper(this);

        // ViewPager
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        // Tabs
        tabs = findViewById(R.id.tablayout);
        tabs.setupWithViewPager(viewPager);

        // Click to open the prepared WebView
        wordTV = findViewById(R.id.tv_title);
        wordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browser.setVisibility(View.VISIBLE);
            }
        });

        // Link to translation page, required by the API
        TextView poweredTV = findViewById(R.id.tv_powered);
        poweredTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://translate.yandex.com/"));
                startActivity(intent);
            }
        });

        // Instantiating a new Word if we didn't instantiate one yet
        getWord();
        // Fetching, trimming, translating and updating main word
        updateWord();

        // Already initializing WebView with search in the background for quicker experience
        browser = findViewById(R.id.webview);
        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(browser, url);
                if (!browser.canGoBack())
                    browser.clearHistory(); // Without this, the user would need to click the back button twice
            }
        });
        browser.getSettings().setJavaScriptEnabled(true);
        String url = "https://www.google.com/#q=" + wordStr + "%20meaning";
        browser.loadUrl(url);
    }

    public static Word getWord() {
        if (word == null) {
            word = new Word();
        }

        return word;
    }

    // Overriding the back button due to the WebView
    @Override
    public void onBackPressed() {
        if (browser.canGoBack()) {
            browser.goBack();
        } else if (!browser.canGoBack() && browser.getVisibility() == View.VISIBLE) {
            browser.setVisibility(View.GONE);
        } else {
            super.onBackPressed();

            if (translatedStr == null) { // Prevents NullException
                translatedStr = wordStr;
            }

            storeWord();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MainActivity.warned) {
            MainActivity.warned = false;
            showWarningBox();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        storeWord();
    }

    // Checks if word isn't already in database and then stores it
    // Also checks if word is correct before storing it
    private void storeWord() {
        if (!db.searchWord(wordStr) && !getWord().getDefinitions().get(0).equals("")) {
            SettingsActivity.setDefaults("databaseNotEmpty", "true", this);
            db.insertData(wordStr, translatedStr, jsonForDatabase); // Having this in onBackPressed() so it can't be called too early
            Toast.makeText(context, getString(R.string.saved_word), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateWord() {
        wordStr = getIntent().getStringExtra("CHOSEN_WORD")
                .trim()
                .toLowerCase()
                .replaceAll("[^a-zA-Z]", "");

        getWord().setWord(wordStr);

        wordTV.setText(wordStr);

        if (!SettingsActivity.isEnglish)
            translate(wordStr);
    }

    private void translate(final String str) {
        Log.d(TAG, "translate: running");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="
                            + Config.YANDEXAPI
                            + "&text=" + str
                            + "&lang=en-" + SettingsActivity.getDefaults("language", context);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    JSONArray jsonArray;
                                    try {
                                        jsonArray = response.getJSONArray("text");
                                        translatedStr = jsonArray.get(0).toString();
                                        getWord().setTranslated(translatedStr);
                                        TextView translatedTV = findViewById(R.id.tv_translated);
                                        if (str.equals(translatedStr)) {
                                            translatedTV.setText(getString(R.string.no_translation));
                                        } else {
                                            translatedTV.setText(translatedStr);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void editWord() {
        // TextView and make it editable via Dialog
        wordTV = findViewById(R.id.tv_title);
        AlertDialog editTextDialog = new AlertDialog.Builder(this).create();
        editText = new EditText(this);

        // Setting up the AlertDialog
        editTextDialog.setTitle(getString(R.string.edit_text));
        editTextDialog.setView(editText);
        editTextDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirm_word), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Resetting the intent
                String str = editText.getText().toString().toLowerCase().trim();
                wordTV.setText(str);
                wordStr = str;
                getWord().setWord(wordStr);
                Intent intent = getIntent();
                intent.putExtra("CHOSEN_WORD", wordStr);
                finish();
                startActivity(intent);
                translate(str);
            }
        });

        // Access from DefinitionsFragment's Snackbar
        editText.setText(wordTV.getText());
        editTextDialog.show();
    }

    // Totally disabling tabs and ViewPager's swipe,
    // used when no results for word returned
    public void disableTabs() {
        viewPager.setPagingEnabled(false);
        tabs.clearOnTabSelectedListeners();
    }

    public void showWarningBox() {
        MainActivity.warned = true;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, android.app.AlertDialog.THEME_HOLO_DARK);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.warning_title));
        builder.setMessage(getString(R.string.warning_message));
        builder.setPositiveButton(getString(R.string.warning_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0); // Removes the animation when this intent resets
    }
}