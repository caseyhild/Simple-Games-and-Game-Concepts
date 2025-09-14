import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class NoiseGraph extends JFrame implements Runnable, KeyListener
{
    private final int width;
    private final int height;
    private Cube c;
    private int size;
    private final int graphSize;
    private int centerX;
    private int centerY;
    private final Cube[][][] graph;
    private final int[][][] colors;
    private boolean left;
    private boolean right;
    private int rotateAmount;
    private final int rotateSpeed;

    private final Thread thread;
    private boolean running;

    public NoiseGraph()
    {
        //set size of screen
        width = 600;
        height = 600;

        addKeyListener(this);
        graphSize = 30;
        graph = new Cube[graphSize][graphSize][graphSize];
        size = 420;
        centerX = width/2;
        centerY = height/2;
        c = new Cube(width/2, height/2, 0, size, RGB(0, 0, 0));
        c.rotateX3D(-30, c.getY(), c.getZ());
        c.rotateY3D(45, c.getX(), c.getZ());
        c.rotateZ3D(22.5, c.getX(), c.getY());
        colors = new int[graph.length][graph[0].length][graph[0][0].length];
        Noise n = new Noise(graph.length);
        n.makeNoise();
        double[][] noise = n.getNoise();
        int[][] noiseColor = n.getColor();
        for(int x = 0; x < graph.length; x++)
        {
            for(int y = 0; y < graph[x].length; y++)
            {
                for(int z = 0; z < graph[x][y].length; z++)
                {
                    if(y <= (int) (graph.length * noise[x][z]) && y >= 2 * graphSize/5)
                        colors[x][y][z] = noiseColor[x][z];
                    else if(y == 2 * graphSize/5)
                        colors[x][y][z] = RGB(0, 0, 255);
                    else
                        colors[x][y][z] = -1;
                }
            }
        }
        left = false;
        right = false;
        rotateAmount = 0;
        rotateSpeed = 1;

        //what will be displayed to the user
        thread = new Thread(this);

        //setting up the window
        setSize(width, height + 28);
        setResizable(false);
        setTitle("Noise Graph");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        //start the program
        start();
    }

    private synchronized void start()
    {
        //starts game
        running = true;
        thread.start();
    }

    private void update()
    {
        //updates everything
        if(left)
            rotateAmount += rotateSpeed;
        if(right)
            rotateAmount -= rotateSpeed;
    }

    private void render()
    {
        //sets up graphics
        BufferStrategy bs = getBufferStrategy();
        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.translate(0, 28);

        g.setColor(new Color(128, 192, 255));
        g.fillRect(0, 0, width, height);
        
        c = new Cube(width/2, height/2, 0, size, RGB(0, 0, 0));
        c.rotateX3D(-30, c.getY(), c.getZ());
        c.rotateY3D(45, c.getX(), c.getZ());
        c.rotateZ3D(22.5, c.getX(), c.getY());
        for(int x = 0; x < graph.length; x++)
        {
            for(int y = 0; y < graph[x].length; y++)
            {
                for(int z = 0; z < graph[x][y].length; z++)
                {
                    if(colors[x][graph[x].length - 1 - y][z] >= 0)
                    {
                        graph[x][y][z] = new Cube(width/2 - size/2 + size/graph.length/2 + size/graph.length * x, height/2 - size/2 + size/graph.length/2 + size/graph.length * y, -size/2 + size/graph.length/2 + size/graph.length * z, size/graph.length, colors[x][graph[x].length - 1 - y][z]);
                        graph[x][y][z].rotateY3D(rotateAmount, c.getX(), c.getZ());
                        graph[x][y][z].rotateX3D(-30, c.getY(), c.getZ());
                        graph[x][y][z].rotateY3D(45, c.getX(), c.getZ());
                        graph[x][y][z].rotateZ3D(22.5, c.getX(), c.getY());
                    }
                }
            }
        }
        
        g.translate(centerX - width/2, centerY - height/2);
        
        double[] dist = new double[graph.length * graph[0][0].length];
        int[] index = new int[graph.length * graph[0][0].length];
        for(int y = graph[0].length - 1; y >= 0; y--)
        {
            for(int x = 0; x < graph.length; x++)
            {
                for(int z = 0; z < graph[x][y].length; z++)
                {
                    if(colors[x][graph[x].length - 1 - y][z] >= 0)
                        dist[x * graph[x][y].length + z] = graph[x][y][z].getDist();
                    index[x * graph[x][y].length + z] = x * graph[x][y].length + z;
                }
            }
            quickSort(dist, index, 0, graph.length * graph[0][0].length - 1);
            for(int i = 0; i < graph.length * graph[0][0].length; i++)
            {
                if(colors[index[i]/graph[0][0].length][graph[0].length - 1 - y][index[i] % graph[0][0].length] >= 0)
                {
                    graph[index[i]/graph[0][0].length][y][index[i] % graph[0][0].length].draw(g);
                    graph[index[i]/graph[0][0].length][y][index[i] % graph[0][0].length].drawOutline(g, RGB(R(graph[index[i]/graph[0][0].length][y][index[i] % graph[0][0].length].getColor())/2, G(graph[index[i]/graph[0][0].length][y][index[i] % graph[0][0].length].getColor())/2, B(graph[index[i]/graph[0][0].length][y][index[i] % graph[0][0].length].getColor())/2));
                }
            }
        }

        //display all the graphics
        bs.show();
    }

    public void run()
    {
        //main game loop
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0; //60 times per second
        double delta = 0;
        requestFocus();
        while(running)
        {
            //updates time
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) //Make sure update is only happening 60 times a second
            {
                //update
                update();
                delta--;
            }
            //display to the screen
            render();
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

    public void keyPressed(KeyEvent key)
    {
        if(key.getKeyCode() == KeyEvent.VK_LEFT)
            left = true;
        if(key.getKeyCode() == KeyEvent.VK_RIGHT)
            right = true;
        if(key.getKeyCode() == KeyEvent.VK_UP)
        {
            double percentX = (centerX - width/2.0) / (size * Math.sqrt(2) * Math.cos(Math.toRadians(rotateAmount)));
            double percentY = (centerY - height/2.0) / (0.875 * c.getHeight());
            size += graphSize * (int) Math.round(size/210.0);
            if(size < 6720)
                centerY = height/2 + (int) Math.round(c.getHeight() * percentY);
            size = Math.max(210, Math.min(size, 6720));
            centerX = width/2 + (int) Math.round((size * Math.sqrt(2) * Math.cos(Math.toRadians(rotateAmount))) * percentX);
        }
        if(key.getKeyCode() == KeyEvent.VK_DOWN)
        {
            double percentX = (centerX - width/2.0) / (size * Math.sqrt(2) * Math.cos(Math.toRadians(rotateAmount)));
            double percentY = (centerY - height/2.0) / (c.getHeight()/0.855);
            size -= graphSize * (int) Math.round(size/210.0);
            if(size > 180)
                centerY = height/2 + (int) Math.round(c.getHeight() * percentY);
            size = Math.max(210, Math.min(size, 6720));
            centerX = width/2 + (int) Math.round((size * Math.sqrt(2) * Math.cos(Math.toRadians(rotateAmount))) * percentX);
        }
        if(key.getKeyCode() == KeyEvent.VK_W)
        {
            centerY += 5;
            centerY = (int) Math.max(-size * Math.sqrt(2)/2, Math.min(centerY, height + size * Math.sqrt(2)/2));
        }
        if(key.getKeyCode() == KeyEvent.VK_A)
        {
            centerX += 5;
            centerX = (int) Math.max(-size * Math.sqrt(2)/2, Math.min(centerX, width + size * Math.sqrt(2)/2));
        }
        if(key.getKeyCode() == KeyEvent.VK_S)
        {
            centerY -= 5;
            centerY = (int) Math.max(-size * Math.sqrt(2)/2, Math.min(centerY, height + size * Math.sqrt(2)/2));
        }
        if(key.getKeyCode() == KeyEvent.VK_D)
        {
            centerX -= 5;
            centerX = (int) Math.max(-size * Math.sqrt(2)/2, Math.min(centerX, width + size * Math.sqrt(2)/2));
        }
    }

    public void keyReleased(KeyEvent key)
    {
        if(key.getKeyCode() == KeyEvent.VK_LEFT)
            left = false;
        if(key.getKeyCode() == KeyEvent.VK_RIGHT)
            right = false;
    }

    public void keyTyped(KeyEvent key)
    {

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

    public static void main(String [] args)
    {
        new NoiseGraph();
    }
}