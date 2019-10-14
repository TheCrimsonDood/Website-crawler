package src.dataminer;

import java.io.*;

public class dataminer {
        
    public static void main (String[] args0){
        
        File path = new File("src/data/");
        File[] parties = path.listFiles();
        FileFilter fileFilter = new FileFilter(){
            public boolean accept (File file){
                return file.isDirectory();
            }
        };
        parties = path.listFiles(fileFilter);

        if (parties.length == 0){
            System.out.println("No directory found.");
        } else{
            for (int i = 0; i<parties.length; i++ ){
                File partyname = parties[i];
                System.out.println(partyname.toString()+" found.");
            }
            for (int j = 0; j<parties.length; j++){
                //new Party
            }
        }
    }
}