package com.dohman.captionsscanner;

import java.util.ArrayList;

public class Word {
	private String word;
	private String translated;
	private ArrayList<String> definitions = new ArrayList<>();
	private ArrayList<String> examples = new ArrayList<>();
	private ArrayList<String> synonyms = new ArrayList<>();
	private ArrayList<String> antonyms = new ArrayList<>();
	
	Word() {
	} // Empty constructor
	
	// This method exists because it will crash if word not found in API
	// Also user will easily understand that word doesn't exist in dictionary
	public void clearWord() {
		definitions.clear();
		examples.clear();
		synonyms.clear();
		antonyms.clear();
		word = "";
		translated = "";
		definitions.add("");
		examples.add("");
		synonyms.add("");
		antonyms.add("");
	}
	
	public String getWord() {
		return word;
	}

	public String getTranslated() {
	    return translated;
    }

	public ArrayList<String> getDefinitions() {
		return definitions;
	}
	
	public ArrayList<String> getExamples() {
		return examples;
	}
	
	public ArrayList<String> getSynonyms() {
		return synonyms;
	}
	
	public ArrayList<String> getAntonyms() {
		return antonyms;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

	public void setTranslated(String translated) {
	    this.translated = translated;
    }
	
	public void setDefinitions(ArrayList<String> definitions) {
		this.definitions = definitions;
	}
	
	public void setExamples(ArrayList<String> examples) {
		this.examples = examples;
	}
	
	public void setSynonyms(ArrayList<String> synonyms) {
		this.synonyms = synonyms;
	}
	
	public void setAntonyms(ArrayList<String> antonyms) {
		this.antonyms = antonyms;
	}
}
