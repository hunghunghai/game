import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MinesweeperGame extends JFrame {
    private static final int BOARD_SIZE = 10;
    private static final int MINE_COUNT = 10;

    private JButton[][] board;
    private boolean[][] mines;
    private int[][] counts;

    public MinesweeperGame() {
        super("Minesweeper Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(BOARD_SIZE * 30, BOARD_SIZE * 30);
        setResizable(false);
        setLocationRelativeTo(null);

        board = new JButton[BOARD_SIZE][BOARD_SIZE];
        mines = new boolean[BOARD_SIZE][BOARD_SIZE];
        counts = new int[BOARD_SIZE][BOARD_SIZE];

        initializeBoard();
        placeMines();
        calculateCounts();

        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(30, 30));
                button.setFont(new Font("Arial", Font.PLAIN, 14));
                button.addActionListener(new ButtonClickListener(row, col));
                board[row][col] = button;
                add(button);
            }
        }
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = new JButton();
            }
        }
    }

    private void placeMines() {
        int count = 0;
        while (count < MINE_COUNT) {
            int row = (int) (Math.random() * BOARD_SIZE);
            int col = (int) (Math.random() * BOARD_SIZE);
            if (!mines[row][col]) {
                mines[row][col] = true;
                count++;
            }
        }
    }

    private void calculateCounts() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (mines[row][col]) {
                    counts[row][col] = -1;
                } else {
                    int count = 0;
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            int nr = row + dr;
                            int nc = col + dc;
                            if (nr >= 0 && nr < BOARD_SIZE && nc >= 0 && nc < BOARD_SIZE && mines[nr][nc]) {
                                count++;
                            }
                        }
                    }
                    counts[row][col] = count;
                }
            }
        }
    }

    private void revealCell(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col].isEnabled()) {
            return;
        }

        board[row][col].setEnabled(false);
        board[row][col].setBackground(Color.LIGHT_GRAY);

        if (counts[row][col] > 0) {
            board[row][col].setText(Integer.toString(counts[row][col]));
        } else if (counts[row][col] == 0) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    revealCell(row + dr, col + dc);
                }
            }
        } else if (counts[row][col] == -1) {
            gameOver();
        }
    }

    private void gameOver() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (mines[row][col]) {
                    board[row][col].setBackground(Color.RED);
                    board[row][col].setText("M");
                }
                board[row][col].setEnabled(false);
            }
        }
        JOptionPane.showMessageDialog(this, "Game Over!", "Minesweeper Game", JOptionPane.INFORMATION_MESSAGE);
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            revealCell(row, col);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MinesweeperGame game = new MinesweeperGame();
            game.setVisible(true);
        });
    }
}
