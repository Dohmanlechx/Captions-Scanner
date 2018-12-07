package com.dohman.captionsscanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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

public class StoredWordsActivity extends AppCompatActivity {
    private final Context context = this;

    private DatabaseHelper db;
    private ArrayList<String> listItem;
    private ListView wordsListView;

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
