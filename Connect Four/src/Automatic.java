/**
 * Represents an automatic player in connect four
 *
 * @author Casey Hild
 * @version 1
 */
public class Automatic implements Player
{
    private final GameBoard board;
    private final String name;
    private int playerNum;

    /**
     * Creates an automatic player with given board size
     * 
     * @param numRows the number of rows
     * @param numColumns the number of columns
     */
    public Automatic(int numRows, int numColumns)
    {
        name = "Computer";
        board = new GameBoard(numRows, numColumns);
    }

    /**
     * Informs the player about which color they are playing
     * 
     * @param color the color
     */
    @Override
    public void init(boolean color)
    {
        if(color)
            playerNum = 1;
        else
            playerNum = 2;
    }

    /**
     * Identifies the player in the logs
     * 
     * @return the name of the player
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * Called when a player is asked to make a move
     * 
     * @return the move the player makes
     */
    @Override
    public int move()
    {
        int searchDepth = 6;
        
        int move = -1;
        int[] scores = new int[board.getBoard()[0].length];
        int max = Integer.MIN_VALUE;
        GameBoard copy = new GameBoard(board);
        for(int c = 0; c < copy.getBoard()[0].length; c++)
        {
            try
            {
                if(board.isLegal(playerNum, c))
                {
                    GameBoard temp = new GameBoard(copy);
                    copy.update(playerNum, c);
                    int score = getScore(searchDepth, copy);
                    if(score == 0)
                        score = minimax(searchDepth, "Other", copy);
                    scores[c] = score;
                    copy = new GameBoard(temp);
                    if(score > max)
                    {
                        max = score;
                        move = c;
                    }
                }
            }
            catch(Exception e)
            {
                System.exit(1);
            }
        }

        boolean allSameScore = true;
        for(int i = 0; i < scores.length - 1; i++)
        {
            if (scores[i] != scores[i + 1]) {
                allSameScore = false;
                break;
            }
        }
        
        if(allSameScore)
        {
            move = (int) (Math.random() * board.getBoard()[0].length);
            while(!board.isLegal(playerNum, move))
                move = (int) (Math.random() * board.getBoard()[0].length);
        }

        try
        {
            if(playerNum == 1)
                board.update(1, move);
            else
                board.update(2, move);
        }
        catch(Exception e)
        {
            System.exit(1);
        }

        System.out.println(name() + "'s move: " + (move + 1));
        return move;
    }

    /**
     * Implements a minimax algorithm to determine the computer's next move
     * 
     * @param depth the search depth for the algorithm
     * @param player the name of the player whose turn it is during the algorithm
     * @param board the current state of the game board
     * 
     * @return the score of the best move
     */
    public int minimax(int depth, String player, GameBoard board) throws Exception
    {
        if(depth == 0)
            return getScore(depth, board);
        else if(player.equals("Computer"))
        {
            int max = Integer.MIN_VALUE;
            for(int c = 0; c < board.getBoard()[0].length; c++)
            {
                if(board.isLegal(playerNum, c))
                {
                    GameBoard copy = new GameBoard(board);
                    board.update(playerNum, c);
                    int score = getScore(depth, board);
                    if(score == 0)
                        score = minimax(depth - 1, "Other", board);
                    board = new GameBoard(copy);
                    if(score > max)
                        max = score;
                }
            }
            return max;
        }
        else
        {
            int min = Integer.MAX_VALUE;
            for(int c = 0; c < board.getBoard()[0].length; c++)
            {
                if(board.isLegal(playerNum % 2 + 1, c))
                {
                    GameBoard copy = new GameBoard(board);
                    board.update(playerNum % 2 + 1, c);
                    int score = getScore(depth, board);
                    if(score == 0)
                        score = minimax(depth - 1, "Computer", board);
                    board = new GameBoard(copy);
                    if(score < min)
                        min = score;
                }
            }
            return min;
        }
    }

    /**
     * Determines how beneficial the current board is the player
     * 
     * @param depth the current depth in the algorithm
     * @param board the current state of the game board
     * 
     * @return the score for the current game board
     */
    public int getScore(int depth, GameBoard board)
    {
        //score for a given board determined by whether there is a winner and how many steps through the algorithm have occurred
        //prioritizes moves that lead to a sooner victory or to prevent an immediate loss
        if(board.winner() == playerNum)
            return depth + 1;
        if(board.winner() == playerNum % 2 + 1)
            return -depth - 1;
        return 0;
    }

    /**
     * Informs the player about the move of the opponent
     * 
     * @param i the move the opponent made
     */
    @Override
    public void inform(int i)
    {
        try
        {
            board.update(playerNum % 2 + 1, i);
        }
        catch(Exception e)
        {
            System.exit(1);
        }
    }
}