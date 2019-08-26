package com.dohman.captionsscanner.threads;

import android.util.Log;

import com.dohman.captionsscanner.WordActivity;
import com.dohman.captionsscanner.gitignore.Config;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.dohman.captionsscanner.WordActivity.getWord;

public class FetchAPIData extends Thread {
	private static final String TAG = "FetchAPIData";
	
	@Override
	public void run() {
		synchronized (this) {
			try {
				HttpResponse<JsonNode> response = Unirest.get("https://wordsapiv1.p.rapidapi.com/words/" + WordActivity.wordStr)
						.header("X-Mashape-Key", Config.WORDSAPIKEY)
						.header("X-Mashape-Host", "wordsapiv1.p.rapidapi.com")
						.asJson();
				
				JSONObject overallJsonObj = response.getBody().getObject();
				JSONArray wordJsonArr;
				if (!overallJsonObj.has("results")) {
					getWord().clearWord();
					notify();
					return; // Aborting the thread if word doesn't exist in API
				} else {
					wordJsonArr = overallJsonObj.getJSONArray("results");
					WordActivity.jsonForDatabase = overallJsonObj.toString();
				}
				
				ArrayList<String> definitionsArr = new ArrayList<>();
				ArrayList<String> examplesArr = new ArrayList<>();
				ArrayList<String> synonymsArr = new ArrayList<>();
				ArrayList<String> antonymsArr = new ArrayList<>();
				
				// ------------------ Definitions ------------------
				for (int i = 0; i < wordJsonArr.length(); i++) {
					JSONObject object = wordJsonArr.getJSONObject(i);
					if (!object.has("definition"))
						continue;
					String formattedStr = (i+1) + ". " + object.optString("definition") + "\n\n";
					
					definitionsArr.add(formattedStr);
				}
				getWord().setDefinitions(definitionsArr);
				
				// ------------------ Examples ------------------
				for (int i = 0; i < wordJsonArr.length(); i++) {
					JSONObject object = wordJsonArr.getJSONObject(i);
					if (!object.has("examples"))
						continue;
					String formattedStr = object.optString("examples")
							.replace(",", "\n\n") + "\n\n";
					
					examplesArr.add(formattedStr);
				}
				getWord().setExamples(examplesArr);

				// ------------------ Synonyms ------------------
				for (int i = 0; i < wordJsonArr.length(); i++) {
					JSONObject object = wordJsonArr.getJSONObject(i);
					if (!object.has("synonyms"))
						continue;
					String formattedStr = object.optString("synonyms")
							.replace(",", "\n");
					
					synonymsArr.add(formattedStr);
				}
				getWord().setSynonyms(synonymsArr);
				
				// ------------------ Antonyms ------------------
				for (int i = 0; i < wordJsonArr.length(); i++) {
					JSONObject object = wordJsonArr.getJSONObject(i);
					if (!object.has("antonyms"))
						continue;
					String formattedStr = object.optString("antonyms")
							.replace(",", "\n");
					
					antonymsArr.add(formattedStr);
				}
				getWord().setAntonyms(antonymsArr);

				Log.d(TAG, "Thread: API call successful");

			} catch (JSONException | UnirestException e) {
				e.printStackTrace();
			}
			notify();
		}
	}
}