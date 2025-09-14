import java.awt.*;
import java.util.*;
public class Tower extends Cell
{
    private int timer;
    private final int timerLength;
    private Projectile projectile;

    public Tower(int x, int y, int c, Cell[][] world, int t)
    {
        super(x, y, c, 1);
        timer = 0;
        timerLength = t;
        projectile = null;
        super.setNeighbors(world, 0, 0);
    }

    public void update(Player player, double[][] noise, Cell[][] world)
    {
        timer++;
        if(timer % timerLength == 0)
            action(player);
        if(projectile != null)
            projectile.update();
    }

    public void action(Player player)
    {
        projectile = new Projectile(xPos, yPos, 10, timerLength);
        projectile.setDestination(new Vector2D(player.getPosition()));
    }

    public void draw(double xoff, double yoff, int xp, int yp, int w, int h, ArrayList<Integer> colors, double[][] noise, Cell[][] world, int width, int height, Graphics g)
    {
        super.draw(xoff, yoff, xp, yp, w, h, colors, noise, world, width, height, g);
    }

    public void drawProjectile(double xoff, double yoff, int xp, int yp, int w, int h, ArrayList<Integer> colors, double[][] noise, Cell[][] world, int width, int height, Graphics g)
    {
        if(projectile != null)
        {
            projectile.draw(xoff, yoff, w, h, width, height, g);
            draw((int) (xoff % 60), (int) (yoff % (Math.sqrt(3) * 20)), xp, yp, w, h, colors, noise, world, width, height, g);
        }
    }
}