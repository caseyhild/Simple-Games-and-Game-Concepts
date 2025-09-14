import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class TerrainGeneration extends JPanel implements ActionListener, KeyListener
{
    Timer tm = new Timer(1, this);
    private static final int width = 600;
    private static final int height = 600;
    private int frame;
    private final Point[][][] points;
    private final int centerX;
    private final int centerY;
    private final int centerZ;
    private final int spacing;
    private double xoff;
    private double yoff;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private final double[][] random;
    private double[][] noise;
    public TerrainGeneration()
    {
        addKeyListener(this);
        frame = 0;
        int size = 64;
        points = new Point[size][1][2 * size];
        centerX = 0;
        centerY = -300;
        centerZ = 0;
        spacing = 30;
        xoff = 1000000;
        yoff = 1000000;
        left = false;
        right = false;
        up = false;
        down = false;
        random = new double[size][2 * size];
        for(int y = 0; y < size; y++)
        {
            for(int x = 0; x < 2 * size; x++)
            {
                random[y][x] = Math.random();
            }
        }
        Noise n = new Noise(random, xoff, yoff);
        noise = n.getNoise();
        for(int x = 0; x < points.length; x++)
        {
            for(int y = 0; y < points[x].length; y++)
            {
                for(int z = 0; z < points[x][y].length; z++)
                {
                    int shade1;
                    int shade2;
                    int color1;
                    int color2;
                    if(x < points.length - 1 && z < points[x][y].length - 1)
                    {
                        shade1 = (int) ((noise[x][z] + noise[x + 1][z + 1] + noise[x][z + 1])/3 * 255);
                        shade2 = (int) ((noise[x][z] + noise[x + 1][z + 1] + noise[x + 1][z])/3 * 255);
                    }
                    else
                    {
                        shade1 = (int) (noise[x][z] * 255);
                        shade2 = (int) (noise[x][z] * 255);
                    }
                    if(shade1 < 0.4 * 255)
                        color1 = rgbNum(0, 0, shade1);
                    else if(shade1 < 0.5 * 255)
                        color1 = rgbNum(0, (int) (2.5 * (shade1 - 0.4 * 255)), 128 - (int) (2.5 * (shade1 - 0.4 * 255)));
                    else
                        color1 = rgbNum(0, shade1/2, 0);
                    if(shade2 < 0.4 * 255)
                        color2 = rgbNum(0, 0, shade2);
                    else if(shade2 < 0.5 * 255)
                        color2 = rgbNum(0, (int) (2.5 * (shade2 - 0.4 * 255)), 128 - (int) (2.5 * (shade2 - 0.4 * 255)));
                    else
                        color2 = rgbNum(0, shade2/2, 0);
                    points[x][y][z] = new Point((x - (points.length - 1)/2.0) * spacing + centerX, (y - (points[x].length - 1)/2.0) * spacing - 125 * noise[x][points[x][y].length - 1 - z] - centerY, (z - (points[x][y].length - 1)/2.0) * spacing + centerZ, color1, color2);
                    points[x][y][z].rotateY3D(10, 0, 0);
                    points[x][y][z].rotateX3D(-10, 0, 0);
                }
            }
        }
        setBackground(new Color(0, 0, 0));
    }

    public void update()
    {
        frame++;
        if(left)
        {
            yoff -= 0.2;
        }
        if(right)
        {
            yoff += 0.2;
        }
        if(up)
        {
            xoff += 1.0;
        }
        if(down)
        {
            xoff -= 1.0;
        }
        if(yoff < 0)
            yoff = 0;
        if(xoff < 0)
            xoff = 0;
        if(yoff > 2000000)
            yoff = 2000000;
        if(xoff > 2000000)
            xoff = 2000000;
        Noise n = new Noise(random, xoff, yoff);
        noise = n.getNoise();
        for(int x = 0; x < points.length; x++)
        {
            for(int y = 0; y < points[x].length; y++)
            {
                for(int z = 0; z < points[x][y].length; z++)
                {
                    int shade1;
                    int shade2;
                    int color1;
                    int color2;
                    if(x < points.length - 1 && z < points[x][y].length - 1)
                    {
                        shade1 = (int) ((noise[x][z] + noise[x + 1][z + 1] + noise[x][z + 1])/3 * 255);
                        shade2 = (int) ((noise[x][z] + noise[x + 1][z + 1] + noise[x + 1][z])/3 * 255);
                    }
                    else
                    {
                        shade1 = (int) (noise[x][z] * 255);
                        shade2 = (int) (noise[x][z] * 255);
                    }
                    if(frame % 2 == 0)
                    {
                        if(shade1 < 0.4 * 255)
                            color1 = rgbNum(0, 0, shade1);
                        else if(shade1 < 0.5 * 255)
                            color1 = rgbNum(0, (int) (2.5 * (shade1 - 0.4 * 255)), 128 - (int) (2.5 * (shade1 - 0.4 * 255)));
                        else
                            color1 = rgbNum(0, shade1/2, 0);
                        if(shade2 < 0.4 * 255)
                            color2 = rgbNum(0, 0, shade2);
                        else if(shade2 < 0.5 * 255)
                            color2 = rgbNum(0, (int) (2.5 * (shade2 - 0.4 * 255)), 128 - (int) (2.5 * (shade2 - 0.4 * 255)));
                        else
                            color2 = rgbNum(0, shade2/2, 0);
                    }
                    else
                    {
                        color1 = points[x][y][z].color1;
                        color2 = points[x][y][z].color2;
                    }
                    points[x][y][z] = new Point((x - (points.length - 1)/2.0) * spacing + centerX, (y - (points[x].length - 1)/2.0) * spacing - 125 * noise[x][points[x][y].length - 1 - z] - centerY, (z - (points[x][y].length - 1)/2.0) * spacing + centerZ, color1, color2);
                    points[x][y][z].rotateY3D(10, 0, 0);
                    points[x][y][z].rotateX3D(-10, 0, 0);
                }
            }
        }
    }

    public void draw(Graphics g)
    {
        g.translate(width/2, height/2);
        double[] dist = new double[points.length * points[0].length * points[0][0].length];
        int[] index = new int[points.length * points[0].length * points[0][0].length];
        for(int x = 0; x < points.length; x++)
        {
            for(int y = 0; y < points[x].length; y++)
            {
                for(int z = 0; z < points[x][y].length; z++)
                {
                    dist[x * points[x].length * points[x][y].length + y * points[x][y].length + z] = points[x][y][z].z;
                    index[x * points[x].length * points[x][y].length + y * points[x][y].length + z] = x * points[x].length * points[x][y].length + y * points[x][y].length + z;
                }
            }
        }
        quickSort(dist, index, 0, dist.length - 1);
        for (int j : index) {
            int x = j / (points[0].length * points[0][0].length);
            int y = j % (points[0].length * points[0][0].length) / points[0][0].length;
            int z = j % (points[0].length * points[0][0].length) % points[0][0].length;
            if (x < points.length - 1 && z < points[x][y].length - 1) {
                int[] xpts1 = {(int) Math.round(points[x][y][z].x), (int) Math.round(points[x + 1][y][z + 1].x), (int) Math.round(points[x][y][z + 1].x)};
                int[] ypts1 = {(int) Math.round(points[x][y][z].y), (int) Math.round(points[x + 1][y][z + 1].y), (int) Math.round(points[x][y][z + 1].y)};
                int[] xpts2 = {(int) Math.round(points[x][y][z].x), (int) Math.round(points[x + 1][y][z + 1].x), (int) Math.round(points[x + 1][y][z].x)};
                int[] ypts2 = {(int) Math.round(points[x][y][z].y), (int) Math.round(points[x + 1][y][z + 1].y), (int) Math.round(points[x + 1][y][z].y)};
                g.setColor(new Color(points[x][y][points[x][y].length - 1 - z].color1));
                g.fillPolygon(xpts1, ypts1, 3);
                g.setColor(new Color(points[x][y][points[x][y].length - 1 - z].color2));
                g.fillPolygon(xpts2, ypts2, 3);
            }
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

    public void addNotify()
    {
        super.addNotify();
        requestFocus();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
        tm.start();
    }

    public void actionPerformed(ActionEvent e)
    {
        update();
        repaint();
    }

    public void keyPressed(KeyEvent key)
    {
        if(key.getKeyCode() == KeyEvent.VK_LEFT)
            left = true;
        if(key.getKeyCode() == KeyEvent.VK_RIGHT)
            right = true;
        if(key.getKeyCode() == KeyEvent.VK_UP)
            up = true;
        if(key.getKeyCode() == KeyEvent.VK_DOWN)
            down = true;
    }

    public void keyReleased(KeyEvent key)
    {
        if(key.getKeyCode() == KeyEvent.VK_LEFT)
            left = false;
        if(key.getKeyCode() == KeyEvent.VK_RIGHT)
            right = false;
        if(key.getKeyCode() == KeyEvent.VK_UP)
            up = false;
        if(key.getKeyCode() == KeyEvent.VK_DOWN)
            down = false;
    }

    public void keyTyped(KeyEvent key)
    {

    }

    private int rgbNum(int r, int g, int b)
    {
        // gets rgb decimal value from rgb input
        return r * 65536 + g * 256 + b;
    }

    public static void main(String[] args)
    {
        TerrainGeneration t = new TerrainGeneration();
        JFrame jf = new JFrame();
        jf.setTitle("Terrain Generation");
        jf.setSize(width, height + 28);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.add(t);
        jf.setVisible(true);
    }
}