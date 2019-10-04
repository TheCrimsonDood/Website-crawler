package src;

import java.io.BufferedReader;
import java.net.URL;
import java.util.HashSet;

public class main {

    public static void main(String[] args0) {

        HashSet<String> wordList = links.getLinks();
        // String pathString = "C:\\Users\\The_Dood\\OneDrive - HFTL Trägergesellschaft MBH Hochschule für Telekommunikation Leipzig\\Projekte\\Github\\SpiegelCrawler\\data";
        String pathString = "C:\\data";

        for (String word : wordList) {


            String tempPathString = pathString + "\\" + word;
            directorys.createDirectory(tempPathString);

            URL link = links.transformWordToURL(word);
            // BufferedReader reader = crawler.createBR(link);

            System.out.println(link);
        }
    }
}