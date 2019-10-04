package src;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

public class links {

    public static HashSet<String> getLinks() {
        HashSet<String> wordList = new HashSet<String>();

        // generiert ein HashSet von Suchwörter
        //Leerzeichen müssen mit + ersetzt werden
        wordList.add("AFD");
        wordList.add("CDU");
        wordList.add("Grüne");
        wordList.add("FDP");
        wordList.add("SPD");
        wordList.add("Piraten");
        wordList.add("NPD");
        wordList.add("Alternative+für+Deutschlands");
        wordList.add("Christlich+Demokratische+Union");
        wordList.add("Sozialdemokratische+Partei+Deutschlands");
        wordList.add("Bündnis+90+DIE+Grünen");
        wordList.add("Piratenpartei");
        wordList.add("Nationaldemokratische+Partei+Deutschlands");

        return wordList;
    }

    public static URL transformWordToURL(String word) {

        // Transformiert ein Word in eine Spiegel Online such URL
        word = word.replace("ü", "%C3%BC");
        word = word.replace("/", "%2F");

        URL tempUrl = null;
        try {
            tempUrl = new URL("https://www.spiegel.de/suche/?suchbegriff=" + word + "&quellenGroup=SPOX");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return tempUrl;

    }
    public static void main(String[] args0) {
        String word = "Günther";
        // Transformiert ein Word in eine Spiegel Online such URL
        word = word.replace("ü", "%C3%BC");
        // word.replace("ü", );
        // word.replace("/", "%2F");
        System.out.println(word);

    }
}