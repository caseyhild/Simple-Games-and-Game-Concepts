/**
 * Represents a human player in connect four
 *
 * @author Casey Hild
 * @version 1
 */
import java.util.*;

public record Human(String name) implements Player {
    /**
     * Creates a human player with a given name
     *
     * @param name the name
     */
    public Human {
    }

    /**
     * Informs the player about which color they are playing
     *
     * @param color the color
     */
    @Override
    public void init(boolean color) {

    }

    /**
     * Identifies the player in the logs
     *
     * @return the name of the player
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * Called when a player is asked to make a move
     *
     * @return the move the player makes
     */
    @Override
    public int move() {
        Scanner sc = new Scanner(System.in);
        System.out.print(name() + "'s move (1 - 7): ");
        int move;
        do {
            try {
                move = sc.nextInt();
            } catch (InputMismatchException e) {
                move = -1;
                sc.next();
            }
            if (move < 1 || move > 7)
                System.out.print("Invalid Entry\nMove (1 - 7): ");
        } while (move < 1 || move > 7);
        move--;
        return move;
    }

    /**
     * Informs the player about the move of the opponent
     *
     * @param i the move the opponent made
     */
    @Override
    public void inform(int i) {
        //human can see move so does not need to internally keep track of opponent's move
    }
}