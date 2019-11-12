package src.dataminer_a_new_beginning;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Party {

    private String time;
    private String date;
    ArrayList<String> searchwords = new ArrayList<String>();
    int count = 0;
    HashMap<String, HashMap<String, Integer>> allKeywordRelations = new HashMap<String, HashMap<String, Integer>>(); // HashMap
                                                                                                                     // mit
                                                                                                                     // der
                                                                                                                     // Anzahl
                                                                                                                     // der
                                                                                                                     // Keywords,
                                                                                                                     // die
                                                                                                                     // im
                                                                                                                     // selben
                                                                                                                     // Artikel
                                                                                                                     // gesetzt
                                                                                                                     // wurden
    HashMap<String, HashMap<String, Integer>> keywordOrigin = new HashMap<String, HashMap<String, Integer>>(); // Ursprung
                                                                                                               // der
                                                                                                               // Keywords
                                                                                                               // anhand
                                                                                                               // der
                                                                                                               // Kategorie
                                                                                                               // in der
                                                                                                               // die
                                                                                                               // Artikelveröffentlicht
                                                                                                               // wurden
    HashMap<String, Integer> keywordCount = new HashMap<String, Integer>();
    String nodeJSON = new String();

    Party(String mainDirectory, String party) throws IOException, ParseException {

        generateSearchwordList();

        String csvFile = "\"sep=|\"\nNr|Artikel|Datum|Uhrzeit|Autor|Kategorien|Artikellänge|Tags|erlaubt Kommentare";

        for (String searchword : this.searchwords) { // Erweitert die CSV Datei um die Anzahl der Schlagwörter
            csvFile = csvFile + "|Schlagwort_" + searchword;
        }
        csvFile = csvFile + "\n";
        File partyDirectory = new File(mainDirectory, party);
        File[] listOfFiles = partyDirectory.listFiles(); // Liste aller Dateien innerhalb des Ordners
        if (listOfFiles.length > 0) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    count++;
                    Article article = new Article(file.getPath());

                    getDateTimeFromArticle(article.date);
                    csvFile = csvFile + count + "|";
                    csvFile = csvFile + file.getName().replaceAll(".txt", "") + "|";
                    csvFile = csvFile + this.date + "|";
                    csvFile = csvFile + this.time + "|";
                    csvFile = csvFile + article.author + "|";
                    csvFile = csvFile + article.category + "|";
                    csvFile = csvFile + article.wordCount + "|";
                    csvFile = csvFile + keywordToString(article.keywords) + "|";
                    csvFile = csvFile + article.hasComments + "|";

                    for (String searchword : this.searchwords) {
                        csvFile = csvFile + doesArticleCotain(article, searchword) + "|";
                    }
                    csvFile = csvFile + "\n";
                    getKeywordFromArticle(article.keywords);
                    generateKeyWordRelation(article.keywords);
                    generateKeywordOrigin(article.keywords, article.category);
                    if ((count % 50) == 0) {
                        System.out.println("\t\t\t\t\t---finished " + partyDirectory.getName() + " Article " + count);
                    }
                }

            }
            File file = new File(mainDirectory + "\\0 CSV\\" + party + ".csv");
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(csvFile);
            bw.close();
        }

    }

    private void generateSearchwordList() {
        // Generiert eine ArrayList mit allen zu suchenden Schlagwörtern
        this.searchwords.add("AFD");
        this.searchwords.add("Alternative für Deutschland");
        this.searchwords.add("CDU");
        this.searchwords.add("Christlich Demokratische Union");
        this.searchwords.add("CSU");
        this.searchwords.add("Christlich-Soziale Union");
        this.searchwords.add("FDP");
        this.searchwords.add("Freie Demokratische Partei");
        this.searchwords.add("Die Grünen");
        this.searchwords.add("Bündnis 90");
        this.searchwords.add("Linke Partei");
        this.searchwords.add("NPD");
        this.searchwords.add("Nationaldemokratische Partei Deutschlands");
        this.searchwords.add("Die Linke");
        this.searchwords.add("Die Partei");
        this.searchwords.add("Piratenpartei");
        this.searchwords.add("SPD");
        this.searchwords.add("Sozialdemokratische Partei Deutschlands");
        this.searchwords.add("Umwelt");
        this.searchwords.add("Klimaschutz");
        this.searchwords.add("Rezo");
        this.searchwords.add("#niemehrCDU");
        this.searchwords.add("Die Zerstörung der CDU");
        this.searchwords.add("Artikel 13");
        this.searchwords.add("Artikel 17");
        this.searchwords.add("Urheberrecht");
        this.searchwords.add("Radikal");
        this.searchwords.add("Nazi");
        this.searchwords.add("Nationalsozialismus");
        this.searchwords.add("Extremismus");
        this.searchwords.add("Kampf");
        this.searchwords.add("Angst");
        this.searchwords.add("Anschlag");
        this.searchwords.add("Flüchtlinge");
    }

    private int doesArticleCotain(Article article, String searchword) {

        if (article.article.contains(searchword)) { // Überprüft, ob das gesamte suchwort mindestend 1x vorhanden ist
            if (searchword.contains(" ")) { // Überprüft, ob searchword aus einzelnen Wörtern besteht
                String[] searchwordInWords = searchword.split(" "); // splittet das zu suchende searchword in einzelne
                // Worte auf
                int count = 0; // anzahl der vorkommnisse des zu suchenden
                for (int j = 0; j <= article.articleInWords.length - 1; j++) { // durchläuft den gesamten Artikel Wort
                                                                               // für
                    // Wort
                    for (int i = 0; i <= searchwordInWords.length - 1; i++) { // zählschleife bis zum ende vom zum
                                                                              // suchenden
                        if (!article.articleInWords[j].contains(searchwordInWords[i])) { // überprüft, ob das wort aus
                            // dem artikel mit einem der
                            // suchwörter übereinstimmt
                            break;
                        } else if (i == searchwordInWords.length - 1
                                && article.articleInWords[j].contains(searchwordInWords[i])) {
                            count++; // erhöht die Anzahl der vorkomnisse um 1
                            break;
                        } else {
                            j++;
                        }
                    }
                }
                return count;
            } else { // Wird durchlaufen, wenn searchword nur aus einem Wort besteht
                int count = 0;
                for (String word : article.articleInWords) {
                    if (word.equals(searchword)) {
                        count++;
                    }
                }
                return count;
            }
        } else {
            return 0;
        }
    }

    private String keywordToString(String[] keywords) {
        String keywordString = new String();
        for (String keyword : keywords) {
            if (keywordString.equals("")) {
                keywordString = keyword;
            } else {
                keywordString = keywordString + ", " + keyword;
            }
        }
        return keywordString;
    }

    private void getDateTimeFromArticle(Calendar calendar) {

        this.date = calendar.get(calendar.DAY_OF_MONTH) + "." + (calendar.get(calendar.MONTH) + 1) + "."
                + calendar.get(calendar.YEAR);
        getTimeOfRelease(calendar.get(calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE),
                calendar.get(calendar.SECOND));

    }

    private void getTimeOfRelease(int hour, int minute, int second) {
        // Formatiert den Zeitpunkt des Hochladens des Artikel und speichert in in einer
        // ArrayList
        String hourString;
        String minuteString;
        String secondString;

        if (hour < 10) {
            hourString = "0" + Integer.toString(hour);
        } else {
            hourString = Integer.toString(hour);
        }

        if (minute < 10) {
            minuteString = "0" + Integer.toString(minute);
        } else {
            minuteString = Integer.toString(minute);
        }

        if (second < 10) {
            secondString = "0" + Integer.toString(second);
        } else {
            secondString = Integer.toString(second);
        }

        this.time = hourString + ":" + minuteString + ":" + secondString;

    }


    private void generateKeyWordRelation(String[] keywords) {
        // Erstellt eine HashMap in der die Anzahl der Keywords gezählt werde, die mit
        // dem Keyword x gesetzt wurden
        // ---Variablen---
        HashMap<String, Integer> tempKeywordList;
        // ---Funktionen---

        for (String keyword : keywords) { // Schleife für alle keywords aus dem Artikel
            keyword = keyword.toLowerCase(); // ändert die Keywords in ein kleingeschriebenes Format, da die HashMaps
                                             // Case sensitive sind

            if (this.allKeywordRelations.containsKey(keyword)) { // Überprüft, ob das Keyword in der gesammten Liste
                                                                 // vohanden ist
                tempKeywordList = this.allKeywordRelations.get(keyword); // Speichert die HashMap zu dem dazugehörigen
                                                                         // Key in eine Temporäre variable aus
            } else { // Wird ausgeführt, wenn die gesammte KeywordListe das Schlüssel Keyword nicht
                     // enthält
                tempKeywordList = new HashMap<String, Integer>();
            }

            for (String keywordToSave : keywords) { // Weitere schleife, die durch alle Keywords der Artikel läuft
                keywordToSave = keywordToSave.toLowerCase(); // ändert die Keywords in ein kleingeschriebenes Format, da
                                                             // die HashMaps Case sensitive sind

                if (!keywordToSave.equals(keyword)) { // Überprüft, ob das zu speichernde Keyword ein anderes ist als
                                                      // das Schlüssel Keyword
                    if (tempKeywordList.containsKey(keywordToSave)) { // Überprüft, ob die Temporäre Liste des Schlüssel
                                                                      // Keywords das zu speichernde Keyword enthält
                        tempKeywordList.put(keywordToSave, tempKeywordList.get(keywordToSave) + 1); // Erhöht die
                                                                                                    // Relation des
                                                                                                    // Keywords mit dem
                                                                                                    // Schlüssel Keyword
                                                                                                    // um eins
                    } else {
                        tempKeywordList.put(keywordToSave, 1); // Speichert die Relation mit dem Schlüssel Keyword zum
                                                               // ersten mal
                    }
                }
            }
            allKeywordRelations.put(keyword, tempKeywordList); // Speichert die Temporäre HashMap als Value für das
                                                               // Schlüssel Keyword
        }
    }

    private void generateKeywordOrigin(String[] keywords, String category) {
        // erstellt eine Hashmap mit den Keywords und einer Liste mit den
        // Ursprungskategorien der Artikel

        for (String singleKeyword : keywords) {
            singleKeyword = singleKeyword.toLowerCase();

            HashMap<String, Integer> tempHashMap;
            if (keywordOrigin.containsKey(singleKeyword)) {
                tempHashMap = keywordOrigin.get(singleKeyword);
            } else {
                tempHashMap = new HashMap<String, Integer>();
            }

            if (tempHashMap.containsKey(category)) {
                tempHashMap.put(category, tempHashMap.get(category) + 1);
            } else {
                tempHashMap.put(category, 1);
            }
            keywordOrigin.put(singleKeyword, tempHashMap);

        }

    }

    private void getKeywordFromArticle(String[] keywords) {
        // Erstellt eine HasmMap mit allen Keywords und der häufigkeit der Verwendung
        for (String keyword : keywords) {
            keyword = keyword.toLowerCase();

            // Überprüft, ob die HashMap das keyword schon beinhält
            if (this.keywordCount.containsKey(keyword)) {
                this.keywordCount.put(keyword, this.keywordCount.get(keyword) + 1);
            } else {
                this.keywordCount.put(keyword, 1);
            }
        }
    }
}