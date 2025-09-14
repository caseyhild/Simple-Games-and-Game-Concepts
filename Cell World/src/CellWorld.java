import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class CellWorld extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
    javax.swing.Timer tm = new javax.swing.Timer(1, this);
    private static final int width = 600;
    private static final int height = 600;
    private Cell[][] world;
    private final ArrayList<Cell> cells;
    private final Player player;
    private final ArrayList<Integer> colors;
    private double xoff;
    private double yoff;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private final double[][] random;
    private double[][] noise;
    private boolean keyPressed;
    private KeyEvent key;
    private boolean mouseClicked;
    private boolean mousePressed;
    private int mouseX;
    private int mouseY;
    public CellWorld()
    {
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        colors = new ArrayList<>();
        colors.add(rgbNum(255, 0, 0));
        colors.add(rgbNum(0, 255, 0));
        colors.add(rgbNum(0, 0, 255));
        colors.add(rgbNum(255, 255, 0));
        colors.add(rgbNum(255, 0, 255));
        colors.add(rgbNum(0, 255, 255));
        colors.add(rgbNum(255, 128, 0));
        colors.add(rgbNum(255, 0, 128));
        colors.add(rgbNum(128, 255, 0));
        colors.add(rgbNum(128, 0, 255));
        colors.add(rgbNum(0, 255, 128));
        colors.add(rgbNum(0, 128, 255));
        colors.add(rgbNum(200, 200, 128));
        colors.add(rgbNum(32, 32, 32));
        colors.add(rgbNum(128, 128, 128));
        colors.add(rgbNum(64, 192, 64));
        world = new Cell[23][25];
        xoff = 0;
        yoff = 0;
        random = new double[32][32];
        for(int y = 0; y < random.length; y++)
        {
            for(int x = 0; x < random[y].length; x++)
            {
                random[y][x] = Math.random();
            }
        }
        makeWorld();
        cells = new ArrayList<>();
        cells.add(new Generator(10, 9, 0, 6, world, 100));
        cells.add(new Tower(11, 12, 4, world, 100));
        cells.add(new Laboratory(12, 6, 3, 1, world, 200));
        player = new Player(world[0].length/2, world.length/2, 0, 0, 2, 20);
        setBackground(new Color(0, 0, 0));
    }

    public void update()
    {
        if(left)
        {
            xoff += 1.0;
            player.moveLeft();
        }
        if(right)
        {
            xoff -= 1.0;
            player.moveRight();
        }
        if(up)
        {
            yoff += 1.0;
            player.moveUp();
        }
        if(down)
        {
            yoff -= 1.0;
            player.moveDown();
        }
        player.update();
        for (Cell cell : cells) {
            cell.update(player, noise, world);
        }

        makeWorld();
        for (Cell cell : cells) {
            if (cell.yPos + (int) (yoff / Math.sqrt(3) / 20) >= 0 && cell.yPos + (int) (yoff / Math.sqrt(3) / 20) < world.length && cell.xPos + 2 * ((int) xoff / 60) >= 0 && cell.xPos + 2 * ((int) xoff / 60) < world[cell.yPos + (int) (yoff / Math.sqrt(3) / 20)].length)
                world[cell.yPos + (int) (yoff / Math.sqrt(3) / 20)][cell.xPos + 2 * ((int) xoff / 60)] = cell;
            for (int j = 0; j < 6; j++) {
                if (cell.neighbors[j] != null && cell.neighbors[j].yPos + (int) (yoff / Math.sqrt(3) / 20) >= 0 && cell.neighbors[j].yPos + (int) (yoff / Math.sqrt(3) / 20) < world.length && cell.neighbors[j].xPos + 2 * ((int) xoff / 60) >= 0 && cell.neighbors[j].xPos + 2 * ((int) xoff / 60) < world[cell.neighbors[j].yPos + (int) (yoff / Math.sqrt(3) / 20)].length)
                    world[cell.neighbors[j].yPos + (int) (yoff / Math.sqrt(3) / 20)][cell.neighbors[j].xPos + 2 * ((int) xoff / 60)] = cell.neighbors[j];
            }
        }
    }

    public void draw(Graphics g)
    {
        g.translate(0, 28);

        //draws all hexagons
        for(int yPos = 0; yPos < world.length; yPos++)
        {
            for(int xPos = 0; xPos < world[yPos].length; xPos++)
            {
                world[yPos][xPos].draw((int) (xoff % 60), (int) (yoff % (Math.sqrt(3) * 20)), xPos, yPos, world[yPos].length, world.length, colors, noise, world, width, height, g);
            }
        }

        //draws any additional items that are a part of the cells
        for(int yPos = 0; yPos < world.length; yPos++)
        {
            for(int xPos = 0; xPos < world[yPos].length; xPos++)
            {
                if(world[yPos][xPos] instanceof Tower)
                    ((Tower) world[yPos][xPos]).drawProjectile(xoff, yoff, xPos, yPos, world[0].length, world.length, colors, noise, world, width, height, g);
            }
        }

        //draws player
        player.draw(width, height, g);
    }

    public void makeWorld()
    {
        world = new Cell[23][25];
        Noise n = new Noise(random, 1000000 - xoff / 30, 1000000 - (int) (yoff / (Math.sqrt(3) * 20)));
        noise = n.getNoise();
        for(int y = 0; y < world.length; y++)
        {
            for(int x = 0; x < world[y].length; x++)
            {
                double shade = noise[y][x];
                int color;
                if (shade < 0.45)
                    color = 2;
                else if (shade < 0.55)
                    color = 12;
                else
                    color = 15;

                world[y][x] = new Cell(x, y, color, shade);
            }
        }
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
        if(mouseClicked)
            mouseClicked = false;
        tm.start();
    }

    public void actionPerformed(ActionEvent e)
    {
        update();
        repaint();
    }

    public void mouseClicked(MouseEvent me)
    {
        mouseClicked = true;
    }

    public void mouseEntered(MouseEvent me)
    {

    }

    public void mouseExited(MouseEvent me)
    {
        mouseClicked = false;
        mousePressed = false;
    }

    public void mousePressed(MouseEvent me)
    {
        mousePressed = true;
    }

    public void mouseReleased(MouseEvent me)
    {
        mousePressed = false;
    }

    public void mouseDragged(MouseEvent me)
    {
        mousePressed = true;
        mouseX = me.getX() - 1;
        mouseY = me.getY() - 31;
    }

    public void mouseMoved(MouseEvent me)
    {
        mousePressed = false;
        mouseX = me.getX() - 1;
        mouseY = me.getY() - 31;
    }

    public void keyPressed(KeyEvent key)
    {
        keyPressed = true;
        if(key.getKeyCode() == KeyEvent.VK_LEFT)
            left = true;
        if(key.getKeyCode() == KeyEvent.VK_RIGHT)
            right = true;
        if(key.getKeyCode() == KeyEvent.VK_UP)
            up = true;
        if(key.getKeyCode() == KeyEvent.VK_DOWN)
            down = true;
        this.key = key;
    }

    public void keyReleased(KeyEvent key)
    {
        keyPressed = false;
        if(key.getKeyCode() == KeyEvent.VK_LEFT)
            left = false;
        if(key.getKeyCode() == KeyEvent.VK_RIGHT)
            right = false;
        if(key.getKeyCode() == KeyEvent.VK_UP)
            up = false;
        if(key.getKeyCode() == KeyEvent.VK_DOWN)
            down = false;
        key = null;
    }

    public void keyTyped(KeyEvent key)
    {

    }

    private int rgbNum(int r, int g, int b)
    {
        //gets rgb decimal value from rgb input
        return r * 65536 + g * 256 + b;
    }

    public static void main(String[] args)
    {
        CellWorld g = new CellWorld();
        JFrame jf = new JFrame();
        jf.setTitle("Cell World Game");
        jf.setSize(width, height + 28);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.add(g);
        jf.setVisible(true);
    }
}