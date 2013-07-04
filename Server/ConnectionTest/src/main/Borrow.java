package main;

import java.util.ArrayList;

public class Borrow {
    // Fields
    private Long start;
    private String name;
    private Long end;
    
    // Constructor
    public Borrow(Long start, String name, Long end) {
        this.start = start;
        this.name = name;
        this.end = end;
    }
    
    // Getter and Setters
    public Long getStart() {
        return start;
    }
    public void setStart(Long start) {
        this.start = start;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getEnd() {
        return end;
    }
    public void setEnd(Long end) {
        this.end = end;
    }

    public static ArrayList<Borrow> fromString(String str) {
        ArrayList<Borrow> result = new ArrayList<Borrow>();
        
        System.out.println("str = " + str);
        String[] items = str.split(":");
        for(String item : items){
            System.out.println("  item = " + item);
            String[] fields = item.split(";");
            for(String field : fields)
                System.out.println("    field = " + field);
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
            
            result.add(new Borrow(start, fields[1], end));
        }
        
        return result;
    }
}
