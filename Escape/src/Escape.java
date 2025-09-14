import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class Escape extends JFrame implements Runnable, MouseListener, MouseMotionListener, KeyListener {
    private final int width, height;
    private final Thread thread;
    private boolean running;

    private int mouseX, mouseY;
    private boolean mousePressed;
    private final boolean[] keys = new boolean[256];

    // Game state
    private String gameState = "menu";
    private int gr = 0;
    private double x = 200, y = 200;      // Player
    private double a = 22.5, b = 22.5;    // Enemy
    private double enemySpeed = 500;
    private int score = 0;

    // Buttons
    private Rectangle playBtn, helpBtn, backBtn, retryBtn;

    public Escape() {
        width = 600;
        height = 600;

        thread = new Thread(this);

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        setSize(width, height + 28);
        setResizable(false);
        setTitle("ESCAPE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        layoutButtons();
        start();
    }

    private void layoutButtons() {
        int btnW = 150, btnH = 70;
        int spacing = (width - 2 * btnW) / 3; // even spacing for 2 buttons

        playBtn = new Rectangle(spacing, height / 2, btnW, btnH);
        helpBtn = new Rectangle(2 * spacing + btnW, height / 2, btnW, btnH);

        backBtn = new Rectangle(width / 2 - 75, height - 120, 150, 60);
        retryBtn = new Rectangle(width / 2 - 75, height / 2 + 100, 150, 70); // shifted down
    }

    private synchronized void start() {
        running = true;
        thread.start();
    }

    private void update() {
        if (gameState.equals("play")) {
            score++;

            double playerSpeed = 2.5;
            if (keys[KeyEvent.VK_LEFT]) x -= playerSpeed;
            if (keys[KeyEvent.VK_RIGHT]) x += playerSpeed;
            if (keys[KeyEvent.VK_UP]) y -= playerSpeed;
            if (keys[KeyEvent.VK_DOWN]) y += playerSpeed;

            if (x <= 22.5) x = 22.5;
            if (x >= width - 22.5) x = width - 22.5;
            if (y <= 22.5) y = 22.5;
            if (y >= height - 22.5) y = height - 22.5;

            a = (x - a) / enemySpeed + a;
            b = (y - b) / enemySpeed + b;

            if (enemySpeed <= 50) {
                enemySpeed -= 0.05;
            } else {
                enemySpeed -= 1;
            }

            if (dist(a, b, x, y) <= 35) {
                gameState = "lose";
            }
        }
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        g.translate(0, 28);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        switch (gameState) {
            case "menu" -> drawMenu(g);
            case "play" -> drawPlay(g);
            case "help" -> drawHelp(g);
            case "lose" -> drawLose(g);
        }

        g.dispose();
        bs.show();
    }

    private void drawMenu(Graphics2D g) {
        gr = Math.min(gr + 1, 255);

        g.setColor(new Color(0, gr, 0));
        g.setFont(new Font("Arial", Font.BOLD, 100));
        drawCenteredText(g, "ESCAPE", width / 2, 150);

        drawButton(g, playBtn, "PLAY", new Color(0, gr, 0));
        drawButton(g, helpBtn, "HELP", new Color(0, gr, 0));

        if (playBtn.contains(mouseX, mouseY) && mousePressed) {
            gameState = "play";
            resetGame();
        }
        if (helpBtn.contains(mouseX, mouseY) && mousePressed) {
            gameState = "help";
        }
    }

    private void drawPlay(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        drawRightAlignedText(g, "Score: " + score, width - 20, 30);

        // Enemy with outline
        g.setColor(new Color(150, 0, 0));
        g.fillOval((int) a - 22, (int) b - 22, 44, 44);
        g.setColor(Color.RED);
        g.fillOval((int) a - 20, (int) b - 20, 40, 40);

        // Player with outline
        g.setColor(new Color(0, 150, 0));
        g.fillOval((int) x - 22, (int) y - 22, 44, 44);
        g.setColor(Color.GREEN);
        g.fillOval((int) x - 20, (int) y - 20, 40, 40);
    }

    private void drawHelp(Graphics2D g) {
        gr = Math.min(gr + 1, 255);

        g.setColor(new Color(0, gr, 0));
        g.setFont(new Font("Arial", Font.BOLD, 50));
        drawCenteredText(g, "HOW TO PLAY", width / 2, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        drawCenteredText(g, "Avoid the red guy chasing you.", width / 2, 170);
        drawCenteredText(g, "Stay alive as long as you can.", width / 2, 200);

        drawButton(g, backBtn, "BACK", new Color(0, gr, 0));

        if (backBtn.contains(mouseX, mouseY) && mousePressed) {
            gameState = "menu";
        }
    }

    private void drawLose(Graphics2D g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        drawCenteredText(g, "YOU DIED", width / 2, 150);
        drawCenteredText(g, "Score: " + score, width / 2, 250);

        drawButton(g, retryBtn, "RETRY", Color.RED);

        if (retryBtn.contains(mouseX, mouseY) && mousePressed) {
            gameState = "play";
            resetGame();
        }
    }

    private void drawButton(Graphics2D g, Rectangle r, String text, Color baseColor) {
        boolean hover = r.contains(mouseX, mouseY);

        Color fill = hover ? baseColor.darker() : baseColor;
        Color outline = baseColor.darker().darker();

        g.setColor(fill);
        g.fillRect(r.x, r.y, r.width, r.height);

        g.setColor(outline);
        g.setStroke(new BasicStroke(3));
        g.drawRect(r.x, r.y, r.width, r.height);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        // Center text inside button
        FontMetrics fm = g.getFontMetrics();
        int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
        int ty = r.y + (r.height - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, tx, ty);
    }

    private void drawCenteredText(Graphics2D g, String text, int cx, int cy) {
        FontMetrics fm = g.getFontMetrics();
        int tx = cx - fm.stringWidth(text) / 2;
        int ty = cy - fm.getHeight() / 2 + fm.getAscent();
        g.drawString(text, tx, ty);
    }

    private void drawRightAlignedText(Graphics2D g, String text, int rx, int y) {
        FontMetrics fm = g.getFontMetrics();
        int tx = rx - fm.stringWidth(text);
        g.drawString(text, tx, y);
    }

    private void resetGame() {
        x = width / 2.0;
        y = height / 2.0;
        a = 22.5;
        b = 22.5;
        enemySpeed = 500;
        score = 0;
    }

    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        requestFocus();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
            }
            render();
        }
    }

    public void mousePressed(MouseEvent me) { mousePressed = true; }
    public void mouseReleased(MouseEvent me) { mousePressed = false; }
    public void mouseDragged(MouseEvent me) { mouseX = me.getX(); mouseY = me.getY() - 28; }
    public void mouseMoved(MouseEvent me) { mouseX = me.getX(); mouseY = me.getY() - 28; }
    public void mouseClicked(MouseEvent me) {}
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < keys.length) keys[e.getKeyCode()] = true;
    }
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < keys.length) keys[e.getKeyCode()] = false;
    }
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new Escape();
    }
}