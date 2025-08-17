import javax.swing.plaf.IconUIResource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/Casino";
        String username = "root";
        String password = "uhmono";

        Connection connection = null;

        Scanner scanner = new Scanner(System.in);

        HashMap<String,customers> customersDB = new HashMap<>();

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connected to the database!");

            Statement statement = connection.createStatement();

            customersDB = customers.createMap(customersDB,statement);

            while (true) {
                System.out.println("1.login 2.register 3.exit");
                int answer = scanner.nextInt();
                if (answer == 1){
                    customers.login(customersDB,statement);
                }else if (answer == 2){
                    customers.createAccount(customersDB,statement);
                }else if (answer == 3){
                    break;
                }else {
                    System.out.println("Wrong input. Please choose one of the options.");
                    continue;
                }
            }

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}