import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class customers {
    private int customer_id;
    private double balance,total_profit;
    private String first_name,last_name,email,password;
    private static int tempid = 0;
    private static Scanner scanner = new Scanner(System.in);

    public customers(){
        this.balance = 0;
        this.total_profit = 0;
        this.customer_id = ++tempid;
    }

    public customers(int customer_id, double balance, String first_name, String last_name, String email, String password) {
        this.customer_id = customer_id;
        this.balance = balance;
        this.total_profit = 0;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public double getBalance() {
        return balance;
    }

    public double getTotal_profit() {
        return total_profit;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setTotal_profit(double total_winnings) {
        this.total_profit = total_winnings;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //the rest from the create account
    public boolean setNewEmail(HashMap<String,customers> customersDB,customers customer){
        outerLoop:
        while (true){
            System.out.println("Please insert your email.Type exit to abort:");
            String email = scanner.nextLine();
            if (email.equals("exit")){
                return false;
            }
            for (customers value : customersDB.values()) {
                if (value.getEmail().equals(email)) {
                    System.out.println("This email is already connected to a account.");
                    continue outerLoop;
                }
            }
            customer.setEmail(email);
            return true;
        }

    }

    public boolean checkBalance(double bet,Statement statement) throws SQLException {
        double tempBalance = this.getBalance() - bet;
        if (tempBalance < 0){
            System.out.println("Your balance is not enough");
            return false;
        }else {
            System.out.println("Bet successful");
            this.updateBalance(statement,-bet);
            return true;
        }
    }

    public void updateBalance (Statement statement, double money) throws SQLException {
        this.balance += money;
        statement.executeUpdate("update customers set balance = " + this.balance + " where customer_id = " + this.customer_id);
    }

    public void updateTotal_Profit (Statement statement, double bet) throws SQLException {
        this.total_profit += bet;
        statement.executeUpdate("update customers set total_profit = " + this.total_profit + " where customer_id = " + this.customer_id);
    }

    @Override
    public String toString() {
        return "customers{" +
                "customer_id=" + customer_id +
                ", balance=" + balance +
                ", total_profit=" + total_profit +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static HashMap<String,customers> createMap(HashMap<String,customers> customersdb, Statement statement){
        try {
            ResultSet resultSet = statement.executeQuery("select * from customers");

            while (resultSet.next()){
                customers customer = new customers();
                Integer customer_id = resultSet.getInt("customer_id");
                tempid = customer_id;
                customer.setCustomer_id(customer_id);
                customer.setBalance(resultSet.getDouble("balance"));
                customer.setFirst_name(resultSet.getString("first_name"));
                customer.setLast_name(resultSet.getString("last_name"));
                customer.setTotal_profit(resultSet.getDouble("total_profit"));
                String email = resultSet.getString("email");
                customer.setEmail(email);
                customer.setPassword(resultSet.getString("password"));

                customersdb.put(email,customer);
            }
            return customersdb;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("The map was not created");
        return customersdb;
    }

    public static HashMap<String,customers> createAccount (HashMap<String,customers> customersDB,Statement statement){
        customers customer = new customers();
        boolean flag = customer.setNewEmail(customersDB,customer);
        if (flag){
            System.out.println("Please insert your password:");
            customer.setPassword(scanner.nextLine());
            System.out.println("Please insert your first name");
            customer.setFirst_name(scanner.nextLine());
            System.out.println("Please insert your last name");
            customer.setLast_name(scanner.nextLine());

            try {
                customersDB.put(customer.getEmail(),customer);
                String sql = "INSERT INTO customers (customer_id, balance, total_profit, first_name, last_name, email, password) " +
                        "VALUES (" + customer.getCustomer_id() + ", " +
                        customer.getBalance() + ", " +
                        customer.getTotal_profit() + ", '" +
                        customer.getFirst_name() + "', '" +
                        customer.getLast_name() + "', '" +
                        customer.getEmail() + "', '" +
                        customer.getPassword() + "')";
                statement.executeUpdate(sql);
                return customersDB;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            return customersDB;
        }

    }

    public static void login(HashMap<String,customers> customersDB, Statement statement) throws SQLException {
        while (true){
            System.out.println("Please insert your email or type exit to abort:");
            String email = scanner.nextLine();

            if (email.equals("exit")){
                return;
            }

            if (email.equals("admin") || email.equals("admin@gmail,com")){
                while (true){
                    System.out.println("Please insert your password or type exit to abort:");
                    String password = scanner.nextLine();
                    for (customers value : customersDB.values()){
                        if (password.equals(value.getPassword())){
                            System.out.println("Welcome Sir Admin!");
                            adminpage.page(value,customersDB);
                            return;
                        }else if (password.equals("exit")){
                            return;
                        }
                    }
                    System.out.println("The password was incorrect.");
                }
            }else {
                for (customers tempvalue : customersDB.values()){
                    if (tempvalue.getEmail().equals(email)){
                        while (true){
                            System.out.println("Please insert your password or type exit to abort:");
                            String password = scanner.nextLine();
                            for (customers value : customersDB.values()){
                                if (password.equals(value.getPassword())){
                                    System.out.println("Welcome, login was successful.");
                                    userpage.page(value,statement);
                                    return;
                                }else if (password.equals("exit")){
                                    return;
                                }
                            }
                            System.out.println("The password was incorrect.");
                        }
                    }
                }
                System.out.println("Your email is not connected to an account.");
            }
        }
    }
}
