import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;

public class Racing3D extends JFrame implements Runnable, KeyListener
{
    private final int width;
    private final int height;
    private final int roadW;
    private final int segL;
    private final double camD;
    private final Line[] lines;

    private int pos;
    private int playerX;

    private final Thread thread;
    private boolean running;
    private boolean keyPressed;
    private boolean keyReleased;
    private boolean keyTyped;
    private KeyEvent key;
    private final KeyEvent oldKey;

    public Racing3D()
    {
        //set size of screen
        width = 640;
        height = 480;

        roadW = 2000;
        segL = 200;
        camD = 0.84;

        Texture random = Texture.random;

        lines = new Line[1600];
        for(int i = 0; i < 1600; i++)
        {
            Line line = new Line();
            line.z = i * segL;

            if(i > 300 && i < 700)
                line.curve = 0.5;

            if(i > 750)
                line.y = Math.sin(i/30.0) * 1500;

            if(i % 20 == 0)
            {
                line.spriteX = -2.5;
                line.texture = random;
            }

            lines[i] = line;
        }

        pos = 0;
        playerX = 0;

        //keyboard input
        keyPressed = false;
        keyReleased = false;
        keyTyped = false;
        key = new KeyEvent(this, 0, 0, 0, 0, KeyEvent.CHAR_UNDEFINED);
        oldKey = new KeyEvent(this, 0, 0, 0, 0, KeyEvent.CHAR_UNDEFINED);

        //keyboard input
        addKeyListener(this);

        //what will be displayed to the user and each pixel of that image
        thread = new Thread(this);

        //setting up the window
        setSize(width, height + 28);
        setResizable(false);
        setTitle("Racing 3D");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setBackground(new Color(0));

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
        if(keyPressed && key.getKeyCode() == KeyEvent.VK_LEFT)
            playerX -= 200;
        if(keyPressed && key.getKeyCode() == KeyEvent.VK_RIGHT)
            playerX += 200;
        if(keyPressed && key.getKeyCode() == KeyEvent.VK_UP)
            pos += 200;
        if(keyPressed && key.getKeyCode() == KeyEvent.VK_DOWN)
            pos -= 200;
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

        g.setColor(new Color(0));
        g.fillRect(0, 0, width, height);

        while(pos >= lines.length * segL)
            pos -= lines.length * segL;
        while(pos < 0)
            pos += lines.length * segL;

        int startPos = pos / segL;
        int camH = 1500 + (int) lines[startPos].y;
        double x = 0, dx = 0;
        int maxy = height;

        for(int n = startPos; n < startPos + 300; n++)
        {
            Line l = lines[n % lines.length];
            l.project(camD, roadW, width, height, playerX - (int) x, camH, pos - (n >= lines.length ? lines.length * segL : 0));
            x += dx;
            dx += l.curve;

            if(l.Y >= maxy)
                continue;
            maxy = (int) l.Y;

            Color grass = (n / 3) % 2 == 0 ? new Color(16, 200, 16) : new Color(0, 154, 0);
            Color rumble = (n / 3) % 2 == 0 ? new Color(255, 255, 255) : new Color(0, 0, 0);
            Color road = (n / 3) % 2 == 0 ? new Color(107, 107, 107) : new Color(105, 105, 105);

            Line p = lines[(n - 1 + lines.length) % lines.length];

            drawQuad(g, grass, 0, (int) p.Y, width, 0, (int) l.Y, width);
            drawQuad(g, rumble, (int) p.X, (int) p.Y, (int) (p.W * 1.2), (int) l.X, (int) l.Y, (int) (l.W * 1.2));
            drawQuad(g, road, (int) p.X, (int) p.Y, (int) p.W, (int) l.X, (int) l.Y, (int) l.W);
        }
        
        for(int n = startPos + 300; n > startPos; n--)
            lines[n % lines.length].drawSprite(g, width);
            
        //reset key states
        if(keyReleased)
            keyReleased = false;
        if(keyTyped)
            keyTyped = false;

        //display all the graphics
        bs.show();
    }

    public void drawQuad(Graphics g, Color c, int x1, int y1, int w1, int x2, int y2, int w2)
    {
        g.setColor(c);
        int[] x = {x1 - w1, x2 - w2, x2 + w2, x1 + w1};
        int[] y = {y1, y2, y2, y1};
        g.fillPolygon(x, y, 4);
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

    public void keyPressed(KeyEvent key)
    {
        keyPressed = !keyTyped;
        this.key = key;
    }

    public void keyReleased(KeyEvent key)
    {
        keyPressed = false;
        keyReleased = true;
        this.key = key;
        oldKey.setKeyChar(KeyEvent.CHAR_UNDEFINED);
    }

    public void keyTyped(KeyEvent key)
    {
        keyTyped = true;
    }

    public static void main(String [] args)
    {
        new Racing3D();
    }
}