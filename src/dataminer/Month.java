package src.dataminer;

import java.util.HashMap;

public class Month {
 
    String name;
    HashMap<String, Integer> articlePerDay;

    public Month(String name){
        this.name = name;
        HashMap<String, Integer> articlePerDay = new HashMap<String, Integer>();
        articlePerDay.put("1", 0);
        articlePerDay.put("2", 0);
        articlePerDay.put("3", 0);
        articlePerDay.put("4", 0);
        articlePerDay.put("5", 0);
        articlePerDay.put("6", 0);
        articlePerDay.put("7", 0);
        articlePerDay.put("8", 0);
        articlePerDay.put("9", 0);
        articlePerDay.put("10", 0);
        articlePerDay.put("11", 0);
        articlePerDay.put("12", 0);
        articlePerDay.put("13", 0);
        articlePerDay.put("14", 0);
        articlePerDay.put("15", 0);
        articlePerDay.put("16", 0);
        articlePerDay.put("17", 0);
        articlePerDay.put("18", 0);
        articlePerDay.put("19", 0);
        articlePerDay.put("20", 0);
        articlePerDay.put("21", 0);
        articlePerDay.put("22", 0);
        articlePerDay.put("23", 0);
        articlePerDay.put("24", 0);
        articlePerDay.put("25", 0);
        articlePerDay.put("26", 0);
        articlePerDay.put("27", 0);
        articlePerDay.put("28", 0);
        articlePerDay.put("29", 0);
        articlePerDay.put("30", 0);
        articlePerDay.put("31", 0);
        this.articlePerDay = articlePerDay;
    }

}