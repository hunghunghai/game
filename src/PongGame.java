import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PongGame extends JFrame implements ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;
    private static final int PADDLE_WIDTH = 20;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 3;

    private int paddle1Y;
    private int paddle2Y;
    private int ballX;
    private int ballY;
    private int ballXSpeed;
    private int ballYSpeed;
    private int player1Score;
    private int player2Score;
    private Timer timer;

    public PongGame() {
        super("Pong Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        paddle1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        paddle2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballXSpeed = BALL_SPEED;
        ballYSpeed = BALL_SPEED;
        player1Score = 0;
        player2Score = 0;

        timer = new Timer(10, this);
        timer.start();

        addKeyListener(new KeyboardAdapter());
        setFocusable(true);
    }

    private void movePaddle1Up() {
        if (paddle1Y > 0) paddle1Y -= PADDLE_SPEED;
    }

    private void movePaddle1Down() {
        if (paddle1Y < HEIGHT - PADDLE_HEIGHT) paddle1Y += PADDLE_SPEED;
    }

    private void movePaddle2Up() {
        if (paddle2Y > 0) paddle2Y -= PADDLE_SPEED;
    }

    private void movePaddle2Down() {
        if (paddle2Y < HEIGHT - PADDLE_HEIGHT) paddle2Y += PADDLE_SPEED;
    }

    private void moveBall() {
        ballX += ballXSpeed;
        ballY += ballYSpeed;

        // Collision with paddles
        if (ballX <= PADDLE_WIDTH && ballY + BALL_SIZE >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
            ballXSpeed = BALL_SPEED;
        } else if (ballX >= WIDTH - PADDLE_WIDTH - BALL_SIZE && ballY + BALL_SIZE >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
            ballXSpeed = -BALL_SPEED;
        }

        // Collision with walls
        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) {
            ballYSpeed = -ballYSpeed;
        }

        // Scoring
        if (ballX < 0) {
            player2Score++;
            resetBall();
        } else if (ballX > WIDTH - BALL_SIZE) {
            player1Score++;
            resetBall();
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballXSpeed = BALL_SPEED;
        ballYSpeed = BALL_SPEED;
    }

    private void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.WHITE);
        g.fillRect(PADDLE_WIDTH, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(WIDTH - 2 * PADDLE_WIDTH, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(String.valueOf(player1Score), WIDTH / 2 - 50, 50);
        g.drawString(String.valueOf(player2Score), WIDTH / 2 + 30, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveBall();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    private class KeyboardAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            switch (keyCode) {
                case KeyEvent.VK_W:
                    movePaddle1Up();
                    break;
                case KeyEvent.VK_S:
                    movePaddle1Down();
                    break;
                case KeyEvent.VK_UP:
                    movePaddle2Up();
                    break;
                case KeyEvent.VK_DOWN:
                    movePaddle2Down();
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PongGame game = new PongGame();
            game.setVisible(true);
        });
    }
}
