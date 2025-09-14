import javax.swing.*;
public class Noise extends JPanel
{
    private final int size;
    private final double[][] noise;
    private final double[][] newNoise;
    private final int[][] pixelColor;

    public Noise(int size)
    {
        this.size = size;
        pixelColor = new int[size][size];
        noise = new double[size][size];
        for(int y = 0; y < size; y++)
        {
            for(int x = 0; x < size; x++)
            {
                noise[y][x] = Math.random();
            }
        }
        newNoise = new double[size][size];
    }

    public double[][] getNoise()
    {
        return newNoise;
    }
    
    public int[][] getColor()
    {
        return pixelColor;
    }
    
    public void makeNoise()
    {
        int color1 = RGB(0, 128, 0);
        for(int y = 0; y < size; y++)
        {
            for(int x = 0; x < size; x++)
            {
                double turbulence = turbulence(x, y, size/8.0);
                newNoise[y][x] = turbulence;
                int color = color1;
                color = RGB((int) (turbulence * R(color)), (int) (turbulence * G(color)), (int) (turbulence * B(color)));
                pixelColor[y][x] = color;
            }
        }
    }

    private double smoothNoise(double x, double y)
    {
        double fractX = x - (int) x;
        double fractY = y - (int) y;
        int x1 = ((int) x + size) % size;
        int y1 = ((int) y + size) % size;
        int x2 = (x1 + size - 1) % size;
        int y2 = (y1 + size - 1) % size;
        double value = 0.0;
        value += fractX * fractY * noise[y1][x1];
        value += (1 - fractX) * fractY * noise[y1][x2];
        value += fractX * (1 - fractY) * noise[y2][x1];
        value += (1 - fractX) * (1 - fractY) * noise[y2][x2];
        return value;
    }

    private double turbulence(double x, double y, double Size)
    {
        double value = 0.0;
        double initialSize = Size;
        while(Size >= 1)
        {
            value += smoothNoise(x / Size, y / Size) * Size;
            Size /= 2.0;
        }
        return(0.5 * value / initialSize);
    }

    private int RGB(int r, int g, int b)
    {
        return r << 16 | g << 8 | b;
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