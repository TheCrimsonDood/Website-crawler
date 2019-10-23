package src.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;


public class links {

    public static HashSet<String> getInitialWords() {
        // generiert ein HashSet von Suchwörter

        // ---Variablen ---
        HashSet<String> wordList = new HashSet<String>();

        // ---Funktionalität ---
        // Leerzeichen müssen mit + ersetzt werden
        // wordList.add("AFD");
        // wordList.add("Alternative+für+Deutschland");
        // wordList.add("CDU");
        // wordList.add("Christlich+Demokratische+Union");
        // wordList.add("CSU");
        // wordList.add("Christlich+Soziale+Union");
        wordList.add("Bündnis+90+Die+Grünen");
        wordList.add("Bündnis+90");
        // wordList.add("FDP");
        // wordList.add("Freie+Demokratische+Partei");
        // wordList.add("Die+Linke");
        // wordList.add("NPD");
        // wordList.add("Nationaldemokratische+Partei+Deutschland");
        // wordList.add("Die+Partei");
        // wordList.add("SPD");
        // wordList.add("Sozialdemokratische+Partei+Deutschland");
        // wordList.add("Piratenpartei");
        // wordList.add("Piraten");
        // wordList.add("Die+Grünen");
       
        return wordList;
    }

    public static URL transformWordToSearchURL(String word, int page) {
        // Transformiert ein Word in eine Spiegel Online such URL

        // --- Variablen ---
        word = word.replace("ü", "%C3%BC");
        word = word.replace("/", "%2F");
        URL tempUrl = null;

        // --- Funktionalität ---
        try {
            tempUrl = new URL(
                    "https://www.spiegel.de/suche/?suchbegriff=" + word + "&quellenGroup=SPOX&pageNumber=" + page);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return tempUrl;

    }

    public static URL transformWordToArticleURL(String link) {
        // Transformiert eine extrahiert teilURL in eine Spiegel Online Artikel URL

        // --- Variablen ---
        URL tempUrl = null;

        // --- Funktionalität ---
        try {
            tempUrl = new URL("https://www.spiegel.de" + link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return tempUrl;

    }
}