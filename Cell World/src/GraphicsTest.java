import java.awt.*;
import javax.swing.*;
public class GraphicsTest extends JPanel
{
    static int width = 600;
    static int height = 600;
    
    public void update()
    {
        
    }

    public void draw(Graphics g)
    {
        int color1 = rgbNum(255, 0, 0);
        int color2 = rgbNum(0, 255, 0);
        int color3 = rgbNum(0, 0, 255);
        int xCenter1 = width/2 - 100;
        int yCenter1 = height/2;
        int xCenter2 = width/2 + 50;
        int yCenter2 = height/2 + (int) (Math.sqrt(3) * 50);
        int xCenter3 = width/2 + 50;
        int yCenter3 = height/2 - (int) (Math.sqrt(3) * 50);
        g.setColor(new Color(color1));
        fillHexagon(xCenter1, yCenter1, xCenter2, yCenter2, xCenter3, yCenter3, 100, color1, color2, color3, g);
        g.setColor(new Color(color2));
        fillHexagon(xCenter2, yCenter2, xCenter3, yCenter3, xCenter1, yCenter1, 100, color2, color3, color1, g);
        g.setColor(new Color(color3));
        fillHexagon(xCenter3, yCenter3, xCenter1, yCenter1, xCenter2, yCenter2, 100, color3, color1, color2, g);
    }
    
    public void fillHexagon(int xCenter1, int yCenter1, int xCenter2, int yCenter2, int xCenter3, int yCenter3, int sideLength, int color1, int color2, int color3, Graphics g)
    {
        int l = (int) Math.round(Math.sqrt(3)/2 * sideLength);
        for(int y = yCenter1 - l; y < yCenter1 + l; y++)
        {
            int l1 = (int) (sideLength/2 - Math.abs(y - yCenter1) / Math.sqrt(3));
            for(int x = xCenter1 - sideLength/2 - l1; x <= xCenter1 + sideLength/2 + l1; x++)
            {
                double dist1 = Math.sqrt((x - xCenter1) * (x - xCenter1) + (y - yCenter1) * (y - yCenter1));
                double dist2 = Math.sqrt((x - xCenter2) * (x - xCenter2) + (y - yCenter2) * (y - yCenter2));
                double dist3 = Math.sqrt((x - xCenter3) * (x - xCenter3) + (y - yCenter3) * (y - yCenter3));
                int red = Math.max(0, Math.min((int) (getR(color1) * (1 - dist1 / sideLength / Math.sqrt(3))) + (int) (getR(color2) * (1 - dist2 / sideLength / Math.sqrt(3))) + (int) (getR(color3) * (1 - dist3 / sideLength / Math.sqrt(3))), 255));
                int green = Math.max(0, Math.min((int) (getG(color1) * (1 - dist1 / sideLength / Math.sqrt(3))) + (int) (getG(color2) * (1 - dist2 / sideLength / Math.sqrt(3))) + (int) (getG(color3) * (1 - dist3 / sideLength / Math.sqrt(3))), 255));
                int blue = Math.max(0, Math.min((int) (getB(color1) * (1 - dist1 / sideLength / Math.sqrt(3))) + (int) (getB(color2) * (1 - dist2 / sideLength / Math.sqrt(3))) + (int) (getB(color3) * (1 - dist3 / sideLength / Math.sqrt(3))), 255));
                g.setColor(new Color(red, green, blue));
                g.drawLine(x, y, x, y);
            }
        }
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        update();
        draw(g);
    }
    
    private int rgbNum(int r, int g, int b)
    {
        //gets rgb decimal value from rgb input
        return r * 65536 + g * 256 + b;
    }

    private int getR(int color)
    {
        //gets r value from rgb decimal input
        return color/65536;
    }

    private int getG(int color)
    {
        //gets g value from rgb decimal input
        return color % 65536/256;
    }

    private int getB(int color)
    {
        //gets b value from rgb decimal input
        return color % 65536 % 256;
    }
    
    public static void main(String[] args)
    {
        GraphicsTest g = new GraphicsTest();
        JFrame jf = new JFrame();
        jf.setTitle("Graphics Test");
        jf.setSize(width + 6, height + 29);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.add(g);
        jf.setVisible(true);
    }
}