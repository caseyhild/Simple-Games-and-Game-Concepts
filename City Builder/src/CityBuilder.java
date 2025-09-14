import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class CityBuilder extends JPanel implements ActionListener, MouseListener, MouseMotionListener
{
    Timer tm = new Timer(5, this);
    private static final int width = 800;
    private static final int height = 600;
    private final int mapWidth = 40;
    private final int mapHeight = 40;
    private final double visibilityLevel;
    private final int[][] visibility = new int[mapWidth][mapHeight];
    private final int[][] map = new int[mapWidth][mapHeight];
    private final int[] xPos = new int[mapWidth * mapHeight];
    private final int[] yPos = new int[mapWidth * mapHeight];
    private final String[] type = new String[mapWidth * mapHeight];
    private final int numBuildings;
    private final String[] building;
    private final int[] buildingCost;
    private int numPlayer;
    private final int playerColor;
    private final Vector player;
    private final Vector dir;
    private final Vector end;
    private int shadeNum;
    private String selected;
    private int mouseX;
    private int mouseY;
    public CityBuilder()
    {
        addMouseListener(this);
        addMouseMotionListener(this);
        playerColor = rgbNum(0, 0, 255);
        numBuildings = 5;
        building = new String[numBuildings];
        buildingCost = new int[numBuildings];
        building[0] = "castle";
        buildingCost[0] = 1000;
        building[1] = "hospital";
        buildingCost[1] = 1000;
        building[2] = "laboratory";
        buildingCost[2] = 1000;
        building[3] = "military";
        buildingCost[3] = 1000;
        building[4] = "mine";
        buildingCost[4] = 1000;
        int startX = (int) (Math.random() * (mapWidth - 2) + 1);
        int startY = (int) (Math.random() * (mapHeight - 2) + 1);
        player = new Vector(15 * startX + 7.5, 15 * startY + 22.5);
        dir = new Vector(0, 0);
        end = new Vector(player.getX(), player.getY());
        xPos[0] = startX;
        yPos[0] = startY;
        type[0] = "castle";
        numPlayer = 1;
        visibilityLevel = 3.2;
        for(int mapX = 0; mapX < mapWidth; mapX++)
        {
            for(int mapY = 0; mapY < mapHeight; mapY++)
            {
                if(Math.sqrt((mapX - startX) * (mapX - startX) + (mapY - startY) * (mapY - startY)) <= visibilityLevel)
                    visibility[mapX][mapY] = rgbNum(255, 255, 255);
                else if(Math.sqrt((mapX - player.getX()) * (mapX - player.getX()) + (mapY - player.getY()) * (mapY - player.getY())) <= visibilityLevel)
                    visibility[mapX][mapY] = rgbNum(255, 255, 255);
                else
                    visibility[mapX][mapY] = rgbNum(0, 0, 0);
            }
        }
        for(int i = 0; i < 5; i++)
        {
            map[(int) (Math.random() * mapWidth)][(int) (Math.random() * mapHeight)] = rgbNum(160, 128, 0);
        }
        map[startX][startY] = playerColor;
        shadeNum = -1;
        selected = "";
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //map
        for(int mapX = 0; mapX < mapWidth; mapX++)
        {
            for(int mapY = 0; mapY < mapHeight; mapY++)
            {
                if((int) (player.getX()/15) == mapX && (int) (player.getY()/15) == mapY)
                {
                    for(int x = 0; x < mapWidth; x++)
                    {
                        for(int y = 0; y < mapHeight; y++)
                        {
                            if(Math.sqrt((x - mapX) * (x - mapX) + (y - mapY) * (y - mapY)) <= visibilityLevel)
                                visibility[x][y] = rgbNum(255, 255, 255);
                        }
                    }
                }
                if(visibility[mapX][mapY] == rgbNum(255, 255, 255) && map[mapX][mapY] != rgbNum(0, 0, 0))
                    g.setColor(new Color(map[mapX][mapY]));
                else
                    g.setColor(new Color(visibility[mapX][mapY]));
                g.fillRect(mapX * height/mapWidth, mapY * height/mapHeight, height/mapWidth, height/mapHeight);
            }
        }
        //player
        g.setColor(new Color(playerColor));
        g.fillOval((int) Math.round(player.getX() - 0.5) - 5, (int) Math.round(player.getY() - 0.5) - 5, 10, 10);
        g.fillOval((int) Math.round(player.getX() - 0.5) - 5, (int) Math.round(player.getY() + 0.5) - 5, 10, 10);
        g.fillOval((int) Math.round(player.getX() + 0.5) - 5, (int) Math.round(player.getY() - 0.5) - 5, 10, 10);
        g.fillOval((int) Math.round(player.getX() + 0.5) - 5, (int) Math.round(player.getY() + 0.5) - 5, 10, 10);
        if(Math.abs(player.getX() - end.getX()) < 0.1 && Math.abs(player.getY() - end.getY()) < 0.1)
            dir.mult(0);
        for(int i = 0; i < numPlayer; i++)
        {
            File file = new File("City Builder/SavedTextures/" + type[i] + ".txt");
            try (Scanner scanner = new Scanner(file)) {
                for (int y = 0; y < 15; y++) {
                    for (int x = 0; x < 15; x++) {
                        if (!scanner.hasNextInt()) {
                            throw new RuntimeException("Not enough integers in file: " + file.getName());
                        }
                        int color = scanner.nextInt();
                        if (color == -1) {
                            g.setColor(new Color(playerColor));
                        } else {
                            g.setColor(new Color(color));
                        }
                        g.drawLine(
                                xPos[i] * height / mapWidth + x,
                                yPos[i] * height / mapHeight + y,
                                xPos[i] * height / mapWidth + x,
                                yPos[i] * height / mapHeight + y
                        );
                    }
                }
            } catch (FileNotFoundException e) {
                System.exit(1);
            }
        }
        //shop
        Font font = new Font("Verdana", Font.PLAIN, 30);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        g.drawString("SHOP", 700 - fm.stringWidth("SHOP")/2, 100);
        g.drawLine(600, 104 - fm.getAscent(), 800, 104 - fm.getAscent());
        g.drawLine(600, 102, 800, 102);
        font = new Font("Verdana", Font.PLAIN, 15);
        g.setFont(font);
        fm = g.getFontMetrics();
        for(int i = 0; i < numBuildings; i++)
        {
            File file = new File("City Builder/SavedTextures/" + building[i] + ".txt");

            try (Scanner scanner = new Scanner(file)) {
                for (int y = 0; y < 15; y++) {
                    for (int x = 0; x < 15; x++) {
                        if (!scanner.hasNextInt()) {
                            throw new RuntimeException("Not enough integers in file: " + file.getName());
                        }
                        int color = scanner.nextInt();
                        if (color == -1) {
                            color = playerColor;
                        }

                        // Apply shading if needed
                        if (shadeNum == i) {
                            g.setColor(new Color(
                                    getR(color) / 2,
                                    getG(color) / 2,
                                    getB(color) / 2
                            ));
                        } else {
                            g.setColor(new Color(color));
                        }

                        g.drawLine(610 + x, 135 + 25 * i + y,
                                610 + x, 135 + 25 * i + y);
                    }
                }
            } catch (FileNotFoundException e) {
                System.exit(1);
            }
            g.setColor(new Color(0, 0, 0));
            g.drawString(building[i].substring(0, 1).toUpperCase() + building[i].substring(1), 630, 148 + 25 * i);
            g.drawString("" + buildingCost[i], 775 - fm.stringWidth("" + buildingCost[i]), 148 + 25 * i);
        }
        g.setColor(new Color(255, 0, 0));
        g.fillRect(width - 22, 104, 20, 20);
        g.setColor(new Color(0, 0, 0));
        g.drawRect(width - 22, 104, 20, 20);
        g.setColor(new Color(255, 255, 255));
        g.drawLine(width - 20, 105, width - 3, 122);
        g.drawLine(width - 21, 105, width - 3, 123);
        g.drawLine(width - 21, 106, width - 4, 123);
        g.drawLine(width - 4, 105, width - 21, 122);
        g.drawLine(width - 3, 105, width - 21, 123);
        g.drawLine(width - 3, 106, width - 20, 123);
        if(!selected.isEmpty())
        {
            File file = new File("City Builder/SavedTextures/" + selected + ".txt");

            try (Scanner scanner = new Scanner(file)) {
                for (int y = 0; y < 15; y++) {
                    for (int x = 0; x < 15; x++) {
                        if (!scanner.hasNextInt()) {
                            throw new RuntimeException("Not enough integers in file: " + file.getName());
                        }
                        int color = scanner.nextInt();
                        if (color == -1) {
                            color = playerColor;
                        }

                        g.setColor(new Color(color));

                        // Draw depending on mouse position
                        if (mouseX >= 0 && mouseX < height && mouseY >= 0 && mouseY < height) {
                            g.drawLine(mouseX / 15 * 15 + x,
                                    mouseY / 15 * 15 + y,
                                    mouseX / 15 * 15 + x,
                                    mouseY / 15 * 15 + y);
                        } else if (mouseX >= 0 && mouseX < height + 7 && mouseY >= 0 && mouseY < height) {
                            g.drawLine(height + x,
                                    mouseY - 7 + y,
                                    height + x,
                                    mouseY - 7 + y);
                        } else {
                            g.drawLine(mouseX - 7 + x,
                                    mouseY - 7 + y,
                                    mouseX - 7 + x,
                                    mouseY - 7 + y);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.exit(1);
            }
        }
        tm.start();
    }

    public void actionPerformed(ActionEvent e)
    {
        player.add(dir);
        repaint();
    }

    public void mouseClicked(MouseEvent me)
    {
        if(mouseX >= 0 && mouseX < height && mouseY >= 0 && mouseY < height)
        {
            if(!selected.isEmpty())
            {
                if(visibility[mouseX/15][mouseY/15] == rgbNum(255, 255, 255) && map[mouseX/15][mouseY/15] != playerColor)
                {
                    xPos[numPlayer] = mouseX/15;
                    yPos[numPlayer] = mouseY/15;
                    type[numPlayer] = selected;
                    map[xPos[numPlayer]][yPos[numPlayer]] = playerColor;
                    numPlayer++;
                    selected = "";
                }
            }
            else
            {
                dir.setX(mouseX);
                dir.setY(mouseY);
                dir.sub(player);
                dir.normalize();
                dir.mult(0.1);
                end.setX(mouseX);
                end.setY(mouseY);
            }
        }
        repaint();
    }

    public void mouseEntered(MouseEvent me)
    {

    }

    public void mouseExited(MouseEvent me)
    {

    }

    public void mousePressed(MouseEvent me)
    {
        shadeNum = -1;
        for(int i = 0; i < numBuildings; i++)
        {
            if(mouseX >= 610 && mouseX <= 625 && mouseY >= 135 + 25 * i && mouseY <= 150 + 25 * i)
                shadeNum = i;
        }
        if(mouseX >= width - 22 && mouseX <= width - 2 && mouseY >= 104 && mouseY <= 124)
            selected = "";
    }

    public void mouseReleased(MouseEvent me)
    {
        if(shadeNum >= 0)
        {
            selected = building[shadeNum];
            shadeNum = -1;
        }
    }

    public void mouseDragged(MouseEvent me)
    {
        mouseX = me.getX();
        mouseY = me.getY();
        shadeNum = -1;
        for(int i = 0; i < numBuildings; i++)
        {
            if(mouseX >= 610 && mouseX <= 625 && mouseY >= 135 + 25 * i && mouseY <= 150 + 25 * i)
                shadeNum = i;
        }
        
        if(mouseX >= 0 && mouseX < height && mouseY >= 0 && mouseY < height)
        {
            if(!selected.isEmpty())
            {
                if(visibility[mouseX/15][mouseY/15] == rgbNum(255, 255, 255) && map[mouseX/15][mouseY/15] != playerColor)
                {
                    xPos[numPlayer] = mouseX/15;
                    yPos[numPlayer] = mouseY/15;
                    type[numPlayer] = selected;
                    map[xPos[numPlayer]][yPos[numPlayer]] = playerColor;
                    numPlayer++;
                    selected = "";
                }
            }
            else
            {
                dir.setX(mouseX);
                dir.setY(mouseY);
                dir.sub(player);
                dir.normalize();
                dir.mult(0.1);
                end.setX(mouseX);
                end.setY(mouseY);
            }
        }
        repaint();
    }

    public void mouseMoved(MouseEvent me)
    {
        mouseX = me.getX();
        mouseY = me.getY();
    }

    private int rgbNum(int r, int g, int b)
    {
        //gets rgb decimal value from rgb input
        return r * 65536 + g * 256 + b;
    }

    private int getR(int color)
    {
        //gets r value from rgb decimal input
        return color/65536;
    }

    private int getG(int color)
    {
        //gets g value from rgb decimal input
        color -= color/65536 * 65536;
        return color/256;
    }

    private int getB(int color)
    {
        //gets b value from rgb decimal input
        color -= color/65536 * 65536;
        color -= color/256 * 256;
        return color;
    }

    public static void main(String[] args)
    {
        CityBuilder g = new CityBuilder();
        JFrame jf = new JFrame();
        jf.setTitle("City Builder");
        jf.setSize(width, height + 28);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.add(g);
        jf.setVisible(true);
    }
}