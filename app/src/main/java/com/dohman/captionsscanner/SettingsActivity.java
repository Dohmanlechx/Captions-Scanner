package com.dohman.captionsscanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    private final Context context = this;

    public static boolean isEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Languages Spinner
        Spinner spinner = findViewById(R.id.spinner);

        // If the user already chose language before
        if (getDefaults("spinnerindex", this) != null) {
            spinner.setSelection(Integer.parseInt(getDefaults(
                    "spinnerindex", context)));
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Languages languages = new Languages();
                String selected = languages.getLang(parent.getItemAtPosition(position).toString()); // Fetching country code
                Log.d(TAG, "onItemSelected: " + selected);
                setDefaults("language", selected, context);
                setDefaults("spinnerindex", String.valueOf(position), context);

                // True if selected language is English, false if not
                if (selected != null)
                isEnglish = selected.toLowerCase().equals("en");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Setter (SharedPreferences), accessed by whole application
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Getter (SharedPreferences), accessed by whole application
    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    // Ban Setter
    public static void setBanBoolean(boolean value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("banned", value);
        editor.apply();
    }

    // Ban Getter
    public static boolean getBanBoolean(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("banned", false);
    }
}
