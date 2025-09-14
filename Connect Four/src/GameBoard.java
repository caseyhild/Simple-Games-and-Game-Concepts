/**
 * Represents the game board in connect four
 *
 * @author Casey Hild
 * @version 1
 */
public class GameBoard
{
    private int[][] gameBoard;

    /**
     * Creates the game board with a given number of rows and columns
     * 
     * @param numRows the number of rows
     * @param numColumns the number of columns
     */
    public GameBoard(int numRows, int numColumns)
    {
        gameBoard = new int[numRows][numColumns];
    }

    /**
     * Creates a copy of another existing GameBoard
     * 
     * @param board the other game board
     */
    public GameBoard(GameBoard board)
    {
        gameBoard = new int[board.getBoard().length][board.getBoard()[0].length];
        for(int r = 0; r < gameBoard.length; r++)
        {
            System.arraycopy(board.gameBoard[r], 0, gameBoard[r], 0, gameBoard[r].length);
        }
    }

    /**
     * Returns the 2D array representation of the board
     * 
     * @return the 2D array
     */
    public int[][] getBoard()
    {
        return gameBoard;
    }

    /**
     * Updates the game board with a move by a certain player
     * Throws an exception to be caught when illegal move is made
     * 
     * @param player the number (1 or 2) representing the current player
     * @param move the move made
     */
    public void update(int player, int move) throws Exception
    {
        for(int i = gameBoard.length - 1; i >= 0; i--)
        {
            if(gameBoard[i][move] == 0)
            {
                gameBoard[i][move] = player;
                return;
            }
        }
        throw new Exception();
    }

    /**
     * Checks whether the entire board is full of tokens
     * 
     * @param player the number (1 or 2) representing the current player
     * @param move the move made
     * 
     * @return true if full, false otherwise
     */
    public boolean isLegal(int player, int move)
    {
        boolean legal = true;
        int[][] copy = new int[gameBoard.length][gameBoard[0].length];
        for(int r = 0; r < gameBoard.length; r++)
        {
            System.arraycopy(gameBoard[r], 0, copy[r], 0, gameBoard[r].length);
        }
        try
        {
            update(player, move);
        }
        catch(Exception e)
        {
            legal = false;
        }
        gameBoard = new int[gameBoard.length][gameBoard[0].length];
        for(int r = 0; r < gameBoard.length; r++)
        {
            System.arraycopy(copy[r], 0, gameBoard[r], 0, gameBoard[r].length);
        }
        return legal;
    }

    /**
     * Checks whether the entire board is full of tokens
     * 
     * @return true if full, false otherwise
     */
    public boolean isFull()
    {
        for (int[] ints : gameBoard) {
            for (int anInt : ints) {
                if (anInt == 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * Prints out the game board to the console window
     */
    public void print()
    {
        for(int c = 0; c < gameBoard[0].length * 2 + 1; c++)
        {
            System.out.print("_");
        }
        System.out.println();
        for (int[] ints : gameBoard) {
            System.out.print("|");
            for (int anInt : ints) {
                if (anInt == 0)
                    System.out.print(" ");
                else if (anInt == 1)
                    System.out.print("R");
                else if (anInt == 2)
                    System.out.print("Y");
                System.out.print("|");
            }
            System.out.println();
        }
        for(int c = 0; c < gameBoard[0].length * 2 + 1; c++)
        {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Returns 0 if noone wins, 1 if red wins, and 2 if yellow wins
     * 
     * @return which color if any wins
     */
    public int winner()
    {
        //horizontal lines
        for (int[] ints : gameBoard) {
            for (int c = 0; c < ints.length - 3; c++) {
                if (ints[c] == 1 && ints[c + 1] == 1 && ints[c + 2] == 1 && ints[c + 3] == 1)
                    return 1;
                if (ints[c] == 2 && ints[c + 1] == 2 && ints[c + 2] == 2 && ints[c + 3] == 2)
                    return 2;
            }
        }
        //vertical lines
        for(int c = 0; c < gameBoard[0].length; c++)
        {
            for(int r = 0; r < gameBoard.length - 3; r++)
            {
                if(gameBoard[r][c] == 1 && gameBoard[r + 1][c] == 1 && gameBoard[r + 2][c] == 1 && gameBoard[r + 3][c] == 1)
                    return 1;
                if(gameBoard[r][c] == 2 && gameBoard[r + 1][c] == 2 && gameBoard[r + 2][c] == 2 && gameBoard[r + 3][c] == 2)
                    return 2;
            }
        }
        //diagonal lines(\)
        for(int r = 0; r < gameBoard.length - 3; r++)
        {
            for(int c = 0; c < gameBoard[r].length - 3; c++)
            {
                if(gameBoard[r][c] == 1 && gameBoard[r + 1][c + 1] == 1 && gameBoard[r + 2][c + 2] == 1 && gameBoard[r + 3][c + 3] == 1)
                    return 1;
                if(gameBoard[r][c] == 2 && gameBoard[r + 1][c + 1] == 2 && gameBoard[r + 2][c + 2] == 2 && gameBoard[r + 3][c + 3] == 2)
                    return 2;
            }
        }
        //diagonal lines(/)
        for(int r = gameBoard.length - 3; r < gameBoard.length; r++)
        {
            for(int c = 0; c < gameBoard[r].length - 3; c++)
            {
                if(gameBoard[r][c] == 1 && gameBoard[r - 1][c + 1] == 1 && gameBoard[r - 2][c + 2] == 1 && gameBoard[r - 3][c + 3] == 1)
                    return 1;
                if(gameBoard[r][c] == 2 && gameBoard[r - 1][c + 1] == 2 && gameBoard[r - 2][c + 2] == 2 && gameBoard[r - 3][c + 3] == 2)
                    return 2;
            }
        }
        return 0;
    }
}