import java.util.ArrayList;
import java.util.Collections;

public class cards {
    private int cardNum;
    private String cardPrint;
    private String cardType;
    private static final String[] types = {"♣","♦","♥","♠"};

    public cards(int cardNum, String cardPrint, String cardType) {
        this.cardNum = cardNum;
        this.cardPrint = cardPrint;
        this.cardType = cardType;
    }

    public int getCardNum() {
        return cardNum;
    }

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardPrint() {
        return cardPrint;
    }

    public void setCardPrint(String cardPrint) {
        this.cardPrint = cardPrint;
    }

    @Override
    public String toString() {
        return "[" + cardPrint + "'" + cardType + "']";
    }


    public static void printAll(ArrayList<cards> hand){
        for (cards card : hand){
            System.out.print(card.toString());
        }
        System.out.println();
    }

    public static void printX(ArrayList<cards> hand, int x){
        for (int i = 0; i <= (x-1); i++){
            cards card = hand.get(i);
            System.out.print(card.toString());
        }
        System.out.println();
    }

    public static ArrayList<cards> createDeck(){
        ArrayList<cards> deck = new ArrayList<>();
        for (int i = 1; i <= 13; i++){
            for (int j = 0; j <= 3; j++) {
                if (i == 1){
                    cards card = new cards(i, "A", types[j]);
                    deck.add(card);
                } else if (i == 11) {
                    cards card = new cards(10, "J", types[j]);
                    deck.add(card);
                } else if (i == 12) {
                    cards card = new cards(10, "Q", types[j]);
                    deck.add(card);
                } else if (i == 13) {
                    cards card = new cards(10, "K", types[j]);
                    deck.add(card);
                } else {
                    cards card = new cards(i, String.valueOf(i), types[j]);
                    deck.add(card);
                }
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    public static cards deal(ArrayList<cards> deck){
        cards temp = deck.get(0);
        deck.remove(0);
        return temp;
    }

    public static ArrayList<cards> repeatDeal(ArrayList<cards> deck, ArrayList<cards> hand, int repeats){
        for (int i = 0; i < repeats; i++){
            hand.add(cards.deal(deck));
        }
        return hand;
    }

    public static int calculateSum(ArrayList<cards> deck){
        int sum = 0;
        for (cards card : deck){
            sum += card.getCardNum();
        }
        return sum;
    }
}
