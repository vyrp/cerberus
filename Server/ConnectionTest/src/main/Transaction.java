package main;

import java.util.ArrayList;
import java.util.Date;

public class Transaction {
    // Fields
    private String start;
    private String name;
    private String end;
    
    // Constructor
    public Transaction(String start, String name, String end) {
        this.start = start;
        this.name = name;
        this.end = end;
    }
    
    // Getter and Setters
    public String getStart() {
        return start;
    }
    public String getName() {
        return name;
    }
    public String getEnd() {
        return end;
    }

    public static ArrayList<Transaction> fromString(String str) {
        ArrayList<Transaction> result = new ArrayList<Transaction>();
        
        String[] items = str.split("\\|");
        for(String item : items){
            String[] fields = item.split(";");
            if(fields.length != 3)
                continue;
            
            Long start = null;
            Long end = null;
            
            try {
                start = Long.parseLong(fields[0]);
            } catch(NumberFormatException e){}
            
            try {
                end = Long.parseLong(fields[2]);
            } catch(NumberFormatException e){}
            
            result.add(new Transaction(toDate(start), fields[1], toDate(end)));
        }
        
        return result;
    }
    
    private static String toDate(Long timestamp){
        return (timestamp == null || timestamp < 0) ? "-" : new Date(timestamp).toString();
    }
}
