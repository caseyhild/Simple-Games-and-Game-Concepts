public class Texture
{
    // all the different textures
    public static Texture bricks = new Texture(64, 64, "bricks");
    public static Texture grass = new Texture(64, 64, "grass");
    public static Texture gravel = new Texture(64, 64, "gravel");
    public static Texture gray = new Texture(64, 64, "gray");
    public static Texture random = new Texture(64, 64, "random");
    //variables for each texture
    private final String name;
    public int SIZEX;
    public int SIZEY;
    public int[] pixels;

    public Texture(int sizeX, int sizeY, String texName)
    {
        //name of texture
        name = texName;
        //size of texture (64)
        SIZEX = sizeX;
        SIZEY = sizeY;
        //all pixels in texture
        pixels = new int[sizeX * sizeY];
        //what design there is for each texture
        switch (texName) {
            case "bricks" -> {
                for (int i = 0; i < sizeX * sizeY; i++) {
                    int backgroundColor = RGB(128, 128, 128);
                    int brickColor = RGB(128, 32, 0);
                    if (i < 3 * sizeX || i >= (sizeX - 3) * sizeX)
                        pixels[i] = backgroundColor;
                    else if (i / sizeX % 10 == 1 || i / sizeX % 10 == 2)
                        pixels[i] = backgroundColor;
                    else if (i / sizeX % 20 >= 3 && i / sizeX % 20 <= 10 && (((i % sizeX % 20 == 1 || i % sizeX % 20 == 2) && !(i % sizeX < 3 || i % sizeX >= sizeX - 3)) || (i % sizeX < 1 || i % sizeX >= sizeX - 1)))
                        pixels[i] = backgroundColor;
                    else if ((i / sizeX % 20 >= 13 || i / sizeX % 20 == 0) && (i % sizeX % 20 == 11 || i % sizeX % 20 == 12))
                        pixels[i] = backgroundColor;
                    else if (i % sizeX % 10 < 5)
                        pixels[i] = brickColor;
                    else
                        pixels[i] = brickColor;
                }
            }
            case "grass" -> {
                int color = RGB(0, 128, 0);
                for (int i = 0; i < sizeX * sizeY; i++) {
                    double random = Math.random();
                    int newColor = RGB((int) (random * getR(color)), (int) (random * getG(color)), (int) (random * getB(color)));
                    pixels[i] = newColor;
                }
            }
            case "gravel" -> {
                for (int i = 0; i < sizeX * sizeY; i++) {
                    int shade = (int) (Math.random() * 128 + 64);
                    pixels[i] = RGB(shade, shade, shade);
                }
            }
            case "gray" -> {
                for (int i = 0; i < sizeX * sizeY; i++) {
                    if (i % sizeX <= 1 || i % sizeX >= sizeX - 2 || i / sizeX <= 1 || i / sizeX >= sizeX - 2)
                        pixels[i] = RGB(96, 96, 96);
                    else
                        pixels[i] = RGB(64, 64, 64);
                }
            }
            case "random" -> {
                for (int i = 0; i < sizeX * sizeY; i++) {
                    pixels[i] = RGB((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
                }
            }
        }
    }
    
    public void rescale(int sizeX, int sizeY)
    {
        Texture t = new Texture(sizeX, sizeY, name);
        SIZEX = t.SIZEX;
        SIZEY = t.SIZEY;
        pixels = t.pixels;
    }

    private int RGB(int r, int g, int b)
    {
        //gets rgb decimal value from rgb input
        return r * 65536 + g * 256 + b;
    }

    private int getR(int color)
    {
        // gets r value from rgb decimal input
        return color/65536;
    }

    private int getG(int color)
    {
        // gets g value from rgb decimal input
        return color % 65536/256;
    }

    private int getB(int color)
    {
        // gets b value from rgb decimal input
        return color % 65536 % 256;
    }
}