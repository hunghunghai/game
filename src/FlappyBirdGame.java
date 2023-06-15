import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlappyBirdGame extends JFrame {
    private static final int BOARD_WIDTH = 400;
    private static final int BOARD_HEIGHT = 600;
    private static final int BIRD_SIZE = 30;
    private static final int PIPE_WIDTH = 80;
    private static final int PIPE_HEIGHT = 400;
    private static final int PIPE_GAP = 200;
    private static final int GRAVITY = 2;
    private static final int FLAP_STRENGTH = 10;

    private JPanel board;
    private JPanel bird;
    private JPanel pipeTop;
    private JPanel pipeBottom;
    private int birdY;
    private int birdVelocity;
    private int pipeX;
    private int score;
    private Timer timer;
    private boolean isGameOver;

    public FlappyBirdGame() {
        super("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        board = new JPanel();
        board.setBackground(Color.CYAN);
        board.setLayout(null);
        add(board);

        bird = new JPanel();
        bird.setBounds(50, BOARD_HEIGHT / 2 - BIRD_SIZE / 2, BIRD_SIZE, BIRD_SIZE);
        bird.setBackground(Color.YELLOW);
        board.add(bird);

        pipeTop = new JPanel();
        pipeTop.setBounds(BOARD_WIDTH, 0, PIPE_WIDTH, PIPE_HEIGHT);
        pipeTop.setBackground(Color.GREEN);
        board.add(pipeTop);

        pipeBottom = new JPanel();
        pipeBottom.setBounds(BOARD_WIDTH, PIPE_HEIGHT + PIPE_GAP, PIPE_WIDTH, PIPE_HEIGHT);
        pipeBottom.setBackground(Color.GREEN);
        board.add(pipeBottom);

        birdY = BOARD_HEIGHT / 2 - BIRD_SIZE / 2;
        birdVelocity = 0;
        pipeX = BOARD_WIDTH;

        timer = new Timer(20, new GameUpdateListener());
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!isGameOver) {
                        birdVelocity -= FLAP_STRENGTH;
                    } else {
                        resetGame();
                    }
                }
            }
        });

        setFocusable(true);
        requestFocus();
    }

    private void updateBird() {
        birdVelocity += GRAVITY;
        birdY += birdVelocity;
        bird.setBounds(50, birdY, BIRD_SIZE, BIRD_SIZE);
        checkCollision();
    }

    private void updatePipes() {
        pipeX -= 5;
        pipeTop.setBounds(pipeX, 0, PIPE_WIDTH, PIPE_HEIGHT);
        pipeBottom.setBounds(pipeX, PIPE_HEIGHT + PIPE_GAP, PIPE_WIDTH, PIPE_HEIGHT);

        if (pipeX + PIPE_WIDTH < 0) {
            pipeX = BOARD_WIDTH;
            score++;
        }
    }

    private void checkCollision() {
        if (birdY + BIRD_SIZE > BOARD_HEIGHT || birdY < 0) {
            gameOver();
        }

        Rectangle birdBounds = new Rectangle(50, birdY, BIRD_SIZE, BIRD_SIZE);
        Rectangle pipeTopBounds = new Rectangle(pipeX, 0, PIPE_WIDTH, PIPE_HEIGHT);
        Rectangle pipeBottomBounds = new Rectangle(pipeX, PIPE_HEIGHT + PIPE_GAP, PIPE_WIDTH, PIPE_HEIGHT);

        if (birdBounds.intersects(pipeTopBounds) || birdBounds.intersects(pipeBottomBounds)) {
            gameOver();
        }
    }

    private void gameOver() {
        timer.stop();
        isGameOver = true;
        JOptionPane.showMessageDialog(this, "Game Over!\nScore: " + score, "Flappy Bird", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetGame() {
        birdY = BOARD_HEIGHT / 2 - BIRD_SIZE / 2;
        birdVelocity = 0;
        pipeX = BOARD_WIDTH;
        score = 0;
        isGameOver = false;
        timer.start();
    }

    private class GameUpdateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateBird();
            updatePipes();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlappyBirdGame game = new FlappyBirdGame();
            game.setVisible(true);
        });
    }
}
