public class Noise
{
    private final int size;
    private final double[][] noise;
    
    public Noise(double[][] random, double xoff, double yoff)
    {
        this.size = random.length;
        noise = new double[random.length][random[0].length];
        for(int y = 0; y < size; y++)
        {
            for(int x = 0; x < 2 * size; x++)
            {
                noise[y][x] = turbulence(random, x + xoff, y + yoff, size/8.0);
            }
        }
    }

    private double smoothNoise(double[][] random, double x, double y)
    {
        double fractX = x - (int) x;
        double fractY = y - (int) y;
        int x1 = ((int) x + size) % size;
        int y1 = ((int) y + size) % size;
        int x2 = (x1 + size - 1) % size;
        int y2 = (y1 + size - 1) % size;
        double value = 0.0;
        value += fractX * fractY * random[y1][x1];
        value += (1 - fractX) * fractY * random[y1][x2];
        value += fractX * (1 - fractY) * random[y2][x1];
        value += (1 - fractX) * (1 - fractY) * random[y2][x2];
        return value;
    }

    private double turbulence(double[][] random, double x, double y, double Size)
    {
        double value = 0.0;
        double initialSize = Size;
        while(Size >= 1)
        {
            value += smoothNoise(random, x / Size, y / Size) * Size;
            Size /= 2.0;
        }
        return (0.5 * value / initialSize);
    }

    public double[][] getNoise()
    {
        return noise;
    }
}