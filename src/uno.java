import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class uno {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<cards> deck = new ArrayList<>();
    static ArrayList<cards> floor = new ArrayList<>();
    static ArrayList<cards> userHand = new ArrayList<>();
    static ArrayList<cards> botHand1 = new ArrayList<>();
    static ArrayList<cards> botHand2 = new ArrayList<>();
    static ArrayList<cards> botHand3 = new ArrayList<>();
    static int roundNum,flag9,flag7,preFlag7,flag8;
    public static void unoGame(customers customer, Statement statement) throws SQLException {


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

            clearField();
            Random random = new Random();
            roundNum = random.nextInt(4);
            while (true){
                System.out.println("Turn " + roundNum);
                checkDeck();
                if (roundNum == 0){
                    userGameplay(false);
                }else {
                    printTable();
                    botGameplay();
                }

                check8();

                if (checkWin()){
                    if (roundNum == 0){
                        customer.updateBalance(statement, (bet * 4));
                        customer.updateTotal_Profit(statement, (bet * 4));
                    }
                    else {
                        customer.updateTotal_Profit(statement, -bet);
                    }
                    break;
                }else {
                    nextRound();
                }
            }
        }
    }

    public static void printTable(){
        System.out.println(floor.getLast());
        System.out.println("Bot 1: " + botHand1.size());
        System.out.println("Bot 2: " + botHand2.size());
        System.out.println("Bot 3: " + botHand3.size());
        cards.printAll(userHand);
//        cards.printAll(botHand1);
//        cards.printAll(botHand2);
//        cards.printAll(botHand3);
    }

    public static void nextRound(){
        boolean flag = floor.getLast().getCardPrint().equals("9");
        if (flag && flag9 != floor.size()){
            System.out.println("Turn skipped");
            flag9 = floor.size();
            if (roundNum == 2){
                roundNum = 0;
            }else if (roundNum == 3){
                roundNum = 1;
            }else {
                roundNum += 2;
            }
        }else {
            if (roundNum == 3){
                roundNum = 0;
            }else {
                roundNum ++;
            }
        }
    }

    public static void Agameplay(int card){
        while (true){
            System.out.println("1.♣ 2.♦ 3.♥ 4.♠");
            int answer = scanner.nextInt();
            if (answer == 1){
                userHand.get(card-1).setCardType("♣");
            }else if (answer == 2){
                userHand.get(card-1).setCardType("♦");
            }else if (answer == 3){
                userHand.get(card-1).setCardType("♥");
            }else if (answer == 4){
                userHand.get(card-1).setCardType("♠");
            }else {
                System.out.println("Wrong input.");
                continue;
            }
            floor.add(userHand.get(card-1));
            userHand.remove(card - 1);
            break;
        }
    }

    public static void check8(){
        if (floor.getLast().getCardPrint().equals("8") && (floor.size() != flag8)){
            flag8 = floor.size();
            if (roundNum == 0){
                userGameplay(false);
            }else {
                printTable();
                botGameplay();
            }
            check8();
        }
    }

    public static boolean check7(){
        if (floor.getLast().getCardPrint().equals("7") && (flag7 != floor.size())){
            if (flag7 != -1){
                preFlag7 = flag7;
            }
            flag7 = floor.size();
            return true;
        }
        return false;
    }

    public static void AbotGameplay(ArrayList<cards> botHand, Integer tempA){
        ArrayList<Integer> num = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            num.add(0);
        }

        for (cards card : botHand){
            if (card.getCardType().equals("♣")){
                num.set(0, num.get(0) + 1);
            }else if (card.getCardType().equals("♦")){
                num.set(1, num.get(1) + 1);
            }else if (card.getCardType().equals("♥")){
                num.set(2, num.get(2) + 1);
            }else if (card.getCardType().equals("♠")){
                num.set(3, num.get(3) + 1);
            }
        }

        Integer tempMax = Collections.max(num);
        int tempType = num.indexOf(tempMax);
        int Acard  = tempA;

        if (tempType == 0){
            botHand.get(Acard).setCardType("♣");
            floor.add(botHand.get(Acard));
            botHand.remove(Acard);
        }else if (tempType == 1){
            botHand.get(Acard).setCardType("♦");
            floor.add(botHand.get(Acard));
            botHand.remove(Acard);
        }else if (tempType == 2){
            botHand.get(Acard).setCardType("♥");
            floor.add(botHand.get(Acard));
            botHand.remove(Acard);
        }else if (tempType == 3){
            botHand.get(Acard).setCardType("♥");
            floor.add(botHand.get(Acard));
            botHand.remove(Acard);
        }
    }

    public static void setUpBot(ArrayList<cards> botHand){
        if (roundNum == 1){
            botHand1.clear();
            botHand1.addAll(botHand);
        }else if (roundNum == 2){
            botHand2.clear();
            botHand2.addAll(botHand);
        }else if (roundNum == 3){
            botHand3.clear();
            botHand3.addAll(botHand);
        }else {
            System.out.println("Error, roundNum out of range");
        }
    }

    public static void userGameplay(boolean draw){
        blackjack.suspense(3000);
        printTable();
        System.out.println("It's your turn.");
        ArrayList<Integer> playableCards = new ArrayList<>();
        cards downCard =  floor.getLast();

        for (int i = 0; i < userHand.size(); i++){
            cards card = userHand.get(i);
            if ( (card.getCardPrint().equals(downCard.getCardPrint()))  || (card.getCardType().equals(downCard.getCardType())) || (card.getCardPrint().equals("A")) ){
                playableCards.add(i + 1);
            }
        }

        if (check7()){
            outerloop:
            for (int nums : playableCards){
                if (userHand.get(nums-1).getCardPrint().equals("7")){
                    while (true){
                        System.out.println("Do you want to play the 7?(1.Yes, 2.No)");
                        int answer = scanner.nextInt();
                        if (answer == 1){
                            floor.add(userHand.get(nums-1));
                            userHand.remove(nums-1);
                            return;
                        }else if (answer == 2){
                            break outerloop;
                        }else {
                            System.out.println("Wrong input");
                        }
                    }
                }
            }
            int tempnum = 0;
            for (int i = (floor.size() - 1); i >= 0; i--){
                if (floor.get(i).getCardPrint().equals("7")){
                    userHand.add(cards.deal(deck));
                    userHand.add(cards.deal(deck));
                    tempnum += 2;
                }else {
                    break;
                }
            }
            System.out.println("You have lost your turn and drawn " + tempnum + " cards");
            return;
        }

        if (playableCards.isEmpty() && draw){
            System.out.println("No playable cards");
            blackjack.suspense(3000);
            return;
        }else if (playableCards.isEmpty()){
            userHand.add(cards.deal(deck));
            userGameplay(true);
            return;
        }

        while (true){
            if (!draw){
                System.out.println("Type the number of your card by order or type 0 to draw.");
                int answer = scanner.nextInt();
                if (answer == 0){
                    userHand.add(cards.deal(deck));
                    userGameplay(true);
                    return;
                }else {
                    if (answer < 1 || answer > userHand.size()){
                        System.out.println("out of range.");
                    }else {
                        for (int num : playableCards){
                            if (num == answer){
                                if (userHand.get(answer-1).getCardPrint().equals("A")){
                                    uno.Agameplay(answer);
                                    return;
                                }else {
                                    floor.add(userHand.get(answer-1));
                                    userHand.remove(answer-1);
                                    return;
                                }
                            }
                        }
                        System.out.println("Wrong card.");
                    }
                }
            }else {
                System.out.println("Type the number of your card by order.");
                int answer = scanner.nextInt();
                if (answer < 1 || answer > userHand.size()){
                    System.out.println("out of range.");
                }else {
                    for (int num : playableCards){
                        if (num == answer){
                            if (userHand.get(answer-1).getCardPrint().equals("A")){
                                uno.Agameplay(answer);
                                return;
                            }else {
                                floor.add(userHand.get(answer-1));
                                userHand.remove(answer-1);
                                return;
                            }
                        }
                    }
                    System.out.println("Wrong card.");
                }
            }
        }
    }

    public static void botGameplay(){
        blackjack.suspense(3000);
        ArrayList<cards> botHand = new ArrayList<>();

        if (roundNum == 1){
            botHand = new ArrayList<>(botHand1);
        }else if (roundNum == 2){
            botHand = new ArrayList<>(botHand2);
        }else if (roundNum == 3){
            botHand = new ArrayList<>(botHand3);
        }

        ArrayList<Integer> playableCards = new ArrayList<>();
        availableCardsBot(botHand, playableCards);

        if (check7()){
            for (int nums : playableCards){
                if (botHand.get(nums).getCardPrint().equals("7")){
                    floor.add(botHand.get(nums));
                    botHand.remove(nums);
                    setUpBot(botHand);
                    return;
                }
            }
            for (int i = (floor.size() - 1); i >= 0; i--){
                if (floor.get(i).getCardPrint().equals("7") && ((i + 1) != preFlag7)){
                    botHand.add(cards.deal(deck));
                    botHand.add(cards.deal(deck));
                }else {
                    break;
                }
            }
            setUpBot(botHand);
            return;
        }

        if (playableCards.isEmpty()) {
            botHand.add(cards.deal(deck));
            availableCardsBot(botHand, playableCards);
        }

        if (playableCards.isEmpty()){
            setUpBot(botHand);
            return;
        }else if (botHand.get(playableCards.getFirst()).getCardPrint().equals("A")){
            AbotGameplay(botHand, playableCards.getFirst());
        }else {
            floor.add(botHand.get(playableCards.getFirst()));
            int num = playableCards.getFirst();
            botHand.remove(num);
        }
        setUpBot(botHand);
    }

    public static void availableCardsBot(ArrayList<cards> botHand, ArrayList<Integer> playableCards){
        cards downCard =  floor.getLast();
        for (int i = 0; i < botHand.size(); i++){
            cards card = botHand.get(i);
            if ( (card.getCardPrint().equals(downCard.getCardPrint()))  || (card.getCardType().equals(downCard.getCardType())) || (card.getCardPrint().equals("A")) ){
                playableCards.add(i);
            }
        }
    }

    public static void clearField(){
        botHand1.clear();
        botHand2.clear();
        botHand3.clear();
        userHand.clear();
        floor.clear();
        flag9 = -1;
        flag8 = -1;
        flag7 = -1;
        preFlag7 = -1;
        deck = cards.createDeck();
        cards.repeatDeal(deck, userHand, 7);
        cards.repeatDeal(deck, botHand1, 7);
        cards.repeatDeal(deck,botHand2, 7);
        cards.repeatDeal(deck, botHand3, 7);
        floor.add(cards.deal(deck));
    }

    public static void checkDeck(){
        if (deck.size() < 3){
            ArrayList<cards> tempDeck = new ArrayList<>(floor);
            tempDeck.removeLast();
            Collections.shuffle(tempDeck);
            deck.addAll(tempDeck);
            floor.subList(0, floor.size() - 1).clear();
        }
    }

    public static boolean checkWin(){
        if (userHand.isEmpty()){
            System.out.println("You won");
            return true;
        }else if (botHand1.isEmpty()){
            System.out.println("Bot 1 won");
            return true;
        }else if (botHand2.isEmpty()){
            System.out.println("Bot 2 won");
            return true;
        }else if (botHand3.isEmpty()){
            System.out.println("Bot 3 won");
            return true;
        }else {
            return false;
        }
    }
}
