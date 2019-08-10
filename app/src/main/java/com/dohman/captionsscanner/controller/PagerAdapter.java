package com.dohman.captionsscanner.controller;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dohman.captionsscanner.fragments.AntonymsFragment;
import com.dohman.captionsscanner.fragments.DefinitionsFragment;
import com.dohman.captionsscanner.fragments.ExamplesFragment;
import com.dohman.captionsscanner.fragments.SynonymsFragment;

public class PagerAdapter extends FragmentPagerAdapter {
	
	public PagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0: return new DefinitionsFragment();
			case 1: return new ExamplesFragment();
			case 2: return new SynonymsFragment();
			case 3: return new AntonymsFragment();
		}
		
		return null;
	}
	
	@Override
	public int getCount() {
		return 4;
	}
	
	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0: return "DEFINITIONS";
			case 1: return "EXAMPLES";
			case 2: return "SYNONYMS";
			case 3: return "ANTONYMS";
		}
		
		return null;
	}
}
