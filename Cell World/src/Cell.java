import java.awt.*;
import java.util.*;
public class Cell
{
    protected int xPos;
    protected int yPos;
    protected int color;
    protected int tempColor;
    protected double shade;
    protected double tempShade;
    protected Cell[] neighbors;
    protected ArrayList<Integer> openNeighbors;
    protected ArrayList<Integer> occupiedNeighbors;

    public Cell(int x, int y, int c, double s)
    {
        xPos = x;
        yPos = y;
        color = c;
        tempColor = -1;
        shade = s;
        tempShade = -1;
        neighbors = new Cell[6];
        openNeighbors = new ArrayList<>();
        occupiedNeighbors = new ArrayList<>();
    }

    public void update(Player player, double[][] noise, Cell[][] world)
    {

    }

    public void action()
    {

    }

    public void draw(double xoff, double yoff, int xp, int yp, int w, int h, ArrayList<Integer> colors, double[][] noise, Cell[][] world, int width, int height, Graphics g)
    {
        int xShift = xp - w/2;
        int yShift = yp - h/2;
        double x = width/2.0 + 30 * xShift;
        double y = height/2.0 + (xp % 2 * Math.sqrt(3) * 10) + Math.sqrt(3) * 20 * yShift;
        int xCenter = (int) (x + xoff);
        int yCenter = (int) (y + yoff);
        if (tempColor == -1 || tempShade == -1) {
            shade /= 2;
            g.setColor(new Color((int) (shade * getR(colors.get(color))), (int) (shade * getG(colors.get(color))), (int) (shade * getB(colors.get(color)))));
            fillHexagon(xCenter, yCenter, 20, g);
            shade *= 2;
            g.setColor(new Color((int) (shade * getR(colors.get(color))), (int) (shade * getG(colors.get(color))), (int) (shade * getB(colors.get(color)))));
            fillHexagon(xCenter, yCenter, 18, g);
        } else {
            tempShade /= 2;
            g.setColor(new Color((int) (tempShade * getR(colors.get(tempColor))), (int) (tempShade * getG(colors.get(tempColor))), (int) (tempShade * getB(colors.get(tempColor)))));
            fillHexagon(xCenter, yCenter, 20, g);
            tempShade *= 2;
            g.setColor(new Color((int) (tempShade * getR(colors.get(tempColor))), (int) (tempShade * getG(colors.get(tempColor))), (int) (tempShade * getB(colors.get(tempColor)))));
            fillHexagon(xCenter, yCenter, 18, g);
        }
    }

    public void fillHexagon(int xCenter, int yCenter, int sideLength, Graphics g)
    {
        int[] xPos = {
                xCenter - sideLength/2,
                xCenter + sideLength/2,
                xCenter + sideLength,
                xCenter + sideLength/2,
                xCenter - sideLength/2,
                xCenter - sideLength};
        int l = (int) Math.round(Math.sqrt(3)/2 * sideLength);
        int[] yPos = {
                yCenter - l,
                yCenter - l,
                yCenter,
                yCenter + l,
                yCenter + l,
                yCenter};
        g.fillPolygon(xPos, yPos, 6);
    }

    public void setNeighbors(Cell[][] world, double xoff, double yoff)
    {
        Arrays.fill(neighbors, null);
        openNeighbors.clear();
        neighbors[0] = world[yPos - 1 + (int) (yoff / Math.sqrt(3) / 20)][xPos + 2 * ((int) xoff / 60)];
        openNeighbors.add(0);
        if (xPos % 2 == 0)
        {
            neighbors[1] = world[yPos - 1 + (int) (yoff / Math.sqrt(3) / 20)][xPos + 1 + 2 * ((int) xoff / 60)];
            openNeighbors.add(1);
        } else {
            neighbors[1] = world[yPos + (int) (yoff / Math.sqrt(3) / 20)][xPos + 1 + 2 * ((int) xoff / 60)];
            openNeighbors.add(1);
        }
        if(xPos % 2 == 0)
        {
            neighbors[2] = world[yPos + (int) (yoff / Math.sqrt(3) / 20)][xPos + 1 + 2 * ((int) xoff / 60)];
            openNeighbors.add(2);
        } else {
            neighbors[2] = world[yPos + 1 + (int) (yoff / Math.sqrt(3) / 20)][xPos + 1 + 2 * ((int) xoff / 60)];
            openNeighbors.add(2);
        }
        neighbors[3] = world[yPos + 1 + (int) (yoff / Math.sqrt(3) / 20)][xPos + 2 * ((int) xoff / 60)];
        openNeighbors.add(3);
        if(xPos % 2 == 0)
        {
            neighbors[4] = world[yPos + (int) (yoff / Math.sqrt(3) / 20)][xPos - 1 + 2 * ((int) xoff / 60)];
            openNeighbors.add(4);
        } else {
            neighbors[4] = world[yPos + 1 + (int) (yoff / Math.sqrt(3) / 20)][xPos - 1 + 2 * ((int) xoff / 60)];
            openNeighbors.add(4);
        }
        if(xPos % 2 == 0)
        {
            neighbors[5] = world[yPos - 1 + (int) (yoff / Math.sqrt(3) / 20)][xPos - 1 + 2 * ((int) xoff / 60)];
            openNeighbors.add(5);
        } else {
            neighbors[5] = world[yPos + (int) (yoff / Math.sqrt(3) / 20)][xPos - 1 + 2 * ((int) xoff / 60)];
            openNeighbors.add(5);
        }
        occupiedNeighbors.clear();
    }

    public void setTempColor(int c)
    {
        tempColor = c;
    }

    public void setTempShade(double s)
    {
        tempShade = s;
    }

    private int getR(int color)
    {
        //gets r value from rgb decimal input
        return color/65536;
    }

    private int getG(int color)
    {
        //gets g value from rgb decimal input
        return color % 65536/256;
    }

    private int getB(int color)
    {
        //gets b value from rgb decimal input
        return color % 65536 % 256;
    }
}