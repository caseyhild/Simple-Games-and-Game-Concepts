/**
 * Represents a card from a standard 52 card deck
 *
 * @author Casey Hild
 * @version 1
 */
public class Card
{
    private final String suit;
    private final String value;
    private final int valueNumber;

    /**
     * Creates a card object
     * 
     * @param s the suit for the card
     * @param v the value for the card
     */
    public Card(String s, String v)
    {
        suit = s;
        value = v;
        switch(v)
        {
            case "Ace":
                valueNumber = 1;
                break;
            case "2":
                valueNumber = 2;
                break;
            case "3":
                valueNumber = 3;
                break;
            case "4":
                valueNumber = 4;
                break;
            case "5":
                valueNumber = 5;
                break;
            case "6":
                valueNumber = 6;
                break;
            case "7":
                valueNumber = 7;
                break;
            case "8":
                valueNumber = 8;
                break;
            case "9":
                valueNumber = 9;
                break;
            case "10":
                valueNumber = 10;
                break;
            case "Jack":
                valueNumber = 11;
                break;
            case "Queen":
                valueNumber = 12;
                break;
            case "King":
                valueNumber = 13;
                break;
            default:
                valueNumber = 0;
                break;
        }
    }

    /**
     * Returns the suit of the card
     * 
     * @return the suit
     */
    public String suit()
    {
        return suit;
    }

    /**
     * Returns the value of the card
     * 
     * @return the value
     */
    public int value()
    {
        return valueNumber;
    }

    /**
     * Returns a formatted description of the card
     * 
     * @return a String containing suit and value of the card
     */
    public String toString()
    {
        return value + " of " + suit;
    }
}