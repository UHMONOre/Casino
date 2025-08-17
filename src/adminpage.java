import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class adminpage {
    private static Scanner scanner = new Scanner(System.in);
    public static void page(customers admin, HashMap<String,customers> customersDB) {
        while (true){
            System.out.println("1.Print the database 2.exit");
            int answer = scanner.nextInt();
            if (answer == 1){ //extra methods and id on order
                for (customers value : customersDB.values()){
                    System.out.println(value.toString());
                }
            }else if (answer == 2){
                System.out.println("Goodbye sir Admin.");
                return;
            }else {
                System.out.println("Wrong input. Please choose one of the options.");
            }
        }
    }
}
