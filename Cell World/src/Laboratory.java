import java.awt.*;
import java.util.*;
public class Laboratory extends Cell
{
    private int timer;
    private final int totalTime;
    private final int color2;
    public Laboratory(int x, int y, int c, int c2, Cell[][] world, int t)
    {
        super(x, y, c, 1);
        timer = 0;
        totalTime = t;
        color2 = c2;
        super.setNeighbors(world, 0, 0);
    }

    public void update(Player player, double[][] noise, Cell[][] world)
    {
        if(totalTime != 0)
        {
            if(timer == totalTime)
                timer = 0;
            action();
        }
    }

    public void action()
    {
        timer++;
    }

    public void draw(double xoff, double yoff, int xp, int yp, int w, int h, ArrayList<Integer> colors, double[][] noise, Cell[][] world, int width, int height, Graphics g)
    {
        super.draw(xoff, yoff, xp, yp, w, h, colors, noise, world, width, height, g);
        double xShift = xp - w/2;
        double yShift = yp - h/2;
        double x = width/2.0 + 30 * xShift;
        double y = height/2.0 + (xp % 2 * Math.sqrt(3) * 10) + Math.sqrt(3) * 20 * yShift;
        g.setColor(new Color(colors.get(color2)));
        g.fillRect((int) (x + xoff - 8), (int) (y + yoff - 4), 16 * timer/totalTime, 8);
        g.setColor(new Color(0));
        g.drawRect((int) (x + xoff - 8), (int) (y + yoff - 4), 16, 8);
    }
}