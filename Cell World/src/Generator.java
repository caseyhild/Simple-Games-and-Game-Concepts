import java.awt.*;
import java.util.*;
public class Generator extends Cell
{
    private int timer;
    private final int timerLength;
    private final int color2;
    private boolean destroying;
    private int collection;

    public Generator(int x, int y, int c, int c2, Cell[][] world, int t)
    {
        super(x, y, c, 1);
        timer = 0;
        timerLength = t;
        color2 = c2;
        destroying = false;
        collection = 0;
        super.setNeighbors(world, 0, 0);
    }

    public void update(Player player, double[][] noise, Cell[][] world)
    {
        timer++;
        if(timer % timerLength == 0)
            action();
        else if(destroying && timer % (timerLength/10) == 0)
        {
            resetRandomNeighbor();
            if(openNeighbors.size() == neighbors.length)
                destroying = false;
        }
    }

    public void action()
    {
        if(!openNeighbors.isEmpty())
            colorRandomNeighbor(color2);
        else
        {
            destroying = true;
            resetRandomNeighbor();
        }
    }

    public void draw(double xoff, double yoff, int xp, int yp, int w, int h, ArrayList<Integer> colors, double[][] noise, Cell[][] world, int width, int height, Graphics g)
    {
        super.draw(xoff, yoff, xp, yp, w, h, colors, noise, world, width, height, g);
        g.setFont(new Font("Verdana", Font.PLAIN, 14));
        FontMetrics fm = g.getFontMetrics();
        g.setColor(new Color(0));
        double xShift = xp - w/2.0;
        double yShift = yp - h/2.0;
        double x = width/2.0 + 30 * xShift;
        double y = height/2.0 + (xp % 2 * Math.sqrt(3) * 10) + Math.sqrt(3) * 20 * yShift;
        g.drawString("" + collection, (int) (x + xoff - fm.stringWidth("" + collection)/2), (int) (y + yoff + fm.getAscent()/2 - 2));
    }

    public void colorRandomNeighbor(int color)
    {
        if(!openNeighbors.isEmpty())
        {
            int random = (int) (Math.random() * openNeighbors.size());
            neighbors[openNeighbors.get(random)].setTempColor(color);
            neighbors[openNeighbors.get(random)].setTempShade(1);
            occupiedNeighbors.add(openNeighbors.remove(random));
        }
    }

    public void resetRandomNeighbor()
    {
        if(!occupiedNeighbors.isEmpty())
        {
            int random = (int) (Math.random() * occupiedNeighbors.size());
            neighbors[occupiedNeighbors.get(random)].setTempColor(-1);
            neighbors[occupiedNeighbors.get(random)].setTempShade(-1);
            openNeighbors.add(occupiedNeighbors.remove(random));
            collection++;
        }
    }
}