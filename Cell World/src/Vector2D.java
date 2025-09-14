public class Vector2D
{
    public double x;
    public double y;

    public Vector2D()
    {
        x = 0; 
        y = 0;
    }

    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v)
    {
        x = v.x;
        y = v.y;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void add(Vector2D v)
    {
        x += v.x;
        y += v.y;
    }

    public static Vector2D sub(Vector2D v1, Vector2D v2)
    {
        Vector2D v = new Vector2D();
        v.setX(v1.x - v2.x);
        v.setY(v1.y - v2.y);
        return v;
    }

    public void mult(double n)
    {
        x *= n;
        y *= n;
    }

    public double mag()
    {
        return Math.sqrt(x * x + y * y);
    }


    public boolean equals(Vector2D v)
    {
        return x == v.x && y == v.y;
    }

    public String toString()
    {
        return x + " " + y;
    }
}