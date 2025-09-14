/**
 * Simulates a game of Nim for the user against a computer
 *
 * @author Casey Hild
 * @version 1
 */
import java.util.Scanner;
public class Nim
{
    /**
     * This is the main function
     * 
     * @param args input arguments
     */
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        int numMarbles = (int) (Math.random() * 91 + 10);

        int turn = (int) (Math.random() * 2); //0 = computer, 1 = human

        int mode = (int) (Math.random() * 2); //0 = smart, 1 = stupid

        if(mode == 0)
            System.out.println("Computer is in smart mode");
        else
            System.out.println("Computer is in stupid mode");
        System.out.println();

        while(numMarbles > 0)
        {
            System.out.println("There are " + numMarbles + " marbles left");

            int numToTake;
            if(turn == 0)
            {
                if(mode == 0)
                {
                    int n = 1;
                    while(2 * n + 1 <= numMarbles)
                    {
                        n = 2 * n + 1;
                    }

                    if(n != numMarbles)
                        numToTake = numMarbles - n;
                    else
                        numToTake = (int) (Math.random() * (numMarbles / 2) + 1);
                }
                else
                {
                    numToTake = (int) (Math.random() * (numMarbles / 2) + 1);
                }
                if(numMarbles == 1)
                    numToTake = 1;

                System.out.println("\nComputer takes " + numToTake + " marbles");
            }
            else
            {
                System.out.println();
                do
                {
                    System.out.print("How many marbles would you like to take? ");
                    numToTake = sc.nextInt();
                    if(numMarbles != 1 && (numToTake < 1 || numToTake > numMarbles / 2))
                        System.out.print("Invalid entry -- ");
                }while(numMarbles != 1 && (numToTake < 1 || numToTake > numMarbles / 2));

                System.out.println("\nYou take " + numToTake + " marbles");
            }

            numMarbles -= numToTake;

            if(numMarbles == 0)
            {
                if(turn == 0)
                    System.out.println("\nYou Win!");
                else
                    System.out.println("\nComputer Wins.");
            }

            turn = 1 - turn;
        }
    }
}