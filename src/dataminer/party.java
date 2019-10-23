package src.dataminer;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    HashMap<String, Month> releaseMonth = new HashMap<String, Month>(); // Zählt die Anzahl der Artikel pro Monat
    HashMap<String, Integer> keywordCount = new HashMap<String, Integer>(); //Zählt die Verwendeten Tags/Keywords diefür die Artikel gesetzt wurden
    HashMap<String, Integer> authorCount = new HashMap<String, Integer>();//Zählt die Anzahlder Artikel pro Autor
    HashMap<String, Integer> exactWordCount = new HashMap<String, Integer>();//Zählt die in Artikeln verwendeten Wörter
    HashMap<String, HashMap<String,Integer>> allKeywordRelations = new HashMap<String, HashMap<String, Integer>>();//HashMap mit der Anzahl der Keywords, die im selben Artikel gesetzt wurden     
    HashMap<String,HashMap<String, Integer>> keywordOrigin = new HashMap<String, HashMap<String, Integer>>(); //Ursprung der Keywords anhand der Kategorie in der die Artikelveröffentlicht wurden
    ArrayList<String> timeOfRelease = new ArrayList<String>();

    Party(String party, String path) throws IOException, ParseException {
        // ---Variablen---
        this.name = party;

        File folder = new File(path + "/"+name); // Erstellt den Ordnerpfad anhand des Namens des Objektes
        File[] listOfFiles = folder.listFiles(); // Liest alle Datein im Ordner
        int totalLength = 0;// Gesamtlänge aller Artikel
        instanciateReleaseMonth();

        // ---Funktionen---
        for (File file : listOfFiles) { // Schleife, die folgende Aktionen für jede Datei ausführen
            if (file.isFile()) {
                Article article = new Article(folder +"/" + file.getName());// Öffnet einen Artikel und ließt wichtige Daten
                                                                       // aus
                this.articleCount++; // Erhöht die Varaible um 1, da sie die Anzahl der Artikel wiedergibt
                          
                System.out.println("\t\t\t\t\t---ARTICLE: " + this.articleCount + " initialized!---");
                
                totalLength += article.wordCount;
                getAuthorFromArticle(article.author);// Ließt den Autor aus und speichert Ihn in der Variable
                                                     // authorCount
                getDateFromArticle(article.date);
                getCategoryFromArticle((article.category));
                getWordCountFromArticle(article.exactWordCount);
                getKeywordsFromArticle(article.keywords);
                generateKeyWordRelation(article.keywords, article.category);
                generateKeywordOrigin(article.keywords, article.category);

            }
        }
        this.averageArticleLength = totalLength / this.articleCount;
    }

    private void getTimeOfRelease(int hour, int minute, int second){
        //Formatiert den Zeitpunkt des Hochladens des Artikel und speichert in in einer ArrayList
        String hourString;
        String minuteString;
        String secondString;

        if (hour < 10){
            hourString = "0" + Integer.toString(hour);
        }else{
            hourString = Integer.toString(hour);
        }
        
        if(minute < 10){
            minuteString = "0" + Integer.toString(minute);
        }else{
            minuteString = Integer.toString(minute);
        }

        if (second < 10){
            secondString = "0" + Integer.toString(second);
        }else{
            secondString = Integer.toString(second);
        }

        String time = hourString + ":" + minuteString +":" + secondString + " Uhr";
        this.timeOfRelease.add(time);
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

        getTimeOfRelease(calendar.get(calendar.HOUR), calendar.get(calendar.MINUTE), calendar.get(calendar.SECOND));
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
            this.releaseMonth.put("Januar", evaluateDayOfArticle(calendar, this.releaseMonth.get("Januar")));
            break;
        case 1:
            this.releaseMonth.put("Februar", evaluateDayOfArticle(calendar, this.releaseMonth.get("Februar")));
            break;
        case 2:
            this.releaseMonth.put("März", evaluateDayOfArticle(calendar, this.releaseMonth.get("März")));
            break;
        case 3:
            this.releaseMonth.put("April", evaluateDayOfArticle(calendar, this.releaseMonth.get("April")));
            break;
        case 4:
            this.releaseMonth.put("Mai", evaluateDayOfArticle(calendar, this.releaseMonth.get("Mai")));
            break;
        case 5:
            this.releaseMonth.put("Juni", evaluateDayOfArticle(calendar, this.releaseMonth.get("Juni")));
            break;
        case 6:
            this.releaseMonth.put("Juli", evaluateDayOfArticle(calendar, this.releaseMonth.get("Juli")));
            break;
        case 7:
            this.releaseMonth.put("August", evaluateDayOfArticle(calendar, this.releaseMonth.get("August")));
            break;
        case 8:
            this.releaseMonth.put("September", evaluateDayOfArticle(calendar, this.releaseMonth.get("September")));
            break;
        case 9:
            this.releaseMonth.put("Oktober", evaluateDayOfArticle(calendar, this.releaseMonth.get("Oktober")));
            break;
        case 10:
            this.releaseMonth.put("November", evaluateDayOfArticle(calendar, this.releaseMonth.get("November")));
            break;
        case 11:
            this.releaseMonth.put("Dezember", evaluateDayOfArticle(calendar, this.releaseMonth.get("Dezember")));
            break;
        default:
            System.out.println("Error. Monat des Artikels konnte nicht in releaseMonth gesetzt werden");
        }
    }
    
    private Month evaluateDayOfArticle(Calendar calendar,Month month){
        //Bekommt einen Kalender und eine HashMap übergeben, an der er den Tag des Monats erfässt und in der entsprechenden Stelle in der HashMap erhöht und zurückgibt        
        
        HashMap<String, Integer> MonthMap = month.articlePerDay;
        switch(calendar.get(calendar.DAY_OF_MONTH)){
            case(1):{MonthMap.put("1", MonthMap.get("1") + 1);break;}
            case(2):{MonthMap.put("2", MonthMap.get("2") + 1);break;}
            case(3):{MonthMap.put("3", MonthMap.get("3") + 1);break;}
            case(4):{MonthMap.put("4", MonthMap.get("4") + 1);break;}
            case(5):{MonthMap.put("5", MonthMap.get("5") + 1);break;}
            case(6):{MonthMap.put("6", MonthMap.get("6") + 1);break;}
            case(7):{MonthMap.put("7", MonthMap.get("7") + 1);break;}
            case(8):{MonthMap.put("8", MonthMap.get("8") + 1);break;}
            case(9):{MonthMap.put("9", MonthMap.get("9") + 1);break;}
            case(10):{MonthMap.put("10", MonthMap.get("10") + 1);break;}
            case(11):{MonthMap.put("11", MonthMap.get("11") + 1);break;}
            case(12):{MonthMap.put("12", MonthMap.get("12") + 1);break;}
            case(13):{MonthMap.put("13", MonthMap.get("13") + 1);break;}
            case(14):{MonthMap.put("14", MonthMap.get("14") + 1);break;}
            case(15):{MonthMap.put("15", MonthMap.get("15") + 1);break;}
            case(16):{MonthMap.put("16", MonthMap.get("16") + 1);break;}
            case(17):{MonthMap.put("17", MonthMap.get("17") + 1);break;}
            case(18):{MonthMap.put("18", MonthMap.get("18") + 1);break;}
            case(19):{MonthMap.put("19", MonthMap.get("19") + 1);break;}
            case(20):{MonthMap.put("20", MonthMap.get("20") + 1);break;}
            case(21):{MonthMap.put("21", MonthMap.get("21") + 1);break;}
            case(22):{MonthMap.put("22", MonthMap.get("22") + 1);break;}
            case(23):{MonthMap.put("23", MonthMap.get("23") + 1);break;}
            case(24):{MonthMap.put("24", MonthMap.get("24") + 1);break;}
            case(25):{MonthMap.put("25", MonthMap.get("25") + 1);break;}
            case(26):{MonthMap.put("26", MonthMap.get("26") + 1);break;}
            case(27):{MonthMap.put("27", MonthMap.get("27") + 1);break;}
            case(28):{MonthMap.put("28", MonthMap.get("28") + 1);break;}
            case(29):{MonthMap.put("29", MonthMap.get("29") + 1);break;}
            case(30):{MonthMap.put("30", MonthMap.get("30") + 1);break;}
            case(31):{MonthMap.put("31", MonthMap.get("31") + 1);break;}

        }
            month.articlePerDay = MonthMap;
            return month;
    }

    private void instanciateReleaseMonth() {
        //Initialisiert die Variable releaseMonth mit einen Satz Keys

        this.releaseMonth.put("Januar", new Month("Januar"));
        this.releaseMonth.put("Februar", new Month("Februar"));
        this.releaseMonth.put("März", new Month("März"));
        this.releaseMonth.put("April", new Month("April"));
        this.releaseMonth.put("Mai", new Month("Mai"));
        this.releaseMonth.put("Juni", new Month("Juni"));
        this.releaseMonth.put("Juli", new Month("Juli"));
        this.releaseMonth.put("August", new Month("August"));
        this.releaseMonth.put("September", new Month("September"));
        this.releaseMonth.put("Oktober", new Month("Oktober"));
        this.releaseMonth.put("November", new Month("November"));
        this.releaseMonth.put("Dezember", new Month("Dezember"));
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
    
    private void generateKeyWordRelation(String[] keywords, String category){
        //Erstellt eine HashMap in der die Anzahl der Keywords gezählt werde, die mit dem Keyword x gesetzt wurden
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

    private void generateKeywordOrigin(String[] keywords, String category){
        //erstellt eine Hashmap mit den Keywords und einer Liste mit den Ursprungskategorien der Artikel

        for(String singleKeyword: keywords){

            HashMap<String, Integer> tempHashMap;
            if (keywordOrigin.containsKey(singleKeyword)){
                tempHashMap = keywordOrigin.get(singleKeyword);
            }else{
                tempHashMap = new HashMap<String, Integer>();
            }

            if(tempHashMap.containsKey(category)){
                tempHashMap.put(category, tempHashMap.get(category)+1);
            }else{
                tempHashMap.put(category, 1);
            }
            keywordOrigin.put(singleKeyword, tempHashMap);

        }

    }
}