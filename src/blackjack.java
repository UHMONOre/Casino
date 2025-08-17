import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class blackjack {
    public static void blackjack1deck(customers customer, Statement statement) throws SQLException {
        ArrayList<cards> deck = new ArrayList<>();
        deck = cards.createDeck();
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("How much do you want to bet? Max betting per hand is 10000 $(To exit type 0)");
            double bet = scanner.nextDouble();
            if (bet > 10000 || bet < 0){
                System.out.println("Bet out of range");
                continue;
            } else if (bet == 0) {
                return;
            }else {
                boolean flag = customer.checkBalance(bet,statement);
                if (!flag){
                    continue;
                }
            }

            boolean aDealer, aUser;
            boolean dealerBj = false, userBj = false, burned = false;
            ArrayList<cards> dealerHand = new ArrayList<>();
            ArrayList<cards> userHand = new ArrayList<>();
            int sumDealer,sumUser;
            userHand.add(cards.deal(deck));
            dealerHand.add(cards.deal(deck));
            dealerHand.add(cards.deal(deck));
            userHand.add(cards.deal(deck));
            aDealer = aCheck(dealerHand);
            aUser = aCheck(userHand);
            System.out.println("Dealer:");
            sumDealer = blackjack.calculateSumBJ(dealerHand, aDealer, true);
            System.out.println("User");
            sumUser = blackjack.calculateSumBJ(userHand, aUser, false);

            while (true){
                if (aUser && ((sumUser + 10) == 21)){
                    break;
                }else if (sumUser == 21){
                    break;
                }else if (sumUser > 21){
                    burned = true;
                    break;
                }

                System.out.println("1.push 2.stay");
                int answer = scanner.nextInt();
                if (answer == 1){
                    System.out.println("User:");
                    userHand.add(cards.deal(deck));
                    aUser = aCheck(userHand);
                    sumUser = blackjack.calculateSumBJ(userHand, aUser, false);
                }else if (answer == 2){
                    break;
                }else {
                    System.out.println("Wrong input.");
                }
            }

            boolean flagRepeat = true;
            while (true){
                suspense(1000);
                if (aDealer && ((sumDealer + 10) <= 21)){
                    if ((sumDealer + 10) >= 17){
                        if (flagRepeat){
                            System.out.println("Dealer:");
                            sumDealer = blackjack.calculateSumBJ(dealerHand, aDealer, false);
                        }
                        break;
                    }
                }else {
                    if (sumDealer >= 17){
                        if (flagRepeat){
                            System.out.println("Dealer:");
                            sumDealer = blackjack.calculateSumBJ(dealerHand, aDealer, false);
                        }
                        break;
                    }
                }

                flagRepeat = false;
                System.out.println("Dealer:");
                dealerHand.add(cards.deal(deck));
                aDealer = aCheck(dealerHand);
                sumDealer = blackjack.calculateSumBJ(dealerHand, aDealer, false);
            }

            if ((sumDealer == 21) || ((sumDealer + 10) == 21)){
                if (dealerHand.size() == 2){
                    dealerBj = true;
                }
            }

            if ((sumUser == 21) && userHand.size() == 2){
                userBj = true;
            }else if (aUser && ((sumUser + 10) == 21)){
                if (userHand.size() == 2){
                    userBj = true;
                }
            }

            if (aDealer && ((sumDealer + 10) <= 21)){
                sumDealer += 10;
            }

            if (aUser && ((sumUser + 10) <= 21)){
                sumDealer += 10;
            }

            if (dealerBj && userBj){
                customer.updateBalance(statement, bet);
            }else if (dealerBj){
                customer.updateTotal_Profit(statement, -bet);
            }else if (userBj){
                customer.updateBalance(statement, (bet * 2.5));
                customer.updateTotal_Profit(statement, (bet * 2.5));
            }else {
                if (burned){
                    customer.updateTotal_Profit(statement, -bet);
                }else if (sumDealer > 21){
                    customer.updateBalance(statement, (bet * 2));
                    customer.updateTotal_Profit(statement, (bet * 2));
                }else if (sumUser == sumDealer){
                    customer.updateBalance(statement, bet);
                }else if (sumUser > sumDealer){
                    customer.updateBalance(statement, (bet * 2));
                    customer.updateTotal_Profit(statement, (bet * 2));
                }else if (sumDealer > sumUser){
                    customer.updateTotal_Profit(statement, -bet);
                }
            }
            System.out.println("Total balance: " + customer.getBalance());
        }
    }

    public static boolean aCheck(ArrayList<cards> hand){
        for (cards card : hand){
            if (card.getCardNum() == 1){
                return true;
            }
        }
        return false;
    }

    public static int calculateSumBJ(ArrayList<cards> hand, boolean aCheck, boolean startDealer){
        int tempSum;
        if (aCheck){
            tempSum = cards.calculateSum(hand);
            if (startDealer){
                cards.printX(hand,1);
            }else {
                cards.printAll(hand);
                if ((tempSum + 10) <= 21){
                    System.out.println("Total: " + tempSum + "/" + (tempSum + 10));
                    return tempSum + 10;
                }else {
                    System.out.println("Total: " + tempSum);
                }
            }
            return tempSum;
        }else {
            tempSum = cards.calculateSum(hand);
            if (startDealer){
                cards.printX(hand, 1);
            }else {
                cards.printAll(hand);
                System.out.println("Total: " + tempSum);
            }
            return tempSum;
        }
    }

    public static void suspense(int m){
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
