package com.dohman.captionsscanner.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dohman.captionsscanner.R;
import com.dohman.captionsscanner.Word;

import static com.dohman.captionsscanner.WordActivity.getWord;
import static com.dohman.captionsscanner.fragments.DefinitionsFragment.formatResponse;

public class ExamplesFragment extends Fragment {
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		
		View examplesView = inflater.inflate(R.layout.examples, container, false);
		TextView examplesTV = examplesView.findViewById(R.id.tv_examples);
		
		examplesTV.setText(formatResponse(getWord(), "examples"));
		
		return examplesView;
	}
}
