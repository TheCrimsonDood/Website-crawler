package src.dataminer;

import java.io.*;
import java.text.ParseException;
import java.util.HashMap;

public class Dataminer {

    Dataminer() throws IOException, ParseException {
        
        File path = new File("src/data/");                                  //sets search path for party directories
        File[] parties = path.listFiles();
        FileFilter fileFilter = new FileFilter(){
            public boolean accept (File file){
                return file.isDirectory();                                  //filter for only directories
            }
        };
        parties = path.listFiles(fileFilter);

        if (parties.length == 0){
            System.out.println("No directory found.");          
        } else{
            for (int i = 0; i<parties.length; i++ ){

                int partyArticleCount = 0; // Anzahl der Artikel
                HashMap<String, Integer> partyCategorys = new HashMap<String, Integer>(); // Auflistung der Kategorien in denen die
                                                                                    // Artikel veröffentlicht wurden mit Anzahl
                int partyBeforeEU = 0;// Anzahl der Artikel, die vor der Eu-Wahl herausgebracht wurden
                int partyAfterEU = 0; // ANzahl der Artikel, die nach der EU-Wahl herausgebracht wurden
                int partyWhileEU = 0; // Anzahl der Artikel, die während der EU-Wahl veröffentlicht wurden
                int partyAverageArticleLength = 0;
                HashMap<String, Integer> articleReleaseMonth = new HashMap<String, Integer>(); // Zählt die Anzahl der Artikel pro Monat
                HashMap<String, Integer> articleKeywordCount = new HashMap<String, Integer>(); //Zählt die Verwendeten Tags/Keywords diefür die Artikel gesetzt wurden
                HashMap<String, Integer> articleAuthorCount = new HashMap<String, Integer>();//Zählt die Anzahlder Artikel pro Autor
                HashMap<String, Integer> articleExactWordCount = new HashMap<String, Integer>();//Zählt die in Artikeln verwendeten Wörter
                HashMap<String, HashMap<String,Integer>> allKeywordRelations = new HashMap<String, HashMap<String, Integer>>();

                File partynameFile = parties[i];
                String partyname = partynameFile.toString();
                partyname = partyname.substring(9);                         //extracts Party name from path
                
                System.out.println(partyname+" found.");                    //prints all found party names

                Party party = new Party(partyname, "src/data/");            //creates new Party object
                
                partyArticleCount = party.articleCount;
                partyCategorys = party.categorys;
                partyBeforeEU = party.beforeEU;
                partyAfterEU = party.afterEU;
                partyWhileEU = party.whileEU;
                partyAverageArticleLength = party.averageArticleLength;
                articleReleaseMonth = party.releaseMonth;
                articleKeywordCount = party.keywordCount;
                articleAuthorCount = party.authorCount;
                articleExactWordCount = party.exactWordCount;
                allKeywordRelations = party.allKeywordRelations;

                System.out.println(partyArticleCount);
                System.out.println(partyCategorys);
                System.out.println(partyBeforeEU);
                System.out.println(partyAfterEU);
                System.out.println(partyWhileEU);
                System.out.println(partyAverageArticleLength);
                System.out.println(articleReleaseMonth);
                System.out.println(articleKeywordCount);
                System.out.println(articleAuthorCount);
                System.out.println(articleExactWordCount);
                System.out.println(allKeywordRelations);

                System.out.println("Completed mining for "+partyname);
            }
        }
    }
}