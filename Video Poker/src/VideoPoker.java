/**
 * Simulates a popular casino game called video poker
 *
 * @author Casey Hild
 * @version 1
 */
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
public class VideoPoker
{
    /**
     * This is the main function
     * 
     * @param args input arguments
     */
    public static void main(String[] args)
    {
        int numTokens = 10;

        //making deck of cards
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
        String[] values = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
        ArrayList<Card> deckOfCards = new ArrayList<>();
        for (String suit : suits) {
            for (String value : values) {
                deckOfCards.add(new Card(suit, value));
            }
        }

        //playing game
        while(true)
        {
            System.out.println("You have " + numTokens + " tokens");
            if(ynQuestion("Would you like to play for 1 token?"))
            {
                numTokens--;

                //shuffling deck
                ArrayList<Card> temp = new ArrayList<>(deckOfCards);
                ArrayList<Card> cards = new ArrayList<>();
                while(!temp.isEmpty())
                {
                    cards.add(temp.remove((int) (Math.random() * temp.size())));
                }

                //present the player with top 5 cards
                System.out.print("\nYour Cards: ");
                ArrayList<Card> presentedCards = new ArrayList<>();
                for(int i = 0; i < 5; i++)
                {
                    System.out.print(cards.getFirst());
                    if(i < 4)
                        System.out.print(", ");
                    presentedCards.add(cards.removeFirst());
                }
                System.out.println();

                //replace each of the 5 cards if asked for
                if(ynQuestion("Would you like to replace your first card?"))
                    presentedCards.set(0, cards.removeFirst());
                if(ynQuestion("Would you like to replace your second card?"))
                    presentedCards.set(1, cards.removeFirst());
                if(ynQuestion("Would you like to replace your third card?"))
                    presentedCards.set(2, cards.removeFirst());
                if(ynQuestion("Would you like to replace your fourth card?"))
                    presentedCards.set(3, cards.removeFirst());
                if(ynQuestion("Would you like to replace your fifth card?"))
                    presentedCards.set(4, cards.removeFirst());

                System.out.print("\nYour Cards: ");
                for(int i = 0; i < presentedCards.size(); i++)
                {
                    System.out.print(presentedCards.get(i));
                    if(i < presentedCards.size() - 1)
                        System.out.print(", ");
                }
                System.out.println();

                //score + payout
                int payout = score(presentedCards);
                numTokens += payout;
                System.out.println();
            }
            else
                break;
        }
    }

    /**
     * Asks a yes or no question and returns the answer from the user
     * 
     * @param question the question to be asked
     * 
     * @return true if yes, false if no
     */
    public static boolean ynQuestion(String question)
    {
        Scanner sc = new Scanner(System.in);
        String answer;
        do
        {
            System.out.print(question + " (y/n) ");
            answer = sc.next();
            if(!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")))
                System.out.print("Invalid entry - ");
        }while(!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")));
        return answer.equalsIgnoreCase("y");
    }

    /**
     * Scores the player's hand
     * 
     * @param cards the hand of cards that the player has
     * 
     * @return the payout from the hand
     */
    public static int score(ArrayList<Card> cards)
    {
        //sort cards by value (selection sort)
        for(int i = 0; i < cards.size() - 1; i++)
        {
            int min = i;
            for(int j = i + 1; j < cards.size(); j++)
            {
                if(cards.get(j).value() < cards.get(min).value())
                    min = j;
            }
            Card temp = cards.get(i);
            cards.set(i, cards.get(min));
            cards.set(min, temp);
        }

        boolean flush = true; //any flush
        for(int i = 0; i < cards.size() - 1; i++)
        {
            if(!Objects.equals(cards.get(i).suit(), cards.get(i + 1).suit()))
                flush = false;
        }

        boolean straight = true; //any straight
        for(int i = 0; i < cards.size() - 1; i++)
        {
            if(cards.get(i).value() + 1 != cards.get(i + 1).value())
                straight = false;
        }

        int[] highStraightValues = {1, 10, 11, 12, 13}; //Ace can also be after King
        boolean highStraight = true;
        for(int i = 0; i < cards.size(); i++)
        {
            if(cards.get(i).value() != highStraightValues[i])
                highStraight = false;
        }

        ArrayList<Card> duplicateCards = new ArrayList<>();
        ArrayList<Integer> duplicateCount = new ArrayList<>();
        for (Card card : cards) {
            boolean duplicate = false;
            for (int j = 0; j < duplicateCards.size(); j++) {
                if (card.value() == duplicateCards.get(j).value()) {
                    duplicateCount.set(j, duplicateCount.get(j) + 1);
                    duplicate = true;
                }
            }
            if (!duplicate) {
                duplicateCards.add(card);
                duplicateCount.add(1);
            }
        }

        if(highStraight && flush)
        {
            System.out.println("Royal Flush");
            System.out.println("Payout: 250");
            return 250;
        }

        if(straight && flush)
        {
            System.out.println("Straight Flush");
            System.out.println("Payout: 50");
            return 50;
        }

        if(duplicateCount.contains(4))
        {
            System.out.println("Four of a Kind");
            System.out.println("Payout: 25");
            return 25;
        }

        if(duplicateCount.contains(3) && duplicateCount.contains(2))
        {
            System.out.println("Full House");
            System.out.println("Payout: 6");
            return 6;
        }

        if(flush)
        {
            System.out.println("Flush");
            System.out.println("Payout: 5");
            return 5;
        }

        if(straight || highStraight)
        {
            System.out.println("Straight");
            System.out.println("Payout: 4");
            return 4;
        }

        if(duplicateCount.contains(3))
        {
            System.out.println("Three of a Kind");
            System.out.println("Payout: 3");
            return 3;
        }

        if(duplicateCount.contains(2))
        {
            int firstPair = duplicateCount.indexOf(2);
            for(int i = firstPair + 1; i < duplicateCount.size(); i++)
            {
                if(duplicateCount.get(i) == 2)
                {
                    System.out.println("Two Pair");
                    System.out.println("Payout: 2");
                    return 2;
                }
            }
            System.out.println("Pair");
            System.out.println("Payout: 1");
            return 1;
        }

        System.out.println("No Pair");
        System.out.println("Payout: 0");
        return 0;
    }
}