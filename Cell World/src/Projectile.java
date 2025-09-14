import java.awt.*;
public class Projectile
{
    Vector2D position;
    Vector2D velocity;
    Vector2D acceleration;
    Vector2D destination;
    Vector2D accPoint;
    private final int timer;
    private final int size;

    public Projectile(double x, double y, int r, int t)
    {
        if(x % 2 == 1)
            y += 0.5;
        position = new Vector2D(x, y);
        velocity = new Vector2D();
        acceleration = new Vector2D();
        destination = new Vector2D(position);
        accPoint = new Vector2D();
        timer = t;
        size = r;
    }

    public void update()
    {
        if(!destination.equals(new Vector2D(-1, -1)))
        {
            if(velocity.equals(new Vector2D()))
            {
                accPoint = Vector2D.sub(destination, position);
                accPoint.mult(0.5);
                accPoint.add(position);
            }
            acceleration = Vector2D.sub(accPoint, position);
            acceleration.mult(0.1 / timer);
            if(Vector2D.sub(position, destination).mag() <= 0.00025)
            {
                velocity.mult(0);
                acceleration.mult(0);
                accPoint.mult(0);
                position = new Vector2D(destination);
                destination = new Vector2D(-1, -1);
            }
        }
        velocity.add(acceleration);
        position.add(velocity);
    }

    public void draw(double xoff, double yoff, int w, int h, int width, int height, Graphics g)
    {

        double xShift = position.x - w/2;
        double yShift = position.y - h/2;
        double x = width/2.0 + 30 * xShift;
        double y = height/2.0 + Math.sqrt(3) * 20 * yShift;
        g.setColor(new Color(0));
        g.drawOval((int) (x + xoff - size/2), (int) (y + yoff - size/2), size, size);
        g.setColor(new Color(0));
        g.fillOval((int) (x + xoff - size/2), (int) (y + yoff - size/2), size, size);
    }

    public void setDestination(Vector2D dest)
    {
        if(velocity.mag() == 0)
        {
            destination = new Vector2D(dest);
        }
    }
}