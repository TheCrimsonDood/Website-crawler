package src.dataminer;

import java.io.*;
import java.text.ParseException;

public class Dataminer {

    public static void main(String[] args0) throws IOException, ParseException {
        
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
                File partynameFile = parties[i];
                String partyname = partynameFile.toString();
                partyname = partyname.substring(9);
                System.out.println(partyname+" found.");                    //prints all found party names
                //Party party = new Party(partyname, "src/data/");
                System.out.println("Completed mining for "+partyname);
            }
        }
    }

//     public static void main(String[] args0) throws IOException, ParseException {

//         String path = "src/data/";
//         Party party = new Party("Piratenpartei", path);

//         System.out.println("stop");

//     }
// }