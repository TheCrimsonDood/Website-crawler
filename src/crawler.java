package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class crawler {

    //gibt den SourceCode der besuchten seite zurück
    public String getLinksOnPage(BufferedReader br) {
        String line =null;
        HashSet<String> allLinks = new HashSet<String>(); //HashSet mit jedem gefundenen Link im SourceCode
        try {
            while ((line = br.readLine()) != null) {
                if(line.contains("<div class=\"search-teaser\">")){
                    String subString = line;
                    subString = subString.substring(subString.indexOf("watch?v=") - 1, subString.indexOf("watch?v=") +19);
                    allLinks.add(subString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0 ; i < allLinks.size(); i++) {
            System.out.println(allLinks.get(i));
        }
    }

    public static BufferedReader createBR(URL url) {

        //lädt den Source Code von der Übergebenen URL
        BufferedReader br = null;
        InputStreamReader isr = null;

        try {
            isr = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        br = new BufferedReader(isr);

        return br;
        
    }


}