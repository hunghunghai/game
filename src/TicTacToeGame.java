import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToeGame extends JFrame {
    private JButton[][] buttons;
    private boolean xTurn;

    public TicTacToeGame() {
        super("Tic-Tac-Toe Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new GridLayout(3, 3));

        buttons = new JButton[3][3];
        xTurn = true;

        // Khởi tạo các nút và thêm ActionListener cho chúng
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 60));
                button.addActionListener(new ButtonClickListener(row, col));
                buttons[row][col] = button;
                add(button);
            }
        }
    }

    private void togglePlayerTurn() {
        xTurn = !xTurn;
    }

    private String getCurrentPlayerSymbol() {
        return xTurn ? "X" : "O";
    }

    private void makeMove(int row, int col) {
        JButton button = buttons[row][col];
        if (button.getText().isEmpty()) {
            button.setText(getCurrentPlayerSymbol());
            togglePlayerTurn();
            checkGameResult();
        }
    }

    private void checkGameResult() {
        String[] symbols = {"X", "O"};
        for (String symbol : symbols) {
            // Kiểm tra hàng ngang
            for (int row = 0; row < 3; row++) {
                if (buttons[row][0].getText().equals(symbol) && buttons[row][1].getText().equals(symbol) && buttons[row][2].getText().equals(symbol)) {
                    showWinnerDialog(symbol);
                    return;
                }
            }

            // Kiểm tra hàng dọc
            for (int col = 0; col < 3; col++) {
                if (buttons[0][col].getText().equals(symbol) && buttons[1][col].getText().equals(symbol) && buttons[2][col].getText().equals(symbol)) {
                    showWinnerDialog(symbol);
                    return;
                }
            }

            // Kiểm tra đường chéo chính
            if (buttons[0][0].getText().equals(symbol) && buttons[1][1].getText().equals(symbol) && buttons[2][2].getText().equals(symbol)) {
                showWinnerDialog(symbol);
                return;
            }

            // Kiểm tra đường chéo phụ
            if (buttons[0][2].getText().equals(symbol) && buttons[1][1].getText().equals(symbol) && buttons[2][0].getText().equals(symbol)) {
                showWinnerDialog(symbol);
                return;
            }
        }

        // Kiểm tra hòa
        boolean isDraw = true;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().isEmpty()) {
                    isDraw = false;
                    break;
                }
            }
        }
        if (isDraw) {
            showDrawDialog();
        }
    }

    private void showWinnerDialog(String winnerSymbol) {
        JOptionPane.showMessageDialog(this, "Player " + winnerSymbol + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        resetGame();
    }

    private void showDrawDialog() {
        JOptionPane.showMessageDialog(this, "It's a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        resetGame();
    }

    private void resetGame() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
            }
        }
        xTurn = true;
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
            makeMove(row, col);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicTacToeGame game = new TicTacToeGame();
            game.setVisible(true);
        });
    }
}
