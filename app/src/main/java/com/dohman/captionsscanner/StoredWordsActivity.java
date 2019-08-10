package com.dohman.captionsscanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dohman.captionsscanner.controller.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.dohman.captionsscanner.MainActivity.banned;
import static com.dohman.captionsscanner.MainActivity.calls;
import static com.dohman.captionsscanner.MainActivity.warned;

public class StoredWordsActivity extends AppCompatActivity {
    private final Context context = this;

    private DatabaseHelper db;
    private ArrayList<String> listItem;
    private ListView wordsListView;

    private int bannedHour;
    private int allowedHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stored_words);

        db = new DatabaseHelper(this);
        listItem = new ArrayList<>();
        wordsListView = findViewById(R.id.listview);
        viewData();

        wordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                calls++;
                if (calls == 6) {
                    if (!warned)
                        warned = true;
                }
                if (calls >= 7) {
                    // User gets banned from API calling for 2 hours
                    // 7 calls in 60 seconds is more than 1 call in 10 seconds = obviously abuse
                    if (!banned) {
                        banned = true;
                        SettingsActivity.setBanBoolean(true, context);
                        Date bannedDate = new Date();
                        SettingsActivity.setDefaults("ban_start", String.valueOf(bannedDate.getTime()), context);
                        Calendar timeBanned = Calendar.getInstance();
                        timeBanned.setTime(new Date());
                        bannedHour = timeBanned.get(Calendar.HOUR_OF_DAY);
                        int minutes = timeBanned.get(Calendar.MINUTE);
                        allowedHour = bannedHour + 2;
                        SettingsActivity.setDefaults("allowed_hour", String.valueOf(allowedHour), context);
                        SettingsActivity.setDefaults("minutes", String.valueOf(minutes), context);
                    }
                }
                Intent intent = new Intent(context, WordActivity.class);
                intent.putExtra("CHOSEN_WORD", parent.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });
    }

    private void viewData() {
        Cursor cursor = db.viewData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                listItem.add(cursor.getString(1)); // Index 0: id, Index 1: word, Index 2: translated, Index 3: json
            }

            ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, listItem);
            wordsListView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> wordsList = new ArrayList<>();

                // Real-time search
                for (String word : listItem) {
                    if (word.toLowerCase().contains(newText.toLowerCase())) {
                        wordsList.add(word);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(StoredWordsActivity.this,
                        android.R.layout.simple_list_item_1, wordsList);
                wordsListView.setAdapter(adapter);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
