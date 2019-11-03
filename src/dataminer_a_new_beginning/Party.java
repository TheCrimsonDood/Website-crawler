package src.dataminer_a_new_beginning;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class Party {

    private String time;
    private String date;
    ArrayList<String> searchwords = new ArrayList<String>();
    int count = 0;

    Party(String mainDirectory, String party) throws IOException, ParseException {

        generateSearchwordList();

        String csvFile = "\"sep=|\"\nNr|Artikel|Datum|Uhrzeit|Autor|Kategorien|Artikellänge|Tags|erlaubt Kommentare";

        for (String searchword : this.searchwords) {// Erweitert die CSV Datei um die Anzahl der Schlagwörter
            csvFile = csvFile + "|Schlagwort_" + searchword;
        }
        csvFile = csvFile + "\n";
        File partyDirectory = new File(mainDirectory, party);
        File[] listOfFiles = partyDirectory.listFiles();// Liste aller Dateien innerhalb des Ordners
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
                    System.out.println("\t\t\t\t\t---Wrote " + partyDirectory.getName() + " Article " + count);

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
                for (int j = 0; j <= article.articleInWords.length -1; j++) { // durchläuft den gesamten Artikel Wort für
                                                                           // Wort
                    for (int i = 0; i <= searchwordInWords.length -1; i++) {// zählschleife bis zum ende vom zum suchenden
                        if (!article.articleInWords[j].contains(searchwordInWords[i])) {// überprüft, ob das wort aus
                                                                                        // dem artikel mit einem der
                                                                                        // suchwörter übereinstimmt
                            break;
                        } else if (i == searchwordInWords.length-1
                                && article.articleInWords[j].contains(searchwordInWords[i])) {
                            count++;// erhöht die Anzahl der vorkomnisse um 1
                            break;
                        } else {
                            j++;
                        }
                    }
                }
                return count;
            } else {// Wird durchlaufen, wenn searchword nur aus einem Wort besteht
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
        
        getTimeOfRelease(calendar.get(calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE),
                calendar.get(calendar.SECOND));
        this.date = calendar.get(calendar.DAY_OF_MONTH) + "." + (calendar.get(calendar.MONTH)) + "."
                + calendar.get(calendar.YEAR);
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
}