import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SnakeGame extends JFrame implements ActionListener {
    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 20;
    private static final int CELL_SIZE = 20;
    private static final int DELAY = 150;

    private int[] snakeX;
    private int[] snakeY;
    private int snakeLength;
    private int foodX;
    private int foodY;
    private Direction direction;
    private Timer timer;

    public SnakeGame() {
        super("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE);
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(new KeyboardAdapter());
        setFocusable(true);

        snakeX = new int[BOARD_WIDTH * BOARD_HEIGHT];
        snakeY = new int[BOARD_WIDTH * BOARD_HEIGHT];
        snakeLength = 1;
        snakeX[0] = BOARD_WIDTH / 2;
        snakeY[0] = BOARD_HEIGHT / 2;

        direction = Direction.RIGHT;

        placeFood();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void placeFood() {
        foodX = (int) (Math.random() * BOARD_WIDTH);
        foodY = (int) (Math.random() * BOARD_HEIGHT);
    }

    private void moveSnake() {
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case UP:
                snakeY[0]--;
                break;
            case DOWN:
                snakeY[0]++;
                break;
            case LEFT:
                snakeX[0]--;
                break;
            case RIGHT:
                snakeX[0]++;
                break;
        }
    }

    private boolean checkCollision() {
        int headX = snakeX[0];
        int headY = snakeY[0];

        if (headX < 0 || headX >= BOARD_WIDTH || headY < 0 || headY >= BOARD_HEIGHT) return true;

        for (int i = 1; i < snakeLength; i++) {
            if (headX == snakeX[i] && headY == snakeY[i]) return true;
        }

        return false;
    }

    private void checkFoodCollision() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            placeFood();
        }
    }

    private void paintBoard(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.GREEN);
        for (int i = 0; i < snakeLength; i++) {
            g.fillRect(snakeX[i] * CELL_SIZE, snakeY[i] * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        g.setColor(Color.RED);
        g.fillRect(foodX * CELL_SIZE, foodY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Snake Game", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveSnake();
        if (checkCollision()) {
            gameOver();
        } else {
            checkFoodCollision();
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintBoard(g);
    }

    private class KeyboardAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            switch (keyCode) {
                case KeyEvent.VK_UP:
                    if (direction != Direction.DOWN) direction = Direction.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != Direction.UP) direction = Direction.DOWN;
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != Direction.RIGHT) direction = Direction.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != Direction.LEFT) direction = Direction.RIGHT;
                    break;
            }
        }
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}
