import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game2048 extends JFrame {
    private static final int BOARD_SIZE = 4;
    private static final int TILE_SIZE = 100;
    private static final int BOARD_PADDING = 20;

    private int[][] board;

    public Game2048() {
        super("2048 Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(BOARD_SIZE * TILE_SIZE + 2 * BOARD_PADDING, BOARD_SIZE * TILE_SIZE + 2 * BOARD_PADDING);
        setResizable(false);
        setLocationRelativeTo(null);

        board = new int[BOARD_SIZE][BOARD_SIZE];

        addKeyListener(new KeyboardAdapter());
        setFocusable(true);

        startGame();
    }

    private void startGame() {
        // Initialize the board with two random tiles
        addRandomTile();
        addRandomTile();
    }

    private void addRandomTile() {
        int row, col;
        do {
            row = (int) (Math.random() * BOARD_SIZE);
            col = (int) (Math.random() * BOARD_SIZE);
        } while (board[row][col] != 0);

        board[row][col] = Math.random() < 0.9 ? 2 : 4;
    }

    private void moveTiles(Direction direction) {
        boolean canMove = false;
        int start, end, step;
        if (direction == Direction.LEFT || direction == Direction.UP) {
            start = 0;
            end = BOARD_SIZE;
            step = 1;
        } else {
            start = BOARD_SIZE - 1;
            end = -1;
            step = -1;
        }

        for (int i = start; i != end; i += step) {
            for (int j = start; j != end; j += step) {
                int value = board[i][j];
                if (value != 0) {
                    int nextI = i, nextJ = j;
                    while (canMove(nextI + direction.dRow, nextJ + direction.dCol) && board[nextI + direction.dRow][nextJ + direction.dCol] == 0) {
                        nextI += direction.dRow;
                        nextJ += direction.dCol;
                    }

                    if (canMove(nextI + direction.dRow, nextJ + direction.dCol) && board[nextI + direction.dRow][nextJ + direction.dCol] == value) {
                        board[nextI + direction.dRow][nextJ + direction.dCol] *= 2;
                        board[i][j] = 0;
                        canMove = true;
                    } else if (nextI != i || nextJ != j) {
                        board[nextI][nextJ] = value;
                        board[i][j] = 0;
                        canMove = true;
                    }
                }
            }
        }

        if (canMove) {
            addRandomTile();
            repaint();
            if (isGameOver()) {
                JOptionPane.showMessageDialog(this, "Game Over!", "2048 Game", JOptionPane.INFORMATION_MESSAGE);
                startGame();
            }
        }
    }

    private boolean canMove(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    private boolean isGameOver() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0 || (canMove(i + 1, j) && board[i][j] == board[i + 1][j]) || (canMove(i, j + 1) && board[i][j] == board[i][j + 1])) {
                    return false;
                }
            }
        }
        return true;
    }

    private void drawTile(Graphics g, int row, int col, int value) {
        int x = col * TILE_SIZE + BOARD_PADDING;
        int y = row * TILE_SIZE + BOARD_PADDING;

        g.setColor(getTileColor(value));
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

        g.setColor(getTextColor(value));
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String text = value > 0 ? String.valueOf(value) : "";
        FontMetrics fm = g.getFontMetrics();
        int textX = x + TILE_SIZE / 2 - fm.stringWidth(text) / 2;
        int textY = y + TILE_SIZE / 2 + fm.getAscent() / 2;
        g.drawString(text, textX, textY);
    }

    private Color getTileColor(int value) {
        switch (value) {
            case 2:
                return new Color(238, 228, 218);
            case 4:
                return new Color(237, 224, 200);
            case 8:
                return new Color(242, 177, 121);
            case 16:
                return new Color(245, 149, 99);
            case 32:
                return new Color(246, 124, 95);
            case 64:
                return new Color(246, 94, 59);
            case 128:
                return new Color(237, 207, 114);
            case 256:
                return new Color(237, 204, 97);
            case 512:
                return new Color(237, 200, 80);
            case 1024:
                return new Color(237, 197, 63);
            case 2048:
                return new Color(237, 194, 46);
            default:
                return new Color(205, 193, 180);
        }
    }

    private Color getTextColor(int value) {
        return value < 16 ? new Color(119, 110, 101) : new Color(249, 246, 242);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(new Color(187, 173, 160));
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                drawTile(g, i, j, board[i][j]);
            }
        }
    }

    private class KeyboardAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_LEFT) {
                moveTiles(Direction.LEFT);
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                moveTiles(Direction.RIGHT);
            } else if (keyCode == KeyEvent.VK_UP) {
                moveTiles(Direction.UP);
            } else if (keyCode == KeyEvent.VK_DOWN) {
                moveTiles(Direction.DOWN);
            }
        }
    }

    private enum Direction {
        LEFT(0, -1), RIGHT(0, 1), UP(-1, 0), DOWN(1, 0);

        private final int dRow;
        private final int dCol;

        Direction(int dRow, int dCol) {
            this.dRow = dRow;
            this.dCol = dCol;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game2048 game = new Game2048();
            game.setVisible(true);
        });
    }
}
