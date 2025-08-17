import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class userpage {
    public static void page(customers customer, Statement statement) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Total balance: " + customer.getBalance());
            System.out.println("1.Blackjack(one deck) 2.uno 3.deposit money 4.exit");
            int answer = scanner.nextInt();
            if (answer == 1) {
                blackjack.blackjack1deck(customer, statement);
                continue;
            }else if (answer == 2){
                uno.unoGame(customer,statement);
            }else if (answer == 3){
                while(true){
                    System.out.println("How much do you want to deposit?(Type 0 to abort)");
                    double answer2 = scanner.nextDouble();
                    if (answer2 < 0){
                        System.out.println("Insufficient amount.");
                        continue;
                    } else if (answer2 == 0) {
                        break;
                    }else {
                        customer.updateBalance(statement, answer2);
                        break;
                    }
                }
            }else if (answer == 4){
                return;
            }else {
                System.out.println("Wrong input.");
            }
        }
    }
}
