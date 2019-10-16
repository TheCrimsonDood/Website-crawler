package src.dataminer;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Party {
    String name; // Name der Partei
    int articleCount = 0; // Anzahl der Artikel
    HashMap<String, Integer> categorys = new HashMap<String, Integer>(); // Auflistung der Kategorien in denen die
                                                                         // Artikel veröffentlicht wurden mit Anzahl
    int beforeEU = 0;// Anzahl der Artikel, die vor der Eu-Wahl herausgebracht wurden
    int afterEU = 0; // ANzahl der Artikel, die nach der EU-Wahl herausgebracht wurden
    int whileEU = 0; // Anzahl der Artikel, die während der EU-Wahl veröffentlicht wurden
    int averageArticleLength;
    HashMap<String, Integer> releaseMonth = new HashMap<String, Integer>(); // Zählt die Anzahl der Artikel pro Monat
    HashMap<String, Integer> keywordCount = new HashMap<String, Integer>(); //Zählt die Verwendeten Tags/Keywords diefür die Artikel gesetzt wurden
    HashMap<String, Integer> authorCount = new HashMap<String, Integer>();//Zählt die Anzahlder Artikel pro Autor
    HashMap<String, Integer> exactWordCount = new HashMap<String, Integer>();//Zählt die in Artikeln verwendeten Wörter
    HashMap<String, HashMap<String,Integer>> allKeywordRelations = new HashMap<String, HashMap<String, Integer>>();



    Party(String party, String path) throws IOException, ParseException {
        // ---Variablen---
        this.name = party;

        File folder = new File(path + name); // Erstellt den Ordnerpfad anhand des Namens des Objektes
        File[] listOfFiles = folder.listFiles(); // Liest alle Datein im Ordner
        int totalLength = 0;// Gesamtlänge aller Artikel
        instanciateReleaseMonth();

        // ---Funktionen---
        for (File file : listOfFiles) { // Schleife, die folgende Aktionen für jede Datei ausführen
            if (file.isFile()) {
                Article article = new Article(folder +"/" + file.getName());// Öffnet einen Artikel und ließt wichtige Daten
                                                                       // aus
                this.articleCount++; // Erhöht die Varaible um 1, da sie die Anzahl der Artikel wiedergibt
                          
                System.out.println("\t\t\t\t\t---ARTICLE: " + this.articleCount + " initialized!");
                
                totalLength += article.wordCount;
                getAuthorFromArticle(article.author);// Ließt den Autor aus und speichert Ihn in der Variable
                                                     // authorCount
                getDateFromArticle(article.date);
                getCategoryFromArticle((article.category));
                getWordCountFromArticle(article.exactWordCount);
                getKeywordsFromArticle(article.keywords);
                generateKeyWordRelation(article.keywords);

            }
        }
        this.averageArticleLength = totalLength / this.articleCount;
    }

    private void getDateFromArticle(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        // Setzt den Zeitpunkt des Startes und Endes der Eu-Wahl
        Calendar euStart, euEnd, calendar;
        euStart = Calendar.getInstance();
        euStart.setTime(sdf.parse("2019-05-23 00:00:00"));
        euEnd = Calendar.getInstance();
        euEnd.setTime(sdf.parse("2019-05-26 23:59:59"));
        calendar = Calendar.getInstance();// erzeugt ein Objekt calendar mit dem übergebenen Datum
        calendar.setTime(date);
        // Bestimmt, ob der Artikel vor, nach oder während der EU-Walh erschienen ist
        // und erhöht die entsprechende Variable
        if (euStart.after(calendar) == true) {
            beforeEU++;
        } else if (euEnd.before(calendar) == true) {
            afterEU++;
        } else {
            whileEU++;
        }

        // setzt das Datum des Kalenders in die HashMap releasMonth
        switch (calendar.get(Calendar.MONTH)) {
        case 0:
            this.releaseMonth.put("Januar", this.releaseMonth.get("Januar") + 1);
            break;
        case 1:
            this.releaseMonth.put("Februar", this.releaseMonth.get("Februar") + 1);
            break;
        case 2:
            this.releaseMonth.put("März", this.releaseMonth.get("März") + 1);
            break;
        case 3:
            this.releaseMonth.put("April", this.releaseMonth.get("April") + 1);
            break;
        case 4:
            this.releaseMonth.put("Mai", this.releaseMonth.get("Mai") + 1);
            break;
        case 5:
            this.releaseMonth.put("Juni", this.releaseMonth.get("Juni") + 1);
            break;
        case 6:
            this.releaseMonth.put("Juli", this.releaseMonth.get("Juli") + 1);
            break;
        case 7:
            this.releaseMonth.put("August", this.releaseMonth.get("August") + 1);
            break;
        case 8:
            this.releaseMonth.put("September", this.releaseMonth.get("September") + 1);
            break;
        case 9:
            this.releaseMonth.put("Oktober", this.releaseMonth.get("Oktober") + 1);
            break;
        case 10:
            this.releaseMonth.put("November", this.releaseMonth.get("November") + 1);
            break;
        case 11:
            this.releaseMonth.put("Dezember", this.releaseMonth.get("Dezember") + 1);
            break;
        default:
            System.out.println("Error. Monat des Artikels konnte nicht in releaseMonth gesetzt werden");
        }
    }

    private void instanciateReleaseMonth() {
        //Initialisiert die Variable releaseMonth mit einen Satz Keys
        this.releaseMonth.put("Januar", 0);
        this.releaseMonth.put("Februar", 0);
        this.releaseMonth.put("März", 0);
        this.releaseMonth.put("April", 0);
        this.releaseMonth.put("Mai", 0);
        this.releaseMonth.put("Juni", 0);
        this.releaseMonth.put("Juli", 0);
        this.releaseMonth.put("August", 0);
        this.releaseMonth.put("September", 0);
        this.releaseMonth.put("Oktober", 0);
        this.releaseMonth.put("November", 0);
        this.releaseMonth.put("Dezember", 0);
    }

    private void getAuthorFromArticle(String author) {
        // Schreibt den Übergebenen Autor in die Varible
        if (authorCount.containsKey(author)) {// Überprüft, ob der Autor schon in die Variable gespeichert wurde
            authorCount.put(author, authorCount.get(author) + 1);// Wenn ja, wird die Anzahl um 1 erhöht
        } else {
            authorCount.put(author, 1);// Wenn nicht, wird er mit dem Wert 1 hinzugefügt
        }
    }

    private void getCategoryFromArticle(String category) {
        // Schreibt die Übergebene Kategorie in die Varible
        if (categorys.containsKey(category)) {// Überprüft, ob die Kategorie schon in die Variable gespeichert wurde
            categorys.put(category, categorys.get(category) + 1);// Wenn ja, wird die Anzahl um 1 erhöht
        } else {
            categorys.put(category, 1);// Wenn nicht, wird sie mit dem Wert 1 hinzugefügt
        }
    }

    private void getWordCountFromArticle(HashMap<String, Integer> countWord){
        //Überträgt alle Wörter aus den Artikel in eine HashMap in der alle Wörter aller Artikel gesammelt werden


        for(Map.Entry<String, Integer> entry : countWord.entrySet()) {//Schleife, die durch alle gesammelten Wörter läuft
            String key = entry.getKey();
            Integer value = entry.getValue();
            
            if(this.exactWordCount.containsKey(key) == true){//Überprüft, ob der key in der Sammel HashMap vorhanden ist
                this.exactWordCount.put(key, this.exactWordCount.get(key) + value);// Wenn ja, wird der Wert zu dem bestehenden hinzuaddiert
            }else{
                this.exactWordCount.put(key, value); //wenn nicht, wird der key mit dem Wert gesetzt
            }            
        }
    }

    private void getKeywordsFromArticle(String[] keywords){
        //Überträgt die Verwendeten Keywords des Artikels in eine HashMap

        for(String keyword : keywords){ //Schleife, die alle übergebenen Keywords durchläuft
            keyword =keyword.toLowerCase(); // ändert die Keywords in ein kleingeschriebenes Format, da die HashMaps Case sensitive sind
            if (this.keywordCount.containsKey(keyword) == true){ //Überprüft, ob die HashMap das Keyword enthält 
                this.keywordCount.put(keyword, this.keywordCount.get(keyword) + 1); //Wenn ja, wird der Wert um eins erhöht
            }else{
                this.keywordCount.put(keyword, 1); //wenn nicht, wird der Key mit den Wert 1 gesetzt
            }
        }
    }
    
    private void generateKeyWordRelation(String[] keywords){
        //---Variablen---
        HashMap<String,Integer> tempKeywordList;
        //---Funktionen---

        for (String keyword: keywords){ //Schleife für alle keywords aus dem Artikel
            keyword =keyword.toLowerCase(); // ändert die Keywords in ein kleingeschriebenes Format, da die HashMaps Case sensitive sind
            
            if (this.allKeywordRelations.containsKey(keyword)){ //Überprüft, ob das Keyword in der gesammten Liste  vohanden ist
                tempKeywordList = this.allKeywordRelations.get(keyword); //Speichert die HashMap zu dem dazugehörigen Key in eine Temporäre variable aus
            }else{ //Wird ausgeführt, wenn die gesammte KeywordListe das Schlüssel Keyword nicht enthält
                tempKeywordList = new HashMap<String,Integer>();
            }

            for(String keywordToSave: keywords){//Weitere schleife, die durch alle Keywords der Artikel  läuft
                keywordToSave =keywordToSave.toLowerCase(); // ändert die Keywords in ein kleingeschriebenes Format, da die HashMaps Case sensitive sind
                
                if (keywordToSave != keyword){//Überprüft, ob das zu speichernde Keyword ein anderes ist als das Schlüssel Keyword
                    if (tempKeywordList.containsKey(keywordToSave)){//Überprüft, ob die Temporäre Liste des Schlüssel Keywords das zu speichernde Keyword enthält
                        tempKeywordList.put(keywordToSave, tempKeywordList.get(keywordToSave) + 1);//Erhöht die Relation des Keywords mit dem Schlüssel Keyword um eins
                    }else{
                        tempKeywordList.put(keywordToSave, 1); //Speichert die Relation mit dem Schlüssel Keyword zum ersten mal
                    }
                }
            }
            allKeywordRelations.put(keyword, tempKeywordList); //Speichert die Temporäre HashMap als Value für das Schlüssel Keyword
        }
    }

    
}