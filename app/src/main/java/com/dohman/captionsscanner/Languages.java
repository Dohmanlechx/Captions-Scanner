package com.dohman.captionsscanner;

import java.util.HashMap;

class Languages {

    private HashMap<String, String> langMap;

    Languages() {
        langMap = new HashMap<>();
        langMap.put("ALBANIAN", "sq");
        langMap.put("ARMENIAN", "hy");
        langMap.put("AZERBAIJANI", "az");
        langMap.put("BELARUSIAN", "be");
        langMap.put("BULGARIAN", "bg");
        langMap.put("CATALAN", "ca");
        langMap.put("CROATIAN", "hr");
        langMap.put("CZECH", "cs");
        langMap.put("DANISH", "da");
        langMap.put("DUTCH", "nl");
        langMap.put("ENGLISH", "en");
        langMap.put("ESTONIAN", "et");
        langMap.put("FINNISH", "fi");
        langMap.put("FRENCH", "fr");
        langMap.put("GERMAN", "de");
        langMap.put("GEORGIAN", "ka");
        langMap.put("GREEK", "el");
        langMap.put("HUNGARIAN", "hu");
        langMap.put("ITALIAN", "it");
        langMap.put("LATVIAN", "lv");
        langMap.put("LITHUANIAN", "lt");
        langMap.put("MACEDONIAN", "mk");
        langMap.put("NORWEGIAN", "no");
        langMap.put("POLISH", "pl");
        langMap.put("PORTUGUESE", "pt");
        langMap.put("ROMANIAN", "ro");
        langMap.put("RUSSIAN", "ru");
        langMap.put("SERBIAN", "sr");
        langMap.put("SLOVAK", "sk");
        langMap.put("SLOVENIAN", "sl");
        langMap.put("SPANISH", "es");
        langMap.put("SWEDISH", "sv");
        langMap.put("TURKISH", "tr");
        langMap.put("UKRAINIAN", "uk");
    }

    String getLang(String choice) {
        return langMap.get(choice.toUpperCase());
    }
}
