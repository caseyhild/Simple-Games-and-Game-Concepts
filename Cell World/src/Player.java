import java.awt.*;
public class Player
{
    private final Vector2D position;
    private final Vector2D direction;
    private final int speed;
    private final int size;
    
    public Player(double xp, double yp, double xd, double yd, int sp, int si)
    {
        position = new Vector2D(xp, yp);
        direction = new Vector2D(xd, yd);
        speed = sp;
        size = si;
    }
    
    public void moveLeft()
    {
        direction.x = -1.0 / 60 * speed;
    }
    
    public void moveRight()
    {
        direction.x = 1.0 / 60 * speed;
    }
    
    public void moveUp()
    {
        direction.y = -1 / Math.sqrt(3) / 40 * speed;
    }
    
    public void moveDown()
    {
        direction.y = 1 / Math.sqrt(3) / 40 * speed;
    }
    
    public void update()
    {
        position.add(direction);
        direction.mult(0);
    }
    
    public void draw(int width, int height, Graphics g)
    {
        g.setColor(new Color(0, 255, 255));
        g.fillOval(width/2 - size/2, height/2 - size/2, size, size);
    }
    
    public Vector2D getPosition()
    {
        return position;
    }
}