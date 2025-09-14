public class Vector
{
    private double x;
    private double y;
    
    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void add(Vector v)
    {
        this.x += v.x;
        this.y += v.y;
    }

    public void div(double n)
    {
        this.x /= n;
        this.y /= n;
    }
    public double mag()
    {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public void normalize()
    {
        if(mag() > 0)
            this.div(this.mag());
    }

    public String toString()
    {
        return x + " " + y;
    }
}
