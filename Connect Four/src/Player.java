/**
 * Represents a player in connect four
 *
 * @author Casey Hild
 * @version 1
 */
public interface Player
{    
    /**
     * Informs the player about which color they are playing
     * 
     * @param color the color
     */
    void init(boolean color);
    
    /**
     * Identifies the player in the logs
     * 
     * @return the name of the player
     */
    String name();
    
    /**
     * Called when a player is asked to make a move
     * 
     * @return the move the player makes
     */
    int move();
    
    /**
     * Informs the player about the move of the opponent
     * 
     * @param i the move the opponent made
     */
    void inform(int i);
}