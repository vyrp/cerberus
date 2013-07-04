package main;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

/**
 * @author Croata
 *
 * This class exemplifies the use of the {@link DataBaseManager}.
 */
public class Program {
    public static void main(String[] args) {
        DataBaseManager manager = new DataBaseManager("http://localhost:8080/", "Felipe");
        
        /* Choose one */
        
        //printAll(manager);
        //createTransaction(manager, "gama");
        //createDevice(manager, "Croata");
        update(manager);
    }
    
    private static void printAll(DataBaseManager manager){
        String result = null;
        
        try {
            result = manager.getAll();
        } catch(ConnectException e){
            System.out.println("\n" + e.getMessage());
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        if(result == null){
            System.out.println("Result is null.");
            return;
        }
        
        ArrayList<Transaction> transactions = Transaction.fromString(result);
        System.out.println("Transactions: ");
        for(Transaction transaction : transactions){
            System.out.println(transaction.getName() + ": " + transaction.getStart() + " => " + transaction.getEnd());
        }
    }
    
    private static void createDevice(DataBaseManager manager, String device){
        try {
            manager.createDevice(device);
        } catch(ConnectException e){
            System.out.println("\n" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Created.");
    }
    
    private static void createTransaction(DataBaseManager manager, String name){
        try {
            manager.put(name);
            System.out.println("Created.");
        } catch(ConnectException e){
            System.out.println("\n" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void update(DataBaseManager manager){
        String result = null;
        
        try {
            result = manager.update();
        } catch(ConnectException e){
            System.out.println("\n" + e.getMessage());
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        if(result == null){
            System.out.println("Result is null.");
            return;
        }
        
        System.out.println("Updated: " + result);
    }
}
