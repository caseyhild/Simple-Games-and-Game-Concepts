import java.awt.*;

public class Line
{
    public double x, y, z;
    public double X, Y, W;
    public double scale, curve, spriteX, clip;
    public Texture texture;

    public Line()
    {
        x = 0;
        y = 0;
        z = 0;
        curve = 0;
    }

    public void project(double camD, double roadW, int width, int height, int camX, int camY, int camZ)
    {
        scale = camD / (z - camZ);
        X = (1 + scale * (x - camX)) * width/2;
        Y = (1 - scale * (y - camY)) * height/2;
        W = scale * roadW * width/2;
    }

    public void drawSprite(Graphics g, int width)
    {
        Texture t = texture;

        if(t != null)
        {
            int w = t.SIZEX;
            int h = t.SIZEY;

            double destX = X + scale * spriteX * width/2;
            double destY = Y + 4;
            double destW = w * W / 266;
            double destH = h * W / 266;

            destX += destW * spriteX;
            destY -= destH;

            double clipH = destY + destH - clip;
            if(clipH < 0)
                clipH = 0;

            if(clipH >= destH)
                return;
            t.rescale((int) (destW/w), (int) (destH/h));

            if(t.pixels.length != 1)
            {
                for(int y = 0; y < t.SIZEY; y++)
                {
                    for(int x = 0; x < t.SIZEX; x++)
                    {
                        g.setColor(new Color(t.pixels[y * t.SIZEX + x]));
                        g.drawLine((int) destX + x, (int) destY + y, (int) destX + x, (int) destY + y);
                    }
                }
            }
        }
    }
}