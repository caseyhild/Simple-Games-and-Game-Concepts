import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Chess extends JFrame implements Runnable, MouseListener, MouseMotionListener
{
    private final int width;
    private final int height;

    private static Sheets sheetsService;
    private static final String APPLICATION_NAME = "Chess";
    private static final String SPREADSHEET_ID = "1KNTa2zGpZH8SEaMgTD1-03V1ZPgh422i819WO3fZo1g";
    private final Thread thread;
    private boolean running;
    private final BufferedImage image;
    private final int[] pixels;

    private String myColor;

    private String turn;
    private int turnsSincePawnMoveOrCapture;

    private final Board board;
    private ArrayList<Move> moves;

    private int selectedRow;
    private int selectedCol;
    private ArrayList<Move> selectedLocationMoves;

    private boolean gameOver;

    private long oldTime;
    private boolean updated;

    private final int whitePieceColor;
    private final int blackPieceColor;
    private final int lightBackgroundColor;
    private final int darkBackgroundColor;

    private final int[][] pawnTexture;
    private final int[][] knightTexture;
    private final int[][] bishopTexture;
    private final int[][] rookTexture;
    private final int[][] queenTexture;
    private final int[][] kingTexture;

    private int mouseX;
    private int mouseY;
    private boolean mousePressed;
    private boolean mouseClicked;

    public Chess()
    {
        //set size of screen
        width = 512;
        height = 512;

        //what will be displayed to the user
        thread = new Thread(this);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        do
        {
            myColor = JOptionPane.showInputDialog("Do you want to play as white or black?").toLowerCase();
        }while(!myColor.equals("white") && !myColor.equals("black"));

        //make size of spreadsheet 1x5

        try
        {
            sheetsService = getSheetsService();
        }
        catch(Exception ignored)
        {

        }

        if(myColor.equals("white"))
        {
            try
            {
                java.util.List<java.util.List<Object>> array = List.of(
                        Arrays.asList("", "", "", "", "")
                );
                String range = APPLICATION_NAME + "!A1:E1";
                ValueRange body = new ValueRange()
                    .setValues(array);
                sheetsService.spreadsheets().values()
                    .update(SPREADSHEET_ID, range, body)
                    .setValueInputOption("RAW")
                    .execute();
            }
            catch(Exception ignored)
            {

            }
        }

        turn = "white";
        turnsSincePawnMoveOrCapture = 0;

        board = new Board(myColor);
        if(myColor.equals("black"))
            board.flipBoard();

        moves = new ArrayList<>();

        selectedRow = -1;
        selectedCol = -1;
        selectedLocationMoves = new ArrayList<>();

        gameOver = false;

        oldTime = System.currentTimeMillis();
        updated = false;

        whitePieceColor = RGB(255, 255, 255);
        blackPieceColor = RGB(0, 0,0);
        lightBackgroundColor = RGB(160, 160, 160);
        darkBackgroundColor = RGB(96, 96, 96);

        pawnTexture = new int[64][64];
        readFile(pawnTexture, "Multiplayer Chess - Google Sheets/SavedTextures//Pawn.txt");
        knightTexture = new int[64][64];
        readFile(knightTexture, "Multiplayer Chess - Google Sheets/SavedTextures//Knight.txt");
        bishopTexture = new int[64][64];
        readFile(bishopTexture, "Multiplayer Chess - Google Sheets/SavedTextures//Bishop.txt");
        rookTexture = new int[64][64];
        readFile(rookTexture, "Multiplayer Chess - Google Sheets/SavedTextures//Rook.txt");
        queenTexture = new int[64][64];
        readFile(queenTexture, "Multiplayer Chess - Google Sheets/SavedTextures//Queen.txt");
        kingTexture = new int[64][64];
        readFile(kingTexture, "Multiplayer Chess - Google Sheets/SavedTextures//King.txt");

        //mouse input
        addMouseListener(this);
        addMouseMotionListener(this);

        //setting up the window
        setSize(width, height + 28);
        setResizable(false);
        setTitle("Multiplayer Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        //start the program
        start();
    }

    private synchronized void start()
    {
        //starts game
        running = true;
        thread.start();
    }

    private void update()
    {
        //updates everything
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                if(x % (width / board.width * 2) < width / board.width && y % (height / board.height * 2) >= height / board.height)
                    pixels[y * width + x] = darkBackgroundColor;
                else if(x % (width / board.width * 2) >= width / board.width && y % (height / board.height * 2) < height / board.height)
                    pixels[y * width + x] = darkBackgroundColor;
                else
                    pixels[y * width + x] = lightBackgroundColor;
            }
        }
        if(!turn.equals(myColor))
            board.flipBoard();
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                if(x % (width / board.width) == 0 && y % (height / board.height) == 0 && board.getPiece(y * board.height / height, x * board.width / width) != null)
                    drawTexture(board.getPiece(y * board.height / height, x * board.width / width).type, board.getPiece(y * board.height / height, x * board.width / width).color, x, y, width / board.width);
            }
        }
        if(!turn.equals(myColor))
            board.flipBoard();
        if(turn.equals(myColor))
        {
            moves = board.getMoves();
            board.addCastleMove(moves);
            board.removeInCheckMoves(moves);
            if(!gameOver && moves.isEmpty() && board.inCheck())
            {
                if(turn.equals("white"))
                    System.out.println("Checkmate, black wins");
                else if(turn.equals("black"))
                    System.out.println("Checkmate, white wins");
                gameOver = true;
            }
            if(!gameOver && moves.isEmpty())
            {
                System.out.println("Draw, stalemate");
                gameOver = true;
            }
            if(!gameOver && board.isDead())
            {
                System.out.println("Draw, dead board");
                gameOver = true;
            }
            if(!gameOver && turnsSincePawnMoveOrCapture >= 100)
            {
                System.out.println("Draw, fifty-move rule");
                gameOver = true;
            }
        }
        if(!gameOver)
        {
            if(turn.equals(myColor))
            {
                selectedLocationMoves.clear();
                if(selectedRow != -1 && selectedCol != -1)
                    selectedLocationMoves = board.getMovesFromLocation(selectedRow, selectedCol, moves);
                if(mouseClicked)
                {
                    Move m = null;
                    for(Move move: selectedLocationMoves)
                    {
                        if(mouseY * board.height / height == move.endRow && mouseX * board.width / width == move.endCol)
                        {
                            m = move;
                            break;
                        }
                    }
                    if(m != null)
                    {
                        makeMove(m);
                        List<List<Object>> spreadsheetInfo = List.of(
                                Arrays.asList("", "", "", "", "")
                        );
                        if(myColor.equals("white"))
                            spreadsheetInfo.getFirst().set(0, 0);
                        else
                            spreadsheetInfo.getFirst().set(0, 1);
                        spreadsheetInfo.getFirst().set(1, m.startRow);
                        spreadsheetInfo.getFirst().set(2, m.startCol);
                        spreadsheetInfo.getFirst().set(3, m.endRow);
                        spreadsheetInfo.getFirst().set(4, m.endCol);
                        try
                        {
                            String range = APPLICATION_NAME + "!A1:E1";
                            ValueRange body = new ValueRange()
                                .setValues(spreadsheetInfo);
                            sheetsService.spreadsheets().values()
                                .update(SPREADSHEET_ID, range, body)
                                .setValueInputOption("RAW")
                                .execute();
                        }
                        catch(Exception ignored)
                        {

                        }
                        updated = false;
                    }
                    else
                    {
                        int oldRow = selectedRow;
                        int oldCol = selectedCol;
                        selectedRow = mouseY * board.height / height;
                        selectedCol = mouseX * board.width / width;
                        if (oldRow == selectedRow && oldCol == selectedCol) {
                            selectedRow = -1;
                            selectedCol = -1;
                        }
                    }
                }
            }
            else
            {
                if(System.currentTimeMillis() - oldTime >= 10000 && !updated)
                {
                    try
                    {
                        String range = APPLICATION_NAME + "!A1:A1";
                        ValueRange response = sheetsService.spreadsheets().values()
                            .get(SPREADSHEET_ID, range)
                            .execute();
                        java.util.List<java.util.List<Object>> values = response.getValues();
                        if(values != null && !values.isEmpty())
                        {
                            if(myColor.equals("white") && values.getFirst().getFirst().equals("1"))
                                spreadsheetUpdate();
                            else if(myColor.equals("black") && values.getFirst().getFirst().equals("0"))
                                spreadsheetUpdate();
                        }
                        oldTime = System.currentTimeMillis();
                    }
                    catch(Exception ignored)
                    {

                    }
                }
                if(mouseClicked)
                {
                    int oldRow = selectedRow;
                    int oldCol = selectedCol;
                    selectedRow = mouseY * board.height / height;
                    selectedCol = mouseX * board.width / width;
                    if (oldRow == selectedRow && oldCol == selectedCol) {
                        selectedRow = -1;
                        selectedCol = -1;
                    }
                }
            }
        }
        if(mousePressed)
            mousePressed = false;
        if(mouseClicked)
            mouseClicked = false;
    }

    private void render()
    {
        //sets up graphics
        BufferStrategy bs = getBufferStrategy();
        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.translate(0, 28);

        //draws chess board and pieces
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);

        //draw boxes around selected square and possible moves
        if(selectedRow != -1 && selectedCol != -1)
        {
            if(turn.equals(myColor))
            {
                g.setColor(new Color(0, 255, 0));
                for(Move move: selectedLocationMoves)
                {
                    g.drawRect(move.endCol * width / board.width - 2, move.endRow * height / board.height - 2, width / board.width + 4, height / board.height + 4);
                    g.drawRect(move.endCol * width / board.width - 1, move.endRow * height / board.height - 1, width / board.width + 2, height / board.height + 2);
                    g.drawRect(move.endCol * width / board.width, move.endRow * height / board.height, width / board.width, height / board.height);
                    g.drawRect(move.endCol * width / board.width + 1, move.endRow * height / board.height + 1, width / board.width - 2, height / board.height - 2);
                    g.drawRect(move.endCol * width / board.width + 2, move.endRow * height / board.height + 2, width / board.width - 4, height / board.height - 4);
                }
            }
            g.setColor(new Color(255, 255, 0));
            g.drawRect(selectedCol * width / board.width - 2, selectedRow * height / board.height - 2, width / board.width + 4, height / board.height + 4);
            g.drawRect(selectedCol * width / board.width - 1, selectedRow * height / board.height - 1, width / board.width + 2, height / board.height + 2);
            g.drawRect(selectedCol * width / board.width, selectedRow * height / board.height, width / board.width, height / board.height);
            g.drawRect(selectedCol * width / board.width + 1, selectedRow * height / board.height + 1, width / board.width - 2, height / board.height - 2);
            g.drawRect(selectedCol * width / board.width + 2, selectedRow * height / board.height + 2, width / board.width - 4, height / board.height - 4);
        }

        //display all the graphics
        bs.show();
    }

    public void run()
    {
        //main game loop
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0; //60 times per second
        double delta = 0;
        requestFocus();
        while(running)
        {
            //updates time
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) //Make sure update is only happening 60 times a second
            {
                //update
                update();
                delta--;
            }
            //display to the screen
            render();
        }
    }

    public void readFile(int[][] array, String fileLoc)
    {
        try
        {
            Scanner file = new Scanner(new File(fileLoc));
            for(int y = 0; y < 64; y++)
            {
                for(int x = 0; x < 64; x++)
                {
                    array[y][x] = file.nextInt();
                }
            }
        }
        catch(IOException ignored)
        {

        }
    }

    private void drawTexture(String texture, String color, int xStart, int yStart, int size)
    {
        for(int y = yStart; y < yStart + size; y++)
        {
            for(int x = xStart; x < xStart + size; x++)
            {
                if(texture.equals("pawn") && pawnTexture[y - yStart][x - xStart] > 0)
                {
                    if(color.equals("white"))
                        pixels[y * width + x] = whitePieceColor;
                    else
                        pixels[y * width + x] = blackPieceColor;
                }
                else if(texture.equals("knight") && knightTexture[y - yStart][x - xStart] > 0)
                {
                    if(color.equals("white"))
                        pixels[y * width + x] = whitePieceColor;
                    else
                        pixels[y * width + x] = blackPieceColor;
                }
                else if(texture.equals("bishop") && bishopTexture[y - yStart][x - xStart] > 0)
                {
                    if(color.equals("white"))
                        pixels[y * width + x] = whitePieceColor;
                    else
                        pixels[y * width + x] = blackPieceColor;
                }
                else if(texture.equals("rook") && rookTexture[y - yStart][x - xStart] > 0)
                {
                    if(color.equals("white"))
                        pixels[y * width + x] = whitePieceColor;
                    else
                        pixels[y * width + x] = blackPieceColor;
                }
                else if(texture.equals("queen") && queenTexture[y - yStart][x - xStart] > 0)
                {
                    if(color.equals("white"))
                        pixels[y * width + x] = whitePieceColor;
                    else
                        pixels[y * width + x] = blackPieceColor;
                }
                else if(texture.equals("king") && kingTexture[y - yStart][x - xStart] > 0)
                {
                    if(color.equals("white"))
                        pixels[y * width + x] = whitePieceColor;
                    else
                        pixels[y * width + x] = blackPieceColor;
                }
            }
        }
    }

    public void makeMove(Move m)
    {
        int numPiecesBeforeMove = board.pieces.size();
        board.move(m);
        board.update(m);
        if(board.getPiece(m.endRow, m.endCol).type.equals("pawn") || numPiecesBeforeMove > board.pieces.size())
            turnsSincePawnMoveOrCapture = 0;
        else
            turnsSincePawnMoveOrCapture++;
        selectedRow = -1;
        selectedCol = -1;
        if(turn.equals("white"))
            turn = "black";
        else if(turn.equals("black"))
            turn = "white";
        board.flipBoard();
    }

    private static Credential authorize() throws IOException, GeneralSecurityException
    {
        InputStream in = Chess.class.getResourceAsStream("credentials.json");
        assert in != null;
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new InputStreamReader(in)
            );

        java.util.List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
            .setAccessType("offline")
            .build();

        return new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver())
            .authorize("user");
    }

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException
    {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(), credential)
        .setApplicationName(APPLICATION_NAME)
        .build();
    }

    public void spreadsheetUpdate()
    {
        try
        {
            String range = APPLICATION_NAME + "!B1:E1";
            ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
            java.util.List<java.util.List<Object>> values = response.getValues();
            if(values != null && !values.isEmpty())
            {
                makeMove(new Move(Integer.parseInt((String) values.getFirst().getFirst()), Integer.parseInt((String) values.getFirst().get(1)), Integer.parseInt((String) values.getFirst().get(2)), Integer.parseInt((String) values.getFirst().get(3))));
            }
        }
        catch(Exception ignored)
        {

        }
        updated = true;
    }

    public void mouseClicked(MouseEvent me)
    {
        mousePressed = true;
        mouseClicked = true;
    }

    public void mouseEntered(MouseEvent me)
    {

    }

    public void mouseExited(MouseEvent me)
    {

    }

    public void mousePressed(MouseEvent me)
    {
        mousePressed = true;
    }

    public void mouseReleased(MouseEvent me)
    {
        mousePressed = false;
        mouseClicked = true;
    }

    public void mouseDragged(MouseEvent me)
    {
        mousePressed = true;
        mouseX = me.getX() - 1;
        mouseY = me.getY() - 28;
    }

    public void mouseMoved(MouseEvent me)
    {
        mousePressed = false;
        mouseX = me.getX() - 1;
        mouseY = me.getY() - 28;
    }

    private int RGB(int r, int g, int b)
    {
        return r << 16 | g << 8 | b;
    }

    public static void main(String [] args)
    {
        new Chess();
    }
}