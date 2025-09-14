import java.awt.*;
public class Cube
{
    private final int x;
    private final int y;
    private final int z;
    private final double[] xPoints;
    private final double[] yPoints;
    private final double[] zPoints;
    private final int color;

    public Cube(int x, int y, int z, int size, int color)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        xPoints = new double[] {x - size/2.0, x - size/2.0, x - size/2.0, x - size/2.0, x + size/2.0, x + size/2.0, x + size/2.0, x + size/2.0};
        yPoints = new double[] {y - size/2.0, y - size/2.0, y + size/2.0, y + size/2.0, y - size/2.0, y - size/2.0, y + size/2.0, y + size/2.0};
        zPoints = new double[] {z - size/2.0, z + size/2.0, z - size/2.0, z + size/2.0, z - size/2.0, z + size/2.0, z - size/2.0, z + size/2.0};
        this.color = color;
    }

    public void draw(Graphics g)
    {
        xPoints[1] = xPoints[3];
        xPoints[4] = xPoints[6];
        xPoints[5] = xPoints[7];
        g.setColor(new Color(R(color), G(color), B(color)));
        int[] xTemp = {(int) xPoints[0], (int) xPoints[1], (int) xPoints[3], (int) xPoints[2]};
        int[] yTemp = {(int) yPoints[0], (int) yPoints[1], (int) yPoints[3], (int) yPoints[2]};
        g.fillPolygon(xTemp, yTemp, 4);
        xTemp = new int[] {(int) xPoints[0], (int) xPoints[1], (int) xPoints[5], (int) xPoints[4]};
        yTemp = new int[] {(int) yPoints[0], (int) yPoints[1], (int) yPoints[5], (int) yPoints[4]};
        g.fillPolygon(xTemp, yTemp, 4);
        xTemp = new int[] {(int) xPoints[0], (int) xPoints[2], (int) xPoints[6], (int) xPoints[4]};
        yTemp = new int[] {(int) yPoints[0], (int) yPoints[2], (int) yPoints[6], (int) yPoints[4]};
        g.fillPolygon(xTemp, yTemp, 4);
        xTemp = new int[] {(int) xPoints[1], (int) xPoints[3], (int) xPoints[7], (int) xPoints[5]};
        yTemp = new int[] {(int) yPoints[1], (int) yPoints[3], (int) yPoints[7], (int) yPoints[5]};
        g.fillPolygon(xTemp, yTemp, 4);
        xTemp = new int[] {(int) xPoints[2], (int) xPoints[3], (int) xPoints[7], (int) xPoints[6]};
        yTemp = new int[] {(int) yPoints[2], (int) yPoints[3], (int) yPoints[7], (int) yPoints[6]};
        g.fillPolygon(xTemp, yTemp, 4);
        xTemp = new int[] {(int) xPoints[4], (int) xPoints[5], (int) xPoints[7], (int) xPoints[6]};
        yTemp = new int[] {(int) yPoints[4], (int) yPoints[5], (int) yPoints[7], (int) yPoints[6]};
        g.fillPolygon(xTemp, yTemp, 4);
    }

    public void drawOutline(Graphics g, int color)
    {
        int[] index = new int[8];
        for(int i = 0; i < 8; i++)
        {
            index[i] = i;
        }
        double[] zPointsCopy = new double[8];
        System.arraycopy(zPoints, 0, zPointsCopy, 0, 8);
        quickSort(zPoints, index, 0, 7);
        System.arraycopy(zPointsCopy, 0, zPoints, 0, 8);
        int farthest = 0;
        for(int i = 0; i < 8; i++)
        {
            if(index[i] == 2 || index[i] == 3 || index[i] == 6 || index[i] == 7)
            {
                farthest = index[i];
                break;
            }
        }
        xPoints[1] = xPoints[3];
        xPoints[4] = xPoints[6];
        xPoints[5] = xPoints[7];
        g.setColor(new Color(R(color), G(color), B(color)));
        int[] xTemp = {(int) xPoints[0], (int) xPoints[1], (int) xPoints[3], (int) xPoints[2]};
        int[] yTemp = {(int) yPoints[0], (int) yPoints[1], (int) yPoints[3], (int) yPoints[2]};
        if(farthest != 0 && farthest != 1 && farthest != 3 && farthest != 2)
            g.drawPolygon(xTemp, yTemp, 4);
        xTemp = new int[] {(int) xPoints[0], (int) xPoints[1], (int) xPoints[5], (int) xPoints[4]};
        yTemp = new int[] {(int) yPoints[0], (int) yPoints[1], (int) yPoints[5], (int) yPoints[4]};
        if(farthest != 0 && farthest != 1 && farthest != 5 && farthest != 4)
            g.drawPolygon(xTemp, yTemp, 4);
        xTemp = new int[] {(int) xPoints[0], (int) xPoints[2], (int) xPoints[6], (int) xPoints[4]};
        yTemp = new int[] {(int) yPoints[0], (int) yPoints[2], (int) yPoints[6], (int) yPoints[4]};
        if(farthest != 0 && farthest != 2 && farthest != 6 && farthest != 4)
            g.drawPolygon(xTemp, yTemp, 4);
        xTemp = new int[] {(int) xPoints[1], (int) xPoints[3], (int) xPoints[7], (int) xPoints[5]};
        yTemp = new int[] {(int) yPoints[1], (int) yPoints[3], (int) yPoints[7], (int) yPoints[5]};
        if(farthest != 1 && farthest != 3 && farthest != 7 && farthest != 5)
            g.drawPolygon(xTemp, yTemp, 4);
        xTemp = new int[] {(int) xPoints[2], (int) xPoints[3], (int) xPoints[7], (int) xPoints[6]};
        yTemp = new int[] {(int) yPoints[2], (int) yPoints[3], (int) yPoints[7], (int) yPoints[6]};
        if(farthest != 2 && farthest != 3 && farthest != 7 && farthest != 6)
            g.drawPolygon(xTemp, yTemp, 4);
        xTemp = new int[] {(int) xPoints[4], (int) xPoints[5], (int) xPoints[7], (int) xPoints[6]};
        yTemp = new int[] {(int) yPoints[4], (int) yPoints[5], (int) yPoints[7], (int) yPoints[6]};
        if(farthest != 4 && farthest != 5 && farthest != 7 && farthest != 6)
            g.drawPolygon(xTemp, yTemp, 4);
    }

    public void rotateZ3D(double theta, int xPt, int yPt)
    {
        double sinTheta = Math.sin(Math.toRadians(theta));
        double cosTheta = Math.cos(Math.toRadians(theta));
        for(int i = 0; i < 8; i++)
        {
            double xCopy = xPoints[i] - xPt;
            double yCopy = yPoints[i] - yPt;
            xPoints[i] = xPt + xCopy * cosTheta - yCopy * sinTheta;
            yPoints[i] = yPt + yCopy * cosTheta + xCopy * sinTheta;
        }
    } 

    public void rotateY3D(double theta, int xPt, int zPt)
    { 
        double sinTheta = Math.sin(Math.toRadians(theta));
        double cosTheta = Math.cos(Math.toRadians(theta));
        for(int i = 0; i < 8; i++)
        {
            double xCopy = xPoints[i] - xPt;
            double zCopy = zPoints[i] - zPt;
            xPoints[i] = xPt + xCopy * cosTheta - zCopy * sinTheta;
            zPoints[i] = zPt + zCopy * cosTheta + xCopy * sinTheta;
        }
    } 

    public void rotateX3D(double theta, int yPt, int zPt)
    { 
        double sinTheta = Math.sin(Math.toRadians(theta));
        double cosTheta = Math.cos(Math.toRadians(theta));
        for(int i = 0; i < 8; i++)
        {
            double yCopy = yPoints[i] - yPt;
            double zCopy = zPoints[i] - zPt;
            yPoints[i] = yPt + yCopy * cosTheta - zCopy * sinTheta;
            zPoints[i] = zPt + zCopy * cosTheta + yCopy * sinTheta;
        }
    }

    public void quickSort(double[] list, int[] index, int first, int last)
    {
        int g = first, h = last;
        int midIndex = (first + last) / 2;
        double dividingValue = list[midIndex];
        do
        {
            while(list[g] < dividingValue)
                g++;
            while(list[h] > dividingValue)
                h--;
            if(g <= h)
            {
                double temp = list[g];
                list[g] = list[h];
                list[h] = temp;
                int tempIndex = index[g];
                index[g] = index[h];
                index[h] = tempIndex;
                g++;
                h--;
            }
        }
        while(g < h);
        if(h > first)
            quickSort(list, index, first, h);
        if(g < last)
            quickSort(list, index, g, last);
    }

    public double getHeight()
    {
        int[] index = new int[8];
        for(int i = 0; i < 8; i++)
        {
            index[i] = i;
        }
        double[] yPointsCopy = new double[8];
        System.arraycopy(yPoints, 0, yPointsCopy, 0, 8);
        quickSort(yPoints, index, 0, 7);
        System.arraycopy(yPointsCopy, 0, yPoints, 0, 8);
        return yPoints[index[7]] - yPoints[index[0]];
    }

    public double getDist()
    {
        return zPoints[0] + zPoints[1] + zPoints[2] + zPoints[3] + zPoints[4] + zPoints[5] + zPoints[6] + zPoints[7];
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public int getColor()
    {
        return color;
    }

    private int R(int color)
    {
        return color >> 16;
    }

    private int G(int color)
    {
        return color >> 8 & 255;
    }

    private int B(int color)
    {
        return color & 255;
    }
}