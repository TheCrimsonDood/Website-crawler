package src.dataminer;

import java.io.IOException;
import java.text.ParseException;
import src.dataminer.Dataminer;;

public class main {

    public static void main(String[] args) throws IOException, ParseException {

        try {
            new Dataminer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}