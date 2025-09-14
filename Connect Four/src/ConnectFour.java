/**
 * Simulates a game of connect four
 * Allows for 2 player games or 1 player vs computer
 * Computer determines moves by use of a minimax algorithm (search depth 6, no alpha-beta pruning)
 *
 * @author Casey Hild
 * @version 1
 */
import java.util.*;
public class ConnectFour
{
    /**
     * This is the main function
     * 
     * @param args input arguments
     */
    public static void main(String[] args)
    {
        //Gets selection for game mode (1 or 2 player)
        Scanner sc = new Scanner(System.in);
        int gameMode = 0;
        System.out.print("Which game mode would you like to play?\nHuman vs Computer (1)\nHuman vs Human    (2)\nSelection: ");
        do
        {
            try
            {
                gameMode = sc.nextInt();
            }
            catch(InputMismatchException e)
            {
                sc.next();
            }
            if(gameMode != 1 && gameMode != 2)
                System.out.print("Invalid entry, please reenter\nSelection: ");
        }while(gameMode != 1 && gameMode != 2);
        System.out.println();

        //Creates players for the game and gets name inputs
        Player player1;
        Player player2;
        int firstPlayer = (int) (Math.random() * 2 + 1);
        if(gameMode == 1)
        {
            System.out.print("Enter Your Name: ");
            player1 = new Human(sc.next());
            player1.init(firstPlayer == 1);
            player2 = new Automatic(6, 7);
            player2.init(firstPlayer == 2);
        }
        else
        {
            System.out.print("Enter Name For Player 1: ");
            player1 = new Human(sc.next());
            player1.init(firstPlayer == 1);
            System.out.print("Enter Name For Player 2: ");
            player2 = new Human(sc.next());
            player2.init(firstPlayer == 2);
        }
        if(firstPlayer == 1)
            System.out.println("\n" + player1.name() + " is playing as Red, " + player2.name() + " is playing as Yellow");
        else
            System.out.println("\n" + player2.name() + " is playing as Red, " + player1.name() + " is playing as Yellow");

        //Creates the game board
        GameBoard board = new GameBoard(6, 7);
        board.print();

        int turn = firstPlayer;

        //game loop (continues until game is over)
        while(true)
        {
            //Determines who's turn it currently is
            Player currentPlayer;
            Player otherPlayer;
            if(turn == 1)
            {
                currentPlayer = player1;
                otherPlayer = player2;
            }
            else
            {
                currentPlayer = player2;
                otherPlayer = player1;
            }

            //Gets the move for the current player
            int move = currentPlayer.move();
            try
            {
                if(firstPlayer == 1)
                    board.update(turn, move);
                else
                    board.update(turn % 2 + 1, move);
                otherPlayer.inform(move);
            }
            catch(Exception e)
            {
                System.exit(1);
            }

            //Prints the board each turn
            board.print();

            //Determines when game is over
            if(board.winner() == firstPlayer)
            {
                System.out.println("Game over, " + player1.name() + " wins!");
                break;
            }
            else if(board.winner() == firstPlayer % 2 + 1)
            {
                System.out.println("Game over, " + player2.name() + " wins!");
                break;
            }
            else if(board.isFull())
            {
                System.out.println("Game over, it is a tie.");
                break;
            }

            //Alternate turns
            if(turn == 1)
                turn = 2;
            else
                turn = 1;
        }
    }
}