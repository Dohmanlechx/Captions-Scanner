package com.dohman.captionsscanner.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dohman.captionsscanner.MainActivity;
import com.dohman.captionsscanner.R;
import com.dohman.captionsscanner.SettingsActivity;
import com.dohman.captionsscanner.Word;
import com.dohman.captionsscanner.WordActivity;
import com.dohman.captionsscanner.threads.FetchAPIData;

import java.util.Objects;

import static com.dohman.captionsscanner.WordActivity.getWord;

public class DefinitionsFragment extends Fragment {
    private static final String TAG = "DefinitionsFragment";

    // ------------------------ NOTE ------------------------ //
    // ----------- This is also the main fragment ----------- //
    // ------ handling stuff for rest of the fragments ------ //
    // ------------------------------------------------------ //

    private static Word word = getWord();
    private Word currentWord;
    private TextView definitionsTV;
    final FetchAPIData mFetchAPIData = new FetchAPIData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View definitionsView = inflater.inflate(R.layout.definitions, container, false);
        definitionsTV = definitionsView.findViewById(R.id.tv_definitions);

        if (MainActivity.banned) {
            // User abused and is banned, don't start Thread for API call
            word.clearWord();
        } else {
            if (currentWord != word) { // Without this, app would start the thread even if user hasn't chose new word
                Log.d(TAG, "It's a new word, run thread");
                startThread();
            } else {
                Log.d(TAG, "It's not a new word, just setText from the existing data");
                definitionsTV.setText(formatResponse(word, "definitions"));
            }
        }

        return definitionsView;
    }

    // Need this to be able to find parent for SnackBar
    @Override
    public void onResume() {
        super.onResume();
        if (word.getDefinitions().get(0).equals("")) {
            ((WordActivity) Objects.requireNonNull(getActivity())).disableTabs();
            CoordinatorLayout parent = Objects.requireNonNull(getActivity()).findViewById(R.id.coordinator_layout);
            if (!MainActivity.banned) {
                Snackbar snackbar = Snackbar.make(parent, getString(R.string.word_no_exist), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.edit_word), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((WordActivity) getActivity()).editWord();
                            }
                        });

                snackbar.show();
            } else {
                String hour = SettingsActivity.getDefaults("allowed_hour", getContext());
                String minutes = SettingsActivity.getDefaults("minutes", getContext());
                if (Integer.parseInt(minutes) < 10) {
                    Snackbar snackbar = Snackbar.make(parent, getString(R.string.banned) +
                            " Try again at " + hour + ":0" + minutes + " o'clock.", Snackbar.LENGTH_INDEFINITE);
                    View snackbarView = snackbar.getView();
                    TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setMaxLines(5);

                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(parent, getString(R.string.banned) +
                            " Try again at " + hour + ":" + minutes + " o'clock.", Snackbar.LENGTH_INDEFINITE);
                    View snackbarView = snackbar.getView();
                    TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setMaxLines(5);

                    snackbar.show();
                }
            }
        }
    }

    // Starting a thread which fetches all data about the word,
    // in other fragments, they won't run any thread but just take values
    // from the finished Word class
    public void startThread() {
        mFetchAPIData.start();

        synchronized (mFetchAPIData) {
            try {
                mFetchAPIData.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            definitionsTV.setText(formatResponse(word, "definitions"));
            currentWord = word;
        }
    }

    public static String formatResponse(Word word, String type) {
        String str = "";

        switch (type) {
            case "definitions":
                str = word.getDefinitions().toString();
                for (int i = 0; i <= 9; i++) {
                    str = str.replace(", " + i, "" + i);
                }
                break;
            case "examples":
                str = word.getExamples().toString();
                str = str.replace(", [", "");
                break;
            case "synonyms":
                str = word.getSynonyms().toString();
                str = str.replace(", ", "\n");
                break;
            case "antonyms":
                str = word.getAntonyms().toString();
                str = str.replace(", ", "\n");
                break;
        }

        str = str.replace("[", "");
        str = str.replace("]", "");
        str = str.replace("{", "");
        str = str.replace("}", "");

        return str;
    }
}