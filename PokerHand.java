import java.util.*;

public class PokerHand implements Problem<String> {
    private final boolean typeOutput = true;
    private Set<Card> cards;
    
    // initializes the pokerhand, takes in a hand of two cards
    // and a board of five or less cards or throws an IllegalArgumentException
    public PokerHand(List<String> hand, List<String> board) {
        if (hand.size() != 2) {
            throw new IllegalArgumentException("Hand must be two");
        } else if (board.size() > 5) {
            throw new IllegalArgumentException("Board cannot have more than five cards");
        }
        // converts the String to a Card class
        cards = new TreeSet<Card>();
        for (String card : hand) {
            cards.add(new Card(card));
        }
        for (String card : board) {
            cards.add(new Card(card));
        }
    }
    
    // check if any of the poker hands are met
    // and if typeOutput is true will print the specific hand
    public boolean isSuccess(List<String> soFar) {
        Hand hand = new Hand(cards, soFar);
        if (typeOutput) {
            typeOutput(hand);
        }
        int length = cards.size() + soFar.size();
        return (hand.isFour() ||
                hand.isFullHouse() ||
                hand.isFlush() ||
                hand.isThree() ||
                hand.isTwoPair() ||
                hand.isPair()) && 
                length == 7;
    }

    // returns true if there are less than 7 cards in play
    public boolean isPartial(List<String> soFar) {
        int length = cards.size() + soFar.size();
        return length < 7;
    }

    // returns a deck without the cards that are in play
    public Iterable<String> options() {
        ArrayList<String> deck = new ArrayList<>();
        for (char suits : List.of('c', 'd', 'h', 's')) {
            for (int ranks : List.of(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,14)) {
                Card card = new Card(ranks, suits);
                if (!cards.contains(card)) {
                    deck.add(card.toString());
                }
            }
        }
        return deck;
    }
    
    // outputs the type of poker hand give a hand class
    private void typeOutput(Hand hand) {
        if (hand.isFour()) {
            System.out.println("Four of a Kind!");
        } else if (hand.isFullHouse()) {
            System.out.println("Full House!");
        } else if (hand.isFlush()) {
            System.out.println("Flush!");
        } else if (hand.isThree()) {
            System.out.println("Three of a Kind!");
        } else if (hand.isTwoPair()) {
            System.out.println("Two Pair!");
        } else if (hand.isPair()) {
            System.out.println("Pair!");
        }
    }
    
    // a hand class represents up to seven cards
    // the methods tell what poker hands are in the hand
    // does not have a straight or straight flush method because of complexity
    private class Hand {
        private Set<Card> hand;
        private Map<String, Integer> rankOccurances;
        
        // creates a new hand from a set of cards
        // and the list of cards that could be played
        public Hand(Set<Card> cards, List<String> soFar) {
            hand = new TreeSet<>(cards);
            for (String card : soFar) {
                hand.add(new Card(card));
            }
            // makes a map of every occurance of ranks
            this.rankOccurances = new TreeMap<>();
            for (Card card : hand) {
                String rank = card.getStrRank();
                if (!rankOccurances.containsKey(rank)) {
                    rankOccurances.put(rank, 0);
                }
                rankOccurances.put(rank, rankOccurances.get(rank) + 1);
            }
        }
        
        // checks how many ranks are the same
        private boolean isSame(int rank) {
            for (String sameRank : rankOccurances.keySet()) {
                if (rank == rankOccurances.get(sameRank)) {
                    return true;
                }
            }
            return false;
        }
        
        // checks if the hand has a pair
        public boolean isPair() {
            return isSame(2);
        }
        
        // checks if the hand has two pair
        public boolean isTwoPair() {
            int numOfPair = 0;
            for (String sameRank : rankOccurances.keySet()) {
                if (rankOccurances.get(sameRank) == 2) {
                    numOfPair++;
                }
            }
            return numOfPair >= 2;
        }
        
        // checks if the hand has tree of a kind
        public boolean isThree() {
            return isSame(3);
        }
        
        // checks if the hand has a full house
        public boolean isFullHouse() {
            return isSame(2) && isSame(3);
        }
        
        // checks if the hand has four of a kind
        public boolean isFour() {
            return isSame(4);
        }
        
        // compares a set of cards to see if it has five or more cards of the sam suit
        private char isFlushType(Set<Card> temp) {
            // makes a map of every occurance of suits
            Map<Character, Integer> suitOccurances = new TreeMap<>();
            for (Card card : temp) {
                char suit = card.getSuit();
                if (!suitOccurances.containsKey(suit)) {
                    suitOccurances.put(suit, 0);
                }
                suitOccurances.put(suit, suitOccurances.get(suit) + 1);
            }
            // checks if that map has more than 5 of the same
            char result = 'n';
            for (char sameSuit : suitOccurances.keySet()) {
                if (suitOccurances.get(sameSuit) >= 5) {
                    result = sameSuit;
                }
            }
            return result;
        }
        
        // checks if the hand has a flush
        public boolean isFlush() {
            return isFlushType(hand) != 'n';
        }
    }
    
    // the Card interface is an easy way to deal with both the suit and rank
    private class Card implements Comparable<Card> {
        private final int AceValue = 14;
        private int rank;
        private char suit;
        
        // given a new String in the form of 
        // (two digit rank) + (first character of suit)
        // constructs the card
        public Card(String card) {
            this.suit = card.charAt(2);
            String strRank = card.substring(0,2);
            this.rank = strToRank(strRank);
        }
        
        // creates a new card with rank number and suit
        public Card(int rank, char suit) {
            this.rank = rank;
            this.suit = suit;
        }
        
        // helps convert the two digit Face cards to values
        private int strToRank(String rank) {
            if (rank.equals("Ki")) {
                return 13;
            } else if (rank.equals("Qu")) {
                return 12;
            } else if (rank.equals("Ja")) {
                return 11;
            } else if (rank.equals("Ac")) {
                return AceValue;
            }
            return Integer.parseInt(rank);
        }
        
        // gets the suit of the card
        public char getSuit() {
            return suit;
        }
        
        // gets the integer value of the card
        public int getIntRank() {
            return rank;
        }
        
        // gets the (two digit rank) form of the card
        public String getStrRank() {
            if (rank == 11) {
                return "Ja";
            } else if (rank == 12) {
                return "Qu";
            } else if (rank == 13) {
                return "Ki";
            } else if (rank == AceValue) {
                return "Ac";
            } else if (rank == 10) {
                return "10";
            }
            return "0" + rank;
        }
        
        // compares cards by suit then rank
        public int compareTo(Card other) {
            if (this.suit - other.suit != 0) {
                return Character.compare(this.suit, other.suit);
            }
            return this.rank - other.rank;
        }
        
        // converts the card to a string;
        public String toString() {
            return getStrRank() + this.suit;
        }
    }
    
    public static void main(String[] args) {
        List<String> myHand = List.of("02h", "03d");
        // first deal
        List<String> myBoard = List.of("Kih", "04c", "05h");
        // second deal
        // List<String> myBoard = List.of(Kih", "04h", "05h", "07c");
        new PokerHand(myHand, myBoard).enumerate();
    }
}