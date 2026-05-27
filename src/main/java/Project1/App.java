package Project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class App extends JPanel implements ActionListener, KeyListener {

    // Screen settings
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    static final int DELAY = 100;

    // Snake body coordinates
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;

    char direction = 'R';
    boolean running = false;

    Timer timer;
    Random random;

    App() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);

        startGame();
    }

    public void startGame() {
        newApple();
        running = true;

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {

            // Apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Snake
            for (int i = 0; i < bodyParts; i++) {

                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 30));

            FontMetrics metrics = getFontMetrics(g.getFont());

            g.drawString(
                    "Score: " + applesEaten,
                    (WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize()
            );

        } else {
            gameOver(g);
        }
    }

    public void newApple() {

        appleX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void resetGame() {
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        running = false;

        for (int i = 0; i < x.length; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        x[0] = 0;
        y[0] = 0;

        startGame();
    }

    public void move() {

        for (int i = bodyParts; i > 0; i--) {

            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {

            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;

            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {

        if ((x[0] == appleX) && (y[0] == appleY)) {

            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {

        // Snake hits itself
        for (int i = bodyParts; i > 0; i--) {

            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // Border collision
        if (x[0] < 0)
            running = false;

        if (x[0] >= WIDTH)
            running = false;

        if (y[0] < 0)
            running = false;

        if (y[0] >= HEIGHT)
            running = false;

        if (!running)
            timer.stop();
    }

    public void gameOver(Graphics g) {

        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 50));

        FontMetrics metrics = getFontMetrics(g.getFont());

        g.drawString(
                "Game Over",
                (WIDTH - metrics.stringWidth("Game Over")) / 2,
                HEIGHT / 2
        );

        g.setFont(new Font("Arial", Font.BOLD, 30));

        g.drawString(
                "Score: " + applesEaten,
                (WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                HEIGHT / 2 + 50
        );

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String restartText = "Press SPACE to restart";
        g.drawString(
                restartText,
                (WIDTH - metrics.stringWidth(restartText)) / 2,
                HEIGHT / 2 + 90
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {

            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            case KeyEvent.VK_SPACE:
                if (!running) {
                    resetGame();
                }
                break;

            case KeyEvent.VK_LEFT:
                if (direction != 'R')
                    direction = 'L';
                break;

            case KeyEvent.VK_RIGHT:
                if (direction != 'L')
                    direction = 'R';
                break;

            case KeyEvent.VK_UP:
                if (direction != 'D')
                    direction = 'U';
                break;

            case KeyEvent.VK_DOWN:
                if (direction != 'U')
                    direction = 'D';
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame();

        App gamePanel = new App();

        frame.add(gamePanel);

        frame.setTitle("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}