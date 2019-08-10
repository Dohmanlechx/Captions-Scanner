package com.dohman.captionsscanner.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dohman.captionsscanner.R;

import static com.dohman.captionsscanner.WordActivity.getWord;
import static com.dohman.captionsscanner.fragments.DefinitionsFragment.formatResponse;

public class AntonymsFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		
		View antonymsView = inflater.inflate(R.layout.antonyms, container, false);
		TextView antonymsTV = antonymsView.findViewById(R.id.tv_antonyms);
		
		antonymsTV.setText(formatResponse(getWord(), "antonyms"));
		
		return antonymsView;
	}
}
