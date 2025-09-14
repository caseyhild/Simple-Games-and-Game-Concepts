import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
public class Pong extends JPanel implements ActionListener, KeyListener
{
    javax.swing.Timer tm = new javax.swing.Timer(5, this);
    private static final int width = 800;
    private static final int height = 600;
    private Vector ballPos;
    private Vector ballVel;
    private int paddle1;
    private int paddle2;
    private final int paddleSpeed;
    private final ArrayList<Integer> list;
    private int score1;
    private int score2;
    private boolean keyPressed;
    private KeyEvent key;
    public Pong()
    {
        addKeyListener(this);
        resetBall();
        paddle1 = height/2;
        paddle2 = height/2;
        paddleSpeed = 1;
        score1 = 0;
        score2 = 0;
        list = new ArrayList<>();
        setBackground(new Color(0, 128, 255));
    }

    public void addNotify()
    {
        super.addNotify();
        requestFocus();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Font font = new Font("Verdana", Font.PLAIN, height/6);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(new Color(0, 0, 0));
        g.drawString(score1 + ":" + score2, width/2 - fm.stringWidth(score1 + ":" + score2)/2, height/4);
        g.setColor(new Color(255, 255, 255));
        g.fillRect(10, paddle1 - 50, 10, 100);
        g.setColor(new Color(0, 0, 0));
        g.drawRect(10, paddle1 - 50, 10, 100);
        g.setColor(new Color(255, 255, 255));
        g.fillRect(width - 20, paddle2 - 50, 10, 100);
        g.setColor(new Color(0, 0, 0));
        g.drawRect(width - 20, paddle2 - 50, 10, 100);
        g.setColor(new Color(255, 255, 255));
        g.fillOval((int) ballPos.getX() - 10, (int) ballPos.getY() - 10, 20, 20);
        g.setColor(new Color(0, 0, 0));
        g.drawOval((int) ballPos.getX() - 10, (int) ballPos.getY() - 10, 20, 20);
        tm.start();
    }

    public void actionPerformed(ActionEvent e)
    {
        ballPos.add(ballVel);
        if((ballPos.getX() <= 30 && ballPos.getX() >= 25 && ballPos.getY() + 5 >= paddle1 - 50 && ballPos.getY() <= paddle1 + 50) || (ballPos.getX() <= 30 && ballPos.getX() >= 15 && ((ballPos.getY() + 5 <= paddle1 - 50 && ballPos.getY() + 5 >= paddle1 - 60) || (ballPos.getY() - 5 >= paddle1 + 50 && ballPos.getY() - 5 <= paddle1 + 60))))
        {
            double angle = -(90 * (paddle1 + 50 - ballPos.getY())/100 - 45);
            ballVel = new Vector(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));
        }
        else if((ballPos.getX() >= width - 30 && ballPos.getX() <= width - 25 && ballPos.getY() + 5 >= paddle2 - 50 && ballPos.getY() <= paddle2 + 50) || (ballPos.getX() >= width - 30 && ballPos.getX() <= width - 15 && ((ballPos.getY() + 5 <= paddle2 - 50 && ballPos.getY() + 5 >= paddle2 - 60) || (ballPos.getY() - 5 >= paddle2 + 50 && ballPos.getY() - 5 <= paddle2 + 60))))
        {
            double angle = -(90 * (ballPos.getY() - (paddle2 - 50))/100 + 135);
            ballVel = new Vector(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));
        }
        else if(ballPos.getX() <= 10)
        {
            score2++;
            resetBall();
        }
        else if(ballPos.getX() >= width - 10)
        {
            score1++;
            resetBall();
        }
        else if(ballPos.getY() <= 10 || ballPos.getY() >= height - 10)
            ballVel.setY(-ballVel.getY());
        list.add((int) ballPos.getY());
        if(list.size() > 100)
            list.removeFirst();
        paddle2 = list.getFirst();
        paddle1 = Math.max(50, Math.min(paddle1, height - 50));
        paddle2 = Math.max(50, Math.min(paddle2, height - 50));
        if(keyPressed && key.getKeyCode() == KeyEvent.VK_UP)
            paddle1 -= paddleSpeed;
        if(keyPressed && key.getKeyCode() == KeyEvent.VK_DOWN)
            paddle1 += paddleSpeed;
        repaint();
    }

    public void resetBall()
    {
        ballPos = new Vector(width/2.0, height/2.0);
        do
        {
            ballVel = new Vector((int) (Math.random() * 21 - 10), (int) (Math.random() * 21 - 10));
        }while(ballVel.getX() == 0  || ballVel.getY() == 0 || Math.abs(ballVel.getX()/ballVel.mag()) < Math.sqrt(2)/2);
        ballVel.normalize();
    }

    public void keyPressed(KeyEvent key)
    {
        keyPressed = true;
        this.key = key;
    }

    public void keyReleased(KeyEvent key)
    {
        keyPressed = false;
    }

    public void keyTyped(KeyEvent key)
    {

    }

    public static void main(String[] args)
    {
        java.awt.EventQueue.invokeLater(() -> {
            Pong frame = new Pong();
            frame.setVisible(true);
        }
        );
        Pong p  = new Pong();
        JFrame jf = new JFrame();
        jf.setTitle("Pong");
        jf.setSize(width, height + 28);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.add(p);
        jf.setVisible(true);
    }
}