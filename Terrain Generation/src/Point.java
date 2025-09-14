public class Point
{
    public double x;
    public double y;
    public double z;
    public int color1;
    public int color2;
    
    public Point(double x, double y, double z, int color1, int color2)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color1 = color1;
        this.color2 = color2;
    }

    public void rotateY3D(double theta, double xPt, double zPt)
    { 
        double sinTheta = Math.sin(Math.toRadians(theta));
        double cosTheta = Math.cos(Math.toRadians(theta));
        double xCopy = x - xPt;
        double zCopy = z - zPt;
        x = xPt + xCopy * cosTheta - zCopy * sinTheta;
        z = zPt + zCopy * cosTheta + xCopy * sinTheta;
    } 

    public void rotateX3D(double theta, double yPt, double zPt)
    { 
        double sinTheta = Math.sin(Math.toRadians(theta));
        double cosTheta = Math.cos(Math.toRadians(theta));
        double yCopy = y - yPt;
        double zCopy = z - zPt;
        y = yPt + yCopy * cosTheta - zCopy * sinTheta;
        z = zPt + zCopy * cosTheta + yCopy * sinTheta;
    }
}